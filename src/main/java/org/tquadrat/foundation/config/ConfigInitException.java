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
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.ValidationException;

/**
 *  This exception type is used to signal a problem with the initialisation of
 *  a configuration bean.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigInitException.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ConfigInitException.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public class ConfigInitException extends ValidationException
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ConfigInitException} instance.
     *
     *  @param  message The message that provides details on the failed
     *      validation.
     */
    public ConfigInitException( final String message )
    {
        super( requireNotEmptyArgument( message, "message" ) );
    }   //  ConfigInitException()

    /**
     *  Creates a new {@code ConfigInitException} instance.
     *
     *  @param  message The message that provides details on the failed
     *      validation.
     *  @param  cause   The exception that is related to the initialisation
     *      error.
     */
    public ConfigInitException( final String message, final Throwable cause )
    {
        super( requireNotEmptyArgument( message, "message" ), cause );
    }   //  ConfigInitException()
}
//  class ConfigInitException

/*
 *  End of File
 */