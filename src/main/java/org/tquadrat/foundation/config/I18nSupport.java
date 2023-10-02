/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.config;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.config.SpecialPropertyType.CONFIG_PROPERTY_MESSAGEPREFIX;
import static org.tquadrat.foundation.i18n.I18nUtil.composeMessageKey;
import static org.tquadrat.foundation.i18n.I18nUtil.composeTextKey;
import static org.tquadrat.foundation.i18n.I18nUtil.createFallback;
import static org.tquadrat.foundation.i18n.I18nUtil.retrieveMessage;
import static org.tquadrat.foundation.i18n.I18nUtil.retrieveText;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.i18n.TextUse;

/**
 *  <p>{@summary A configuration bean specification has to extend this
 *  interface in order to get the i18n support.}</p>
 *  <p>The extending bean needs to provide two String constants:</p>
 *  <ul>
 *      <li>The base bundle name, marked with the annotation
 *      {@link org.tquadrat.foundation.i18n.BaseBundleName &#64;BaseBundleName}</li>
 *      <li>The message prefix, marked with the annotation
 *      {@link org.tquadrat.foundation.i18n.MessagePrefix &#64;MessagePrefix}</li>
 *  </ul>
 *  <p>If the annotation processor cannot find these annotations in the
 *  configuration bean specification, it will throw an error. This behaviour is
 *  different from that when the i18n support is used independent from a
 *  configuration bean.</p>
 *  <p>When the methods</p>
 *  <ul>
 *      <li>{@link #getMessage(int,Object...)}</li>
 *      <li>{@link #getMessage(String,Object...)}</li>
 *      <li>{@link #getString(Enum)}</li>
 *      <li>{@link #getText(String, Object...)}</li>
 *      <li>{@link #getText(Class, TextUse, String, Object...)}</li>
 *  </ul> TODO ??
 *
 *  @note   The configuration bean specification may not define a setter for
 *      the resource bundle property if it extends this interface!
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: I18nSupport.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.2
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: I18nSupport.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@API( status = STABLE, since = "0.0.2" )
public interface I18nSupport extends ConfigBeanSpec
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the message for the given message id.
     *
     *  @param  messageId   The message id.
     *  @param  args    The message arguments.
     *  @return The message.
     */
    public default String getMessage( final String messageId, final Object... args )
    {
        final var retValue = getResourceBundle().map( resourceBundle -> retrieveMessage( resourceBundle, getMessagePrefix(), messageId, true, args ) )
            .orElseGet( () -> createFallback( composeMessageKey( getMessagePrefix(), messageId ), args ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getMessage()

    /**
     *  Returns the message for the given message id.
     *
     *  @param  messageId   The message id.
     *  @param  args    The message arguments.
     *  @return The message.
     */
    public default String getMessage( final int messageId, final Object... args )
    {
        final var retValue = getResourceBundle().map( resourceBundle -> retrieveMessage( resourceBundle, getMessagePrefix(), messageId, true, args ) )
            .orElseGet( () -> createFallback( composeMessageKey( getMessagePrefix(), messageId ), args ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getMessage()

    /**
     *  Returns the message prefix.
     *
     *  @return The message prefix.
     *
     *  @see org.tquadrat.foundation.i18n.MessagePrefix
     */
    @SpecialProperty( CONFIG_PROPERTY_MESSAGEPREFIX )
    public String getMessagePrefix();

    /**
     *  Returns the String representation for the given {@code enum}.
     *
     *  @param  <E> The type of the {@code enum} value.
     *  @param  value   The enum value.
     *  @return The String representation.
     */
    public default <E extends Enum<?>> String getString( final E value )
    {
        final var retValue = getResourceBundle()
            .map( bundle -> retrieveText( bundle, value ) )
            .orElse( createFallback( composeTextKey( value ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getString()

    /**
     *  Returns the text for the key that will be composed of the given
     *  components.
     *
     *  @param  sourceClass The class that defines the text.
     *  @param  use The text use.
     *  @param  id  The id for the text.
     *  @param  args    The text arguments.
     *  @return The text.
     *
     *  @see org.tquadrat.foundation.i18n.I18nUtil#composeTextKey(Class, TextUse, String)
     */
    public default String getText( final Class<?> sourceClass, final TextUse use, final String id, final Object... args )
    {
        return getText( composeTextKey( sourceClass, use, id ), args );
    }   //  getText()

    /**
     *  Returns the text for the given key.
     *
     *  @param  key The resource bundle key for the text.
     *  @param  args    The text arguments.
     *  @return The text.
     */
    public default String getText( final String key, final Object... args )
    {
        final var retValue = getResourceBundle()
            .map( bundle -> retrieveText( bundle, key, args ) )
            .orElse( createFallback( key, args ) )
            .translateEscapes();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getText()
}
//  class I18nSupport

/*
 *  End of File
 */