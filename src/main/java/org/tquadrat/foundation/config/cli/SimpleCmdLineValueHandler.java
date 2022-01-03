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

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.config.internal.Commons.createException;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  The implementation of
 *  {@link CmdLineValueHandler}
 *  for all those types for that an implementation of
 *  {@link StringConverter}
 *  exists.
 *
 *  @param  <T> The target type.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SimpleCmdLineValueHandler.java 892 2021-04-03 18:07:28Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: SimpleCmdLineValueHandler.java 892 2021-04-03 18:07:28Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public sealed class SimpleCmdLineValueHandler<T> extends CmdLineValueHandler<T>
    permits YesNoValueHandler
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The implementation of
     *  {@link StringConverter}
     *  that is used to translate the String value from the command line into
     *  the desired object instance.
     */
    private final StringConverter<? extends T> m_StringConverter;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code SimpleCmdLineValueHandler} instance.
     *
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      that is used to translate the String value from the command line
     *      into the desired object instance.
     */
    public SimpleCmdLineValueHandler( final BiConsumer<String,T> valueSetter, final StringConverter<? extends T> stringConverter )
    {
        super( valueSetter );
        m_StringConverter = stringConverter;
    }   //  SimpleCmdLineValueHandler()

    /**
     *  Creates a new {@code SimpleCmdLineValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The
     *      {@link BiConsumer Consumer}
     *      that places the translated value to the property.
     *  @param  stringConverter The implementation of
     *      {@link StringConverter}
     *      that is used to translate the String value from the command line
     *      into the desired object instance.
     */
    public SimpleCmdLineValueHandler( final CLIDefinition context, final BiConsumer<String,T> valueSetter, final StringConverter<? extends T> stringConverter )
    {
        super( context, valueSetter );
        m_StringConverter = stringConverter;
    }   //  SimpleCmdLineValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Translates the command line values that can be referenced via the
     *  {@code params} argument to the target type.
     *
     *  @param  params  The command line values to translate.
     *  @return A collection with the result; each entry in the collection
     *      corresponds to one value from the command line.
     *  @throws CmdLineException    The given parameters cannot be parsed to
     *      the target type.
     */
    protected final Collection<T> translate( final Parameters params ) throws CmdLineException
    {
        Collection<T> retValue = List.of();
        final var source = requireNonNullArgument( params, "params" ).getParameter( 0 );
        try
        {
            final var result = m_StringConverter.fromString( source );
            retValue = List.of( result );
        }
        catch( final IllegalArgumentException e )
        {
            throw createException( m_StringConverter.getClass(), e, source );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class SimpleCmdLineValueHandler

/*
 *  End of File
 */