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

import static org.apiguardian.api.API.Status.STABLE;

import java.util.EventListener;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  <p>{@summary The definition of a listener for
 *  {@linkplain ConfigurationChangeEvent configuration change events}.} The
 *  {@linkplain #propertyChange(ConfigurationChangeEvent) listener method}
 *  is called by
 *  {@link org.tquadrat.foundation.config.spi.ConfigChangeListenerSupport#fireEvent(String, Object, Object)}
 *  each time a property of a configuration bean is changed.</p>
 *
 *  @note   The listener method is executed asynchronously through a dedicated
 *      thread.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigurationChangeListener.java 930 2021-06-20 18:08:47Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "InterfaceNeverImplemented" )
@ClassVersion( sourceVersion = "$Id: ConfigurationChangeListener.java 930 2021-06-20 18:08:47Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
@FunctionalInterface
public interface ConfigurationChangeListener extends EventListener
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  This method gets called when a configuration property has changed.
     *
     *  @note   This method is executed asynchronously through a dedicated
     *      thread. The configuration bean may be still locked when this method
     *      is invoked.
     *
     *  @param  event   A
     *      {@link ConfigurationChangeEvent} object describing the event source
     *      and the property that has changed.
     */
    public void propertyChange( ConfigurationChangeEvent event );
}
//  interface ConfigurationChangeListener

/*
 *  End of File
 */