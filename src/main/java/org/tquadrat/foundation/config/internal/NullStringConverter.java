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

package org.tquadrat.foundation.config.internal;

import static org.apiguardian.api.API.Status.INTERNAL;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  This implementation of
 *  {@link StringConverter}
 *  is used only as a mock for
 *  {@link org.tquadrat.foundation.config.SystemProperty#stringConverter() &#64;SystemProperty.stringConverter()}
 *  and
 *  {@link org.tquadrat.foundation.config.EnvironmentVariable#stringConverter() &#64;SystemProperty.stringConverter()}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NullStringConverter.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.2
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: NullStringConverter.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.2" )
public class NullStringConverter implements StringConverter<Void>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Void fromString( final CharSequence source ) throws IllegalArgumentException { return null; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString( final Void source ) { return null; }
}
//  class NullStringConverter

/*
 *  End of File
 */