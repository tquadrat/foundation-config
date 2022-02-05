/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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
import static org.tquadrat.foundation.config.SpecialPropertyType.CONFIG_PROPERTY_CHARSET;
import static org.tquadrat.foundation.config.SpecialPropertyType.CONFIG_PROPERTY_LOCALE;
import static org.tquadrat.foundation.config.SpecialPropertyType.CONFIG_PROPERTY_RESOURCEBUNDLE;
import static org.tquadrat.foundation.config.SpecialPropertyType.CONFIG_PROPERTY_TIMEZONE;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_IS_DEBUG;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_IS_TEST;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.PropertyName;

/**
 *  <p>{@summary The base for the specification of a configuration bean; the
 *  final specification interface must also be annotated with
  * {@link ConfigurationBeanSpecification &#64;ConfigurationBeanSpecification}
  * in order to be recognised properly.}</p>
 *  <p>The generation process will generate only getter and setter methods,
 *  other methods must be provided as {@code default}.</p>
 *  <p>If the configuration bean should be initialised through command line
 *  arguments, the specification interface must extend
 *  {@link org.tquadrat.foundation.config.CLIBeanSpec},
 *  and if it should work with
 *  {@link java.util.prefs.Preferences Preferences},
 *  the interface needs to extend
 *  {@link org.tquadrat.foundation.config.PreferencesBeanSpec}. It could
 *  extend both.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigBeanSpec.java 1010 2022-02-05 19:28:36Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ConfigBeanSpec.java 1010 2022-02-05 19:28:36Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public interface ConfigBeanSpec
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a new
     *  {@link ConfigurationChangeListener}
     *  to this configuration bean.
     *
     *  @param  listener    The new listener.
     */
    public void addListener( final ConfigurationChangeListener listener );

    /**
     *  Removes the given
     *  {@link ConfigurationChangeListener}
     *  from this configuration bean.
     *
     *  @param  listener    The listener to remove.
     */
    public void removeListener( final ConfigurationChangeListener listener );

    /**
     *  Returns the
     *  {@link Charset}
     *  that is used throughout the program run. If it was not modified by a
     *  call to
     *  {@link #setCharset(Charset)},
     *  this method will return the default {@code Charset} or a
     *  {@code Charset} that was previously stored as a preference value, if
     *  configured.<br>
     *  <br>The {@code Charset} cannot be set via a self-defined command line
     *  option, but through JVM options:
     *  <pre><code>java -Dfile.encoding=&lt;<i>charset</i>&gt; &lt;<i>program</i>&gt; &lt;<i>program_options</i> &hellip;&gt;</code></pre>
     *
     *  @return The {@code Charset}.
     *
     *  @see Charset#defaultCharset()
     *  @see org.tquadrat.foundation.lang.CommonConstants#PROPERTY_FILE_ENCODING
     */
    @SpecialProperty( CONFIG_PROPERTY_CHARSET )
    @ExemptFromToString
    public Charset getCharset();

    /**
     *  Returns the locale that is used throughout the program run. If it was
     *  not modified by a call to
     *  {@link #setLocale(Locale)}
     *  this method will return the default locale or a locale that was
     *  previously stored as a preference value, if configured.<br>
     *  <br>The locale cannot be set via a self-defined command line option,
     *  but through JVM options:
     *  <pre><code>java -Duser.country=&lt;<i>country_code</i>&gt; -Duser.language=&lt;<i>language_code</i>&gt; &lt;<i>program</i>&gt; &lt;<i>program_options</i> &hellip;&gt;</code></pre>
     *
     *  @return The locale.
     *
     *  @see Locale#getDefault()
     *  @see org.tquadrat.foundation.lang.CommonConstants#PROPERTY_USER_COUNTRY
     *  @see org.tquadrat.foundation.lang.CommonConstants#PROPERTY_USER_LANG
     */
    @SpecialProperty( CONFIG_PROPERTY_LOCALE )
    public Locale getLocale();

    /**
     *  <p>{@summary Returns the
     *  {@link ResourceBundle}
     *  that should be used to retrieve texts and messages for the
     *  application.}</p>
     *  <p>Usually the resource bundle is set under the hood (i.e. when the
     *  configuration bean specification implements
     *  {@link org.tquadrat.foundation.config.I18nSupport I18nSupport})
     *  or with the method {@code initData()}. But if necessary, a setter for
     *  this attribute can be added to the configuration bean specification
     *  like this:</p>
     *  <pre><code>  &#47;**
     *   *  Sets the
     *   *  {&#64;link ResourceBundle}
     *   *  that should be used to retrieve texts and messages for the
     *   *  application.&lt;br&gt;
     *   *  &lt;br&gt;As default, there is no {&#64;code ResourceBundle} set
     *   *  ({&#64;link #getResourceBundle()}
     *   *  returns
     *   *  {&#64;link Optional#empty()}),
     *   *  but it not allowed to call this method with the argument {&#64;code null}
     *   *  for the {&#64;code bundle} argument.
     *   *
     *   *  &#64;param  bundle  The resource bundle.
     *   *&#47;
     *  &#64;SpecialProperty( CONFIG_PROPERTY_RESOURCEBUNDLE )
     *  &#64;CheckNull
     *  public void setResourceBundle( final ResourceBundle bundle );</code></pre>
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the resource bundle.
     */
    @SpecialProperty( CONFIG_PROPERTY_RESOURCEBUNDLE )
    @ExemptFromToString
    public Optional<ResourceBundle> getResourceBundle();

    /**
     *  Returns the time zone that is used throughout the program run. If it
     *  was not modified by a call to
     *  {@link #setTimezone(ZoneId)}
     *  this method will return the default time zone or a time zone that was
     *  previously stored as a preference value, if configured.<br>
     *  <br>The time zone cannot be set via a self-defined command line option,
     *  but through JVM options:
     *  <pre><code>java -Duser.timezone=&lt;<i>zone_id</i>&gt; &lt;<i>program</i>&gt; &lt;<i>program_options</i> &hellip;&gt;</code></pre>
     *
     *  @return The time zone.
     *
     *  @see ZoneId#systemDefault()
     *  @see org.tquadrat.foundation.lang.CommonConstants#PROPERTY_USER_TIMEZONE
     */
    @SpecialProperty( CONFIG_PROPERTY_TIMEZONE )
    public ZoneId getTimezone();

    /**
     *  Returns a flag that indicates whether the application should produce
     *  debug output of some kind.
     *
     *  @return {@code true} if the application is in debug mode, {@code false}
     *      otherwise.
     *
     *  @see org.tquadrat.foundation.lang.CommonConstants#PROPERTY_IS_DEBUG
     */
    @SystemProperty( value = PROPERTY_IS_DEBUG, defaultValue = "false" )
    @PropertyName( "isDebug" )
    public boolean isDebug();

    /**
     *  Returns a flag that indicates whether the application should produce
     *  test output of some kind.
     *
     *  @return {@code true} if the application is in test mode, {@code false}
     *      otherwise.
     *
     *  @see org.tquadrat.foundation.lang.CommonConstants#PROPERTY_IS_TEST
     */
    @SystemProperty( value = PROPERTY_IS_TEST, defaultValue = "false"  )
    @PropertyName( "isTest")
    public boolean isTest();

    /**
     *  Sets the
     *  {@link Charset}
     *  that should be used throughout the program run.<br>
     *  <br>Setting a new {@code Charset} with this method will not change the
     *  system default {@code Charset}.
     *
     *  @param  charset The new {@code Charset}; if {@code null}, the
     *      {@code Charset} will be set to the default locale.
     *
     *  @see Charset#defaultCharset()
     */
    @SpecialProperty( CONFIG_PROPERTY_CHARSET )
    public void setCharset( final Charset charset );

    /**
     *  Sets the locale that should be used throughout the program run.<br>
     *  <br>Setting a new locale with this method will not change the system
     *  default locale.
     *
     *  @param  locale  The new locale; if {@code null}, the locale will be set
     *      to the default locale.
     *
     *  @see Locale#getDefault()
     */
    @SpecialProperty( CONFIG_PROPERTY_LOCALE )
    public void setLocale( final Locale locale );

    /**
     *  Sets the time zone that should be used throughout the program run.<br>
     *  <br>Setting a new time zone with this method will not change the system
     *  default time zone.
     *
     *  @param  timezone    The new time zone; if {@code null} the time zone
     *      will be set to the default time zone.
     */
    @SpecialProperty( CONFIG_PROPERTY_TIMEZONE )
    public void setTimezone( final ZoneId timezone );
}
//  class ConfigBeanSpec

/*
 *  End of File
 */