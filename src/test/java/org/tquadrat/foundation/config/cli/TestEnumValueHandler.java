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
import static java.util.Locale.ROOT;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.capitalize;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.time.Month;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.exception.NullArgumentException;

/**
 *  <p>{@summary Tests for the class
 * {@link EnumValueHandler}.}</p>
 *  <p>The test uses the enum type
 *  {@link Month}
 *  as the value type.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestEnumValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $
 *  @since 11
 */
@ClassVersion( sourceVersion = "$Id: TestEnumValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestEnumValueHandler" )
public class TestEnumValueHandler extends ValueHandlerTestBase<Month>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CmdLineValueHandler<Month> createCandidate()
    {
        final var retValue = new EnumValueHandler<>( Month.class, this::valueSetter );

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

        EnumValueHandler<Month> candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new EnumValueHandler<>( Month.class, null );
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

        try
        {
            candidate = new EnumValueHandler<>( null, this::valueSetter );
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
        final Map<String,Month> values = new TreeMap<>();
        //noinspection UnusedAssignment
        candidate = new EnumValueHandler<>( Month.class, values::put );
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

        final var candidate = (EnumValueHandler<Month>) createCandidate();
        createDefinition( candidate );

        final Class<? extends Throwable> expectedException = CmdLineException.class;
        try
        {
            candidate.parseCmdLine( createParameters( "Invalid Month!" ) );
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

        var monthName = "JUNE";
        final var expected = Month.valueOf( monthName.toUpperCase( ROOT ) );
        candidate.parseCmdLine( createParameters( monthName ) );
        checkValue( expected );

        monthName = monthName.toLowerCase( ROOT );
        try
        {
            candidate.parseCmdLine( createParameters( monthName ) );
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

        monthName = capitalize( monthName );
        try
        {
            candidate.parseCmdLine( createParameters( monthName ) );
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
    }   //  testParseCmdLine()
}
//  class TestEnumValueHandler

/*
 *  End of File
 */