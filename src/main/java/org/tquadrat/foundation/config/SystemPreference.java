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
 *  <p>{@summary This annotation indicates that the property for the annotated
 *  getter is initialised from a {@code SYSTEM}
 *  {@linkplain java.util.prefs.Preferences Preferences value}
 *  with the path and name.}</p>
 *  <p>This annotation implies the
 *  {@link NoPreference}
 *  annotation.</p>
 *
 *  @note   Even when no setter is defined for a property with this annotation
 *      – making it effectively immutable – the field for it will not be
 *      {@code final}. This is because of the way the initialisation works.
 *
 *  @note   Changes to the value of a property marked with this annotation will
 *      not be reflected to the SYSTEM {@code Preferences}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SystemPreference.java 942 2021-12-20 02:04:04Z tquadrat $
 *  @since 0.0.1
 *
 *  @see java.util.prefs.Preferences#get(String, String)
 */
@ClassVersion( sourceVersion = "$Id: SystemPreference.java 942 2021-12-20 02:04:04Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.0.1" )
public @interface SystemPreference
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The key for the {@code SYSTEM} preference value to read.
     *
     *  @return The key for the {@code SYSTEM} preference.
     */
    String key();

    /**
     *  The path for the {@code SYSTEM} preference node that holds the value.
     *
     *  @return The path for the {@code SYSTEM} preference node.
     */
    String path();

    /**
     *  <p>{@summary The implementation of
     *  {@link PreferenceAccessor}
     *  that is used to access the {@code Preferences} and to translate the
     *  value into the type of the property.}</p>
     *  <p>This is mandatory, no default is used, nor will it be somehow
     *  inferred.
     *
     *  @return The class for the preferences accessor.
     */
    @API( status = STABLE, since = "0.1.0" )
    Class<? extends PreferenceAccessor<?>> accessor();
}
//  annotation SystemPreference

/*
 *  End of File
 */