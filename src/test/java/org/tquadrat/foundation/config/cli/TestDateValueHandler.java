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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;

/**
 *  Tests for the class
 *  {@link DateValueHandler}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDateValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 */
@SuppressWarnings( "UseOfObsoleteDateTimeApi" )
@ClassVersion( sourceVersion = "$Id: TestDateValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestDateValueHandler" )
public class TestDateValueHandler extends ValueHandlerTestBase<Date>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CmdLineValueHandler<Date> createCandidate()
    {
        final var retValue = new DateValueHandler( this::valueSetter );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCandidate()

    /**
     *  Tests the constructor.
     */
    @Test
    public final void testConstructor()
    {
        skipThreadTest();

        DateValueHandler candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new DateValueHandler( null );
            assertNotNull( candidate );
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

        @SuppressWarnings( "MismatchedQueryAndUpdateOfCollection" )
        final Map<String,Date> values = new TreeMap<>();
        //noinspection UnusedAssignment
        candidate = new DateValueHandler( values::put );
    }   //  testConstructor()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "CastToConcreteClass" )
    @Override
    @Test
    protected final void testParseCmdLine()
    {
        skipThreadTest();

        final var candidate = (DateValueHandler) createCandidate();
        createDefinition( candidate, "yyyy-MM-dd" );

        final var value = "2018-09-17";
        final var calendar = Calendar.getInstance();
        calendar.setLenient( true );
        calendar.set( 2018, Calendar.SEPTEMBER, 17, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        final var expected = calendar.getTime();

        candidate.parseCmdLine( createParameters( value ) );
        checkDateValue( expected );
    }   //  testParseCmdLine()
}
//  class TestDateValueHandler

/*
 *  End of File
 */