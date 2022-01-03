/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 * http://www.gnu.org/licenses/lgpl.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/**
 *  <p>{@summary The API for the Command Line Interface (CLI)} can also be used
 *  without the annotation processor, in two different ways, as described
 *  below.</p>
 *  <p>Unless otherwise stated, {@code null} argument values will cause methods
 *  and constructors of all classes in this package to throw an
 *  {@link java.lang.Exception Exception},
 *  usually a
 *  {@link org.tquadrat.foundation.exception.NullArgumentException},
 *  but in some rare cases, it could be also a
 *  {@link java.lang.NullPointerException}.</p>
 *
 *  <h2>The Code Approach</h2>
 *  <p>The API can be used directly from the code, without any external
 *  configuration. This may look like this:</p>
 *  <div class="source-container"><pre>  <span class="source-line-no">201</span>&hellip;
 *  <span class="source-line-no">202</span>import static org.tquadrat.foundation.ui.spi.ConfigUtil.parseCommandLine;
 *  <span class="source-line-no">203</span>import static org.tquadrat.foundation.ui.spi.ConfigUtil.printUsage;
 *  <span class="source-line-no">204</span>
 *  <span class="source-line-no">205</span>import java.io.IOException;
 *  <span class="source-line-no">206</span>import java.util.ArrayList;
 *  <span class="source-line-no">207</span>import java.util.List;
 *  <span class="source-line-no">208</span>import java.util.Optional;
 *  <span class="source-line-no">209</span>import java.util.function.BiConsumer;
 *  <span class="source-line-no">210</span>
 *  <span class="source-line-no">211</span>import org.tquadrat.foundation.ui.spi.CLIArgumentDefinition;
 *  <span class="source-line-no">212</span>import org.tquadrat.foundation.ui.spi.CLIDefinition;
 *  <span class="source-line-no">213</span>import org.tquadrat.foundation.ui.spi.CLIOptionDefinition;
 *  <span class="source-line-no">214</span>import org.tquadrat.foundation.ui.spi.CmdLineException;
 *  <span class="source-line-no">215</span>import org.tquadrat.foundation.ui.spi.CmdLineValueHandler;
 *  <span class="source-line-no">216</span>
 *  <span class="source-line-no">217</span>&hellip;
 *  <span class="source-line-no">218</span>
 *  <span class="source-line-no">219</span>&#47;**
 *  <span class="source-line-no">220</span> *  The argument value.
 *  <span class="source-line-no">221</span> *&#47;
 *  <span class="source-line-no">222</span>private String m_ArgumentValue;
 *  <span class="source-line-no">223</span>
 *  <span class="source-line-no">224</span>&#47;**
 *  <span class="source-line-no">225</span> *  The option value.
 *  <span class="source-line-no">226</span> *&#47;
 *  <span class="source-line-no">227</span>private String m_OptionValue;
 *  <span class="source-line-no">228</span>
 *  <span class="source-line-no">229</span>&hellip;
 *  <span class="source-line-no">230</span>
 *  <span class="source-line-no">231</span>private final boolean execute( final String [] args ) throws IOException
 *  <span class="source-line-no">232</span>{
 *  <span class="source-line-no">233</span>    //---* Create the CLI definition *-------------------------------------
 *  <span class="source-line-no">234</span>    final BiConsumer&lt;String,String&gt; argumentValueSetter = ($,value) -&gt; m_ArgumentValue = value;
 *  <span class="source-line-no">235</span>    final BiConsumer&lt;String,String&gt; optionValueSetter = ($,value) -&gt; m_OptionValue = value;
 *  <span class="source-line-no">236</span>    final CmdLineValueHandler&lt;String&gt; argumentHandler = new StringValueHandler( argumentValueSetter );
 *  <span class="source-line-no">237</span>    final CmdLineValueHandler&lt;String&gt; optionHandler = new StringValueHandler( optionValueSetter );
 *  <span class="source-line-no">238</span>    final List&lt;CLIDefinition&gt; cliDefinitions = new ArrayList&lt;&gt;();
 *  <span class="source-line-no">239</span>    cliDefinitions.add(
 *  <span class="source-line-no">240</span>        new CLIArgumentDefinition(
 *  <span class="source-line-no">241</span>            "argument", // The property name; not used in this context
 *  <span class="source-line-no">242</span>            0, // The index for the argument
 *  <span class="source-line-no">243</span>            "The value for the argument", // The usage text for the help
 *  <span class="source-line-no">244</span>            "MSGKEY_Argument", // The resource bundle key; not used here
 *  <span class="source-line-no">245</span>            "ARGUMENT", // The meta variable for the help
 *  <span class="source-line-no">246</span>            true, // Arguments are usually required
 *  <span class="source-line-no">247</span>            argumentHandler, // The value handler
 *  <span class="source-line-no">248</span>            false, // The argument is not multi-value
 *  <span class="source-line-no">249</span>            null ) ); // No special format
 *  <span class="source-line-no">250</span>    cliDefinitions.add(
 *  <span class="source-line-no">251</span>        new CLIOptionDefinition(
 *  <span class="source-line-no">252</span>            "option", // The property name; not used in this context
 *  <span class="source-line-no">253</span>            List.of( "--option", "-o" ), // The option names
 *  <span class="source-line-no">254</span>            "The option", // The usage text for the help
 *  <span class="source-line-no">255</span>            "MSGKEY_Option", // The resource bundle key; not used here
 *  <span class="source-line-no">256</span>            "VALUE", // The meta variable of the option value for the help
 *  <span class="source-line-no">257</span>            false, // Options are usually optional &hellip;
 *  <span class="source-line-no">258</span>            optionHandler, // The value handler
 *  <span class="source-line-no">259</span>            false, // The option value is not multi-value
 *  <span class="source-line-no">260</span>            null ) ); // No special format
 *  <span class="source-line-no">261</span>
 *  <span class="source-line-no">262</span>    //---* Parse the command line *----------------------------------------
 *  <span class="source-line-no">263</span>    var retValue = false;
 *  <span class="source-line-no">264</span>    try
 *  <span class="source-line-no">265</span>    {
 *  <span class="source-line-no">266</span>        parseCommandLine( cliDefinitions, args );
 *  <span class="source-line-no">267</span>        retValue = true; // Success!
 *  <span class="source-line-no">268</span>    }
 *  <span class="source-line-no">269</span>    catch( final CmdLineException e )
 *  <span class="source-line-no">270</span>    {
 *  <span class="source-line-no">271</span>        err.println( e.getLocalizedMessage() );
 *  <span class="source-line-no">272</span>        printUsage( err, Optional.empty(), getClass().getSimpleName(), cliDefinitions );
 *  <span class="source-line-no">273</span>    }
 *  <span class="source-line-no">274</span>
 *  <span class="source-line-no">275</span>    //---* Done *----------------------------------------------------------
 *  <span class="source-line-no">276</span>    return retValue;
 *  <span class="source-line-no">277</span>}   //  execute()
 *  <span class="source-line-no">278</span>
 *  <span class="source-line-no">279</span>&hellip;</pre></div>
 *
 *  TODO Check the statement below!!
 *  <p>For another sample, refer to the source of
 *  { link org.tquadrat.foundation.ui.tools.ShowCLISpec}.</p>
 *
 *  <h2>Configuration of the Command Line with XML</h2>
 *  <p>The command line can be defined in an XML file that could be provided as
 *  a resource or on the file system; the method
 *  {@link org.tquadrat.foundation.config.ConfigUtil#parseCommandLine(java.io.InputStream, boolean, String...) parseCommandLine()}
 *  just expects an instance of
 *  {@link java.io.InputStream}
 *  with the XML. The drawback of this approach is that the value from the
 *  command line will be returned as an instance of
 *  {@link java.util.Map Map&lt;String,Object&gt;}
 *  that uses the {@code propertyName} (see the DTD below) as the key, instead
 *  of storing them directly to attributes, making the access less
 *  type-safe.</p>
 *  <p>The XML needs to confirm the DTD below, and it can be validated against
 *  the also provided XML Schema file.</p>
 *  <p>Sample code:</p>
 *
 *  <div class="source-container"><pre>  <span class="source-line-no">201</span>&hellip;
 *  <span class="source-line-no">201</span>final var cliDefinition = getClass().getResourceAsStream( CLI_DEFINITION_XML );
 *  <span class="source-line-no">202</span>final Map&lt;String,Object&gt; cmdLineData = ConfigUtil.parseCommandLine( cliDefinition, false, args );;
 *  <span class="source-line-no">203</span>&hellip;</pre></div>
 *
 *  <p>Of course the error handling was omitted here.</p>
 *
 *  <h3>The DTD</h3>
 *  {@include ${resources}/CLIDefinition.dtd:SOURCE}
 *
 *  <h3>The XML Schema</h3>
 *  {@include ${resources}/CLIDefinition.xsd:SOURCE}
 *
 *  TODO Check the statement below!!
 *  <p>The above mentioned class
 *  { link org.tquadrat.foundation.ui.tools.ShowCLISpec}
 *  can be used to dump both the DTD and the Schema file to load them into
 *  a validating XML editor or alike.</p>
 */

package org.tquadrat.foundation.config.cli;

/*
 *  End of File
 */