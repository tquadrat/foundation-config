/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  <p>{@summary The abstract base class for implementations of
 *  {@link PreferenceAccessor}
 *  for instances of implementations of
 *  {@link Collection}.}</p>
 *
 *  @note   This class requires that there is an implementation of
 *      {@code StringConverter} available for the collection's component type.
 *
 *  @param  <T> The component type of the {@code Collection}.
 *  @param  <C> The type of the {@code Collection}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CollectionAccessor.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: CollectionAccessor.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public abstract sealed class CollectionAccessor<T,C extends Collection<T>> extends PreferenceAccessor<C>
    permits ListAccessor, SetAccessor
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The instance of
     *  {@link StringConverter}
     *  that is used to convert the collection component values.
     */
    private final StringConverter<T> m_StringConverter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CollectionAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      for the component type.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    protected CollectionAccessor( final String propertyName, final StringConverter<T> stringConverter, final Getter<C> getter, final Setter<C> setter )
    {
        super( propertyName, getter, setter );
        m_StringConverter = requireNonNullArgument( stringConverter, "stringConverter" );
    }   //  CollectionAccessor()

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
     *  @param  node The {@code Preferences} node that provides the value.
     *  @param  index   The value index.
     *  @param  s   The String value; can be {@code null}.
     *  @return The value instance; will be {@code null} if the provided
     *      String is {@code null} or cannot be converted to the type of the
     *      property.
     *  @throws InvalidPreferenceValueException The preferences value cannot be
     *      translated to the property type.
     */
    protected final T fromString( final Preferences node, final int index, final String s ) throws InvalidPreferenceValueException
    {
        final T retValue;
        try
        {
            retValue = m_StringConverter.fromString( s );
        }
        catch( final IllegalArgumentException e )
        {
            throw new InvalidPreferenceValueException( node, format( "%s:%d", getPropertyName(), index ), s, e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Creates the collection data structure.
     *
     *  @param  size    The suggested size of the collection; can be ignored if
     *      not appropriate.
     *  @return The collection data structure.
     */
    protected abstract C createCollection( final int size );

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void readPreference( final Preferences node ) throws BackingStoreException, InvalidPreferenceValueException
    {
        requireNonNullArgument( node, "node" );
        final var propertyName = getPropertyName();
        var collection = getter().get();
        if( node.nodeExists( propertyName ) )
        {
            final var childNode = node.node( propertyName );
            final var keys = stream( childNode.keys() )
                .filter( key -> key.startsWith( propertyName ) )
                .toArray( String []::new );
            collection = createCollection( keys.length );
            for( var i = 0; i < keys.length; ++i )
            {
                final var value = fromString( childNode, i, childNode.get( keys [i], null ) );
                if( !isNull( value ) ) collection.add( value );
            }
        }
        setter().set( collection );
    }   //  readPreferences()

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
    protected final String toString( final T t )
    {
        final var retValue = m_StringConverter.toString( t );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node ) throws BackingStoreException
    {
        requireNonNullArgument( node, "node" );
        final var propertyName = getPropertyName();
        final var collection = getter().get();
        if( isNull( collection ) )
        {
            if( node.nodeExists( propertyName ) ) node.node( propertyName ).removeNode();
        }
        else
        {
            final var childNode = node.node( propertyName );
            var counter = 0;
            for( final var element : collection )
            {
                final var value = toString( element );
                if( nonNull( value ) ) childNode.put( format( "%s[%d]", propertyName, ++counter ), value );
            }
        }
    }   //  writePreferences()
}
//  class CollectionAccessor

/*
 *  End of File
 */