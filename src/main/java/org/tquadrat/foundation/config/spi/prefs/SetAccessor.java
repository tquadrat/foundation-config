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

import static java.lang.Math.max;
import static org.apiguardian.api.API.Status.STABLE;

import java.util.HashSet;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The implementation of
 *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor}
 *  for instances of
 *  {@link Set}.
 *
 *  @note   This class requires that there is an implementation of
 *      {@code StringConverter} available for the {@code Set}'s element type.
 *
 *  @param  <T> The element type of the {@code Set}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SetAccessor.java 912 2021-05-06 22:27:04Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: SetAccessor.java 912 2021-05-06 22:27:04Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class SetAccessor<T> extends CollectionAccessor<T,Set<T>>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SetAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      for the {@code Set} element type.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public SetAccessor( final String propertyName, final StringConverter<T> stringConverter, final Getter<Set<T>> getter, final Setter<Set<T>> setter )
    {
        super( propertyName, stringConverter, getter, setter );
    }   //  SetAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Set<T> createCollection( final int size )
    {
        final var initialCapacity = max( size, 16 );
        final var loadFactor = 0.75f;
        final Set<T> retValue = new HashSet<>( initialCapacity, loadFactor );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCollection()
}
//  class SetAccessor

/*
 *  End of File
 */