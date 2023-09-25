/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.config.internal.Commons.retrieveText;
import static org.tquadrat.foundation.i18n.I18nUtil.composeTextKey;
import static org.tquadrat.foundation.i18n.I18nUtil.resolveText;
import static org.tquadrat.foundation.i18n.TextUse.TXT;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_Object_ARRAY;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.breakText;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIArgumentDefinition;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.CLIOptionDefinition;
import org.tquadrat.foundation.i18n.Text;
import org.tquadrat.foundation.i18n.Translation;

/**
 *  Builds the <i>usage</i> message that will be printed to the console (or
 *  wherever) in case help is requested on the command line, or an invalid
 *  option or argument is provided on it.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: UsageBuilder.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: UsageBuilder.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public class UsageBuilder
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The maximum line length: {@value}.
     */
    public static final int MAX_LINE_LENGTH = 80;

    /**
     *  The Text 'Arguments'.
     */
    @Text
    (
        description = "The text that introduces the 'Arguments' section in the usage message.",
        use = TXT,
        id = "Arguments",
        translations =
        {
            @Translation( language = "en", text = "Arguments" ),
            @Translation( language = "de", text = "Argumente" )
        }
    )
    public static final String TXT_Arguments = composeTextKey( UsageBuilder.class, TXT, "Arguments" );

    /**
     *  The text 'Options'.
     */
    @Text
    (
        description = "The text that introduces the 'Options' section in the usage message.",
        use = TXT,
        id = "Options",
        translations =
        {
            @Translation( language = "en", text = "Options" ),
            @Translation( language = "de", text = "Optionen" )
        }
    )
    public static final String TXT_Options = composeTextKey( UsageBuilder.class, TXT, "Options" );

    /**
     *  The text 'Usage: ' that introduces the <i>usage</i> text.
     */
    @Text
    (
        description = "The text that introduces the usage message.",
        use = TXT,
        id = "Usage",
        translations =
        {
            @Translation( language = "en", text = "Usage: " ),
            @Translation( language = "de", text = "Verwendung: " )
        }
    )
    public static final String TXT_Usage = composeTextKey( UsageBuilder.class, TXT, "Usage" );

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The resource bundle that is used to translate the descriptions of
     *  options and arguments.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<ResourceBundle> m_CallerResourceBundle;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code UsageBuilder} instance.
     *
     *  @param  callerResourceBundle   The resource bundle that is used to
     *      translate the descriptions of options and arguments.
     */
    public UsageBuilder( final ResourceBundle callerResourceBundle )
    {
        this( Optional.ofNullable( callerResourceBundle ) );
    }   //  UsageBuilder()

    /**
     *  Creates a new {@code UsageBuilder} instance.
     *
     *  @param  callerResourceBundle   The resource bundle that is used to
     *      translate the descriptions of options and arguments.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public UsageBuilder( final Optional<ResourceBundle> callerResourceBundle )
    {
        m_CallerResourceBundle = requireNonNullArgument( callerResourceBundle, "resources" );
    }   //  UsageBuilder()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds the arguments to the output.
     *
     *  @param  buffer  The buffer for the result.
     *  @param  arguments   The arguments.
     */
    private void addArguments( final StringBuilder buffer, final Map<String, ? extends CLIArgumentDefinition> arguments )
    {
        if( !arguments.isEmpty() )
        {
            buffer.append( '\n' )
                .append( retrieveText( TXT_Arguments ) )
                .append( ':' );
            @SuppressWarnings( "OptionalGetWithoutIsPresent" )
            final var widthLeft = arguments.values().stream()
                .map( CLIDefinition::metaVar )
                .mapToInt( String::length )
                .max()
                .getAsInt() + 2;
            final var widthRight = MAX_LINE_LENGTH - widthLeft;

            final List<String> leftLines = new ArrayList<>();
            final List<String> rightLines = new ArrayList<>();

            for( final var argument : arguments.values() )
            {
                leftLines.add( argument.metaVar() );

                final var usageText = resolveMessage( m_CallerResourceBundle, argument );
                rightLines.addAll( breakText( usageText, widthRight )
                    .toList() );

                padLines( leftLines, widthLeft, rightLines, widthRight );

                buffer.append( IntStream.range( 0, leftLines.size() )
                    .mapToObj( i -> leftLines.get( i ) + rightLines.get( i ) )
                    .collect( joining( "\n", "\n", EMPTY_STRING ) ) );
                leftLines.clear();
                rightLines.clear();
            }
            buffer.append( '\n' );
        }
    }   //  addArguments()

    /**
     *  Adds the options to the output.
     *
     *  @param  buffer  The buffer for the result.
     *  @param  options The options.
     */
    private void addOptions( final StringBuilder buffer, final Map<String, ? extends CLIOptionDefinition> options )
    {
        if( !options.isEmpty() )
        {
            buffer.append( "\n\n" )
                .append( retrieveText( TXT_Options ) )
                .append( ':' );
            @SuppressWarnings( "OptionalGetWithoutIsPresent" )
            final var widthLeft = options.values().stream()
                .flatMap( definition ->
                {
                    final Builder<String> builder = Stream.builder();
                    builder.add( format( "%s %s", definition.name(), definition.metaVar() ) );
                    for( final var a : definition.aliases() )
                    {
                        builder.add( format( "%s %s", a, definition.metaVar() ) );
                    }
                    return builder.build();
                } )
                .mapToInt( String::length )
                .max()
                .getAsInt() + 2;
            final var widthRight = MAX_LINE_LENGTH - widthLeft;

            final List<String> leftLines = new ArrayList<>();
            final List<String> rightLines = new ArrayList<>();

            for( final var option : options.values() )
            {
                leftLines.add( format( "%s %s", option.name(), option.metaVar() ) );
                for( final var a : option.aliases() )
                {
                    leftLines.add( format( "%s %s", a, option.metaVar() ) );
                }

                final var usageText = resolveMessage( m_CallerResourceBundle, option );
                rightLines.addAll( breakText( usageText, widthRight )
                    .toList() );

                padLines( leftLines, widthLeft, rightLines, widthRight );

                buffer.append( IntStream.range( 0, leftLines.size() )
                    .mapToObj( i -> leftLines.get( i ) + rightLines.get( i ) )
                    .collect( joining( "\n", "\n", EMPTY_STRING ) ) );
                leftLines.clear();
                rightLines.clear();
            }
            buffer.append( '\n' );
        }
    }   //  addOptions()

    /**
     *  Builds the <i>usage</i> text.
     *
     *  @param  command The command string.
     *  @param  definitions The CLI definitions.
     *  @return The usage text.
     */
    public final String build( final CharSequence command, final Collection<? extends CLIDefinition> definitions )
    {
        final Map<String,CLIArgumentDefinition> arguments = new TreeMap<>();
        final Map<String,CLIOptionDefinition> options = new TreeMap<>();

        requireNonNullArgument( definitions, "definitions" ).forEach( definition ->
        {
            if( definition.isArgument() )
            {
                arguments.put( definition.getSortKey(), (CLIArgumentDefinition) definition );
            }
            else
            {
                options.put( definition.getSortKey(), (CLIOptionDefinition) definition );
            }
        } );

        final var buffer = new StringBuilder( composeCommandLine( requireNotEmptyArgument( command, "command" ), options, arguments ) );
        addOptions( buffer, options );
        addArguments( buffer, arguments );

        final var retValue = buffer.toString();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  build()

    /**
     *  Composes the sample command line.
     *
     *  @param  command The command.
     *  @param  options The options.
     *  @param  arguments   The arguments.
     *  @return The command line.
     */
    private final String composeCommandLine( final CharSequence command, final Map<String, ? extends CLIOptionDefinition> options, final Map<String, ? extends CLIArgumentDefinition> arguments )
    {
        final var buffer = new StringBuilder( retrieveText( TXT_Usage ) ).append( command );
        for( final var definition : options.values() )
        {
            buffer.append( ' ' );
            if( !definition.required() ) buffer.append( '[' );
            buffer.append( definition.name() );
            if( isNotEmptyOrBlank( definition.metaVar() ) )
            {
                buffer.append( '\u202f' )
                    .append( definition.metaVar() );
            }
            if( !definition.required() ) buffer.append( ']' );
        }
        for( final var definition : arguments.values() )
        {
            buffer.append( ' ' );
            if( !definition.required() ) buffer.append( '[' );
            buffer.append( definition.metaVar() );
            if( !definition.required() ) buffer.append( ']' );
        }

        final var retValue = breakText( buffer, MAX_LINE_LENGTH ).collect( joining( "\n" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeCommandLine()

    /**
     *  Ensures that the left and the right part have the same number of
     *  entries and that the lines have the same length.
     *
     *  @param  leftLines   The lines on the left side.
     *  @param  widthLeft   The length for the lines on the left side.
     *  @param  rightLines  The lines on the right side.
     *  @param  widthRight  The length for the lines on the right side.
     */
    private static final void padLines( final List<String> leftLines, final int widthLeft, final Collection<? super String> rightLines, @SuppressWarnings( "unused" ) final int widthRight )
    {
        switch( Integer.signum( leftLines.size() - rightLines.size() ) )
        {
            case -1:
                while( leftLines.size() < rightLines.size() )
                {
                    leftLines.add( EMPTY_STRING );
                }
                break;

            case 0: break;

            case 1:
                while( rightLines.size() < leftLines.size() )
                {
                    rightLines.add( EMPTY_STRING );
                }
                break;

            default: break; // Cannot happen!!
        }

        final List<String> list = new ArrayList<>( leftLines );
        leftLines.clear();
        final var buffer = new StringBuilder( list.get( 0 ) );
        while( buffer.length() < widthLeft - 2 ) buffer.append( ' ' );
        buffer.append( ": " );
        leftLines.add( buffer.toString() );

        for( var i = 1; i < list.size(); ++i )
        {
            buffer.setLength( 0 );
            buffer.append( list.get( i ) );
            while( buffer.length() < widthLeft ) buffer.append( ' ' );
            leftLines.add( buffer.toString() );
        }
    }   //  padLines()

    /**
     *  Returns the message from the given
     *  {@link CLIDefinition}
     *
     *  @param  bundle  The resource bundle.
     *  @param  definition  The {@code CLIDefinition}.
     *  @return The resolved message.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public final String resolveMessage( final Optional<ResourceBundle> bundle, final CLIDefinition definition )
    {
        final var retValue = resolveText( bundle, requireNonNullArgument( definition, "definition" ).usage(), definition.usageKey(), EMPTY_Object_ARRAY );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  resolveMessage()
}
//  class UsageBuilder

/*
 *  End of File
 */