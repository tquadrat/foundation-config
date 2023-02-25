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

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;

/**
 *  <p>{@summary Provides access to a list of arguments on the command
 *  line.}</p>
 *  <p>Objects that implement this interface are passed to an instance of
 *  {@link CmdLineValueHandler CmdLineValueHandler}
 *  to make it safe and easy to parse additional parameters for options.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Parameters.java 1049 2023-02-25 19:13:40Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Parameters.java 1049 2023-02-25 19:13:40Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public interface Parameters
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Returns the additional parameter to a related option.}</p>
     *  <p>Specifying 0 for {@code index} will retrieve the token next to the
     *  option. For example, if the command line looks like</p>
     *  <pre><code>-o abc -d x</code></pre>
     *  <p>then {@code getParameter(0)} for the option &quot;{@code -o}&quot;
     *  returns &quot;<code><i>abc</i></code>&quot;; {@code getParameter(1)}
     *  would return &quot;<code><i>-d</i></code>&quot;, but as this is an
     *  option, a
     *  {@link CmdLineException}
     *  will be thrown instead.</p>
     *
     *  @param  index   The index for the requested parameter; must be 0 or
     *      greater.
     *  @return The requested parameter.
     *  @throws CmdLineException    An attempt is made to access a non-existent
     *      index, or the index is for a non-parameter entry on the command
     *      line.
     */
    public String getParameter( final int index ) throws CmdLineException;

    /**
     *  <p>{@summary Tests whether the given index is for an additional
     *  parameter to a related option.} The first additional parameter has the
     *  index 0, a second one will have 1 and so on.</p>
     *  <p>Assume the command line looks like this:</p>
     *  <pre><code>-o abc def -d x</code></pre>
     *  <p>Then {@code isParameter(0)} and {@code isParameter(1)} would return
     *  {@code true}, but {@code isParameter(3)} returns {@code false}:
     *  {@code x} is an additional parameter, but for the option {@code -d} in
     *  this case.</p>
     *
     *  @param  index   The index for the requested parameter; must be 0 or
     *      greater.
     *  @return {@code true} if the argument at the given location is an
     *      additional parameter for the current option, {@code false} if not.
     *  @since 0.1.2
     */
    @API( status = STABLE, since = "0.1.2" )
    public boolean isParameter( final int index );
}
//  interface Parameters

/*
 *  End of File
 */