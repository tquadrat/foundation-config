/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
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

import static java.lang.Boolean.TRUE;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.util.stringconverter.BooleanStringConverter;

/**
 *  An implementation of
 *  {@link CmdLineValueHandler}
 *  for {@code boolean} and
 *  {@link Boolean}
 *  values.
 *
 *  @extauthor Kohsuke Kawaguchi - kk@kohsuke.org
 *  @modified Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: BooleanValueHandler.java 898 2021-04-06 23:19:36Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: BooleanValueHandler.java 898 2021-04-06 23:19:36Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public class BooleanValueHandler extends CmdLineValueHandler<Boolean>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code BooleanValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public BooleanValueHandler( final CLIDefinition context, final BiConsumer<String,Boolean> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( context, valueSetter );
    }   //  BooleanValueHandler()

    /**
     *  Creates a new {@code BooleanValueHandler} instance.
     *
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public BooleanValueHandler( final BiConsumer<String,Boolean> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter );
    }   //  BooleanValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public int parseCmdLine( final Parameters params )
    {
        var retValue = -1;
        try
        {
            final var result = translate( requireNonNullArgument( params, "params" ) );
            retValue = result.size();
            if( retValue == 0 )
            {
                getValueSetter().accept( getPropertyName(), TRUE );
            }
            else
            {
                result.forEach( r -> getValueSetter().accept( getPropertyName(), r ) );
            }
        }
        catch( final CmdLineException e ) { throw e; }
        catch( final RuntimeException e )
        {
            throw new CmdLineException( getCLIDefinition().orElse( null ), e );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parseCmdLine()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected Collection<Boolean> translate( final Parameters params ) throws CmdLineException
    {
        Collection<Boolean> retValue;
        try
        {
            final var param = params.getParameter( 0 );
            retValue = List.of( BooleanStringConverter.INSTANCE.fromString( param ) );
        }
        catch( @SuppressWarnings( "unused" ) final CmdLineException e )
        {
            retValue = List.of();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class BooleanValueHandler

/*
 *  End of File
 */