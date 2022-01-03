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
import static org.apiguardian.api.API.Status.INTERNAL;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary Excludes the value of the property belonging to annotated
 *  getter from being included into the output of
 *  {@link Object#toString()}.}</p>
 *  <p>Sometimes it does not make sense to show the value of a property when
 *  {@link Object#toString()}
 *  is called on the configuration bean (or it is not wanted, that the value
 *  is shown). In such a case, the getter for the property can be annotated
 *  with this annotation, and
 *  {@link org.tquadrat.foundation.lang.Stringer#BASE_STRINGER}
 *  is used to show the property in the output of {@code toString()}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ExemptFromToString.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.2
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ExemptFromToString.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.2" )
@Retention( CLASS )
@Target( METHOD )
@Documented
public @interface ExemptFromToString
{ /* Empty */ }
//  annotation ExemptFromToString

/*
 *  End of File
 */