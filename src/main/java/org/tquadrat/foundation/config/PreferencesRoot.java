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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.prefs.PreferenceChangeListenerBase;

/**
 *  <p>{@summary Provides some general configuration settings for a
 *  configuration bean that implements
 *  {@link PreferencesBeanSpec}.}</p>
 *  <p>At default, the node for the &quot;preferences&quot; is determined by the
 *  class of the configuration bean specification, but with this annotation, it
 *  could get another name.</p>
 *  <p>Also usually, there is no listener for changes to the underlying
 *  preferences. But this annotation allows to configure it.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferencesRoot.java 944 2021-12-21 21:56:24Z tquadrat $
 *  @since 0.1.0
 *
 *  @see java.util.prefs.Preferences#userNodeForPackage(Class)
 */
@ClassVersion( sourceVersion = "$Id: PreferencesRoot.java 944 2021-12-21 21:56:24Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( TYPE )
@API( status = STABLE, since = "0.1.0" )
public @interface PreferencesRoot
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The alternative name for the &quot;preferences&quot; node.
     *
     *  @return The node name.
     */
    String nodeName();

    /**
     *  <p>{@summary The class for the preferences change listener.} It has to
     *  extend
     *  {@link PreferenceChangeListenerBase}.</p>
     *  <p>The class
     *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceChangeListenerImpl}
     *  provides a default implementation.</p>
     *
     *  @return The class for the change listener; if {@code null}, no listener
     *      support will be generated.
     */
    Class<? extends PreferenceChangeListenerBase> changeListenerClass();
}
//  annotation PreferencesRoot

/*
 *  End of File
 */