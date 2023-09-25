/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.config.cli;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.MountPoint;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;

/**
 *  <p>{@summary The abstract base class for the value handler that takes a
 *  String value from the command line, translates it to the target type and
 *  sets the value to the property.}</p>
 *  <p>To circumvent possible problems with reflection in a modularised
 *  context, the handler uses an instance of
 *  {@link BiConsumer}
 *  to set the property; the
 *  {@link BiConsumer#accept(Object, Object)}
 *  method of that instance is called with two arguments:</p>
 *  <ol>
 *      <li>the name of the property to set</li>
 *      <li>the value for that property</li>
 *  </ol>
 *  <p>Basically, the implementation of such a value setter function may be
 *  implemented in any way, but the annotation processor creates them as simple
 *  as possible. To set a value to the attribute {@code m_Value}, it would look
 *  like</p>
 *  <pre><code>(p,v) -&gt; m_Value = v;</code></pre>
 *  <p>and to add a value to the list attribute {@code m_List}, it would be</p>
 *  <pre><code>(p,v) -&gt; m_List.add( v );</code></pre>
 *  <p>That means that the name of the property is ignored.</p>
 *  <p>But customer implementations may use other implementations as well.</p>
 *
 *  @param  <T> The target type.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CmdLineValueHandler.java 936 2021-12-13 16:08:37Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CmdLineValueHandler.java 936 2021-12-13 16:08:37Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public abstract class CmdLineValueHandler<T>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The default property name that is used if no context is given: {@value}.
     */
    public static final String DEFAULT_PROPERTY_NAME = "dummyProperty";

    /**
     *  The error message for an invalid entry on the command line: {@value}.
     */
    public static final String MSG_InvalidParameter = "'%1$s' cannot be parsed as a valid command line parameter";

    /**
     *  The resource bundle key for the message about an invalid entry on the
     *  command line.
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The generic message about an invalid entry on the command line",
        translations =
        {
            @Translation( language = "en", text = MSG_InvalidParameter ),
            @Translation( language = "de", text = "'%1$s' kann nicht zu einem gültigen Kommandozeilenparameter umgewandelt werden" )
        }
    )
    public static final int MSGKEY_InvalidParameter = 1;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The CLI definition that provides the context for this value handler.
     */
    private CLIDefinition m_CLIDefinition;

    /**
     *  <p>{@summary The value setter.}</p>
     *  <p>The arguments of the lambda are:</p>
     *  <ol>
     *      <li>the name of the property to set</li>
     *      <li>the value for that property</li>
     *  </ol>
     *  <p>In most usages, the first argument is ignored.</p>
     */
    private final BiConsumer<String,T> m_ValueSetter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CmdLineValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    protected CmdLineValueHandler( final BiConsumer<String,T> valueSetter )
    {
        m_ValueSetter = requireNonNullArgument( valueSetter, "valueSetter" );
    }   //  CmdLineValueHandler()

    /**
     *  Creates a new {@code CmdLineValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    protected CmdLineValueHandler( final CLIDefinition context, final BiConsumer<String,T> valueSetter )
    {
        this( valueSetter );
        m_CLIDefinition = requireNonNullArgument( context, "context" );
    }   //  CmdLineValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns a reference to the context.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the CLI definition.
     */
    protected final Optional<CLIDefinition> getCLIDefinition() { return Optional.ofNullable( m_CLIDefinition ); }

    /**
     *  Returns the name of the property that is the target for the value.
     *
     *  @return The property name.
     */
    protected String getPropertyName()
    {
        final var retValue = getCLIDefinition().map( CLIDefinition::propertyName ).orElse( DEFAULT_PROPERTY_NAME );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getPropertyName()

    /**
     *  Returns a reference for the value setter.
     *
     *  @return The value setter.
     */
    protected final BiConsumer<String,T> getValueSetter() { return m_ValueSetter; }

    /**
     *  Parses the given command line snippet and stores the result to the
     *  property.
     *
     *  @note This method can be overridden, but in most cases, the
     *      implementation provided here should be sufficient.
     *
     *  @param  params  The command line values to parse.
     *  @return The number of values that the method took from the command
     *      line.
     *  @throws CmdLineException    An error occurred when parsing the
     *      parameters.
     */
    @MountPoint
    public int parseCmdLine( final Parameters params )
    {
        var retValue = -1;
        try
        {
            final var result = translate( requireNonNullArgument( params, "params" ) );
            retValue = result.size();
            result.forEach( r -> m_ValueSetter.accept( m_CLIDefinition.propertyName(), r ) );
        }
        catch( final CmdLineException e ) { throw e; }
        catch( @SuppressWarnings( "OverlyBroadCatchBlock" ) final Exception e )
        {
            throw new CmdLineException( m_CLIDefinition, e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseCmdLine()

    /**
     *  Sets the CLI definition that provides the context for this value
     *  handler.
     *
     *  @param  context The CLI definition.
     */
    public final void setContext( final CLIDefinition context )
    {
        m_CLIDefinition = requireNonNullArgument( context, "context" );
    }   //  setContext()

    /**
     *  Translates the command line values that can be referenced via the
     *  {@code params} argument to the target type.
     *
     *  @param  params  The command line values to translate.
     *  @return A collection with the result; each entry in the collection
     *      corresponds to one value from the command line.
     *  @throws CmdLineException    The given parameters cannot be parsed to
     *      the target type.
     */
    protected abstract Collection<T> translate( final Parameters params ) throws CmdLineException;
}
//  class CmdLineValueHandler

/*
 *  End of File
 */