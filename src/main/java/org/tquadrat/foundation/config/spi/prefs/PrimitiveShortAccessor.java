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
 *  for {@code Short}.}</p>
 *  <p>This differs from
 *  {@link ShortAccessor}
 *  as it forces 0 (zero) as the default value, while the other
 *  implementation has {@code null} as the default value.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PrimitiveShortAccessor.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PrimitiveShortAccessor.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class PrimitiveShortAccessor extends PreferenceAccessor<Short>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code PrimitiveShortAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public PrimitiveShortAccessor( final String propertyName, final Getter<Short> getter, final Setter<Short> setter )
    {
        super( propertyName, getter, setter );
    }   //  PrimitiveShortAccessor()

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
        //noinspection NumericCastThatLosesPrecision
        setter().set( Short.valueOf( (short) requireNonNullArgument( node, "node" ).getInt( getPropertyName(), isNull( defaultValue ) ? 0 : defaultValue.intValue() ) ) );
    }   //  readPreference()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void writePreference( final Preferences node )
    {
        node.putInt( getPropertyName(), (int) getter().get().shortValue() );
    }   //  writePreference()
}
//  class PrimitiveShortAccessor

/*
 *  End of File
 */