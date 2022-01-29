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

import java.nio.charset.Charset;
import java.time.Clock;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The types of the special properties for a configuration bean.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SpecialPropertyType.java 920 2021-05-23 14:27:24Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 *
 *  @see SpecialProperty
 */
@ClassVersion( sourceVersion = "$Id: SpecialPropertyType.java 920 2021-05-23 14:27:24Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public enum SpecialPropertyType
{
        /*------------------*\
    ====** Enum Declaration **=================================================
        \*------------------*/
    /**
     *  <p>{@summary The current
      * {@link Charset}.}</p>
     *  <p>The property will be initialised with a call to
     *  {@link java.nio.charset.Charset#defaultCharset()},
     *  and the same will be made also when the setter is called with the
     *  argument {@code null} later.</p>
     *  <p>Getter and setter for this special are defined in
     *  {@link ConfigBeanSpec}.</p>
     *
     *  @see java.nio.charset.Charset
     *  @see ConfigBeanSpec#getCharset()
     *  @see ConfigBeanSpec#setCharset(java.nio.charset.Charset)
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_CHARSET( "charset" ),

    /**
     *  <p>{@summary The current
      * {@link Clock}.}</p>
     *  <p>The property will be initialised with a call to
     *  {@link java.time.Clock#systemUTC()},
     *  and the same call will be made also when the setter is called with the
     *  argument {@code null} later.</p>
     *  <p>Getter and setter for this special property can be added to a
     *  configuration bean specification.</p>
     *
     *  @see java.time.Clock
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_CLOCK( "clock" ),

    /**
     *  <p>{@summary The current
      * {@link Locale}.}</p>
     *  <p>The property will be initialised with a call to
     *  {@link java.util.Locale#getDefault()},
     *  and the same call will be made also when the setter is called with the
     *  argument {@code null}.</p>
     *  <p>Getter and setter for this property are defined in
     *  {@link ConfigBeanSpec}.</p>
     *
     *  @see java.util.Locale
     *  @see ConfigBeanSpec#getLocale()
     *  @see ConfigBeanSpec#setLocale(java.util.Locale)
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_LOCALE( "locale" ),

    /**
     *  <p>{@summary The message prefix.}</p>
     *  <p>A getter for this special property is added to a configuration bean
     *  specification with the
     *  {@link I18nSupport};
     *  a setter for it is not allowed.</p>
     */
    @API( status = STABLE, since = "0.1.0" )
    CONFIG_PROPERTY_MESSAGEPREFIX( "messagePrefix" ),

    /**
     *  <p>{@summary The process id.}</p>
     *  <p>A getter for this special property can be added to a configuration
     *  bean specification; a setter for it is not allowed.</p>
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_PID( "processId" ),

    /**
     *  <p>{@summary A random number generator.}</p>
     *  <p>A getter for this special property can be added to a configuration
     *  bean specification; a setter for it is not allowed.</p>
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_RANDOM( "random" ),

    /**
     *  <p>{@summary The
     *  {@link ResourceBundle}
     *  for messages and other texts.}</p>
     *  <p>A getter for this property is defined in
     *  {@link ConfigBeanSpec},
     *  a setter is optional.</p>
     *
     *  @note   This property is also used internally.
     *
     *  @see ConfigBeanSpec#getResourceBundle()
     *  @see I18nSupport
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_RESOURCEBUNDLE( "resourceBundle" ),

    /**
     *  <p>{@summary The session key.}</p>
     *  <p>A getter for this special property is defined in
     *  {@link SessionBeanSpec};
     *  a setter for it is not allowed, and the special property may not be
     *  used in other contexts than that of a session bean.</p>
     *
     *  @see SessionBeanSpec#getSessionKey()
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_SESSION( "session" ),

    /**
     *  <p>{@summary The current time zone.}</p>
     *  <p>The property will be initialised with a call to
     *  {@link java.time.ZoneId#systemDefault()},
     *  and this call will be made also when the setter is called with the
     *  argument {@code null} later.</p>
     *  <p>Getter and setter for this property are defined in
     *  {@link ConfigBeanSpec}.</p>
     *
     *  @see java.time.ZoneId
     *  @see ConfigBeanSpec#getTimezone()
     *  @see ConfigBeanSpec#setTimezone(java.time.ZoneId)
     */
    @API( status = STABLE, since = "0.0.1" )
    CONFIG_PROPERTY_TIMEZONE( "timezone" );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The name of the property.
     */
    private final String m_PropertyName;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new instance of {@code SpecialPropertyType}.
     *
     *  @param  name    The name of the property.
     */
    private SpecialPropertyType( final String name )
    {
        m_PropertyName = name;
    }   //  SpecialPropertyType()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the property name for the special property.
     *
     *  @return The property name.
     */
    public final String getPropertyName() { return m_PropertyName; }
}
//  enum SpecialPropertyType

/*
 *  End of File
 */