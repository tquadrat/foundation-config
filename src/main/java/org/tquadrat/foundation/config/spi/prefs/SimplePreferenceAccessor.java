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

import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The implementations of
 *  {@link PreferenceAccessor}
 *  that uses the given instance of
 *  {@link StringConverter}
 *  to translate the values back and forth.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SimplePreferenceAccessor.java 910 2021-05-06 21:38:06Z tquadrat $
 *  @since 0.1.0
 *
 *  @param  <T> The type of the property value.
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"AbstractClassNeverImplemented", "AbstractClassWithoutAbstractMethods"} )
@ClassVersion( sourceVersion = "$Id: SimplePreferenceAccessor.java 910 2021-05-06 21:38:06Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public sealed class SimplePreferenceAccessor<T> extends PreferenceAccessorBase<T>
    permits EnumAccessor
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
     *  Creates a new {@code SimplePreferenceAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      that is used to convert the preference values back and forth.
     */
    public SimplePreferenceAccessor( final String propertyName, final Getter<T> getter, final Setter<T> setter, final StringConverter<T> stringConverter )
    {
        super( propertyName, getter, setter );
        m_StringConverter = requireNonNullArgument( stringConverter, "stringConverter" );
    }   //  SimplePreferenceAccessorBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final T fromString( final Preferences node, final String s ) throws InvalidPreferenceValueException
    {
        final T retValue;
        try
        {
            retValue = m_StringConverter.fromString( s );
        }
        catch( final IllegalArgumentException e )
        {
            throw new InvalidPreferenceValueException( node, getPropertyName(), s, e );
        }
        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected String toString( final T t )
    {
        final var retValue = m_StringConverter.toString( t );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class SimplePreferenceAccessor

/*
 *  End of File
 */