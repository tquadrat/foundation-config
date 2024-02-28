/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.config;

import static java.lang.System.err;
import static java.util.Comparator.comparing;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.config.internal.CLIDefinitionParser.parse;
import static org.tquadrat.foundation.i18n.I18nUtil.resolveText;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_Object_ARRAY;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.JavaUtils.findMainClass;
import static org.tquadrat.foundation.util.JavaUtils.loadClass;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.config.internal.ArgumentParser;
import org.tquadrat.foundation.config.internal.UsageBuilder;
import org.tquadrat.foundation.config.spi.CLIArgumentDefinition;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.CLIOptionDefinition;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.function.tce.TCEBiFunction;
import org.tquadrat.foundation.function.tce.TCEFunction;
import org.tquadrat.foundation.lang.AutoLock;

/**
 *  <p>{@summary Utility methods that can be used to handle configuration
 *  beans.}</p>
 *  <p>The main API is defined by the two methods:</p>
 *  <ul>
 *      <li>{@link #getConfiguration(Class, TCEFunction)}</li>
 *      <li>{@link #getConfiguration(Class, String, TCEBiFunction)}</li>
 *  </ul>
 *  <p>They are used to generate and return the configuration bean instance for
 *  the given configuration bean specification. For the same specification, it
 *  returns always the same instance (for an implementation of
 *  {@link SessionBeanSpec},
 *  it will be the same instance when specification and session key are the
 *  same).</p>
 *  <p>The {@code factory} argument for
 *  {@link #getConfiguration(Class, TCEFunction)}
 *  can be a lambda like this:</p>
 *  <blockquote><pre><code>c -&gt; c.getConstructor().newInstance()</code></pre></blockquote>
 *  <p>and for</p>
 *  {@link #getConfiguration(Class, String, TCEBiFunction)},
 *  <p>it could be:</p>
 *  <blockquote><pre><code>(c,s) -&gt; c.getConstructor( String.class ).newInstance( s )</code></pre></blockquote>
 *  <p>The factory is required because the code in the module
 *  {@code org.tquadrat.foundation.ui} cannot access classes in the
 *  {@code ~.generated} package of the module that uses the configuration, and
 *  that holds the generated configuration beans.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigUtil.java 1084 2024-01-03 15:31:20Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@UtilityClass
@ClassVersion( sourceVersion = "$Id: ConfigUtil.java 1084 2024-01-03 15:31:20Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class ConfigUtil
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary The registry for global configuration beans.}</p>
     *  <p>The key is the configuration bean specification interface, the
     *  value is the initialised instance of the related configuration
     *  bean.</p>
     */
    @SuppressWarnings( "StaticCollection" )
    private static final Map<Class<? extends ConfigBeanSpec>,ConfigBeanSpec> m_ConfigurationBeanRegistry = new HashMap<>();

    /**
     *  <p>{@summary The registry for session configuration beans.}</p>
     *  <p>The key is the configuration bean specification interface, the
     *  value is a map holding the initialised instances of the related
     *  configuration beans, indexed by the session identifier.</p>
     */
    @SuppressWarnings( "StaticCollection" )
    private static final Map<Class<? extends SessionBeanSpec>,Map<String,? extends SessionBeanSpec>> m_SessionConfigBeanRegistry = new HashMap<>();

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The lock for the
     *  {@link #m_ConfigurationBeanRegistry}.
     */
    private static final AutoLock m_ConfigurationBeanRegistryLock;

    /**
     *  The lock for the
     *  {@link #m_SessionConfigBeanRegistry}.
     */
    private static final AutoLock m_SessionConfigBeanRegistryLock;

    static
    {
        //---* Creating the locks *--------------------------------------------
        m_ConfigurationBeanRegistryLock = AutoLock.of( new ReentrantLock() );
        m_SessionConfigBeanRegistryLock = AutoLock.of( new ReentrantLock() );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private ConfigUtil() { throw new PrivateConstructorForStaticClassCalledError( ConfigUtil.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Drops the configuration bean for the given specification
     *  and the given session key.}</p>
     *  <p>The &quot;session key&quot; can be any arbitrary kind of a unique
     *  identifier: a user id, a session id, a URI, or a UUID.</p>
     *  <p>Nothing happens if the there is not configuration bean for the
     *  given specification and/or session key.</p>
     *
     *  @param  <T> The type of the configuration bean specification.
     *  @param  specification   The specification interface for the
     *      configuration bean.
     *  @param  sessionKey  The session key.
     */
    @API( status = STABLE, since = "0.0.1" )
    public static final <T extends SessionBeanSpec> void dropConfiguration( final Class<T> specification, final String sessionKey )
    {
        requireNonNullArgument( specification, "specification" );
        requireNotEmptyArgument( sessionKey, "sessionKey" );

        try( @SuppressWarnings( "unused" ) final var ignored = m_SessionConfigBeanRegistryLock.lock() )
        {
            @SuppressWarnings( "unchecked" )
            final var beans = (Map<String,SessionBeanSpec>) m_SessionConfigBeanRegistry.get( specification );
            if( nonNull( beans ) ) beans.remove( sessionKey );
        }
    }   //  dropConfiguration()

    /**
     *  Dumps a parameter file template for the provided command line
     *  definition to the given
     *  {@link OutputStream}.
     *
     *  @param  cmdLineDefinition   The command line definition.
     *  @param  outputStream    The target output stream.
     *  @throws IOException Something went wrong when writing to the output
     *      stream.
     */
    @API( status = STABLE, since = "0.0.2" )
    public static final void dumpParamFileTemplate( final List<? extends CLIDefinition> cmdLineDefinition, final OutputStream outputStream ) throws IOException
    {
        try( final Writer writer = new OutputStreamWriter( requireNonNullArgument( outputStream, "outputStream" ), UTF8 ) )
        {
            final List<CLIOptionDefinition> options = new ArrayList<>();
            final List<CLIArgumentDefinition> arguments = new ArrayList<>();

            for( final var definition : requireNonNullArgument( cmdLineDefinition, "cmdLineDefinition" ) )
            {
                if( definition.isArgument() )
                {
                    arguments.add( (CLIArgumentDefinition) definition );
                }
                else
                {
                    options.add( (CLIOptionDefinition) definition );
                }
            }

            options.sort( comparing( CLIOptionDefinition::getSortKey ) );
            arguments.sort( comparing( CLIArgumentDefinition::getSortKey ) );

            for( final var definition : options )
            {
                writer.append( definition.name() )
                    .append( ' ' )
                    .append( definition.metaVar() )
                    .append( '\n' );
            }
            for( final var definition : arguments )
            {
                writer.append( definition.metaVar() ).append( '\n' );
            }
            writer.flush();
        }
    }   //  dumpParamFileTemplate()

    /**
     *  Retrieves the configuration bean for the given specification.
     *
     *  @param  <T> The type of the configuration bean specification.
     *  @param  specification   The specification interface for the
     *      configuration bean.
     *  @param  factory The factory that instantiates the configuration bean.
     *  @return The configuration bean.
     */
    @API( status = STABLE, since = "0.0.1" )
    public static final <T extends ConfigBeanSpec> T getConfiguration( final Class<? extends T> specification, final TCEFunction<Class<T>,T> factory )
    {
        requireNonNullArgument( factory, "factory" );

        final T retValue;
        try( @SuppressWarnings( "unused" ) final var ignored = m_ConfigurationBeanRegistryLock.lock() )
        {
            @SuppressWarnings( "unchecked" )
            final var bean = (T) m_ConfigurationBeanRegistry.computeIfAbsent( requireNonNullArgument( specification, "specification" ), aClass -> loadConfigurationBean( aClass, factory ) );
            retValue = bean;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getConfiguration()

    /**
     *  <p>{@summary Retrieves the configuration bean for the given
     *  specification and the given session key.}</p>
     *  <p>The &quot;session key&quot; can be any arbitrary kind of a unique
     *  identifier: a user id, a session id, a URI, or a UUID.</p>
     *
     *  @param  <T> The type of the configuration bean specification.
     *  @param  specification   The specification interface for the
     *      configuration bean.
     *  @param  sessionKey  The session key.
     *  @param  factory The factory that instantiates the configuration bean.
     *  @return The configuration bean.
     */
    @API( status = STABLE, since = "0.0.1" )
    public static final <T extends SessionBeanSpec> T getConfiguration( final Class<? extends T> specification, final String sessionKey, final TCEBiFunction<Class<T>, ? super String, T> factory )
    {
        requireNonNullArgument( factory, "factory" );

        final T retValue;
        try( @SuppressWarnings( "unused" ) final var ignored = m_SessionConfigBeanRegistryLock.lock() )
        {
            @SuppressWarnings( "unchecked" )
            final var beans = (Map<String,SessionBeanSpec>) m_SessionConfigBeanRegistry.computeIfAbsent( requireNonNullArgument( specification, "specification" ), $ -> new HashMap<>() );
            @SuppressWarnings( "unchecked" )
            final var bean = (T) beans.computeIfAbsent( requireNotEmptyArgument( sessionKey, "sessionKey" ), s -> loadSessionBean( specification, s, factory ) );
            retValue = bean;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getConfiguration()

    /**
     *  Retrieves the configuration bean class and loads it.
     *
     *  @param  <T> The type of the configuration bean specification.
     *  @param  specification   The specification interface for the
     *      configuration bean.
     *  @param  factory The factory that instantiates the configuration bean.
     *  @return The configuration bean.
     */
    private static final <T extends ConfigBeanSpec> T loadConfigurationBean( final Class<? extends ConfigBeanSpec> specification, final TCEFunction<? super Class<T>, T> factory )
    {
        final var className = retrieveClassName( specification );
        T retValue = null;
        try
        {
            @SuppressWarnings( "unchecked" )
            final var beanClass = (Class<T>) loadClass( className, specification ).orElseThrow( () -> new ClassNotFoundException( className ) );
            retValue = factory.apply( beanClass );
        }
        catch( final ClassNotFoundException e )
        {
            throw new ValidationException( "Invalid configuration bean specification: %s".formatted( specification.getName() ), e );
        }
        catch( final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e )
        {
            throw new ValidationException( "Unable to instantiate configuration bean for specification: %s".formatted( specification.getName() ), e );
        }
        catch( final Exception e )
        {
            throw new ValidationException( "Problems to instantiate configuration bean for specification: %s".formatted( specification.getName() ), e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadConfigurationBean()

    /**
     *  Retrieves the configuration bean class for a session bean and loads it.
     *
     *  @param  <T> The type of the configuration bean specification.
     *  @param  specification   The specification interface for the
     *      configuration bean.
     *  @param  sessionKey  The session key.
     *  @param  factory The factory that instantiates the configuration bean.
     *  @return The configuration bean.
     */
    private static final <T extends ConfigBeanSpec> T loadSessionBean( final Class<? extends T> specification, final String sessionKey, final TCEBiFunction<? super Class<T>, ? super String, T> factory )
    {
        final var className = retrieveClassName( specification );
        T retValue = null;
        try
        {
            @SuppressWarnings( "unchecked" )
            final var beanClass = (Class<T>) loadClass( className, specification ).orElseThrow( () -> new ClassNotFoundException( "className" ) );
            retValue = factory.apply( beanClass, sessionKey );
        }
        catch( final ClassNotFoundException e )
        {
            throw new ValidationException( "Invalid configuration bean specification: %s".formatted( specification.getName() ), e );
        }
        catch( final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e )
        {
            throw new ValidationException( "Unable to instantiate configuration bean  for specification: %s".formatted( specification.getName() ), e );
        }
        catch( final Exception e )
        {
            throw new ValidationException( "Problems to instantiate configuration bean for specification: %s".formatted( specification.getName() ), e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  loadSessionBean()

    /**
     *  Parses the given command line arguments based on the provided list of
     *  {@link CLIDefinition}
     *  instances.
     *
     *  @param cmdLineDefinition    The definition for the expected/allowed
     *      command line options and arguments.
     *  @param  args    The command line arguments.
     *  @throws CmdLineException    The parsing failed for some reason.
     */
    @API( status = STABLE, since = "0.0.1" )
    public static final void parseCommandLine( final Collection<? extends CLIDefinition> cmdLineDefinition, final String... args ) throws CmdLineException
    {
        final var parser = new ArgumentParser( cmdLineDefinition );
        parser.parse( args );
    }   //  parseCommandLine()

    /**
     *  Parses the given command line arguments based on the given instance
     *  of
     *  {@link InputStream}
     *  that provides the XML CLI definition. In case of an invalid entry on
     *  the command line, an error message will be printed to
     *  {@link System#err}.
     *
     *  @param cmdLineDefinition    The definition for the expected/allowed
     *      command line options and arguments.
     *  @param  validate    {@code true} if the given XML should be validated
     *      against the schema {@code CLIDefinition.xsd} previous to parsing
     *      it, {@code false} if the validation can be omitted.
     *  @param  args    The command line arguments.
     *  @return The command line values; the key for the result map is the
     *      value from the
     *      <code>{@value org.tquadrat.foundation.config.internal.CLIDefinitionParser#XMLATTRIBUTE_PropertyName}</code>
     *      property.
     *  @throws CmdLineException    The parsing of the command line failed for
     *      some reason.
     *  @throws XMLStreamException  The parsing for the XML CLI definition
     *      failed for some reason.
     *  @throws IOException Reading the XML CLI definition failed.
     */
    @API( status = STABLE, since = "0.0.1" )
    public static final Map<String,Object> parseCommandLine( final InputStream cmdLineDefinition, final boolean validate, final String... args ) throws CmdLineException, XMLStreamException, IOException
    {
        final var retValue = parseCommandLine( null, cmdLineDefinition, validate, args );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseCommandLine()

    /**
     *  Parses the given command line arguments based on the given instance
     *  of
     *  {@link InputStream}
     *  that provides the XML CLI definition. In case of an invalid entry on
     *  the command line, an error message will be printed to
     *  {@link System#err}.
     *
     *  @param  resourceBundle  The
     *      {@link ResourceBundle}
     *      for the messages.
     *  @param  cmdLineDefinition    The definition for the expected/allowed
     *      command line options and arguments.
     *  @param  validate    {@code true} if the given XML should be validated
     *      against the schema {@code CLIDefinition.xsd} previous to parsing
     *      it, {@code false} if the validation can be omitted.
     *  @param  args    The command line arguments.
     *  @return The command line values; the key for the result map is the
     *      value from the
     *      <code>{@value org.tquadrat.foundation.config.internal.CLIDefinitionParser#XMLATTRIBUTE_PropertyName}</code>
     *      property.
     *  @throws CmdLineException    The parsing of the command line failed for
     *      some reason.
     *  @throws XMLStreamException  The parsing for the XML CLI definition
     *      failed for some reason.
     *  @throws IOException Reading the XML CLI definition failed.
     */
    @API( status = STABLE, since = "0.0.2" )
    public static final Map<String,Object> parseCommandLine( final ResourceBundle resourceBundle, final InputStream cmdLineDefinition, final boolean validate, final String... args ) throws CmdLineException, XMLStreamException, IOException
    {
        final Map<String,Object> retValue = new HashMap<>();
        final var cliDefinitions = parse( cmdLineDefinition, retValue, validate );
        final var cliParser = new ArgumentParser( cliDefinitions );
        try
        {
            cliParser.parse( args );
        }
        catch( final CmdLineException e )
        {
            final var command = findMainClass().orElse( "<MainClass>" );
            //noinspection UseOfSystemOutOrSystemErr
            err.println( e.getMessage() );
            //noinspection UseOfSystemOutOrSystemErr
            printUsage( err, Optional.ofNullable( resourceBundle ), command, cliDefinitions );
            throw e;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseCommandLine()

    /**
     *  Prints a <i>usage</i> message to the given
     *  {@link OutputStream}.
     *
     *  @param  outputStream    The output stream.
     *  @param  resources   The resource bundle that is used for translation.
     *  @param  command The command used to start the program.
     *  @param  definitions The CLI definitions.
     *  @throws IOException A problem occurred on writing to the output stream.
     */
    @SuppressWarnings( {"OptionalUsedAsFieldOrParameterType"} )
    @API( status = STABLE, since = "0.0.1" )
    public static final void printUsage( final OutputStream outputStream, final Optional<ResourceBundle> resources, final CharSequence command, final Collection<? extends CLIDefinition> definitions ) throws IOException
    {
        final var builder = new UsageBuilder( resources );
        final var usage = builder.build( command, definitions );
        requireNonNullArgument( outputStream, "outputStream" ).write( usage.getBytes( Charset.defaultCharset() ) );
    }   //  printUsage()

    /**
     *  Returns the message from the given
     *  {@link CLIDefinition}.
     *
     *  @param  bundle  The resource bundle.
     *  @param  definition  The {@code CLIDefinition}.
     *  @return The resolved message.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    @API( status = STABLE, since = "0.0.2" )
    public static final String resolveMessage( final Optional<ResourceBundle> bundle, final CLIDefinition definition )
    {
        final var retValue = resolveText( bundle, requireNonNullArgument( definition, "definition" ).usage(), definition.usageKey(), EMPTY_Object_ARRAY );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  resolveMessage()

    /**
     *  Retrieves the class name for the configuration bean from the given
     *  specification.
     *
     *  @param  specification   The specification.
     *  @return The class name.
     */
    private static final String retrieveClassName( final Class<? extends ConfigBeanSpec> specification )
    {
        final var annotation = specification.getAnnotation( ConfigurationBeanSpecification.class );
        final var simpleName = annotation.name().isEmpty() ? specification.getSimpleName() + "Impl" : annotation.name();

        final var packageName = specification.getPackageName();

        final var retValue = (packageName.isEmpty() ? EMPTY_STRING : packageName + ".") + (annotation.samePackage() ? EMPTY_STRING : "generated.") + simpleName;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveClassName()
}
//  class ConfigUtil

/*
 *  End of File
 */