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
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary This annotation defines the implementation of
 *  {@link StringConverter}
 *  that is used to convert the value of annotated property into a String, or
 *  a String into the value.}</p>
 *  <p>If not applied, an attempt is made to derive the respective
 *  implementation from the type of the property; if this fails, no String
 *  conversion is used.</p>
 *  <p>If the relevant {@code StringConverter} is implemented in the current
 *  project, it always has to be specified with this annotation, even when it
 *  is published as a service.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: StringConversion.java 1002 2022-02-01 21:33:00Z tquadrat $
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: StringConversion.java 1002 2022-02-01 21:33:00Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.1.0" )
public @interface StringConversion
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The class of the
     *  {@link StringConverter}
     *  implementation.
     *
     *  @return The String converter class.
     */
    public Class<? extends StringConverter<?>> value();
}
//  annotation StringConversion

/*
 *  End of File
 */