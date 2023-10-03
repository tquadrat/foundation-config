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

package org.tquadrat.foundation.config;

import static java.io.OutputStream.nullOutputStream;
import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.config.ConfigUtil.parseCommandLine;
import static org.tquadrat.foundation.config.ConfigUtil.printUsage;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.internal.TestArgumentParser;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for
 *  {@link ConfigUtil}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestConfigUtil.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.TestConfigUtil" )
public class TestConfigUtil extends TestBaseClass
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The container for the values. The name of the property is the key.
     */
    private final Map<String,Object> m_Values = new TreeMap<>();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Cleanup after each test.
     */
    @AfterEach
    final void afterEach()
    {
        m_Values.clear();
    }   //  afterEach()

    /**
     *  Creates a list of option and argument definitions.
     *
     *  @param  target  The container for the values.
     *  @return The list with
     *      {@link CLIDefinition}
     *      instances.
     */
    private static final List<CLIDefinition> createCLIDefinitions( final Map<? super String, Object> target )
    {
        return TestArgumentParser.createCLIDefinitions( target );
    }   //  createCLIDefinitions()

    /**
     *  Test parsing command lines.
     *
     *  @see ConfigUtil#parseCommandLine(List, String...)
     */
    @SuppressWarnings( "UseOfObsoleteDateTimeApi" )
    @Test
    final void testParseCommandLine()
    {
        skipThreadTest();

        List<CLIDefinition> definitions;
        String [] args;

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;

            definitions = null;
            args = EMPTY_String_ARRAY;
            try
            {
                parseCommandLine( definitions, args );
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

            definitions = List.of();
            args = null;
            try
            {
                parseCommandLine( definitions, args );
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
        }

        definitions = List.of();
        args = EMPTY_String_ARRAY;
        parseCommandLine( definitions, args );

        final Class<? extends Throwable> expectedException = CmdLineException.class;

        definitions = List.of();
        args = new String [] { "eins" };
        try
        {
            parseCommandLine( definitions, args );
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

        definitions = createCLIDefinitions( m_Values );
        args = new String [] {
            "--string=Value",
            "--date=2018-09-17",
            "value",
            "2018-09-17" };
        parseCommandLine( definitions, args );
        assertEquals( "Value", m_Values.get( "stringOption" ) );
        assertEquals( "value", m_Values.get( "stringArgument" ) );

        final var calendar = Calendar.getInstance();
        calendar.setLenient( true );
        calendar.set( 2018, Calendar.SEPTEMBER, 17, 0, 0, 0 );
        calendar.set( Calendar.MILLISECOND, 0 );
        assertEquals( calendar.getTime().getTime(), ((Date) m_Values.get( "dateOption" )).getTime() );
        assertEquals( calendar.getTime().getTime(), ((Date) m_Values.get( "dateArgument" )).getTime() );
    }   //  testParseCommandLine()

    /**
     *  Tests for the method
     *  {@link ConfigUtil#printUsage(OutputStream, Optional, CharSequence, List)}.
     *
     *  @throws IOException Something unexpected went wrong.
     */
    @SuppressWarnings( "resource" )
    @Test
    final void testPrintUsage() throws IOException
    {
        skipThreadTest();
        Locale.setDefault( ENGLISH );

        OutputStream outputStream;
        Optional<ResourceBundle> resources;
        String command;
        List<CLIDefinition> definitions;

        {
            final Class<? extends Throwable> expectedException = EmptyArgumentException.class;

            outputStream = nullOutputStream();
            resources = Optional.empty();
            command = EMPTY_STRING;
            definitions = List.of();
            try
            {
                printUsage( outputStream, resources, command, definitions );
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
        }
        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;

            outputStream = null;
            resources = Optional.empty();
            command = "command";
            definitions = List.of();
            try
            {
                printUsage( outputStream, resources, command, definitions );
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

            outputStream = nullOutputStream();
            //noinspection OptionalAssignedToNull
            resources = null;
            command = "command";
            definitions = List.of();
            try
            {
                printUsage( outputStream, resources, command, definitions );
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

            outputStream = nullOutputStream();
            resources = Optional.empty();
            command = null;
            definitions = List.of();
            try
            {
                printUsage( outputStream, resources, command, definitions );
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

            outputStream = nullOutputStream();
            resources = Optional.empty();
            command = "command";
            definitions = null;
            try
            {
                printUsage( outputStream, resources, command, definitions );
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
        }

        outputStream = new ByteArrayOutputStream();
        resources = Optional.empty();
        command = "command";
        definitions = createCLIDefinitions( m_Values );

        final var expected =
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
        printUsage( outputStream, resources, command, definitions );
        final var actual = outputStream.toString();
        assertEquals( expected, actual );
    }   //  testPrintUsage()

    /**
     *  Validates whether the class is static.
     */
    @Test
    final void validateClass()
    {
        assertTrue( validateAsStaticClass( ConfigUtil.class ) );
    }   //  validateClass()
}
//  class TestConfigUtil

/*
 *  End of File
 */