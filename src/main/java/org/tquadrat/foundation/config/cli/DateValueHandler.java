/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_IllegalOperand;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_InvalidFormat;
import static org.tquadrat.foundation.config.CmdLineException.MSG_IllegalOperand;
import static org.tquadrat.foundation.config.CmdLineException.MSG_InvalidFormat;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;

/**
 *  <p>{@summary An implementation of
 *  {@link CmdLineValueHandler}
 *  for
 *  {@link Date}
 *  values.}</p>
 *  <p>The method
 *  {@link #translate(Parameters)}
 *  will use
 *  {@link SimpleDateFormat}
 *  to parse the given String to an instance of {@code Date}.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DateValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "UseOfObsoleteDateTimeApi" )
@ClassVersion( sourceVersion = "$Id: DateValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class DateValueHandler extends CmdLineValueHandler<Date>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code DateValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public DateValueHandler( final CLIDefinition context, final BiConsumer<String,Date> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( context, valueSetter );
    }   //  DateValueHandler()

    /**
     *  Creates a new {@code DateValueHandler} instance.
     *
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public DateValueHandler( final BiConsumer<String,Date> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter );
    }   //  DateValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Retrieves the format that was given with the annotation; if that is
     *  empty or {@code null}, the default &quot;{@code yyyy-MM-dd}&quot; will
     *  be returned.<br>
     *  <br>Override this method for a different default.
     *
     *  @return The date format to use by the parser.
     */
    private final String getFormat()
    {
        final var retValue = getCLIDefinition()
            .flatMap( CLIDefinition::format )
            .orElse( "yyyy-MM-dd" );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getFormat()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Collection<Date> translate( final Parameters params ) throws CmdLineException
    {
        final Collection<Date> retValue;
        final var argument = requireNonNullArgument( params, "params" ).getParameter( 0 );
        try
        {
            final var parser = new SimpleDateFormat( getFormat(), Locale.getDefault() );
            parser.setLenient( false );
            final var date = parser.parse( requireNonNullArgument( argument, "argument" ) );
            retValue = List.of( date );
        }
        catch( final ParseException e )
        {
            final var metaVar = getCLIDefinition()
                .map( CLIDefinition::metaVar )
                .orElse( "DATE" );
            throw new CmdLineException( MSG_IllegalOperand, e, MSGKEY_IllegalOperand, metaVar, argument );
        }
        catch( final IllegalArgumentException e )
        {
            final var format = getFormat();
            throw new CmdLineException( MSG_InvalidFormat, e, MSGKEY_InvalidFormat, format );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class DateValueHandler

/*
 *  End of File
 */