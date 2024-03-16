/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.util.stringconverter.LocaleStringConverter;

import java.util.Locale;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 *  Tests for the class
 *  {@link SimpleCmdLineValueHandler}
 *  for the type
 *  {@link Locale}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestLocaleValueHandler.java 1120 2024-03-16 09:48:00Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestLocaleValueHandler.java 1120 2024-03-16 09:48:00Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestLocaleValueHandler" )
public class TestLocaleValueHandler extends ValueHandlerTestBase<Locale>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CmdLineValueHandler<Locale> createCandidate()
    {
        final var retValue = new SimpleCmdLineValueHandler<>( this::valueSetter, LocaleStringConverter.INSTANCE );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCandidate()

    /**
     *  {@inheritDoc}
     */
    @Override
    @Test
    protected final void testParseCmdLine()
    {
        skipThreadTest();

        final var candidate = createCandidate();
        createDefinition( candidate );

        final Class<? extends Throwable> expectedException = CmdLineException.class;
        try
        {
            candidate.parseCmdLine( createParameters( "In_valid_Locale_!" ) );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        var expected = GERMANY;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        expected = GERMAN;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        var language = "in";
        expected = Locale.of( language );
        candidate.parseCmdLine( createParameters( language ) );
        checkValue( expected );

        var country = "IN";
        expected = Locale.of( language, country );
        candidate.parseCmdLine( createParameters( format( "%s_%s", language, country ) ) );
        checkValue( expected );

        final var variant = "Variant";
        expected = Locale.of( language, country, variant );
        candidate.parseCmdLine( createParameters( format( "%s_%s_%s", language, country, variant ) ) );
        checkValue( expected );

        language = "ger";
        country = "756";
        expected = Locale.of( language, country );
        candidate.parseCmdLine( createParameters( format( "%s_%s", language, country ) ) );
        checkValue( expected );
    }   //  testParseCmdLine()
}
//  class TestLocaleValueHandler

/*
 *  End of File
 */