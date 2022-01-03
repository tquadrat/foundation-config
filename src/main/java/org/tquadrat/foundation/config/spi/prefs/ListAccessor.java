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

import java.util.ArrayList;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The implementation of
 *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor}
 *  for instances of
 *  {@link List}.
 *
 *  @note   This class requires that there is an implementation of
 *      {@code StringConverter} available for the {@code List}'s element type.
 *
 *  @param  <T> The element type of the {@code List}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ListAccessor.java 912 2021-05-06 22:27:04Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ListAccessor.java 912 2021-05-06 22:27:04Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class ListAccessor<T> extends CollectionAccessor<T,List<T>>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ListAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      for the {@link List} element type.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public ListAccessor( final String propertyName, final StringConverter<T> stringConverter, final Getter<List<T>> getter, final Setter<List<T>> setter )
    {
        super( propertyName, stringConverter, getter, setter );
    }   //  ListAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final List<T> createCollection( final int size )
    {
        final List<T> retValue = new ArrayList<>( max( size, 10 ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCollection()
}
//  class ListAccessor

/*
 *  End of File
 */