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
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeDateStringConverter;

/**
 *  <p>{@summary The abstract base class for implementations of
 *  {@link CmdLineValueHandler}
 *  for types that extend
 *  {@link Temporal}.}</p>
 *  <p>Except for
 *  {@link InstantValueHandler InstantValueHandler},
 *  the format for the date/time data on the command line can be modified by
 *  setting the
 *  {@link CLIDefinition#format() format}
 *  parameter of the
 *  {@link org.tquadrat.foundation.config.Option &#64;Option}
 *  or
 *  {@link org.tquadrat.foundation.config.Argument &#64;Argument}
 *  annotation. The value for that parameter has to conform the requirements as
 *  for
 *  {@link DateTimeFormatter#ofPattern(String)}.</p>
 *  <p>All implementations do allow the value &quot;now&quot; instead of a
 *  concrete date/time value; this will be interpreted always as the current
 *  date and/or time.</p>
 *
 *  @param  <T> The type that is handled by this class.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 *
 *  @see DateTimeFormatter
 */
@ClassVersion( sourceVersion = "$Id: TimeValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public abstract class TimeValueHandler<T extends Temporal> extends CmdLineValueHandler<T>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message about an invalid date/time on the command line:
     *  {@value}.
     */
    public static final String MSG_InvalidDateTimeFormat = TimeDateStringConverter.MSG_InvalidDateTimeFormat;

    /**
     *  The resource bundle key for the message about an invalid date/time
     *  String on the command line.
     */
    @Message
    (
        description = "The error message about an invalid date/time String on the command line.",
        translations =
        {
            @Translation( language = "en", text = TimeDateStringConverter.MSG_InvalidDateTimeFormat ),
            @Translation( language = "de", text = "'%1$s' ist keine gültige Datums-/Zeitangabe" )
        }
    )
    public static final int MSGKEY_InvalidDateTimeFormat = 30;

    /**
     *  '{@value}' stands for the current time.
     */
    public static final String NOW = "now";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The implementation of
     *  {@link StringConverter}
     *  that is used to translate the String value from the command line into
     *  the desired object instance.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final TimeDateStringConverter<T> m_StringConverter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code TimeValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      that is used to translate the String value from the command line
     *      into the desired object instance in case no formatter is provided.
     */
    protected TimeValueHandler( final CLIDefinition context, final BiConsumer<String,T> valueSetter, final TimeDateStringConverter<T> stringConverter )
    {
        super( context, valueSetter );
        m_StringConverter = requireNonNullArgument( stringConverter, "stringConverter" );
    }   //  TimeValueHandler()

    /**
     *  Creates a new {@code TimeValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      that is used to translate the String value from the command line
     *      into the desired object instance in case no formatter is provided.
     */
    protected TimeValueHandler( final BiConsumer<String,T> valueSetter, final TimeDateStringConverter<T> stringConverter )
    {
        super( valueSetter );
        m_StringConverter = requireNonNullArgument( stringConverter, "stringConverter" );
    }   //  TimeValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates a non-standard string converter that uses the provided format.
     *
     *  @return An instance o
     *      {@link Optional}
     *      that holds the
     *      {@link StringConverter}.
     */
    protected abstract Optional<TimeDateStringConverter<T>> createCustomStringConverter();

    /**
     *  Creates an instance of
     *  {@link DateTimeFormatter}
     *  from the provided format.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the formatter.
     *
     *  @see CLIDefinition#format()
     */
    protected final Optional<DateTimeFormatter> getFormatter()
    {
        final var retValue = getCLIDefinition()
            .flatMap( CLIDefinition::format )
            .map( DateTimeFormatter::ofPattern );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getFormatter()

    /**
     *  Get the current time.
     *
     *  @return The current time.
     */
    protected abstract T getNow();

    /**
     *  Returns the implementation of
     *  {@link StringConverter}
     *  that is used to translate the String value from the command line into
     *  the desired object instance.
     *
     *  @return The string converter.
     */
    protected final TimeDateStringConverter<T> getStringConverter() { return m_StringConverter; }

    /**
     *  Parses the given String to an instance of
     *  {@link Temporal}.
     *
     *  @param  value   The String to parse.
     *  @return The time/date value.
     *  @throws IllegalArgumentException  The given value cannot be parsed to a
     *      {@code Temporal}.
     */
    private final T parseDateTime( final CharSequence value ) throws IllegalArgumentException
    {
        final var stringConverter = createCustomStringConverter().orElse( getStringConverter() );
        final var retValue = stringConverter.fromString( value );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseDateTime()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Collection<T> translate( final Parameters params ) throws CmdLineException
    {
        Collection<T> retValue = List.of();
        final var value = requireNonNullArgument( params, "params" ).getParameter( 0 );
        if( value.equalsIgnoreCase( NOW ) )
        {
            retValue = List.of( getNow() );
        }
        else
        {
            try
            {
                retValue = List.of( parseDateTime( value ) );
            }
            catch( final IllegalArgumentException e )
            {
                throw new CmdLineException( MSG_InvalidDateTimeFormat, e, MSGKEY_InvalidDateTimeFormat, value );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class TimeValueHandler

/*
 *  End of File
 */