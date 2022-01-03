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

import static java.lang.System.getProperty;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.Template.replaceVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.config.cli.DateValueHandler;
import org.tquadrat.foundation.config.cli.StringValueHandler;
import org.tquadrat.foundation.config.spi.CLIArgumentDefinition;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.CLIOptionDefinition;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Tests for
 *  {@link ArgumentParser}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestArgumentParser.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 10
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestArgumentParser.java 896 2021-04-05 20:25:33Z tquadrat $" )
public class TestArgumentParser extends TestBaseClass
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
    public static final List<CLIDefinition> createCLIDefinitions( final Map<? super String, Object> target )
    {
        String property;
        var index = 0;
        List<String> names;
        String usage;
        String usageKey;
        String metaVar;
        boolean required;
        CmdLineValueHandler<?> handler;
        boolean multiValued;
        String format;

        final List<CLIDefinition> retValue = new ArrayList<>();

        property = "stringArgument";
        usage = "A String value as an argument";
        usageKey ="USAGE_StringArgument";
        metaVar = "STRING_ARG";
        required = true;
        handler = new StringValueHandler( target::put );
        multiValued = false;
        format = null;
        retValue.add( new CLIArgumentDefinition( property, index++, usage, usageKey, metaVar, required, handler, multiValued, format ) );

        property = "dateArgument";
        usage = "A Date value as an argument";
        usageKey ="USAGE_DateArgument";
        metaVar = "DATE_ARG";
        required = true;
        handler = new DateValueHandler( target::put );
        multiValued = false;
        format = "yyyy-MM-dd";
        //noinspection UnusedAssignment
        retValue.add( new CLIArgumentDefinition( property, index++, usage, usageKey, metaVar, required, handler, multiValued, format ) );

        property = "stringOption";
        names = List.of( "--string", "-s" );
        usage = "A String value as an option";
        usageKey ="USAGE_StringOption";
        metaVar = "STRING";
        required = false;
        handler = new StringValueHandler( target::put );
        multiValued = false;
        format = null;
        retValue.add( new CLIOptionDefinition( property, names, usage, usageKey, metaVar, required, handler, multiValued, format ) );

        property = "dateOption";
        names = List.of( "--date", "-d" );
        usage = "A Date value as an option";
        usageKey ="USAGE_DateOption";
        metaVar = "DATE";
        required = true;
        handler = new DateValueHandler( target::put );
        multiValued = false;
        format = "yyyy-MM-dd";
        retValue.add( new CLIOptionDefinition( property, names, usage, usageKey, metaVar, required, handler, multiValued, format ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCLIDefinitions()

    /**
     *  Tests the command line resolution.
     *
     *  @see ArgumentParser#resolveCommandLine(String[])
     */
    @Test
    final void testCommandLineResolution()
    {
        skipThreadTest();

        String [] args;
        String actual, expected;

        final var candidate = new ArgumentParser( List.of( new CLIOptionDefinition( "property", List.of( "--option" ), null, null, "property", false, new StringValueHandler( ( p, v) -> {/* Does nothing*/} ), false, null ) ) );
        assertNotNull( candidate );

        /*
         * Just one argument on the command line.
         */
        args = new String [] {"value"};
        expected = "value";
        actual = candidate.resolveCommandLine( args );
        assertEquals( expected, actual );

        /*
         * Different formats for options.
         */
        args = new String [] {"-o", "--option", "-oValue", "--option=Value", "-o", "Value", "--option", "Value"};
        expected = "-o --option -o Value --option Value -o Value --option Value";
        actual = candidate.resolveCommandLine( args );
        assertEquals( expected, actual );

        /*
         * Options as arguments. Arguments will not be split, and no options
         * will follow after '--'
         */
        args = new String [] {"-o", "--option", "--", "-oValue", "--option=Value", "-o", "Value", "--option", "Value"};
        expected = "-o --option -- -oValue --option=Value -o Value --option Value";
        actual = candidate.resolveCommandLine( args );
        assertEquals( expected, actual );

        /*
         * Missing file.
         */
        args = new String [] {"@OneMissingFile", "--", "@AnotherMissingFile"};
        expected = "@OneMissingFile -- @AnotherMissingFile";
        actual = candidate.resolveCommandLine( args );
        assertEquals( expected, actual );

        /*
         * Existing file.
         */
        args = new String [] {"@src/test/data/ArgFile1"};
        expected = "--option value --option value argument \"Also sentences with blanks are allowed\"";
        actual = candidate.resolveCommandLine( args );
        assertEquals( expected, actual );

        /*
         * Existing file with variable resolution.
         */
        args = new String [] {"@src/test/data/ArgFile2"};
        expected = replaceVariable( "--option value --option --user ${user.name} value argument", k -> Optional.ofNullable( getProperty( k ) ) );
        actual = candidate.resolveCommandLine( args );
        assertEquals( expected, actual );
    }   //  testCommandLineResolution()

    /**
     *  Tests the creation of an instance for
     *  {@link CLIArgumentDefinition}.
     */
    @Test
    final void testCreateArgumentDefinition()
    {
        skipThreadTest();

        CLIArgumentDefinition candidate;
        String property;
        final int index;
        final String usage;
        final String usageKey;
        String metaVar;
        final boolean required;
        CmdLineValueHandler<?> handler;
        final boolean multiValued;
        final String format;

        index = 0;
        usage = null;
        usageKey = null;
        required = false;
        multiValued = false;
        format = null;

        {
            final Class<? extends Throwable> expectedException = EmptyArgumentException.class;

            property = EMPTY_STRING;
            metaVar = "metaVar";
            handler = new StringValueHandler( (n,s) -> {/* Does nothing */} );
            try
            {
                candidate = new CLIArgumentDefinition( property, index, usage, usageKey, metaVar, required, handler, multiValued, format );
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

            property = "property";
            metaVar = EMPTY_STRING;
            handler = new StringValueHandler( (n,s) -> {/* Does nothing */} );
            try
            {
                candidate = new CLIArgumentDefinition( property, index, usage, usageKey, metaVar, required, handler, multiValued, format );
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
        }

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;

            property = null;
            metaVar = "metaVar";
            handler = new StringValueHandler( (n,s) -> {/* Does nothing */} );
            try
            {
                candidate = new CLIArgumentDefinition( property, index, usage, usageKey, metaVar, required, handler, multiValued, format );
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

            property = "property";
            metaVar = null;
            handler = new StringValueHandler( (n,s) -> {/* Does nothing */} );
            try
            {
                candidate = new CLIArgumentDefinition( property, index, usage, usageKey, metaVar, required, handler, multiValued, format );
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

            property = "property";
            metaVar = "metaVar";
            handler = null;
            try
            {
                candidate = new CLIArgumentDefinition( property, index, usage, usageKey, metaVar, required, handler, multiValued, format );
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
        }

        property = "property";
        metaVar = "metaVar";
        handler = new StringValueHandler( (n,s) -> {/* Does nothing */} );
        candidate = new CLIArgumentDefinition( property, index, usage, usageKey, metaVar, required, handler, multiValued, format );
        assertNotNull( candidate );
    }   //  testCreateArgumentDef()

    /**
     *  Tests the creation of an instance for
     *  {@link ArgumentParser}.
     */
    @Test
    final void testCreateArgumentParser()
    {
        skipThreadTest();

        ArgumentParser candidate;

        List<CLIDefinition> cliDefinitions;

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;
            try
            {
                candidate = new ArgumentParser( null );
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
        }

        //---* Empty list *----------------------------------------------------
        cliDefinitions = List.of();
        candidate = new ArgumentParser( cliDefinitions );
        candidate.parse();

        {
            final Class<? extends Throwable> expectedException = CmdLineException.class;
            try
            {
                candidate.parse( "value" );
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
                candidate.parse( "--option=value" );
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
                candidate.parse( "-ovalue" );
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
                candidate.parse( "--option value" );
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
                candidate.parse( "-o value" );
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
                candidate.parse( "--option" );
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
                candidate.parse( "-o" );
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

        //---* Mocked definitions *--------------------------------------------
        final var propertyName = "value1";
        final var value = "value";
        cliDefinitions = List.of( new CLIArgumentDefinition( propertyName, 0, "Sets value1", "USAGE_value1", "VALUE1", true, new StringValueHandler( m_Values::put ), false, null ) );
        candidate = new ArgumentParser( cliDefinitions );
        assertNotNull( candidate );
        candidate.parse( value );
        assertFalse( m_Values.isEmpty() );
        assertTrue( m_Values.containsKey( propertyName ) );
        final var v = m_Values.get( propertyName );
        assertNotNull( v );
        assertTrue( v instanceof String );
        assertEquals( value, v.toString() );
    }   //  testCreateArgumentParser()
}
//  class TestArgumentParser

/*
 *  End of File
 */