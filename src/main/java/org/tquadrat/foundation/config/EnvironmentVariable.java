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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary This annotation indicates that the property for the annotated
 *  getter is initialised from an environment variable with the given
 *  name.}</p>
 *  <p>The initialisation uses the default
 *  {@link StringConverter}
 *  for the type of the property if none is provided; if there is no default
 *  String converter, an error is thrown during compile time.</p>
 *  <p>Changing the configuration property value will not have an effect to
 *  the environment variable.</p>
 *  <p>This annotation implies the
 *  {@link org.tquadrat.foundation.config.NoPreference}
 *  annotation.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EnvironmentVariable.java 1010 2022-02-05 19:28:36Z tquadrat $
 *  @since 0.0.1
 *
 *  @see System#getenv(String)
 */
@SuppressWarnings( "removal" )
@ClassVersion( sourceVersion = "$Id: EnvironmentVariable.java 1010 2022-02-05 19:28:36Z tquadrat $" )
@Documented
@Retention( CLASS )
@Target( METHOD )
@API( status = STABLE, since = "0.0.2" )
public @interface EnvironmentVariable
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  <p>{@summary The default value for the environment variable.} It will
     *  be used for the initialisation of the property if the environment
     *  variable is not set.</p>
     *  <p>It is mandatory to provide a default value for primitive type
     *  properties.</p>
     *  <p>The default setting is a String containing only a {@code NUL}; it
     *  will be treated as _null</p>
     *
     *  @return The default value.
     */
    public String defaultValue() default "\0";

    /**
     *  The name for the environment variable to read.
     *
     *  @return The name for the environment variable.
     */
    String value();

    /**
     *  The implementation of
     *  {@link StringConverter}
     *  that is used to translate the value of the environment variable.
     *
     *  @return The class for the String converter.
     *
     *  @deprecated Use the annotation
     *      {@link StringConversion &#64;StringConversion}
     *      instead.
     */
    @Deprecated( since = "0.1.0", forRemoval = true )
    Class<? extends StringConverter<?>> stringConverter() default org.tquadrat.foundation.config.internal.NullStringConverter.class;
}
//  annotation EnvironmentVariable

/*
 *  End of File
 */