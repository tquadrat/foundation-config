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

import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;

/**
 *  <p>{@summary The implementation of
 *  {@link PreferenceAccessor}
 *  for {@code boolean}.}</p>
 *  <p>This differs from
 *  {@link BooleanAccessor}
 *  as it forces {@code false} as the default value, while the other
 *  implementation has {@code null} as the default value.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PrimitiveBooleanAccessor.java 913 2021-05-06 22:28:06Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PrimitiveBooleanAccessor.java 913 2021-05-06 22:28:06Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class PrimitiveBooleanAccessor extends PreferenceAccessor<Boolean>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code PrimitiveBooleanAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public PrimitiveBooleanAccessor( final String propertyName, final Getter<Boolean> getter, final Setter<Boolean> setter )
    {
        super( propertyName, getter, setter );
    }   //  PrimitiveBooleanAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void readPreference( final Preferences node ) throws InvalidPreferenceValueException
    {
        final var defaultValue = getter().get();
        setter().set( Boolean.valueOf( requireNonNullArgument( node, "node" ).getBoolean( getPropertyName(), !isNull( defaultValue ) && defaultValue.booleanValue() ) ) );
    }   //  readPreference()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node )
    {
        requireNonNullArgument( node, "node" );
        final var propertyValue = getter().get();
        node.putBoolean( getPropertyName(), !isNull( propertyValue ) && propertyValue.booleanValue() );
    }   //  writePreference()
}
//  class PrimitiveBooleanAccessor

/*
 *  End of File
 */