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

package org.tquadrat.foundation.config.spi;

import static org.tquadrat.foundation.config.internal.Commons.retrieveMessage;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.io.Serial;
import java.util.prefs.Preferences;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;

/**
 *  The is exception will be thrown by implementations of
 *  {@link PreferenceAccessor}
 *  in cases when the value from the preferences node (that is stored there as
 *  a
 *  {@link String})
 *  cannot be converted into the target format, for whatever reason.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: InvalidPreferenceValueException.java 914 2021-05-07 21:22:12Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: InvalidPreferenceValueException.java 914 2021-05-07 21:22:12Z tquadrat $" )
public class InvalidPreferenceValueException extends IllegalArgumentException
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message indicating an invalid preference value.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @Message
    (
        description = "The message indicating an invalid preference value.",
        translations =
        {
            @Translation( language = "en", text = "Preference value for '%2$s' from '%1$s' cannot converted to target format" )
        }
    )
    public static final int MSGKEY_InvalidValue1 = 37;

    /**
     *  The message indicating an invalid preference value.
     */
    @SuppressWarnings( "SimplifiableAnnotation" )
    @Message
    (
        description = "The message indicating an invalid preference value.",
        translations =
        {
            @Translation( language = "en", text = "Preference value '%3$s' for '%2$s' from '%1$s' cannot converted to target format" )
        }
    )
    public static final int MSGKEY_InvalidValue2 = 38;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code InvalidPreferenceValueException}.
     *
     *  @param  preferences The preferences node.
     *  @param  propertyName    The name of the property.
     */
    public InvalidPreferenceValueException( final Preferences preferences, final String propertyName )
    {
        super( retrieveMessage( MSGKEY_InvalidValue1, true, requireNonNullArgument( preferences, "preferences" ).absolutePath(), requireNotEmptyArgument( propertyName, "propertyName" ) ) );
    }   //  InvalidPreferencesValueException()

    /**
     *  Creates a new {@code InvalidPreferenceValueException}.
     *
     *  @param  preferences The preferences node.
     *  @param  propertyName    The name of the property.
     *  @param  value   The invalid value.
     */
    public InvalidPreferenceValueException( final Preferences preferences, final String propertyName, final String value )
    {
        super( retrieveMessage( MSGKEY_InvalidValue1, true, requireNonNullArgument( preferences, "preferences" ).absolutePath(), requireNotEmptyArgument( propertyName, "propertyName" ), value ) );
    }   //  InvalidPreferencesValueException()

    /**
     *  Creates a new {@code InvalidPreferenceValueException}.
     *
     *  @param  preferences The preferences node.
     *  @param  propertyName    The name of the property.
     *  @param  cause   The exception that caused the failure.
     */
    public InvalidPreferenceValueException( final Preferences preferences, final String propertyName, final Throwable cause )
    {
        super( retrieveMessage( MSGKEY_InvalidValue1, true, requireNonNullArgument( preferences, "preferences" ).absolutePath(), requireNotEmptyArgument( propertyName, "propertyName" ) ), cause );
    }   //  InvalidPreferencesValueException()

    /**
     *  Creates a new {@code InvalidPreferenceValueException}.
     *
     *  @param  preferences The preferences node.
     *  @param  propertyName    The name of the property.
     *  @param  value   The invalid value.
     *  @param  cause   The exception that caused the failure.
     */
    public InvalidPreferenceValueException( final Preferences preferences, final String propertyName, final String value, final Throwable cause )
    {
        super( retrieveMessage( MSGKEY_InvalidValue1, true, requireNonNullArgument( preferences, "preferences" ).absolutePath(), requireNotEmptyArgument( propertyName, "propertyName" ), value ), cause );
    }   //  InvalidPreferencesValueException()
}
//  class InvalidPreferenceValueException

/*
 *  End of File
 */