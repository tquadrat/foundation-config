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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.config.cli.DateValueHandler;

/**
 *  <p>{@summary This annotation is used in the context of a configuration bean
 *  specification to mark a property that receives the value of a command line
 *  option.} It will be placed to the getter for the property.</p>
 *  <p>A command line option has a name that identifies it on the command line
 *  so that it can appear nearly everywhere in the parameter list, but of
 *  course before all
 *  {@linkplain Argument arguments}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Kohsuke Kawaguchi - kk@kohsuke.org
 *  @thanks Mark Sinke
 *  @version $Id: Option.java 907 2021-05-05 23:09:17Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Option.java 907 2021-05-05 23:09:17Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.0.1" )
public @interface Option
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary Aliases for the
     *  {@linkplain #name() option name}.}</p>
     *  <p>The aliases has to follow the same rules as for the option name
     *  itself.</p>
     *
     *  @return The list of aliases.
     *
     *  @see #name()
     */
    String [] aliases() default {};

    /**
     *  <p>{@summary Some special value handlers (like the
     *  {@link DateValueHandler DateValueHandler})
     *  use this field for additional validation information, like a format
     *  String.} It is ignored by most others.</p>
     *  <p>Refer to the documentation of those value handlers for the exact
     *  contents specification.</p>
     *
     *  @return The extended format specification according to the option
     *      handler, or the empty String.
     */
    String format() default "";

    /**
     *  <p>{@summary Specifies the
     *  {@linkplain CmdLineValueHandler command line value handler}
     *  that translates the command line argument value to the type of the
     *  target property and places that value to the property.}</p>
     *  <p>The default value {@code CmdLineValueHandler.class} indicates that
     *  the effective {@code CmdLineValueHandler} implementation will be
     *  inferred from the type of the annotated property.</p>
     *  <p>If it is set to a class that extends
     *  {@link CmdLineValueHandler},
     *  an instance of that class will be created (therefore it has to provide
     *  a constructor with the signature
     *  {@code &lt;<i>Constructor</i>&gt;(CLIDefinition, BiConsumer)})
     *  that is used as the handler. This is convenient for defining a
     *  non-standard option parsing semantics.</p>
     *  <p><b>Example</b></p>
     *  <pre><code>
     *  // this is a normal &quot;-r&quot; option
     *  &#064;Option()
     *  boolean getFlag();
     *
     *  // This causes that MyHandler is used instead of the default handler
     *  // provided for boolean
     *  &#064;Option( handler = MyHandler.class )
     *  boolean getYesNo();</code></pre>
     *
     *  @return The {@code CmdLineValueHandler} implementation.
     */
    Class<?>  handler() default CmdLineValueHandler.class;

    /**
     *  <p>{@summary A name for the option value that is used in messages.}</p>
     *  <p>If left unspecified, that name is inferred from the type of the
     *  configuration property itself.</p>
     *
     *  @return A meta variable string.
     */
    String metaVar() default "";

    /**
     *  <p>{@summary A flag that indicates whether the option is multi-valued,
     *  for mappings to a
     *  {@link Collection Collection}.}</p>
     *  <p>If set to {@code true}, the same option can appear multiple times on
     *  the command line, and each value will be added to the underlying data
     *  structure.</p>
     *
     *  @return {@code true} if the option is multivalued,
     *      {@code false} otherwise.
     */
    boolean multiValued() default false;

    /**
     *  <p>{@summary The name of the option.} It has to be either a single dash
     *  (&quot;-&quot;), followed by a single character (short option), or two
     *  dashes followed by more than one character (long option).</p>
     *  <p>The name may contain letters, numbers, and most special characters
     *  that are allowed on a command line, but no whitespace characters.</p>
     *  <p>Some samples:</p>
     *  <ul>
     *      <li>{@code -r}</li>
     *      <li>{@code --port}</li>
     *      <li>{@code -1} &ndash; valid but not really recommended</li>
     *      <li>{@code --} &ndash; invalid, but allowed on the command line,
     *          having a special meaning there</li>
     *      <li>{@code -} &ndash; invalid</li>
     *      <li>{@code -name} &ndash; invalid: had to be started with two
     *          dashes</li>
     *      <li>{@code --f} &ndash; invalid: not enough characters after the
     *          two dashes</li>
     *      <li>{@code --port-number} &ndash; valid, but dashes within the name
     *          are discouraged</li>
     *      <li>{@code --port number} &ndash; invalid because of the blank</li>
     *      <li>{@code --port_number}</li>
     *      <li>{@code -@} &ndash; valid but strongly discouraged</li>
     *  </ul>
     *
     *  @return The option name.
     *
     *  @see org.tquadrat.foundation.config.CLIBeanSpec#LEAD_IN
     */
    String name();

    /**
     *  <p>{@summary Specifies that the option is mandatory.} This implies that
     *  the return type of the getter that is annotated with this annotation
     *  may not be
     *  {@link java.util.Optional}.</p>
     *  <p>Note that in most of the command line interface design principles,
     *  options should be really optional. So use caution when using this flag.
     *  Consequently the default is {@code false}.</p>
     *
     *  @return {@code true} if the option is mandatory,
     *      {@code false} otherwise.
     */
    boolean required() default false;

    /**
     *  <p>{@summary A help text that will be displayed in the usage output if
     *  {@link ConfigBeanSpec#getResourceBundle() ConfigBeanSpec.getResourceBundle()}
     *  returns no
     *  {@link ResourceBundle ResourceBundle}
     *  instance or the call to
     *  {@link ResourceBundle#getString(String) getString()}
     *  with the value of
     *  {@link #usageKey()}
     *  on the retrieved resources throws a
     *  {@link MissingResourceException}.}</p>
     *  <p>The default is the empty String.</p>
     *
     *  @return The usage help text.
     */
    String usage() default "";

    /**
     *  <p>{@summary The
     *  {@linkplain ResourceBundle#getString(String) resource bundle key}
     *  for a help text that will be displayed in the usage output.}</p>
     *  <p>If not specified, the value will be derived from the name of the
     *  property like this:</p>
     *  <pre><code>  USAGE_&lt;<i>PropertyName</i>&gt;</code></pre>
     *  <p>The text will be retrieved from the
     *  {@link java.util.ResourceBundle ResourceBundle}
     *  that is returned from
     *  {@link org.tquadrat.foundation.config.ConfigBeanSpec#getResourceBundle() ConfigBeanSpec.getResourceBundle()};
     *  if that is {@code null} the value of
     *  {@link #usage()}
     *  is taken instead.</p>
     *  <p>This allows to localise the usage output.</p>
     *
     *  @return The resource bundle key for the usage text.
     *
     *  @see org.tquadrat.foundation.config.spi.CLIDefinition#USAGE_KEY_FORMAT
     */
    String usageKey() default "";
}
//  annotation Option

/*
 *  End of File
 */