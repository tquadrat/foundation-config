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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.config.cli.DateValueHandler;

/**
 *  <p>{@summary This annotation is used in the context of a configuration bean
 *  specification to mark a property that receives the value of a command line
 *  argument.} It will be placed to the getter for the property.</p>
 *  <p>A command line argument will be identified by its relative position on
 *  the parameter list, after any
 *  {@linkplain Option command line options}.
 *  Therefore it has an index, where an option has a name.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Kohsuke Kawaguchi - kk@kohsuke.org
 *  @thanks Mark Sinke
 *  @version $Id: Argument.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Argument.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.0.1" )
public @interface Argument
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary Some value handlers (like the
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
    public Class<?> handler() default CmdLineValueHandler.class;

    /**
     *  A command line argument is identified by its relative position on the
     *  command line, instead by a name. The first position has the index 0,
     *  the second is 1 and so on.
     *
     *  @return The index for the argument.
     */
    int index();

    /**
     *  <p>{@summary A name for the argument that is used in messages.}</p>
     *  <p>If left unspecified, that name is inferred from the name of the
     *  configuration property itself.</p>
     *
     *  @return A meta variable string.
     */
    String metaVar() default "";

    /**
     *  A flag that indicates whether the argument is multivalued, for
     *  mappings to a
     *  {@link java.util.Collection Collection}.
     *  As this will consume all remaining arguments from the command line,
     *  the annotated property has to be the last argument.
     *
     *  @return {@code true} if the argument is multivalued,
     *      {@code false} otherwise.
     */
    boolean multiValued() default false;

    /**
     *  A flag that specifies whether this argument is mandatory. This implies
     *  that all previous arguments (those with lower indexes) are mandatory as
     *  well.
     *
     *  @return {@code true} if the argument is mandatory,
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
//  annotation Argument

/*
 *  End of File
 */