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

package org.tquadrat.foundation.config.cli;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.util.stringconverter.EnumStringConverter;

/**
 *  An implementation of
 *  {@link CmdLineValueHandler}
 *  for types that are derived from
 *  {@link Enum}.
 *
 *  @param  <T> The concrete data type that is handled by this value handler
 *      implementation.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: EnumValueHandler.java 892 2021-04-03 18:07:28Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: EnumValueHandler.java 892 2021-04-03 18:07:28Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class EnumValueHandler<T extends Enum<T>> extends CmdLineValueHandler<T>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The implementation of
     *  {@link org.tquadrat.foundation.lang.StringConverter}
     *  that is used to translate the String value from the command line into
     *  the desired object instance.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final EnumStringConverter<T> m_StringConverter;

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for the name of an unknown {@code enum} value on the
     *  command line: {@value}.
     */
    public static final String MSG_UnknownValue = EnumStringConverter.MSG_UnknownValue;

    /**
     *  The resource bundle key for the message about the name for an unknown
     *  {@code enum} value on the command line.
     */
    @Message
    (
        description = "The message about the name for an unknown enum value on the command line",
        translations =
        {
            @Translation( language = "en", text = EnumStringConverter.MSG_UnknownValue ),
            @Translation( language = "de", text = "Unbekannter/ungültiger Wert: %1$s" )
        }
    )
    public static final int MSGKEY_UnknownValue = 2;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code EnumValueHandler} instance.
     *
     *  @param  enumType    The data type for the property.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public EnumValueHandler( final Class<T> enumType, final BiConsumer<String,T> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter );
        m_StringConverter = new EnumStringConverter<>( enumType );
    }   //  EnumValueHandler()

    /**
     *  Creates a new {@code EnumValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  enumType    The data type for the property.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     */
    public EnumValueHandler( final CLIDefinition context, final Class<T> enumType, final BiConsumer<String,T> valueSetter )
    {
        super( context, valueSetter );
        m_StringConverter = new EnumStringConverter<>( enumType );
    }   //  EnumValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Collection<T> translate( final Parameters params ) throws CmdLineException
    {
        Collection<T> retValue = List.of();
        final var value = requireNonNullArgument( params, "params" ).getParameter( 0 );
        try
        {
            final var result = m_StringConverter.fromString( value );
            retValue = List.of( result );
        }
        catch( final IllegalArgumentException e )
        {
            throw new CmdLineException( format( MSG_UnknownValue, value ), e, MSGKEY_UnknownValue, value );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class EnumValueHandler

/*
 *  End of File
 */