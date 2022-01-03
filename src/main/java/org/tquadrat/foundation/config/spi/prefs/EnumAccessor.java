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

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.util.stringconverter.EnumStringConverter;

/**
 *  The implementation of
 *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor}
 *  for classes that extends
 *  {@link Enum}.
 *
 *  @param  <T> The concrete data type that is handled by this preference
 *      accessor type implementation.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EnumAccessor.java 910 2021-05-06 21:38:06Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: EnumAccessor.java 910 2021-05-06 21:38:06Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class EnumAccessor<T extends Enum<T>> extends SimplePreferenceAccessor<T>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code EnumAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  propertyType    The type of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public EnumAccessor( final String propertyName, final Class<T> propertyType, final Getter<T> getter, final Setter<T> setter )
    {
        super( propertyName, getter, setter, new EnumStringConverter<>( propertyType ) );
    }   //  EnumAccessor()
}
//  class EnumAccessor

/*
 *  End of File
 */