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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.Argument;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Run-time copy of the
 *  {@link Argument &#64;Argument}
 *  annotation. By definition, unnamed options are arguments (and instances of
 *  this class). Named options are actually another subclass
 *  of
 *  {@link CLIDefinition}.
 *
 *  @see CLIOptionDefinition
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Mark Sinke
 *  @version $Id: CLIArgumentDefinition.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CLIArgumentDefinition.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public class CLIArgumentDefinition extends CLIDefinition
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The index; only used for arguments (not for options).
     */
    private final int m_Index;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CLIArgumentDefinition} instance.
     *
     *  @param  property    The name of the property.
     *  @param  index   The position for this argument on the command line.
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
    @SuppressWarnings( "BooleanParameter" )
    public CLIArgumentDefinition( final String property, final int index, final String usage, final String usageKey, final String metaVar, final boolean required, final CmdLineValueHandler<?> handler, final boolean multiValued, final String format )
    {
        super( property, true, usage, usageKey, requireNotEmptyArgument( metaVar, "metaVar" ), required, handler, multiValued, format );
        m_Index = index;
    }   //  CLIArgumentDefinition()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getSortKey() { return Integer.toString( m_Index ); }

    /**
     *  Returns the index of this argument.
     *
     *  @return The index.
     */
    public final int index() { return m_Index; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return StringUtils.format( required() ? "%s" : "[%s]", metaVar() ); }
}
//  class CLIArgumentDefinition

/*
 *  End of File
 */