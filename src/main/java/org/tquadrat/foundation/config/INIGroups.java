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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import javax.swing.GroupLayout.Group;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The container for
 *  {@link INIGroup &#64;INIGroup}
 *  annotations.
 *
 *  @version $Id: INIGroups.java 949 2021-12-28 11:09:25Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: INIGroups.java 949 2021-12-28 11:09:25Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( TYPE )
@API( status = STABLE, since = "0.1.0" )
public @interface INIGroups
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The contained annotations.
     *
     *  @return The
     *      {@link Group &#64;Group}
     *      annotations.
     */
    public INIGroup[] value();
}
//  @interface INIGroups

/*
 *  End of File
 */