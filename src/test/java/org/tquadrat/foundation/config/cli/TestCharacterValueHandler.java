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

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.util.stringconverter.CharacterStringConverter;

/**
 *  Tests for an instance of
 *  {@link CmdLineValueHandler}
 *  that handles
 *  {@link Character}
 *  and {@code char} properties.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestCharacterValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestCharacterValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestCharacterValueHandler" )
public class TestCharacterValueHandler extends ValueHandlerTestBase<Character>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CmdLineValueHandler<Character> createCandidate()
    {
        final var retValue = new SimpleCmdLineValueHandler<>( this::valueSetter, CharacterStringConverter.INSTANCE );
        createDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCandidate()

    /**
     *  {@inheritDoc}
     */
    @Override
    @Test
    protected void testParseCmdLine()
    {
        skipThreadTest();

        final var candidate = createCandidate();

        final Class<? extends Throwable> expectedException = CmdLineException.class;
        try
        {
            candidate.parseCmdLine( createParameters( "value" ) );
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

        final var expected = Character.valueOf( 'v' );
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );
    }   //  testParseCmdLine()
}
//  class TestCharacterValueHandler

/*
 *  End of File
 */