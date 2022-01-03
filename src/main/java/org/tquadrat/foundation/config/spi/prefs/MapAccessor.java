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
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The implementation of
 *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor}
 *  for instances of
 *  {@link Map}.
 *
 *  @note   This class requires that there is an implementation of
 *      {@code StringConverter} available for both the {@code Map}'s key and
 *      value types.
 *
 *  @param  <K> The key type of the {@code Map}.
 *  @param  <V> The value type of the {@code Map}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: MapAccessor.java 942 2021-12-20 02:04:04Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: MapAccessor.java 942 2021-12-20 02:04:04Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class MapAccessor<K,V> extends PreferenceAccessor<Map<K,V>>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The instance of
     *  {@link StringConverter}
     *  that is used to convert the key values.
     */
    private final StringConverter<K> m_KeyStringConverter;

    /**
     *  The instance of
     *  {@link StringConverter}
     *  that is used to convert the key values.
     */
    private final StringConverter<V> m_ValueStringConverter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code MapAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  keyStringConverter The implementation of
     *      {@link StringConverter}
     *      for the key type.
     *  @param  valueStringConverter The implementation of
     *      {@link StringConverter}
     *      for the value type.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public MapAccessor( final String propertyName, final StringConverter<K> keyStringConverter, final StringConverter<V> valueStringConverter, final Getter<Map<K,V>> getter, final Setter<Map<K,V>> setter )
    {
        super( propertyName, getter, setter );
        m_KeyStringConverter = requireNonNullArgument( keyStringConverter, "keyStringConverter" );
        m_ValueStringConverter = requireNonNullArgument( valueStringConverter, "valueStringConverter" );
    }   //  MapAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Converts the given {@code Map<String,String>} to a {@code Map<K,V>}.
     *
     *  @param  node The {@code Preferences} node that provides the key and
     *      value.
     *  @param  map The input map; can be {@code null}.
     *  @return The converted map; will be {@code null} if the provided map was
     *      {@code null}.
     *  @throws InvalidPreferenceValueException The preferences value cannot be
     *      translated to the property type.
     */
    private final Map<K,V> fromStringMap( final Preferences node, final Map<String,String> map ) throws InvalidPreferenceValueException
    {
        requireNonNullArgument( node, "node" );
        Map<K,V> retValue = null;
        if( nonNull( map ) )
        {
            retValue = new HashMap<>();
            ConversionLoop: for( final var entry : map.entrySet() )
            {
                final K keyInstance;
                final V valueInstance;

                final var key = entry.getKey();
                final var value = entry.getValue();
                try
                {
                    keyInstance = m_KeyStringConverter.fromString( key );
                }
                catch( final IllegalArgumentException e )
                {
                    throw new InvalidPreferenceValueException( node, format( "%s:(%s:%s)", getPropertyName(), key, value ), key, e );
                }
                try
                {
                    valueInstance = m_ValueStringConverter.fromString( value );
                }
                catch( final IllegalArgumentException e )
                {
                    throw new InvalidPreferenceValueException( node, format( "%s:(%s:%s)", getPropertyName(), key, value ), value, e );
                }
                if( nonNull( keyInstance ) && nonNull( valueInstance ) )
                {
                    retValue.put( keyInstance, valueInstance );
                }
            }   //  ConversionLoop:
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromStringMap()


    /**
     *  {@inheritDoc}
     */
    @Override
    public final void readPreference( final Preferences node ) throws BackingStoreException, InvalidPreferenceValueException
    {
        requireNonNullArgument( node, "node" );
        final var propertyName = getPropertyName();
        var map = getter().get();
        if( node.nodeExists( propertyName ) )
        {
            final var childNode = node.node( propertyName );
            final var keys = childNode.keys();
            final Map<String,String> stringMap = new HashMap<>();
            for( final var key : keys )
            {
                final var value = childNode.get( key, null );
                if( nonNull( value ) ) stringMap.put( key, value );
            }
            map = fromStringMap( childNode, stringMap );
        }
        setter().set( map );
    }   //  readPreferences()

    /**
     *  Converts the given {@code Map<K,V>} to a {@code Map<String,String>}.
     *
     *  @param  map The input map.
     *  @return The converted map.
     */
    private final Map<String,String> toStringMap( final Map<? extends K, ? extends V> map )
    {
        Map<String,String> retValue = null;
        if( nonNull( map ) )
        {
            retValue = new HashMap<>();
            for( final var entry : map.entrySet() )
            {
                final var key = m_KeyStringConverter.toString( entry.getKey() );
                final var value = m_ValueStringConverter.toString( entry.getValue() );
                if( nonNull( key ) && nonNull( value ) ) retValue.put( key, value );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toStringMap()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node ) throws BackingStoreException
    {
        final var propertyName = getPropertyName();
        final var valueMap = toStringMap( getter().get() );
        if( isNull( valueMap ) )
        {
            if( node.nodeExists( propertyName ) ) node.node( propertyName ).removeNode();
        }
        else
        {
            final var childNode = node.node( propertyName );
            for( final var entry : valueMap.entrySet() )
            {
                childNode.put( entry.getKey(), entry.getValue() );
            }
        }
    }   //  writePreferences()
}
//  class MapAccessor

/*
 *  End of File
 */