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
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The abstract base class for custom implementations of
 *  {@link PreferenceAccessor}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferenceAccessorBase.java 941 2021-12-18 22:34:37Z tquadrat $
 *  @since 0.0.1
 *
 *  @param  <T> The type of the property.
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"AbstractClassNeverImplemented", "AbstractClassWithoutAbstractMethods"} )
@ClassVersion( sourceVersion = "$Id: PreferenceAccessorBase.java 941 2021-12-18 22:34:37Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public abstract class PreferenceAccessorBase<T> extends PreferenceAccessor<T>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code PreferenceAccessorBase} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    protected PreferenceAccessorBase( final String propertyName, final Getter<T> getter, final Setter<T> setter )
    {
        super( propertyName, getter, setter );
    }   //  PreferenceAccessorBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Converts the given String to an instance of the property
     *  type.}</p>
     *  <p>This implementation uses
     *  {@link StringConverter#fromString(CharSequence)}
     *  for the conversion.</p>
     *
     *  @param  node The &quot;preferences&quot; node that provides the value.
     *  @param  s   The String value; can be {@code null}.
     *  @return The propertyType instance; will be {@code null} if the provided
     *      String is {@code null} or cannot be converted to the property type.
     *  @throws InvalidPreferenceValueException The preferences value cannot be
     *      translated to the property type.
     */
    protected abstract T fromString( final Preferences node, final String s ) throws InvalidPreferenceValueException;

    /**
     *  {@inheritDoc}
     */
    @Override
    public void readPreference( final Preferences node ) throws BackingStoreException, InvalidPreferenceValueException
    {
        final var value = fromString( requireNonNullArgument( node, "node" ), node.get( getPropertyName(), toString( getter().get() ) ) );
        setter().set( value );
    }   //  readReference()

    /**
     *  <p>{@summary Converts the given instance of the property type to a
     *  String.}</p>
     *  <p>This implementation uses
     *  {@link StringConverter#toString(Object)}
     *  for the conversion.</p>
     *
     *  @param  t   The property value; can be {@code null}.
     *  @return The String implementation; will be {@code null} if the provided
     *      value is {@code null} or cannot be converted to a String.
     */
    protected abstract String toString( final T t );

    /**
     *  {@inheritDoc}
     */
    @Override
    public void writePreference( final Preferences node ) throws BackingStoreException
    {
        requireNonNullArgument( node, "node" );
        final var value = toString( getter().get() );
        if( isNotEmpty( value ) )
        {
            node.put( getPropertyName(), value );
        }
        else
        {
            node.remove( getPropertyName() );
        }
    }   //  writePreference()
}
//  class PreferenceAccessorBase

/*
 *  End of File
 */