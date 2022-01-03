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

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.util.stringconverter.LocalTimeStringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeDateStringConverter;

/**
 *  The implementation of
 *  {@link TimeValueHandler}
 *  for
 *  {@link java.time.LocalTime}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LocalTimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LocalTimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class LocalTimeValueHandler extends TimeValueHandler<LocalTime>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LocalTimeValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public LocalTimeValueHandler( final CLIDefinition context, final BiConsumer<String, LocalTime> valueSetter )
    {
        super( context, valueSetter, LocalTimeStringConverter.INSTANCE );
    }   //  LocalTimeValueHandler()

    /**
     *  Creates a new {@code LocalTimeValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public LocalTimeValueHandler( final BiConsumer<String,LocalTime> valueSetter )
    {
        super( valueSetter, LocalTimeStringConverter.INSTANCE );
    }   //  LocalTimeValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Optional<TimeDateStringConverter<LocalTime>> createCustomStringConverter()
    {
        final Optional<TimeDateStringConverter<LocalTime>> retValue = getFormatter().map( LocalTimeStringConverter::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCustomerConverter()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final LocalTime getNow() { return LocalTime.now(); }
}
//  class LocalTimeValueHandler

/*
 *  End of File
 */