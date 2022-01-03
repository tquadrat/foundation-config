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

package org.tquadrat.foundation.config.cli;

import static org.apiguardian.api.API.Status.INTERNAL;

import java.time.Instant;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.util.stringconverter.InstantStringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeDateStringConverter;

/**
 *  The implementation of
 *  {@link TimeValueHandler}
 *  for
 *  {@link java.time.Instant}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: InstantValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: InstantValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public class InstantValueHandler extends TimeValueHandler<Instant>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code InstantValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public InstantValueHandler( final CLIDefinition context, final BiConsumer<String,Instant> valueSetter )
    {
        super( context, valueSetter, InstantStringConverter.INSTANCE );
    }   //  InstantValueHandler()

    /**
     *  Creates a new {@code InstantValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public InstantValueHandler( final BiConsumer<String,Instant> valueSetter )
    {
        super( valueSetter, InstantStringConverter.INSTANCE );
    }   //  InstantValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Optional<TimeDateStringConverter<Instant>> createCustomStringConverter()
    {
        final Optional<TimeDateStringConverter<Instant>> retValue = getFormatter().map( InstantStringConverter::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCustomerConverter()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Instant getNow() { return Instant.now(); }
}
//  class InstantValueHandler

/*
 *  End of File
 */