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

import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;

/**
 *  <p>{@summary The implementation of
 *  {@link PreferenceAccessor}
 *  for instances of
 *  {@link Date}.}</p>
 *  <p>As instances of {@code Date} do not keep a time zone, and because there
 *  is no easy (and reliable for all locales) way to convert them to and from
 *  Strings, they are stored to the preferences as {@code long} values.</p>
 *  <p>If it is desired/required to store human readable time/date values to
 *  the preferences, the classes from the package
 *  {@link java.time}
 *  should be considered for the property type.</p>
 *
 *  @see Date#Date(long)
 *  @see Date#getTime()
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DateAccessor.java 910 2021-05-06 21:38:06Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "UseOfObsoleteDateTimeApi" )
@ClassVersion( sourceVersion = "$Id: DateAccessor.java 910 2021-05-06 21:38:06Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class DateAccessor extends PreferenceAccessor<Date>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code DateAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public DateAccessor( final String propertyName, final Getter<Date> getter, final Setter<Date> setter )
    {
        super( propertyName, getter, setter );
    }   //  DateAccessor()

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
        Date propertyValue = null;
        if( isNull( defaultValue ) )
        {
            if( hasKey( node ) )
            {
                propertyValue = new Date( node.getLong( getPropertyName(), 0 ) );
            }
        }
        else
        {
            propertyValue = new Date( node.getLong( getPropertyName(), defaultValue.getTime() ) );
        }
        setter().set( propertyValue );
    }   //  readPreference()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node ) throws BackingStoreException
    {
        requireNonNullArgument( node, "node" );
        final var propertyValue = getter().get();
        if( isNull( propertyValue ) )
        {
            node.remove( getPropertyName() );
        }
        else
        {
            node.putLong( getPropertyName(), propertyValue.getTime() );
        }
    }   //  writePreference()
}
//  class DateAccessor

/*
 *  End of File
 */