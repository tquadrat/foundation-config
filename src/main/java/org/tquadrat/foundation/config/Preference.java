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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor;

/**
 *  {@summary Forces a property to have a preferences reference and configures
 *  it.} Usually, all attributes of a property that are required for the
 *  preferences will be inferred from the property itself. This annotation
 *  allows to modify these default values.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Preference.java 941 2021-12-18 22:34:37Z tquadrat $
 *  @since 0.0.1
 */
@ClassVersion( sourceVersion = "$Id: Preference.java 941 2021-12-18 22:34:37Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.0.1" )
public @interface Preference
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary The accessor that is used to set the property from the
     *  preference, and the preference from the property value. For most types,
     *  this can be derived from the type, but for others, it needs to be set
     *  explicitly.}</p>
     *  <p>The value
     *  {@link PreferenceAccessor PreferenceAccessor.class}
     *  indicates the default setting.</p>
     *
     *  @return The class for the accessor.
     *
     *  @see PreferenceAccessor
     */
    Class<?> accessor() default PreferenceAccessor.class;

    /**
     *  The key for the preference; the default is the property name.
     *
     *  @return The key.
     */
    String key() default "";
}
//  class Preference

/*
 *  End of File
 */