/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
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

package org.tquadrat.foundation.config.spi;

import static java.lang.Character.isWhitespace;
import static java.util.Locale.ROOT;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.config.CLIBeanSpec.LEAD_IN;
import static org.tquadrat.foundation.config.internal.Commons.retrieveMessage;
import static org.tquadrat.foundation.i18n.TextUse.USAGE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Base class for the run-time copies of the
 *  {@link org.tquadrat.foundation.config.Option &#64;Option} or
 *  {@link org.tquadrat.foundation.config.Argument &#64;Argument}
 *  annotation. By definition, unnamed options are arguments, and named options
 *  are <i>real</i> command line options.
 *
 *  @see CLIOptionDefinition
 *  @see CLIArgumentDefinition
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Mark Sinke
 *  @version $Id: CLIDefinition.java 938 2021-12-15 14:42:53Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CLIDefinition.java 938 2021-12-15 14:42:53Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public abstract class CLIDefinition
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message indicating that the empty String is not allowed as an
     *  option name.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @API( status = STABLE, since = "0.1.0" )
    @Message
    (
        description = "The message indicating that the empty String is not allowed as an option name.",
        translations =
        {
            @Translation( language = "en", text = "The empty String is not a valid option name" )
        }
    )
    public static final int MSGKEY_EmptyIsInvalid = 31;

    /**
     *  The message indicating an invalid option name.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @API( status = STABLE, since = "0.1.0" )
    @Message
    (
        description = "The message indicating an invalid option name.",
        translations =
        {
            @Translation( language = "en", text = "'%1$s' is not a valid option name" )
        }
    )
    public static final int MSGKEY_InvalidName = 32;

    /**
     *  The message indicating that the option name is reserved.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @API( status = STABLE, since = "0.1.0" )
    @Message
    (
        description = "The message indicating that the option name is reserved.",
        translations =
        {
            @Translation( language = "en", text = "'%1$s' is a reserved name" )
        }
    )
    public static final int MSGKEY_ReservedName = 33;

    /**
     *  The message indicating a whitespace character as option name.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @API( status = STABLE, since = "0.1.0" )
    @Message
    (
        description = "The message indicating a whitespace character as option name.",
        translations =
        {
            @Translation( language = "en", text = "Character after '%s' may not be whitespace" )
        }
    )
    public static final int MSGKEY_Whitespace1 = 34;

    /**
     *  The message indicating a whitespace character as part of an option
     *  name.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @API( status = STABLE, since = "0.1.0" )
    @Message
    (
        description = "The message indicating a whitespace character as part of an option name.",
        translations =
        {
            @Translation( language = "en", text = "An option name may not contain any whitespace characters" )
        }
    )
    public static final int MSGKEY_Whitespace2 = 35;

    /**
     *  The message indicating an invalid lead-in for an option name.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @API( status = STABLE, since = "0.0.1" )
    @Message
    (
        description = "The message indicating an invalid lead-in for an option name.",
        translations =
        {
            @Translation( language = "en", text = "The name '%2$s' must start with '%1$s'" )
        }
    )
    public static final int MSGKEY_WrongLeadIn = 36;

    /**
     *  <p>{@summary The format for the default usage keys.}</p>
     *  <p>The resource bundle key for a
     *  {@link org.tquadrat.foundation.i18n.TextUse#USAGE USAGE}
     *  text is prepended with the name of the class that defines it.</p>
     */
    public static final String USAGE_KEY_FORMAT = StringUtils.format( "%%s.%s_%%s", USAGE.name() );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The optional format string.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<String> m_Format;

    /**
     *  The handler that is used to parse and store the option or argument
     *  value.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final CmdLineValueHandler<?> m_Handler;

    /**
     *  {@code true} if this is an argument, {@code false} if it is
     *  an option.
     */
    private final boolean m_IsArgument;

    /**
     *  {@code true} if the target property is multi-valued,
     *  {@code false} otherwise.
     */
    private final boolean m_IsMultiValued;

    /**
     *  {@code true} if the option or argument is required,
     *  {@code false} otherwise.
     */
    private final boolean m_IsRequired;

    /**
     *  The name of the meta variable that is used in examples.
     */
    private final String m_MetaVar;

    /**
     *  The name of the property.
     */
    private final String m_Property;

    /**
     *  The usage text.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<String> m_Usage;

    /**
     *  The resource key for the usage text.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<String> m_UsageKey;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CLIDefinition} instance.
     *
     *  @param  property    The name of the property.
     *  @param  isArgument  {@code true} for an argument,
     *      {@code false} for an option.
     *  @param  usage   The usage text.
     *  @param  usageKey    The resource bundle key for the usage text.
     *  @param  metaVar The meta variable name.
     *  @param  required    {@code true} if the argument or option is
     *      mandatory.
     *  @param  handler The handler for the option or argument value.
     *  @param  multiValued {@code true} if the option or argument allows
     *      more than one value.
     *  @param  format  The optional format.
     */
    protected CLIDefinition( final String property, final boolean isArgument, final String usage, final String usageKey, final String metaVar, final boolean required, final CmdLineValueHandler<?> handler, final boolean multiValued, final String format )
    {
        m_Property = requireNotEmptyArgument( property, "property" );
        m_IsArgument = isArgument;
        m_Usage = Optional.ofNullable( usage );
        m_UsageKey = Optional.ofNullable( usageKey );
        m_MetaVar = nonNull( metaVar ) ? metaVar :  property.toUpperCase( ROOT );
        m_IsRequired = required;
        m_Handler = requireNonNullArgument( handler, "handler" );
        m_Handler.setContext( this );
        m_IsMultiValued = multiValued;
        m_Format = Optional.ofNullable( format );
    }   //  CLIDefinition()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the optional format.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the format.
     *
     *  @see org.tquadrat.foundation.config.Argument#format()
     *  @see org.tquadrat.foundation.config.Option#format()
     */
    public final Optional<String> format() { return m_Format; }

    /**
     *  Returns the sort key for the option or argument.
     *
     *  @return The sort key.
     */
    public abstract String getSortKey();

    /**
     *  Returns the handler.
     *
     *  @return The handler
     */
    public final CmdLineValueHandler<?> handler() { return m_Handler; }

    /**
     *  Returns a flag that indicates whether this is the definition for an
     *  argument or an option.
     *
     *  @return {@code true} if this instance of {@code OptionDef} is defined
     *      by an
     *      {@link org.tquadrat.foundation.config.Argument &#64;Argument}
     *      annotation, {@code false} if it is defined by an
     *      {@link org.tquadrat.foundation.config.Option &#64;Option}
     *      annotation.
     */
    public final boolean isArgument() { return m_IsArgument; }

    /**
     *  Returns a flag that indicates whether this option or argument is
     *  multi-valued.
     *
     *  @return {@code true} if multi-valued, {@code false}
     *      otherwise.
     */
    public final boolean isMultiValued() { return m_IsMultiValued; }

    /**
     *  The name of the meta variable.
     *
     *  @return The meta variable.
     */
    public final String metaVar() { return m_MetaVar; }

    /**
     *  Processes the given parameter(s).
     *
     *  @param  params  A reference to the command line arguments as for this
     *      option or argument definition. This method can use this object to
     *      access the values if necessary. The object is valid only during the
     *      method call.
     *  @return The number of command line arguments consumed by this method.
     *      For example, it will return 0 if option defined by this instance
     *      does not take any parameters.
     *  @throws CmdLineException    Parsing the parameter(s) failed.
     */
    public final int processParameters( final Parameters params ) throws CmdLineException
    {
        final var retValue = m_Handler.parseCmdLine( params );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processParameters()

    /**
     *  Returns the property name for this CLI element.
     *
     *  @return The property name.
     */
    public final String propertyName() { return m_Property; }

    /**
     *  Returns a flag indicating if the option or argument is mandatory.
     *
     *  @return {@code true} if the argument or option is required,
     *      {@code false} otherwise.
     *
     *  @see org.tquadrat.foundation.config.Argument#required()
     *  @see org.tquadrat.foundation.config.Option#required()
     */
    public final boolean required() { return m_IsRequired; }

    /**
     *  {@inheritDoc}<br>
     */
    @Override
    public abstract String toString();

    /**
     *  Returns the usage text.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the usage text.
     */
    public final Optional<String> usage() { return m_Usage; }

    /**
     *  Returns the resource key for the usage text.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the key for the usage text.
     */
    public final Optional<String> usageKey() { return m_UsageKey; }

    /**
     *  Checks whether the given name for a command line option is valid.
     *
     *  @param  name    The intended name for the command line option.
     *  @return {@code true} if the given name is valid, {@code false}
     *      otherwise.
     *  @throws IllegalArgumentException    The given name is invalid.
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    public static final boolean validateOptionName( final String name ) throws IllegalArgumentException
    {
        final var retValue = switch( requireNonNullArgument( name, "name" ).length() )
        {
            case 0 ->  throw new IllegalArgumentException( retrieveMessage( MSGKEY_EmptyIsInvalid, true ) );
            case 1 ->
            {
                if( name.equals( LEAD_IN ) )
                {
                    throw new IllegalArgumentException( retrieveMessage( MSGKEY_InvalidName, true, LEAD_IN ) );
                }
                throw new IllegalArgumentException( retrieveMessage( MSGKEY_WrongLeadIn, true, LEAD_IN, name ) );
            }

            case 2 ->
            {
                if( name.equals( LEAD_IN + LEAD_IN ) )
                {
                    throw new IllegalArgumentException( retrieveMessage( MSGKEY_ReservedName, true, LEAD_IN + LEAD_IN ) );
                }
                else if( !name.startsWith( LEAD_IN ) )
                {
                    throw new IllegalArgumentException( retrieveMessage( MSGKEY_WrongLeadIn, true, LEAD_IN, name ) );
                }
                else if( isWhitespace( name.charAt( 1 ) ) )
                {
                    throw new IllegalArgumentException( retrieveMessage( MSGKEY_Whitespace1, true, LEAD_IN ) );
                }
                yield true;
            }

            default ->
            {
                if( !name.startsWith( LEAD_IN + LEAD_IN ) )
                {
                    throw new IllegalArgumentException( retrieveMessage( MSGKEY_WrongLeadIn, true, LEAD_IN + LEAD_IN, name ) );
                }
                else if( name.codePoints().anyMatch( Character::isWhitespace ) )
                {
                    throw new IllegalArgumentException( retrieveMessage( MSGKEY_Whitespace2, true ) );
                }
                yield true;
            }
        };

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  validateOptionName()
}
//  class CLIDefinition

/*
 *  End of File
 */