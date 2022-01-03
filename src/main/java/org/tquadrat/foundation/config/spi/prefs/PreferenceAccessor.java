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

import static java.util.Arrays.asList;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;

/**
 *  <p>{@summary The abstract base class for the preference accessors.}</p>
 *  <p>Custom preference accessors can be implemented based directly on this
 *  class. If a
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  implementation exists for the given type, the accessor for that type should
 *  be implemented based on
 *  {@link PreferenceAccessorBase}.</p>
 *  <p>Accessors for types that should be stored as {@code byte} arrays (or
 *  BLOBs) can be implemented by extending
 *  {@link BulkDataAccessorBase}.</p>
 *
 *  @see Preferences#MAX_VALUE_LENGTH
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferenceAccessor.java 920 2021-05-23 14:27:24Z tquadrat $
 *  @since 0.0.1
 *
 *  @param  <T> The type of the property.
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "AbstractClassNeverImplemented" )
@ClassVersion( sourceVersion = "$Id: PreferenceAccessor.java 920 2021-05-23 14:27:24Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public abstract class PreferenceAccessor<T>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The property getter.
     */
    private final Getter<T> m_Getter;

    /**
     *  The property name.
     */
    private final String m_PropertyName;

    /**
     *  The property setter.
     */
    private final Setter<T> m_Setter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code PreferenceAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    protected PreferenceAccessor( final String propertyName, final Getter<T> getter, final Setter<T> setter )
    {
        m_PropertyName = requireNotEmptyArgument( propertyName, "propertyName" );
        m_Getter = requireNonNullArgument( getter, "getter" );
        m_Setter = requireNonNullArgument( setter, "setter" );
    }   //  PreferenceAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the property name.
     *
     *  @return The property name.
     */
    public final String getPropertyName() { return m_PropertyName; }

    /**
     *  Returns the getter.
     *
     *  @return The getter.
     */
    protected final Getter<T> getter() { return m_Getter; }

    /**
     *  Checks whether the provided preferences node has a key name like the
     *  property name.
     *
     *  @param  node    The preferences node.
     *  @return {@code true} if there is a key with that name, {@code false}
     *      otherwise.
     *  @throws BackingStoreException   There are problems on reading the given
     *      node.
     */
    protected final boolean hasKey( final Preferences node ) throws BackingStoreException
    {
        final var retValue = asList( node.keys() ).contains( m_PropertyName );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  hasKey()

    /**
     *  Reads the preference value from the given node and writes it to the
     *  property.
     *
     *  @param  node    The preference node.
     *  @throws BackingStoreException   There are problems on reading the given
     *      node.
     *  @throws InvalidPreferenceValueException The preferences value cannot be
     *      translated to the property type.
     */
    public abstract void readPreference( final Preferences node ) throws BackingStoreException, InvalidPreferenceValueException;

    /**
     *  Returns the setter.
     *
     *  @return The setter.
     */
    protected final Setter<T> setter() { return m_Setter; }

    /**
     *  Writes the preference value from the property and writes it to the
     *  given node.
     *
     *  @param  node    The preference node.
     *  @throws BackingStoreException   There are problems on writing the given
     *      node.
     */
    public abstract void writePreference( final Preferences node ) throws BackingStoreException;
}
//  class PreferenceAccessor

/*
 *  End of File
 */