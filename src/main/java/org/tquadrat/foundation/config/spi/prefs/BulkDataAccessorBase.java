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
 *  <p>{@summary The abstract base class for an implementation of
 *  {@link PreferenceAccessor}
 *  for bulk data.}</p>
 *  <p>The method
 *  {@link Preferences#put(String, String) Preferences.put()}
 *  limits the length for a value String to
 *  {@value Preferences#MAX_VALUE_LENGTH}. If more data should be stored, the
 *  method
 *  {@link Preferences#putByteArray(String, byte[]) Preferences.putByteArray()}
 *  can be used.</p>
 *
 *  @param  <T> The type of the property.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: BulkDataAccessorBase.java 942 2021-12-20 02:04:04Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: BulkDataAccessorBase.java 942 2021-12-20 02:04:04Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public abstract class BulkDataAccessorBase<T> extends PreferenceAccessor<T>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code BulkDataAccessorBase} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    protected BulkDataAccessorBase( final String propertyName, final Getter<T> getter, final Setter<T> setter )
    {
        super( propertyName, getter, setter );
    }   //  BulkDataAccessorBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts the given {@code byte} array to an instance of the property
     *  type.
     *
     *  @param  source  The byte array; can be {@code null}.
     *  @param  node    The reference to the {@code Preferences} node that
     *      provides the value.
     *  @return The instance, or {@code null} if the source was already
     *      {@code null}.
     *  @throws InvalidPreferenceValueException The preferences value cannot be
     *      translated to the property type.
     */
    protected abstract T fromByteArray( final Preferences node, final byte [] source) throws InvalidPreferenceValueException;

    /**
     *  {@inheritDoc}
     */
    @Override
    public void readPreference( final Preferences node ) throws BackingStoreException, InvalidPreferenceValueException
    {
        final var defaultValue = toByteArray( requireNonNullArgument( node, "node" ), getter().get() );
        final var prefsValue = node.getByteArray( getPropertyName(), defaultValue );
        final var value = fromByteArray( node, prefsValue );
        setter().set( value );
    }   //  readPreference()

    /**
     *  Converts the given instance of the property type into a {@code byte}
     *  array.
     *
     *  @param  source  The instance; can be {@code null}.
     *  @param  node    The reference to the {@code Preferences} node that is
     *      used to store the preferences value.
     *  @return The {@code byte} array, or {@code null} if the source was
     *      already {@code null}.
     *  @throws InvalidPreferenceValueException   The conversion failed to a
     *      {@code byte} array failed.
     */
    protected abstract byte [] toByteArray( @SuppressWarnings( "unused" ) final Preferences node, final T source ) throws InvalidPreferenceValueException;

    /**
     *  {@inheritDoc}
     */
    @Override
    public void writePreference( final Preferences node ) throws BackingStoreException
    {
        requireNonNullArgument( node, "node" );
        final var propertyValue = getter().get();
        if( isNull( propertyValue ) )
        {
            node.remove( getPropertyName() );
        }
        else
        {
            final var prefsValue = toByteArray( node, propertyValue );
            node.putByteArray( getPropertyName(), prefsValue );
        }
    }   //  writePreference()
}
//  class BulkDataAccessorBase

/*
 *  End of File
 */