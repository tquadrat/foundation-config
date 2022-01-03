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
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary Excludes the property from having a preferences
 *  reference.}</p>
 *  <p>If a configuration bean specification implements the interface
 *  {@link PreferencesBeanSpec},
 *  usually all the properties do have references to a preferences
 *  instance.</p>
 *  <p>If applied to the property's getter method, this annotation is used to
 *  exclude the annotated property from the preferences.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NoPreference.java 941 2021-12-18 22:34:37Z tquadrat $
 *  @since 0.0.1
 */
@ClassVersion( sourceVersion = "$Id: NoPreference.java 941 2021-12-18 22:34:37Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( {METHOD, TYPE} )
@API( status = STABLE, since = "0.0.1" )
public @interface NoPreference
{ /* Empty */ }
//  annotation NoPreference

/*
 *  End of File
 */