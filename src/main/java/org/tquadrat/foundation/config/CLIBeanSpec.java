/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.STABLE;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  When a configuration bean should be initialised from the command line, the
 *  respective specification interface needs to extend this interface.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CLIBeanSpec.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CLIBeanSpec.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public interface CLIBeanSpec extends ConfigBeanSpec
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The escape character for argument files: {@value}.
     */
    public static final String ARG_FILE_ESCAPE = "@";

    /**
     *  The lead-in character for an option name: {@value}.
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    public static final String LEAD_IN = "-";

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Dumps a parameter file template to the given
     *  {@link OutputStream}.
     *
     *  @param  outputStream    The target output stream.
     *  @throws IOException Something went wrong when writing to the output
     *      stream.
     *
     *  @see #parseCommandLine(String[])
     */
    public void dumpParamFileTemplate( final OutputStream outputStream ) throws IOException;

    /**
     *  Parses the command line.<br>
     *  <br>As a result from parsing the given command line arguments, the
     *  accordingly annotated properties will be initialised with the values
     *  from the command line.<br>
     *  <br>Arguments starting with <code>&#64;</code> (like
     *  <code>&#64;param.lst</code>) are treated as a file that contains
     *  further arguments.<br>
     *  <br>Assuming the file {@code param.lst} has the following contents:
     *  <blockquote><pre><code>-opt0
     *value0
     *-opt1
     *value1
     *--
     *arg0
     *arg1</code></pre></blockquote>
     *  and {@code args} looks like this: <code>-opt value &#64;param.lst
     *  arg</code>, the resulting command line arguments set would be:
     *  <pre><code>-opt value -opt0 value0 -opt1 value1 -- arg0 arg1 arg</code></pre>
     *  In case the file could not be opened for whatever reason, the parameter
     *  will not be replaced.
     *
     *  @param  args    The command line arguments; usually the same as the
     *      arguments to the method {@code main()}.
     *  @return {@code true} if the command line could be parsed without
     *      issues, {@code false} otherwise.
     */
    @SuppressWarnings( {"MethodCanBeVariableArityMethod", "BooleanMethodNameMustStartWithQuestion"} )
    public boolean parseCommandLine( final String [] args );

    /**
     *  Prints a <i>usage</i> message to the given
     *  {@link OutputStream}.
     *
     *  @param  outputStream    The output stream.
     *  @param  command The command used to start the program.
     *  @throws IOException A problem occurred on writing to the output stream.
     */
    public void printUsage( final OutputStream outputStream, final CharSequence command ) throws IOException;

    /**
     *  Retrieves the message for the error caused by the last call to
     *  {@link #parseCommandLine(String[])},
     *  given that this return {@code false}.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the error message.
     */
    public Optional<String> retrieveParseErrorMessage();
}
//  interface CLIBeanSpec

/*
 *  End of File
 */