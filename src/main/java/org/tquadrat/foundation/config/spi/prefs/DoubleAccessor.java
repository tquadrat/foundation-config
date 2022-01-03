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
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;

/**
 *  The implementation of
 *  {@link PreferenceAccessor}
 *  for instances of
 *  {@link Double}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DoubleAccessor.java 910 2021-05-06 21:38:06Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: DoubleAccessor.java 910 2021-05-06 21:38:06Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class DoubleAccessor extends PreferenceAccessor<Double>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code DoubleAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public DoubleAccessor( final String propertyName, final Getter<Double> getter, final Setter<Double> setter )
    {
        super( propertyName, getter, setter );
    }   //  DoubleAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void readPreference( final Preferences node ) throws BackingStoreException, InvalidPreferenceValueException
    {
        requireNonNullArgument( node, "node" );
        final var defaultValue = getter().get();
        if( isNull( defaultValue ) )
        {
            if( hasKey( node ) )
            {
                setter().set( Double.valueOf( node.getDouble( getPropertyName(), Double.NaN ) ) );
            }
            else
            {
                setter().set( null );
            }
        }
        else
        {
            setter().set( Double.valueOf( node.getDouble( getPropertyName(), defaultValue.doubleValue() ) ) );
        }
    }   //  readPreference()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node )
    {
        requireNonNullArgument( node, "node" );
        final var propertyValue = getter().get();
        if( isNull( propertyValue ) )
        {
            node.remove( getPropertyName() );
        }
        else
        {
            node.putDouble( getPropertyName(), propertyValue.doubleValue() );
        }
    }   //  writePreference()
}
//  class DoubleAccessor

/*
 *  End of File
 */