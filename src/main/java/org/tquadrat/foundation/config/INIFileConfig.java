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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The definition of an {@code INI} file that is used in the context of a
 *  configuration bean implementing
 *  {@link INIBeanSpec}.
 *
 *  @version $Id: INIFileConfig.java 1015 2022-02-09 08:25:36Z tquadrat $
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @since 0.1.0
 */
@ClassVersion( sourceVersion = "$Id: INIFileConfig.java 1015 2022-02-09 08:25:36Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( TYPE )
@API( status = STABLE, since = "0.1.0" )
public @interface INIFileConfig
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  In case the configuration file is created by the program, this comment
     *  will be written to it.
     *
     *  @return The file comment.
     */
    public String comment() default "";

    /**
     *  Flag that indicates whether the configuration file must already exist
     *  prior to the first attempt to open it. If {@code true} and the file
     *  does not exist, the creation of the configuration bean will fail with
     *  an
     *  {@link ExceptionInInitializerError}
     *  that has an
     *  {@link java.io.FileNotFoundException FileNotFoundException}
     *  as the
     *  {@linkplain Throwable#getCause() cause}.
     *
     *  @return {@code true} if the file needs to exist on program start,
     *      {@code false} if it will be created when missing.
     */
    public boolean mustExist() default false;

    /**
     *  <p>{@summary The filename of the {@code INI} file.}</p>
     *  <p>If the filename starts …</p>
     *  <ul>
     *      <li>… with {@code '/'} (on UNIX) or {@code 'C:\'} (or another drive
     *      letter on MS&nbsp;Windows) it represents an absolute path and will
     *      be used as is.<br>
     *      Sample: {@code /etc/configs/app.ini}</li>
     *      <li>… with the prefix {@code $USER/}, it will be resolved against the
     *      home folder of the current user.<br>
     *      Sample: {@code $USER/.config/app.ini}<br>
     *      On Ubuntu, and for the user otto, this resolves to
     *      {@code /home/otto/.config/app.ini}.</li>
     *      <li>… with the prefix {@code ${<property>}}, it will be resolved
     *      against the path that is represented by the property with the name
     *      {@code <property>}. Obviously, the property has to be of type
     *      {@link java.nio.file.Path},
     *      otherwise the generated configuration bean will not compile.<br>
     *      Sample: {@code ${configPath}app.ini}<br>
     *      If {@code getConfigPath()} return {@code /opt/data/configs}, this
     *      resolves to {@code /opt/data/configs/app.ini}.</li>
     *  </ul>
     *  <p>In any other case, the filename represents a relative path that is
     *  resolved against the current working directory. So if the value is
     *  {@code data/config/app.ini}, and the current working directory is
     *  {@code /home/otto}, it resolves to
     *  {@code /home/otto/config/app.ini}.</p>
     *
     *  @return The filename.
     */
    public String path();
}
//  @interface INIFileConfig

/*
 *  End of File
 */