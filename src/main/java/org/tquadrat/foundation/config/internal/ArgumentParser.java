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

import static java.nio.file.Files.lines;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterators.spliterator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.config.CLIBeanSpec.ARG_FILE_ESCAPE;
import static org.tquadrat.foundation.config.CLIBeanSpec.LEAD_IN;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_ArgumentMissing;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_MissingOperand;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_NoArgumentAllowed;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_OptionInvalid;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_OptionMissing;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_TooManyArguments;
import static org.tquadrat.foundation.config.CmdLineException.MSG_ArgumentMissing;
import static org.tquadrat.foundation.config.CmdLineException.MSG_MissingOperand;
import static org.tquadrat.foundation.config.CmdLineException.MSG_NoArgumentAllowed;
import static org.tquadrat.foundation.config.CmdLineException.MSG_OptionInvalid;
import static org.tquadrat.foundation.config.CmdLineException.MSG_OptionMissing;
import static org.tquadrat.foundation.config.CmdLineException.MSG_TooManyArguments;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;
import static org.tquadrat.foundation.util.SystemUtils.systemPropertiesAsStringMap;
import static org.tquadrat.foundation.util.Template.replaceVariable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIArgumentDefinition;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.CLIOptionDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  The parser for the command line arguments.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ArgumentParser.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ArgumentParser.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public class ArgumentParser
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary This class is essentially a pointer over the
     *  {@link String}
     *  array with the command line arguments.} It can move forward, and it can
     *  look ahead.</p>
     *  <p>But it will also resolve the references to argument files, and it
     *  will translate single letter options without blanks between option and
     *  value into two entries, as well as long entries where an equal sign is
     *  used.</p>
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: ArgumentParser.java 896 2021-04-05 20:25:33Z tquadrat $
     *  @since 0.0.1
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: ArgumentParser.java 896 2021-04-05 20:25:33Z tquadrat $" )
    @API( status = INTERNAL, since = "0.0.1" )
    private final class CmdLineImpl implements Iterator<String>, Parameters
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The arguments list.
         */
        private final List<String> m_ArgumentList = new ArrayList<>();

        /**
         *  The current position in the arguments list.
         */
        private int m_CurrentPos;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new object for CmdLineImpl.
         *
         *  @param  args    The arguments list.
         */
        @SuppressWarnings( "IfStatementWithTooManyBranches" )
        public CmdLineImpl( final String... args )
        {
            assert nonNull( args ) : "args is null";

            final Collection<String> failedFiles = new HashSet<>();
            var expandedToFile = true; // Flag that indicates whether an argument file was encountered
            var inBuffer = List.of( args );
            List<String> outBuffer;

            while( expandedToFile )
            {
                expandedToFile = false;
                outBuffer = new ArrayList<>( inBuffer.size() );
                for( final var arg : inBuffer )
                {
                    if( arg.startsWith( ARG_FILE_ESCAPE ) && !failedFiles.contains( arg ) )
                    {
                        outBuffer.addAll( loadArgumentFile( arg, failedFiles ) );
                        expandedToFile = true;
                    }
                    else
                    {
                        outBuffer.add( arg );
                    }
                }
                inBuffer = outBuffer;
            }

            var parsingOptions = parsingOptions();
            for( final var arg : inBuffer )
            {
                if( parsingOptions )
                {
                    if( arg.equals( LEAD_IN + LEAD_IN ) )
                    {
                        //---* The 'Stop Options Processing' token *-----------
                        m_ArgumentList.add( arg );
                        parsingOptions = false;
                    }
                    else if( arg.startsWith( LEAD_IN + LEAD_IN ) )
                    /*
                     * Sequence is crucial! We need to check for the '--'
                     * prefix before we check for the '-' prefix, otherwise
                     * the result is ... interesting
                     */
                    {
                        //---* Long option *-----------------------------------
                        final var pos = arg.indexOf( '=' );
                        if( pos > 3 )
                        {
                            m_ArgumentList.add( arg.substring( 0, pos ) );
                            m_ArgumentList.add( arg.substring( pos + 1 ) );
                        }
                        else
                        {
                            m_ArgumentList.add( arg );
                        }
                    }
                    else if( arg.startsWith( LEAD_IN ) )
                    {
                        //---* Single letter option *--------------------------
                        if( arg.length() > 2 )
                        {
                            m_ArgumentList.add( arg.substring( 0, 2 ) );
                            m_ArgumentList.add( arg.substring( 2 ) );
                        }
                        else
                        {
                            m_ArgumentList.add( arg );
                        }
                    }
                    else
                    {
                        //---* No option at all, or an option argument *-------
                        m_ArgumentList.add( arg );
                    }
                }
                else
                {
                    m_ArgumentList.add( arg );
                }
            }

            //---* We start at 0 *---------------------------------------------
            m_CurrentPos = 0;
        }   //  CmdLineImpl()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the current token from the arguments list.
         *
         *  @return The current token.
         */
        public final String getCurrentToken() { return m_ArgumentList.get( m_CurrentPos ); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String getParameter( final int index ) throws CmdLineException
        {
            assert index >= 0 : "index is less than 0";

            String retValue = null;
            if( m_CurrentPos + index < m_ArgumentList.size() )
            {
                retValue = m_ArgumentList.get( m_CurrentPos + index );
                if( retValue.startsWith( LEAD_IN ) && ArgumentParser.this.parsingOptions() )
                {
                    //---* We found the next option *--------------------------
                    throw new CmdLineException( getCurrentOptionDefinition(), MSG_MissingOperand, MSGKEY_MissingOperand, getOptionName() );
                }
            }
            else
            {
                throw new CmdLineException( getCurrentOptionDefinition(), MSG_MissingOperand, MSGKEY_MissingOperand, getOptionName() );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  getParameter()

        /**
         *  Checks if there are more entries.
         *
         *  @return {@code true} if there are more entries,
         *      {@code false} otherwise.
         */
        @Override
        public final boolean hasNext() { return m_CurrentPos < m_ArgumentList.size(); }

        /**
         *  <p>{@summary Loads an argument file as specified by the given
         *  argument and returns the contents of that file as additional
         *  command line arguments.}</p>
         *  <p>If no file could be retrieved for the name given with the
         *  argument, that argument will be added to the list of failed files
         *  and the unchanged argument will be returned.</p>
         *  <p>Variables of the form <code>${<i>&lt;name&gt;</i>}</code> will
         *  be replaced by the value for <i>name</i> from the system properties
         *  ({@link System#getProperty(String)}).</p>
         *  <p>Lines that starts with &quot;#&quot; are comments; if an
         *  argument has to start with &quot;#&quot;, it has to be escaped with
         *  &quot;\#&quot;.</p>
         *
         *  @param  arg The command line argument.
         *  @param  failedFiles The list of failed files.
         *  @return The additional command line arguments as read from the
         *      file, if it could be opened.
         */
        @SuppressWarnings( "resource" )
        private final Collection<String> loadArgumentFile( final String arg, @SuppressWarnings( "BoundedWildcard" ) final Collection<String> failedFiles )
        {
            final var systemProperties = systemPropertiesAsStringMap();

            Collection<String> retValue;
            if( failedFiles.contains( arg ) )
            {
                retValue = List.of( arg );
            }
            else
            {
                try
                {
                    final var argumentFile = new File( arg.substring( 1 ) )
                        .getCanonicalFile()
                        .getAbsoluteFile();
                    retValue = lines( argumentFile.toPath() )
                        .filter( line -> !line.startsWith( "#" ) ) // Drop the comments
                        .map( line -> line.startsWith( "\\" ) ? line.substring( 1 ) : line )
                        .filter( StringUtils::isNotEmptyOrBlank ) // Drop empty lines
                        .map( line -> replaceVariable( line, systemProperties ) )
                        .collect( toList() );
                }
                catch( @SuppressWarnings( "unused" ) final IOException e )
                {
                    retValue = List.of( arg );
                    failedFiles.add( arg );
                }
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  loadArgumentFile()

        /**
         *  {@inheritDoc}
         */
        @SuppressWarnings( "NewExceptionWithoutArguments" )
        @Override
        public final String next() throws NoSuchElementException
        {
            if( !hasNext() ) throw new NoSuchElementException();
            final var retValue = getCurrentToken();
            proceed( 1 );

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  next()

        /**
         *  Skip the given number of entries..
         *
         *  @param  n   The number of entries to skip; must be greater 0.
         */
        public final void proceed( final int n )
        {
            assert n >= 0 : "n less than 0";

            m_CurrentPos += n;
        }   //  proceed()

        /**
         *  In case the entry is a combination from option and the related
         *  parameter (like {@code --arg value} or, for single character
         *  options, {@code -p value}), this method is used to put it back to
         *  the command line.
         *
         *  @param  part1   The first part, usually the option.
         *  @param  part2   The second part, usually the value.
         */
        @SuppressWarnings( "unused" )
        public final void putback( final String part1, final String part2 )
        {
            assert isNotEmpty( part1 ) : "part1 is empty";
            assert isNotEmpty( part2 ) : "part2 is empty";

            m_ArgumentList.set( m_CurrentPos, part2 );
            m_ArgumentList.add( m_CurrentPos, part1 );
        }   //  putback()
    }
    //  class CmdLineImpl

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The
     *  {@link CLIDefinition}
     *  instances for arguments.
     */
    private final List<CLIArgumentDefinition> m_ArgumentDefinitions = new ArrayList<>();

    /**
     *  The definition for the current command line entry.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private CLIOptionDefinition m_CurrentOptionDefinition = null;

    /**
     *  The
     *  {@link CLIDefinition}
     *  instances for options.
     */
    private final Map<String,CLIOptionDefinition> m_OptionDefinitions = new TreeMap<>();

    /**
     *  {@code true} (the default) if options has to be parsed. If set to
     *  {@code false}, only arguments are parsed.
     *
     *  @see #stopOptionParsing()
     */
    private boolean m_ParsingOptions = false;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ArgumentParser} instance.
     *
     *  @param  cliDefinitions  The definition for the command line options and
     *      arguments from the configuration bean specification.
     */
    public ArgumentParser( final Collection<? extends CLIDefinition> cliDefinitions )
    {
        //---* Add the definitions to the registry *---------------------------
        for( final var cliDefinition : requireNonNullArgument( cliDefinitions, "cliDefinitions" ) )
        {
            if( cliDefinition.isArgument() )
            {
                addArgument( cliDefinition );
            }
            else
            {
                addOption( cliDefinition );
            }
        }

        //---* Check the consistency of the arguments list *-------------------
        if( m_ArgumentDefinitions.stream().anyMatch( Objects::isNull ) )
        {
            final var indexes = Stream.iterate( 0, i -> i < m_ArgumentDefinitions.size(), i -> i + 1 )
                .filter( i -> isNull( m_ArgumentDefinitions.get( i ) ) )
                .map( i -> Integer.toString( i ) )
                .collect( joining( ", " ) );
            throw new IllegalArgumentException( format( "Missing index: %s - Gap in Sequence", indexes ) );
        }
    }   //  ArgumentParser()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds an argument definition to the registry for arguments.
     *
     *  @param  definition  The argument definition to add.
     */
    private final void addArgument( final CLIDefinition definition )
    {
        @SuppressWarnings( "CastToConcreteClass" )
        final var argumentDefinition = (CLIArgumentDefinition) requireNonNullArgument( definition, "definition" );

        //--* Make sure the argument will fit in the list *--------------------
        final var index = argumentDefinition.index();
        while( index >= m_ArgumentDefinitions.size() )
        {
            m_ArgumentDefinitions.add( null );
        }
        if( nonNull( m_ArgumentDefinitions.get( index ) ) )
        {
            throw new IllegalArgumentException( format( "Argument index '%d' is used more than once", index ) );
        }
        m_ArgumentDefinitions.set( index, argumentDefinition );
    }   //  addArgument()

    /**
     *  Adds an option definition to the registry for options.
     *
     *  @param  definition  The option definition to add.
     */
    private final void addOption( final CLIDefinition definition )
    {
        //---* We have at least one option ... *-------------------------------
        m_ParsingOptions = true;

        @SuppressWarnings( "CastToConcreteClass" )
        final var optionDefinition = (CLIOptionDefinition) requireNonNullArgument( definition, "definition" );
        checkOptionNotYetUsed( optionDefinition.name() );
        m_OptionDefinitions.put( optionDefinition.name(), optionDefinition );
        for( final var alias : optionDefinition.aliases() )
        {
            checkOptionNotYetUsed( alias );
            m_OptionDefinitions.put( alias, optionDefinition );
        }
}   //  addOption()

    /**
     *  Finds an option definition by the given option name.
     *
     *  @param  name    The option name.
     *  @return The option definition.
     *  @throws CmdLineException    There is not option definition for the
     *      given option name.
     */
    private final CLIOptionDefinition findOptionDefinition( final String name )
    {
        assert nonNull( name ) : "name is null";

        final var retValue = m_OptionDefinitions.get( name );
        if( isNull( retValue ) )
        {
            throw new CmdLineException( format( MSG_OptionInvalid, name ), MSGKEY_OptionInvalid, name );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  findOptionDefinition()

    /**
     *  Returns the current CLI option definition.
     *
     *  @return The current CLI option definition.
     */
    final CLIOptionDefinition getCurrentOptionDefinition() { return m_CurrentOptionDefinition; }

    /**
     *  Returns the name of the option that is being processed currently.
     *
     *  @return The name of the current option.
     */
    final String getOptionName()
    {
        final var retValue = nonNull( m_CurrentOptionDefinition ) ? m_CurrentOptionDefinition.name() : null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getOptionName()

    /**
     *  Checks if the given token is an option (as opposed to an argument).
     *  Option tokens will have a hyphen
     *  ({@value org.tquadrat.foundation.config.CLIBeanSpec#LEAD_IN})
     *  as their first character.
     *
     *  @param  token   The token to test.
     *  @return {@code true} if the given token is an option,
     *      {@code false} if it is an argument, or if no (more) options are
     *      expected at all.
     *
     *  @see #stopOptionParsing()
     *  @see #m_ParsingOptions
     */
    private final boolean isOption( final String token )
    {
        assert nonNull( token ) : "token is null";
        assert !EMPTY_STRING.equals( token ) : "token is empty";

        final var retValue = m_ParsingOptions && token.startsWith( LEAD_IN );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isOption()

    /**
     *  Checks the command line definition whether the given name is not yet
     *  used, either as a name or an alias.
     *
     *  @param  name    The name to check.
     *  @throws IllegalArgumentException    The given name is already in use.
     */
    private final void checkOptionNotYetUsed( final String name ) throws IllegalArgumentException
    {
        assert nonNull( name ) : "name is null";

        if( m_OptionDefinitions.containsKey( name ) )
        {
            throw new IllegalArgumentException( format( "Option name '%s' is used more than once", name ) );
        }
    }   //  checkOptionNotYetUsed()

    /**
     *  Parses the given command line arguments and sets the retrieved values
     *  to the configuration bean.
     *
     *  @param  args    The command line arguments to parse.
     *  @throws CmdLineException    An error occurred while parsing the
     *      arguments or a mandatory option or argument is missing on the
     *      command line.
     */
    public final void parse( final String... args ) throws CmdLineException
    {
        final var cmdLine = new CmdLineImpl( requireNonNullArgument( args, "args" ) );

        final Collection<CLIDefinition> present = new HashSet<>();
        var argIndex = 0;
        ParseLoop: while( cmdLine.hasNext() )
        {
            final var arg = cmdLine.getCurrentToken();
            if( isOption( arg ) )
            {
                m_CurrentOptionDefinition = findOptionDefinition( arg );
                present.add( m_CurrentOptionDefinition );

                //---* We know the option; skip its name *---------------------
                cmdLine.proceed( 1 );

                //---* Set the value *-----------------------------------------
                cmdLine.proceed( m_CurrentOptionDefinition.processParameters( cmdLine ) );
            }
            else
            {
                if( argIndex >= m_ArgumentDefinitions.size() )
                {
                    final var message = m_ArgumentDefinitions.isEmpty() ? MSG_NoArgumentAllowed : MSG_TooManyArguments;
                    final var messageKey = m_ArgumentDefinitions.isEmpty() ? MSGKEY_NoArgumentAllowed : MSGKEY_TooManyArguments;
                    throw new CmdLineException( message, messageKey, arg );
                }

                //---* We know the argument ... *------------------------------
                final var currentArgumentDefinition = m_ArgumentDefinitions.get( argIndex );
                present.add( currentArgumentDefinition );
                if( !currentArgumentDefinition.isMultiValued() )
                {
                    /*
                     * Multi-valued arguments are only allowed as the last
                     * argument, and we can have as many values for them as we
                     * want (or the operating systems allows on the command
                     * line).
                     */
                    ++argIndex;
                }

                //---* Set the value *-----------------------------------------
                cmdLine.proceed( currentArgumentDefinition.processParameters( cmdLine ) );
            }
        }   //  ParseLoop:

        //---* Make sure that all mandatory options are present *--------------
        for( final var optionDefinition : m_OptionDefinitions.values() )
        /*
         * We can live with the fact that in case of an alias an option
         * definition is inspected twice or even more often.
         */
        {
            if( optionDefinition.required() && !present.contains( optionDefinition ) )
            {
                throw new CmdLineException( optionDefinition, MSG_OptionMissing, MSGKEY_OptionMissing, optionDefinition.name() );
            }
        }

        //---* Make sure that all mandatory arguments are present *------------
        for( final var argumentDefinition : m_ArgumentDefinitions )
        {
            if( argumentDefinition.required() && !present.contains( argumentDefinition ) )
            {
                throw new CmdLineException( argumentDefinition, MSG_ArgumentMissing, MSGKEY_ArgumentMissing, argumentDefinition.metaVar() );
            }
        }
    }   //  parse()

    /**
     *  Returns {@code true} if this {@code ArgumentParser} will parse
     *  options. This can be set to {@code false} either when no
     *  {@link org.tquadrat.foundation.config.Option &#64;Option}
     *  annotation was found on the configuration bean specification interface,
     *  when the stop token (&quot;--&quot;) was encountered on the command
     *  line, or when
     *  {@link #stopOptionParsing()}
     *  was called manually.
     *
     *  @return {@code true} when options are parsed, {@code false}
     *      if not.
     *
     *  @see org.tquadrat.foundation.config.CLIBeanSpec#LEAD_IN
     */
    public final boolean parsingOptions() { return m_ParsingOptions; }

    /**
     *  <p>{@summary Resolves the given command line.}</p>
     *  <p>The method is mainly meant for test and debugging purposes.</p>
     *
     *  @param  args    The command line arguments.
     *  @return The resolved command line as a single String.
     */
    public final String resolveCommandLine( final String... args )
    {
        final Iterator<String> cmdLine = new CmdLineImpl( args );
        final var spliterator = spliterator( cmdLine, args.length, IMMUTABLE | NONNULL );
        final var retValue = StreamSupport.stream( spliterator, false )
            .map( a -> a.contains( " " ) ? format( "\"%s\"", a ) : a )
            .collect( joining( " " ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  resolveCommandLine()

    /**
     *  Stops the parsing for options. After the call, the argument list will
     *  be parsed only for
     *  {@linkplain org.tquadrat.foundation.config.Argument arguments}.
     */
    public final void stopOptionParsing() { m_ParsingOptions = false; }
}
//  class ArgumentParserImpl

/*
 *  End of File
 */