/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.STABLE;

import java.util.Optional;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary When a configuration bean should be connected with
 *  {@link Preferences Preferences},
 *  the respective configuration bean specification interface needs to extend
 *  this interface.}</p>
 *  <p>Only user preferences are supported.</p>
 *  <p>The preferences values will <i>not</i> be loaded automatically. A call
 *  to
 *  {@link #loadPreferences()}
 *  is required.</p>
 *  <p>A call to
 *  {@link #updatePreferences()}
 *  persists the respective value.</p>
 *  <p>The name of the preferences node for the values is the class name of the
 *  configuration bean specification interface.</p>
 *  <p>If the requirement is to initialise a property from a
 *  {@link Preferences#systemRoot() SYSTEM Preference},
 *  consider to use the
 *  {@link SystemPreference &#64;SystemPreference}
 *  annotation instead of implementing this interface.</p>
 *
 *  @note   None of the methods in this interface can be called from
 *      {@code initData()}!
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferencesBeanSpec.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 *
 *  @see Preference
 *  @see NoPreference
 */
@ClassVersion( sourceVersion = "$Id: PreferencesBeanSpec.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public interface PreferencesBeanSpec extends ConfigBeanSpec
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Loads the preference values from the
     *  {@link Preferences Preferences} instances connected
     *  with the configuration bean based on the specification that is
     *  extending this interface into the properties of that configuration
     *  bean.
     */
    public void loadPreferences();

    /**
     *  Returns the user preferences node that backs this configuration bean.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the
     *      {@link Preferences}
     *      instance.
     */
    public default Optional<Preferences> obtainPreferencesNode() { return Optional.empty(); }

    /**
     *  Updates the instances of
     *  {@link Preferences Preferences},
     *  that are connected to the configuration bean based on the specification
     *  that is extending this interface, with the property values of this
     *  configuration bean.
     */
    public void updatePreferences();
}
//  interface PreferencesBeanSpec

/*
 *  End of File
 */