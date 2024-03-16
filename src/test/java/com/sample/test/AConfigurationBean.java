/*
 * ============================================================================
 *  Copyright © 2002-2024 by Thomas Thrien.
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

package com.sample.test;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.PropertyName;
import org.tquadrat.foundation.config.CLIBeanSpec;
import org.tquadrat.foundation.config.CheckEmpty;
import org.tquadrat.foundation.config.ConfigurationBeanSpecification;
import org.tquadrat.foundation.config.PreferencesBeanSpec;

import java.util.Optional;

/**
 *  <p>{@summary The definition for a configuration bean that is used for
 *  testing.}</p>
 *  <p>It extends
 *  {@link org.tquadrat.foundation.config.ConfigBeanSpec}
 *  (indirectly, via the two other interfaces),
 *  {@link CLIBeanSpec}
 *  and
 *  {@link PreferencesBeanSpec}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AConfigurationBean.java 1120 2024-03-16 09:48:00Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: AConfigurationBean.java 1120 2024-03-16 09:48:00Z tquadrat $" )
@ConfigurationBeanSpecification
public interface AConfigurationBean extends CLIBeanSpec, PreferencesBeanSpec
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns an optional configuration value that is a
     *  {@link String}.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the configuration value.
     */
    @PropertyName( "value1" )
    public Optional<String> getConfigValue1();

    /**
     *  Returns a flag.
     *
     *  @return The flag.
     */
    public boolean isFlag();

    /**
     *  Returns a number.
     *
     *  @return The number.
     */
    public int getNumber();

    /**
     *  Sets the configuration value {@code value2}.
     *
     *  @param  value   The new value.
     */
    public void setValue2( final int value );

    /**
     *  Sets the configuration value {@code value3}.
     *
     *  @param  value   The new value.
     */
    @CheckEmpty
    public void setValue3( final String value );
}
//  interface AConfigurationBean

/*
 *  End of File
 */