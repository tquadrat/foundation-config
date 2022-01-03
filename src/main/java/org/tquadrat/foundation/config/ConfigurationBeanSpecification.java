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
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apiguardian.api.API.Status.STABLE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;

/**
 *  The marker for a configuration bean specification. It will work only on
 *  interfaces that extend
 *  {@link ConfigBeanSpec}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigurationBeanSpecification.java 920 2021-05-23 14:27:24Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ConfigurationBeanSpecification.java 920 2021-05-23 14:27:24Z tquadrat $" )
@Documented
@Retention( RUNTIME )
@Target( TYPE )
@API( status = STABLE, since = "0.0.1" )
public @interface ConfigurationBeanSpecification
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The optional base class for the new configuration bean. The default is
     *  {@link Object} (and will be ignored).
     *
     *  @return The base class.
     */
    public Class<?> baseClass() default Object.class;

    /**
     *  <p>{@summary The name of the resource with the initialisation data for
     *  the configuration bean.}</p>
     *  <p>The resource file must have the format of a Java properties file,
     *  and the name is relative to the location of the configuration bean
     *  specification interface itself.</p>
     *  <p>The special value &quot;=&quot; translates to the simple name of
     *  the interface, suffixed with {@code .properties}; this means, for a
     *  specification interface named {@code com.sample.Specification},
     *  the resulting name for the property would be
     *  {@code Specification.properties}.</p>
     *  <p>The keys for the properties are properties as specified in the
     *  configuration bean specification interface.</p>
     *
     *  @return The resource name for an initialisation data property; the
     *      default is not having such a resource.
     *
     *  @see java.util.Properties
     *  @see Class#getResourceAsStream(String)
     */
    String initDataResource() default "";

    /**
     *  <p>{@summary The fully qualified name of the class that implements the
     *  configuration bean.} The default is the empty String.</p>
     *  <p>If no explicit class name is set, it will be derived from the name
     *  of the interface that specifies the configuration bean as below:
     *  <pre><code>  var packageName = specification.getClass().getPackageName();
     *  var specificationName = specification.getClass().getSimpleName();
     *  var className = format( "%sgenerated.%sImpl", packageName.isEmpty()
     *      ? ""
     *      : packageName + ".", specificationName );</code></pre>
     *  For a specification interface named {@code com.sample.Specification},
     *  the resulting name would be
     *  {@code com.sample.generated.SpecificationImpl}.
     *
     *  @return The intended name of the configuration bean class.
     */
    String name() default "";

    /**
     *  The flag that determines the package for the generated bean. If
     *  {@code false} (the default) it will be put to a package named
     *  {@code generated} below the package of the specification interface, on
     *  {@code true} it will be stored to the same package with the
     *  specification interface.
     *
     *  @return {@code true} if the generated configuration bean should be
     *      placed in the same package as the specification interface,
     *      {@code false} if it should be placed to the {@code generated}
     *      sub-package.
     */
    boolean samePackage() default false;

    /**
     *  The flag that indicates whether the access to the configuration bean
     *  properties should be synchronised. The default is {@code true}.
     *
     *  @return {@code true} if any access to a configuration value has to be
     *      thread-safe, {@code false} otherwise.
     */
    boolean synchronizeAccess() default true;
}
//  annotation ConfigurationBeanSpecification

/*
 *  End of File
 */