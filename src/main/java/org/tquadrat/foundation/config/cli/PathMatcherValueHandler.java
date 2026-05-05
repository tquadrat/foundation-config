/*
 * ============================================================================
 *  Copyright © 2002-2026 by Thomas Thrien.
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
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.IOUtils.getPathMatcher;

import java.nio.file.PathMatcher;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;

/**
 *  <p>{@summary This class is an implementation of
 *  {@link CmdLineValueHandler}
 *  for
 *  {@link PathMatcher}.}</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PathMatcherValueHandler.java 1231 2026-05-05 14:28:23Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PathMatcherValueHandler.java 1231 2026-05-05 14:28:23Z tquadrat $" )
@API( status = STABLE, since = "0.25.5" )
public final class PathMatcherValueHandler extends CmdLineValueHandler<PathMatcher>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code PathMatcherHandler}.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public PathMatcherValueHandler( final BiConsumer<String,PathMatcher> valueSetter )
    {
        super( valueSetter );
    }   //  PathMatcherHandler()

    /**
     *  Creates a new instance of {@code PathMatcherHandler}.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public PathMatcherValueHandler( final CLIDefinition context, final BiConsumer<String,PathMatcher> valueSetter )
    {
        super( context, valueSetter );
    }   //  PathMatcherHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected Collection<PathMatcher> translate( final Parameters params ) throws CmdLineException
    {
        final Collection<PathMatcher> retValue;
        final var argument = requireNonNullArgument( params, "params" ).getParameter( 0 );
        try
        {
            final var pathMatcher = getPathMatcher( requireNotEmptyArgument( argument, "argument" ) );
            retValue = List.of( pathMatcher );
        }
        catch( final IllegalArgumentException | UnsupportedOperationException e )
        {
            throw new CmdLineException( getCLIDefinition(), e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class PathMatcherHandler

/*
 *  End of File
 */