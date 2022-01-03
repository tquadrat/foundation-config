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

package org.tquadrat.foundation.config;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary The marker for properties that will be persisted in an
 *  {@code INI} file.}</p>
 *  <p>This annotation implies the
 *  {@link NoPreference &#64;NoPreference}
 *  annotation.</p>
 *  <p>Any type of a property can be persisted to an {@code INI} file as long
 *  as a proper implementation of
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  can be provided for that type.</p>
 *
 *  @version $Id: INIValue.java 946 2021-12-23 14:48:19Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @since 0.1.0
 *
 *  @see StringConversion &#64;StringConversion
 */
@ClassVersion( sourceVersion = "$Id: INIValue.java 946 2021-12-23 14:48:19Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.1.0" )
public @interface INIValue
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The comment for the value.
     *
     *  @return The comment.
     */
    public String comment() default "";

    /**
     *  The mandatory group for the value in the configuration file.
     *
     *  @return The group.
     */
    public String group();

    /**
     *  The key for the value in the configuration file. If not provided, the
     *  property name is used instead.
     *
     *  @return The key.
     */
    public String key() default "";
}
//  @interface INIValue

/*
 *  End of File
 */