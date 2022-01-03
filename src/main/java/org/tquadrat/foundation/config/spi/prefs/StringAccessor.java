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
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;

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
 *  {@link String}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: StringAccessor.java 914 2021-05-07 21:22:12Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: StringAccessor.java 914 2021-05-07 21:22:12Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class StringAccessor extends PreferenceAccessor<String>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code StringAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public StringAccessor( final String propertyName, final Getter<String> getter, final Setter<String> setter )
    {
        super( propertyName, getter, setter );
    }   //  StringAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void readPreference( final Preferences node ) throws InvalidPreferenceValueException
    {
        setter().set( requireNonNullArgument( node, "node" ).get( getPropertyName(), getter().get() ) );
    }   //  readPreference()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node )
    {
        requireNonNullArgument( node, "node" );
        final var value = getter().get();
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
//  class StringAccessor

/*
 *  End of File
 */