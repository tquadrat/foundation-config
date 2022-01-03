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
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary The abstract implementation of
 *  {@link BulkDataAccessorBase}
 *  for bulk data where an instance of
 *  {@link StringConverter}
 *  can be provided for the property data type.}</p>
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
 *  @version $Id: SimpleBulkDataAccessor.java 914 2021-05-07 21:22:12Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: SimpleBulkDataAccessor.java 914 2021-05-07 21:22:12Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public abstract class SimpleBulkDataAccessor<T> extends BulkDataAccessorBase<T>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *   The implementation of
     *   {@link StringConverter}
     *   that is used to convert the preference values back and force.
     */
    private final StringConverter<T> m_StringConverter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SimpleBulkDataAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      that is used to convert the preference values back and force.
     */
    protected SimpleBulkDataAccessor( final String propertyName, final Getter<T> getter, final Setter<T> setter, final StringConverter<T> stringConverter )
    {
        super( propertyName, getter, setter );
        m_StringConverter = requireNonNullArgument( stringConverter, "stringConverter" );
    }   //  SimpleBulkDataAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final T fromByteArray( @SuppressWarnings( "unused" ) final Preferences node, final byte [] source) throws InvalidPreferenceValueException
    {
        T retValue = null;
        if( nonNull( source ) )
        {
            final var string = new String( source, UTF8 );
            retValue = m_StringConverter.fromString( string );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromByteArray()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final byte [] toByteArray( @SuppressWarnings( "unused" ) final Preferences node, final T source ) throws InvalidPreferenceValueException
    {
        final var retValue = isNull( source ) ? null : m_StringConverter.toString( source ).getBytes( UTF8 );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toByteArray()
}
//  class SimpleBulkDataAccessor

/*
 *  End of File
 */