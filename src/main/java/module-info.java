/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
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

/**
 *  This module provides tools for the program configuration together with a
 *  sophisticated CLI.
 *
 *  @todo task.list
 */

module org.tquadrat.foundation.config
{
    requires java.base;
    requires java.desktop;
    requires transitive java.prefs;
    requires java.scripting;

    //---* The foundation modules *--------------------------------------------
    requires org.tquadrat.foundation.base;
    requires org.tquadrat.foundation.util;
    requires org.tquadrat.foundation.i18n;
    requires org.tquadrat.foundation.inifile;

    //---* The exports *-------------------------------------------------------
    exports org.tquadrat.foundation.config;
    exports org.tquadrat.foundation.config.cli;
    exports org.tquadrat.foundation.config.spi;
    exports org.tquadrat.foundation.config.spi.prefs;
}

/*
 *  End of File
 */