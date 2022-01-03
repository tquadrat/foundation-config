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

import static org.apiguardian.api.API.Status.DEPRECATED;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  When a configuration bean should be initialised from the command line, the
 *  respective specification interface needs to extend this interface.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CLIBean.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 *
 *  @deprecated Replaced by
 *      {@link CLIBeanSpec}.
 */
@SuppressWarnings( {"InterfaceNeverImplemented", "MarkerInterface", "removal"} )
@Deprecated( since = "0.1.0", forRemoval = true )
@ClassVersion( sourceVersion = "$Id: CLIBean.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = DEPRECATED, since = "0.0.1" )
public interface CLIBean extends ConfigBeanBase, CLIBeanSpec
{ /* Is empty since all the stuff has gone to CLIBeanSpec */ }
//  class CLIBean

/*
 *  End of File
 */