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
 *  The base for the specification of a configuration bean; the final
 *  specification interface must also be annotated with
 *  {@link ConfigurationBeanSpecification &#64;ConfigurationBeanSpecification}
 *  in order to be recognised properly.<br>
 *  <br>The generation process will only generate getter and setter methods,
 *  other methods must be provided as {@code default}.<br>
 *  If the configuration bean should be initialised through command line
 *  arguments, the specification interface must extend
 *  {@code org.tquadrat.foundation.ui.cli.CLIBean},
 *  and if it should work with
 *  {@code java.util.prefs.Preferences Preferences},
 *  the interface needs to extend
 *  {@code org.tquadrat.foundation.ui.preferences.PreferencesBean}. It could
 *  extend both.<br>
 *  <br>To add the i18n feature, the configuration bean specification must also
 *  extend
 *  {@code org.tquadrat.foundation.ui.i18n.I18nSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigBeanBase.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 *
 *  @deprecated Replaced by
 *      {@link ConfigBeanSpec}.
 */
@SuppressWarnings( {"InterfaceNeverImplemented", "MarkerInterface"} )
@Deprecated( since = "0.1.0", forRemoval = true )
@ClassVersion( sourceVersion = "$Id: ConfigBeanBase.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = DEPRECATED, since = "0.0.1" )
public interface ConfigBeanBase extends ConfigBeanSpec
{ /* Is empty since all the stuff has gone to ConfigBeanSpec */ }
//  class ConfigBeanBase

/*
 *  End of File
 */