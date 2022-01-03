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

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;

/**
 *  <p>{@summary An implementation of
 *  {@link CmdLineValueHandler}
 *  for
 *  {@link String}
 *  values.}</p>
 *  <p>The value can be validated against a regular expression that is
 *  provided via
 *  {@link org.tquadrat.foundation.config.spi.CLIDefinition#format()}.</p>
 *
 *  @see Pattern
 *  @see org.tquadrat.foundation.config.Option#format()
 *  @see org.tquadrat.foundation.config.Argument#format()
 *
 *  @extauthor Kohsuke Kawaguchi - kk@kohsuke.org
 *  @modified Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: StringValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: StringValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class StringValueHandler extends CmdLineValueHandler<String>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message that indicates a failed validation: {@value}.
     */
    public static final String MSG_ValidationFailed = "Validation failed for '%1$s'";

    /**
     *  The resource bundle key for the message that indicates that the
     *  validation for the command line value had failed.
     */
    @Message
    (
        description = "The error message that indicates that the validation for the command line value had failed.",
        translations =
        {
            @Translation( language = "en", text = MSG_ValidationFailed ),
            @Translation( language = "de", text = "Validierung für '%1$s' ist fehlgeschlagen" )
        }
    )
    public static final int MSGKEY_ValidationFailed = 29;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code StringValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public StringValueHandler( final CLIDefinition context, final BiConsumer<String,String> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( context, valueSetter );
    }   //  StringValueHandler()

    /**
     *  Creates a new {@code StringValueHandler} instance.
     *
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public StringValueHandler( final BiConsumer<String,String> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter );
    }   //  StringValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected Collection<String> translate( final Parameters params ) throws CmdLineException
    {
        Collection<String> retValue = List.of();
        final var value = params.getParameter( 0 );
        final var definition = getCLIDefinition();
        definition.flatMap( CLIDefinition::format )
            .map( Pattern::compile )
            .ifPresent( p ->
            {
                if( !p.matcher( value ).matches() )
                {
                    throw new CmdLineException( MSG_ValidationFailed, MSGKEY_ValidationFailed, value );
                }
            } );
        retValue = List.of( value );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class StringValueHandler

/*
 *  End of File
 */