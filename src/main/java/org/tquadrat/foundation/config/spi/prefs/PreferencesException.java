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

package org.tquadrat.foundation.config.spi.prefs;

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The exception that is thrown from a configuration bean in case an
 *  operation on the connected
 *  {@link java.util.prefs.Preferences}
 *  instance fails.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferencesException.java 941 2021-12-18 22:34:37Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PreferencesException.java 941 2021-12-18 22:34:37Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class PreferencesException extends RuntimeException
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
     *  Creates a new {@code PreferencesException} instance.
     */
    public PreferencesException() { super(); }

    /**
     *  Creates a new {@code PreferencesException} instance with the specified
     *  detail message. The cause is not initialised, and may subsequently be
     *  initialised by a call to
     *  {@link #initCause(Throwable)}.
     *
     *  @param  message The detail message; it is saved for later retrieval by
     *      the
     *      {@link #getMessage()}
     *      method.
     */
    public PreferencesException( final String message ) { super( message ); }

    /**
     *  Creates a new {@code PreferencesException} instance with the specified
     *  cause and a detail message of
     *  <pre><code>(cause == null ? null : cause.toString() )</code></pre>
     *  (which typically contains the class and detail message of
     *  {@code cause}). This constructor is useful for exceptions that are
     *  little more than wrappers for other instances of
     *  {@link Throwable}.
     *
     *  @param  cause   The cause (which is saved for later retrieval by the
     *      {@link #getCause()}
     *      method). A {@code null} value is permitted, and indicates that the
     *      cause is non-existent or unknown.
     */
    public PreferencesException( final Throwable cause ) { super( cause ); }

    /**
     *  Creates a new {@code PreferencesException} instance with the specified
     *  detail message and cause.
     *
     *  @note   The detail message associated with {@code cause} is <i>not</i>
     *      automatically incorporated in this exception's detail message.
     *
     *  @param  message The detail message; it is saved for later retrieval
     *      by the
     *      {@link #getMessage()}
     *      method.
     *  @param  cause   The cause (which is saved for later retrieval by the
     *      {@link #getCause()}
     *      method). A {@code null} value is permitted, and indicates that the
     *      cause is non-existent or unknown.
     */
    public PreferencesException( final String message, final Throwable cause ) { super( message, cause ); }
}
//  class PreferencesException

/*
 *  End of File
 */