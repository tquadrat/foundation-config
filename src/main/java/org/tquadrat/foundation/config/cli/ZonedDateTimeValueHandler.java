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

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.util.stringconverter.TimeDateStringConverter;
import org.tquadrat.foundation.util.stringconverter.ZonedDateTimeStringConverter;

/**
 *  The implementation of
 *  {@link TimeValueHandler}
 *  for
 *  {@link java.time.ZonedDateTime}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ZonedDateTimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ZonedDateTimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class ZonedDateTimeValueHandler extends TimeValueHandler<ZonedDateTime>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ZonedDateTimeValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public ZonedDateTimeValueHandler( final CLIDefinition context, final BiConsumer<String,ZonedDateTime> valueSetter )
    {
        super( context, valueSetter, ZonedDateTimeStringConverter.INSTANCE );
    }   //  ZonedDateTimeValueHandler()

    /**
     *  Creates a new {@code ZonedDateTimeValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public ZonedDateTimeValueHandler( final BiConsumer<String,ZonedDateTime> valueSetter )
    {
        super( valueSetter, ZonedDateTimeStringConverter.INSTANCE );
    }   //  ZonedDateTimeValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Optional<TimeDateStringConverter<ZonedDateTime>> createCustomStringConverter()
    {
        final Optional<TimeDateStringConverter<ZonedDateTime>> retValue = getFormatter().map( ZonedDateTimeStringConverter::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCustomerConverter()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final ZonedDateTime getNow() { return ZonedDateTime.now(); }
}
//  class ZonedDateTimeValueHandler

/*
 *  End of File
 */