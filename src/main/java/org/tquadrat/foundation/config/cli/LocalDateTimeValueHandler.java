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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.util.stringconverter.LocalDateTimeStringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeDateStringConverter;

/**
 *  The implementation of
 *  {@link TimeValueHandler}
 *  for
 *  {@link java.time.LocalDateTime}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LocalDateTimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LocalDateTimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class LocalDateTimeValueHandler extends TimeValueHandler<LocalDateTime>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LocalDateTimeValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public LocalDateTimeValueHandler( final CLIDefinition context, final BiConsumer<String,LocalDateTime> valueSetter )
    {
        super( context, valueSetter, LocalDateTimeStringConverter.INSTANCE );
    }   //  LocalDateTimeValueHandler()

    /**
     *  Creates a new {@code LocalDateTimeValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public LocalDateTimeValueHandler( final BiConsumer<String,LocalDateTime> valueSetter )
    {
        super( valueSetter, LocalDateTimeStringConverter.INSTANCE );
    }   //  LocalDateTimeValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Optional<TimeDateStringConverter<LocalDateTime>> createCustomStringConverter()
    {
        final Optional<TimeDateStringConverter<LocalDateTime>> retValue = getFormatter().map( LocalDateTimeStringConverter::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCustomerConverter()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final LocalDateTime getNow() { return LocalDateTime.now(); }
}
//  class LocalDateTimeValueHandler

/*
 *  End of File
 */