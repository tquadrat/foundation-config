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
 *  @version $Id: Parameters.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"InterfaceNeverImplemented", "InterfaceMayBeAnnotatedFunctional"} )
@ClassVersion( sourceVersion = "$Id: Parameters.java 884 2021-03-22 18:02:51Z tquadrat $" )
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
    public String getParameter( int index ) throws CmdLineException;
}
//  interface Parameters

/*
 *  End of File
 */