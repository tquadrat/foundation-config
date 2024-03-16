/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;

/**
 *  Run-time copy of the
 *  {@link org.tquadrat.foundation.config.Option}
 *  annotation. By definition, named options are <i>real</i> options (and
 *  instances of this class). Unnamed options are arguments and actually
 *  another subclass of
 *  {@link CLIDefinition}.
 *
 *  @see CLIArgumentDefinition
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Mark Sinke
 *  @version $Id: CLIOptionDefinition.java 1120 2024-03-16 09:48:00Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CLIOptionDefinition.java 1120 2024-03-16 09:48:00Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public class CLIOptionDefinition extends CLIDefinition
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The aliases for the option name.
     */
    private final List<String> m_Aliases;

    /**
     *  The option name.
     */
    private final String m_Name;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CLIOptionDefinition} instance.
     *
     *  @param  property    The name of the property.
     *  @param  names   The names for the option.
     *  @param  usage   The usage text.
     *  @param  usageKey    The resource bundle key for the usage text.
     *  @param  metaVar The meta variable name; can be {@code null}.
     *  @param  required    {@code true} if the argument or option is
     *      mandatory.
     *  @param  handler The handler for the option or argument value.
     *  @param  multiValued {@code true} if the option or argument allows
     *      more than one value.
     *  @param  format  The optional format.
     */
    @SuppressWarnings( "BooleanParameter" )
    public CLIOptionDefinition( final String property, final List<String> names, final String usage, final String usageKey, final String metaVar, final boolean required, final CmdLineValueHandler<?> handler, final boolean multiValued, final String format )
    {
        super( property, false, usage, usageKey, isNull( metaVar ) ? EMPTY_STRING : metaVar, required, handler, multiValued, format );

        m_Name = names.getFirst();
        m_Aliases = names.size() > 1 ? List.copyOf( names.subList( 1, names.size() ) ) : List.of();
    }   //  CLIOptionDefinition()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the aliases.
     *
     *  @return The aliases; may be empty, but will never be {@code null}.
     */
    public final Collection<String> aliases() { return m_Aliases; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getSortKey() { return m_Name; }

    /**
     *  Returns the name of the option.
     *
     *  @return The option's name.
     */
    public final String name() { return m_Name; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var buffer = new StringBuilder();
        if( !required() ) buffer.append( '[' );
        buffer.append( name() );
        if( !aliases().isEmpty() )
        {
            buffer.append( aliases().stream().collect( joining( ", ", " (", ")" ) ) );
        }
        if( isNotEmpty( metaVar() ) ) buffer.append( ' ' ).append( metaVar() );
        if( !required() ) buffer.append( ']' );
        final var retValue = buffer.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class CLIOptionDefinition

/*
 *  End of File
 */