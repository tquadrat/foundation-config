/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
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

package org.tquadrat.foundation.config.internal;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.config.internal.MessageRegistry.MSG_REGISTRY_FALLBACK;
import static org.tquadrat.foundation.config.internal.MessageRegistry.m_MessageRegistry;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.i18n.BaseBundleName;
import org.tquadrat.foundation.i18n.I18nUtil;
import org.tquadrat.foundation.i18n.MessagePrefix;
import org.tquadrat.foundation.i18n.UseAdditionalTexts;
import org.tquadrat.foundation.lang.Objects;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The internal tools for the configuration module.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Commons.java 1016 2022-02-09 16:51:08Z tquadrat $
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@UtilityClass
@ClassVersion( sourceVersion = "$Id: Commons.java 1016 2022-02-09 16:51:08Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
@UseAdditionalTexts
public final class Commons
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name of the resource bundle with the messages and texts for this
     *  module: {@value}.
     */
    @SuppressWarnings( "DefaultAnnotationParam" )
    @BaseBundleName( defaultLanguage = "en" )
    public static final String BASE_BUNDLE_NAME = "org.tquadrat.foundation.config.MessagesAndTexts";

    /**
     *  The message prefix: {@value}.
     */
    @MessagePrefix
    public static final String MESSAGE_PREFIX = "CFG";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The resource bundle that is used by this module.
     */
    private static final ResourceBundle m_ResourceBundle;

    static
    {
        final var module = Commons.class.getModule();
        try
        {
            if( module.isNamed() )
            {
                m_ResourceBundle = ResourceBundle.getBundle( BASE_BUNDLE_NAME, module );
            }
            else
            {
                m_ResourceBundle = ResourceBundle.getBundle( BASE_BUNDLE_NAME );
            }
        }
        catch( final MissingResourceException e )
        {
            final var error = new ExceptionInInitializerError( format( "Cannot load ResourceBundle '%s'", BASE_BUNDLE_NAME ) );
            error.initCause( e );
            throw error;
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private Commons() { throw new PrivateConstructorForStaticClassCalledError( Commons.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates an instance of
     *  {@link org.tquadrat.foundation.config.CmdLineException}
     *  based on the given arguments.
     *   @param  stringConverterClass    The class of the
     *      {@link StringConverter}
     *      implementation.
     *  @param  cause   The causing
     *      {@link Exception}.
     *  @param  value   The problematic value.
     *  @return The instance of
     *      {@link CmdLineException}.
     */
    @SuppressWarnings( {"rawtypes", "UseOfConcreteClass"} )
    public static final CmdLineException createException( final Class<? extends StringConverter> stringConverterClass, final Throwable cause, final String value )
    {
        final var entry = m_MessageRegistry.getOrDefault( requireNonNullArgument( stringConverterClass, "stringConverterClass" ), MSG_REGISTRY_FALLBACK );
        final var retValue = new CmdLineException( entry.message(), cause, entry.key(), value );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createException()

    /**
     *  <p>{@summary Retrieves the message with the given key from the resource
     *  bundle and applies the given arguments to it.}</p>
     *  <p>If the resource bundle does not contain a message for the given key,
     *  the key itself will be returned, appended with the arguments.</p>
     *
     *  @param  key The key for the message.
     *  @param  addKey  The recommended value is {@code true}; this means that
     *      the message will be prefixed with the generated message key.
     *  @param  args    The arguments for the message.
     *  @return The text.
     *
     *  @see Objects#toString(Object)
     */
    public static final String retrieveMessage( final int key, final boolean addKey, final Object... args )
    {
        final var retValue = I18nUtil.retrieveMessage( m_ResourceBundle, MESSAGE_PREFIX, key, addKey, args );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveMessage()

    /**
     *  <p>{@summary Retrieves the text with the given key from the resource
     *  bundle and applies the given arguments to it.}</p>
     *  <p>If the resource bundle does not contain a text for the given key,
     *  the key itself will be returned, appended with the arguments.</p>
     *
     *  @param  key The key for the text.
     *  @param  args    The arguments for the text.
     *  @return The text.
     *
     *  @see Objects#toString(Object)
     */
    public static final String retrieveText( final String key, final Object... args )
    {
        final var retValue = I18nUtil.retrieveText( m_ResourceBundle, key, args );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveText()
}
//  class Commons

/*
 *  End of File
 */