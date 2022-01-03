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

import static org.apiguardian.api.API.Status.STABLE;

import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.inifile.INIFile;

/**
 *  <p>{@summary When a configuration bean should be connected with an
 *  {@link org.tquadrat.foundation.inifile.INIFile INI}
 *  file (a Windows style configuration file), the respective configuration
 *  bean specification interface needs to extend this interface.}</p>
 *  <p>A configuration bean can be connected to only one {@code INI} file, and
 *  properties that are kept in
 *  {@link java.util.prefs.Preferences Preferences}
 *  cannot be held in the {@code INI}, too, and vice versa.</p>
 *  <p>The configuration bean specification must use the annotation
 *  {@link INIFileConfig &#64;INIFileConfig}
 *  to specify the path to the configuration file. In addition, each getter has
 *  to be annotated with
 *  {@link INIValue &#64;INIValue}
 *  to define its coordinates in the configuration.</p>
 *  <p>When the file is created by the program itself, the respective
 *  comments from the annotations will be added to the new file; for the
 *  groups, these can be provided through the
 *  {@link INIGroup &#64;INIGroup}
 *  and
 *  {@link INIGroups &#64;INIGroups}
 *  annotations</p>
 *  <p>The configuration values will <i>not</i> be loaded automatically from
 *  the {@code INI} file. A call to
 *  {@link #loadINIFile()}
 *  is required.</p>
 *  <p>A call to
 *  {@link #updateINIFile()}
 *  persists the respective value.</p>
 *
 *  @version $Id: INIBeanSpec.java 946 2021-12-23 14:48:19Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@SuppressWarnings( "InterfaceNeverImplemented" )
@ClassVersion( sourceVersion = "$Id: INIBeanSpec.java 946 2021-12-23 14:48:19Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public interface INIBeanSpec extends ConfigBeanSpec
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Loads the configuration values from the
     *  {@link INIFile}
     *  that is connected to this configuration bean.
     */
    public void loadINIFile();

    /**
     *  Returns a reference to the
     *  {@link INIFile}
     *  instance that backs up this configuration bean.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the {@code INIFile} instance.
     */
    public Optional<INIFile> obtainINIFile();

    /**
     *  Persists the configuration values to the
     *  {@link INIFile}
     *  that is connected to this configuration bean.
     */
    public void updateINIFile();
}
//  interface INIBeanSpec

/*
 *  End of File
 */