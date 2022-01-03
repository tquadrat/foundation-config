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

package org.tquadrat.foundation.config.internal;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for the class
 *  {@link UsageBuilder}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestUsageBuilder.java 893 2021-04-03 19:07:07Z tquadrat $
 *  @since 10
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestUsageBuilder.java 893 2021-04-03 19:07:07Z tquadrat $" )
public class TestUsageBuilder extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link UsageBuilder#build(CharSequence, List)}
     */
    @Test
    final void testBuild()
    {
        skipThreadTest();

        final Map<String,Object> target = new HashMap<>();

        final var candidate = new UsageBuilder( Optional.empty() );
        final var command = "command";
        final var definitions = TestArgumentParser.createCLIDefinitions( target );

        final String actual;
        final String expected;

        expected =
            """
            Usage: command --date DATE [--string STRING] STRING_ARG DATE_ARG

            Options:
            --date DATE    : A Date value as an option
            -d DATE         \s
            --string STRING: A String value as an option
            -s STRING       \s

            Arguments:
            STRING_ARG: A String value as an argument
            DATE_ARG  : A Date value as an argument
            """;
        actual = candidate.build( command, definitions );
        assertEquals( expected, actual );
    }   //  testBuild()

    /**
     *  Tests for the method
     *  {@link UsageBuilder#build(CharSequence, List)}
     */
    @Test
    final void testBuild_Empty()
    {
        skipThreadTest();

        final var candidate = new UsageBuilder( Optional.empty() );

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate.build( EMPTY_STRING, List.of() );
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
    }   //  testBuild_Empty()

    /**
     *  Tests for the method
     *  {@link UsageBuilder#build(CharSequence, List)}
     */
    @Test
    final void testBuild_Null()
    {
        skipThreadTest();

        final var candidate = new UsageBuilder( Optional.empty() );

        String command;
        List<CLIDefinition> definitions;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        command = null;
        definitions = List.of();
        try
        {
            candidate.build( command, definitions );
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

        command = "command";
        definitions = null;
        try
        {
            candidate.build( command, definitions );
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
    }   //  testBuild_Null()

    /**
     *  Tests the creation of an instance for
     *  {@link UsageBuilder}.
     */
    @SuppressWarnings( "OptionalAssignedToNull" )
    @Test
    final void testCreateUsageBuilder()
    {
        skipThreadTest();

        UsageBuilder candidate;

        ResourceBundle resources;
        Optional<ResourceBundle> optionalResources;

        optionalResources = null;
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new UsageBuilder( optionalResources );
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

        resources = null;
        candidate = new UsageBuilder( resources );
        assertNotNull( candidate );

        optionalResources = Optional.ofNullable( resources );
        candidate = new UsageBuilder( optionalResources );
        assertNotNull( candidate );

        resources = mock( ResourceBundle.class );
        candidate = new UsageBuilder( resources );
        assertNotNull( candidate );

        optionalResources = Optional.of( resources );
        candidate = new UsageBuilder( optionalResources );
        assertNotNull( candidate );
    }   //  testCreateUsageBuilder
}
//  class TestUsageBuilder

/*
 *  End of File
 */