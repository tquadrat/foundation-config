/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
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

package org.tquadrat.foundation.config;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.PROPERTY_IS_DEBUG;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.io.Serial;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.internal.Commons;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.util.stringconverter.FileStringConverter;

/**
 *  <p>{@summary Signals an error in the user input.}</p>
 *  <p>The message keys corresponds to the current default message by the
 *  suffix: the default message for
 *  {@link #MSGKEY_Aborted}
 *  is
 *  {@link #MSG_Aborted}
 *  and so on.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @thanks Kohsuke Kawaguchi - kk@kohsuke.org
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyConstructors" )
@ClassVersion( sourceVersion = "$Id: CmdLineException.java 1050 2023-02-25 21:37:07Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class CmdLineException extends ValidationException
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The default error message: {@value}.
     */
    public static final String MSG_Aborted = "The command line parsing was aborted due to an exception: %1$s";

    /**
     *  The error message for an argument that is missing on the command line:
     *  {@value}.
     */
    public static final String MSG_ArgumentMissing = "The mandatory argument '%1$s' is missing on the command line";

    /**
     *  The error message for an illegal option argument: {@value}.
     */
    public static final String MSG_IllegalOperand = "'%2$s' is not a valid value for '%1$s'";

    /**
     *  The error message for an invalid file name on the command line:
     *  {@value}.
     */
    public static final String MSG_InvalidFileName = FileStringConverter.MSG_InvalidFileName;

    /**
     *  The error message for an invalid format: {@value}.
     */
    public static final String MSG_InvalidFormat = "The date format pattern '%1$s' is not valid";

    /**
     *  The error message for a missing option argument: {@value}.
     */
    public static final String MSG_MissingOperand = "Option '%1$s' requires an argument";

    /**
     *  The error message for an argument where none is allowed: {@value}.
     */
    public static final String MSG_NoArgumentAllowed = "No arguments allowed: %1$s";

    /**
     *  The error message for an invalid option: {@value}.
     */
    public static final String MSG_OptionInvalid = "The option '%1$s' is invalid";

    /**
     *  The error message for a mandatory option that is missing on the command
     *  line: {@value}.
     */
    public static final String MSG_OptionMissing = "The mandatory option '%1$s' is missing on the command line";

    /**
     *  The message for an unspecified failure of the command line parsing:
     *  {@value}.
     */
    public static final String MSG_ParseFailed = "Parsing the command line failed";

    /**
     *  The error message for too many arguments on the command line: {@value}.
     */
    public static final String MSG_TooManyArguments = "Too many arguments provided: %1$s";

    /**
     *  The message key for the default error message.
     *
     *  @see #MSG_Aborted
     */
    @Message
    (
        description = "The default error message",
        translations =
        {
            @Translation( language = "en", text = MSG_Aborted ),
            @Translation( language = "de", text = "Die Auswertung der Kommandozeile wurde mit einer Exception abgebrochen: %1$s" )
        }
    )
    public static final int MSGKEY_Aborted = 3;

    /**
     *  The message key for the error message about an argument that is missing
     *  on the command line.
     *
     *  @see #MSG_ArgumentMissing
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an argument that is missing on the command line.",
        translations =
        {
            @Translation( language = "en", text = MSG_ArgumentMissing ),
            @Translation( language = "de", text = "Das notwendige Argument '%1$s' fehlt auf der Kommandozeile" )
        }
    )
    public static final int MSGKEY_ArgumentMissing = 4;

    /**
     *  The message key for an error message about an illegal operand.
     *
     *  @see #MSG_IllegalOperand
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an illegal operand.",
        translations =
        {
            @Translation( language = "en", text = MSG_IllegalOperand ),
            @Translation( language = "de", text = "'%2$s' ist kein gültiger Wert für '%1$s'" )
        }
    )
    public static final int MSGKEY_IllegalOperand = 5;

    /**
     *  The resource bundle key for the message about an invalid file name
     *  String on the command line.
     */
    @Message
    (
        description = "The error message for an invalid file name on the command line.",
        translations =
            {
                @Translation( language = "en", text = FileStringConverter.MSG_InvalidFileName ),
                @Translation( language = "de", text = "'%2$s' ist kein gültiger Wert für '%1$s'" )
            }
    )
    public static final int MSGKEY_InvalidFileName = 6;

    /**
     *  The message key for an error message about an invalid format.
     *
     *  @see #MSG_InvalidFormat
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an invalid format.",
        translations =
        {
            @Translation( language = "en", text = MSG_InvalidFormat ),
            @Translation( language = "de", text = "Das Format '%1$s' ist nicht gültig für eine Datums-/Zeitangabe" )
        }
    )
    public static final int MSGKEY_InvalidFormat = 7;

    /**
     *  The message key for an error message about a missing option argument.
     *
     *  @see #MSG_MissingOperand
     */
    @Message
    (
        description = "The error message about a missing option argument.",
        translations =
        {
            @Translation( language = "en", text = MSG_MissingOperand ),
            @Translation( language = "de", text = "Die Option '%1$s' erfordert ein Argument" )
        }
    )
    public static final int MSGKEY_MissingOperand = 8;

    /**
     *  The message key for an error message about an argument where none is
     *  allowed.
     *
     *  @see #MSG_NoArgumentAllowed
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an argument where none is allowed.",
        translations =
        {
            @Translation( language = "en", text = MSG_NoArgumentAllowed ),
            @Translation( language = "de", text = "Keine Argumente zulässig: %1$s" )
        }
    )
    public static final int MSGKEY_NoArgumentAllowed = 9;

    /**
     *  The message key for the error message about an invalid option.
     *
     *  @see #MSG_OptionInvalid
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an invalid option.",
        translations =
        {
            @Translation( language = "en", text = MSG_OptionInvalid ),
            @Translation( language = "de", text = "Die Option '%1$s' ist ungültig" )
        }
    )
    public static final int MSGKEY_OptionInvalid = 10;

    /**
     *  The message key for the error message about an option that is missing
     *  on the command line.
     *
     *  @see #MSG_OptionMissing
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an option that is missing on the command line.",
        translations =
        {
            @Translation( language = "en", text = MSG_OptionMissing ),
            @Translation( language = "de", text = "Die erforderliche Option '%1$s' fehlt auf der Kommandozeile" )
        }
    )
    public static final int MSGKEY_OptionMissing = 11;

    /**
     *  The message key for the message about an unspecified failure of the
     *  parsing.
     *
     *  @see #MSG_ParseFailed
     */
    @Message
    (
        description = "The error message about aan unspecified failure of the parsing.",
        translations =
        {
            @Translation( language = "en", text = MSG_ParseFailed ),
            @Translation( language = "de", text = "Die Auswertung der Kommandozeile ist fehlgeschlagen" )
        }
    )
    public static final int MSGKEY_ParseFailed = 12;

    /**
     *  The message key for the error message about too many arguments on the
     *  command line.
     *
     *  @see #MSG_TooManyArguments
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @Message
    (
        description = "The error message about an unspecified failure of the parsing.",
        translations =
        {
            @Translation( language = "en", text = MSG_TooManyArguments ),
            @Translation( language = "de", text = "Zu viele Argumente auf der Kommandozeile: %1$s" )
        }
    )
    public static final int MSGKEY_TooManyArguments = 13;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The CLI definition for the argument/option that caused this exception.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<CLIDefinition> m_CLIDefinition;

    /**
     *  The arguments for the message that is retrieved with the
     *  {@link #m_MessageKey}.
     */
    private final Object [] m_MessageArguments;

    /**
     *  The message key.
     */
    private final int m_MessageKey;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     */
    @Serial
    private static final long serialVersionUID = -8574071211991372980L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  message The error message.
     *  @param  messageKey  The resource bundle key for an alternative message.
     *  @param  messageArguments   The arguments for the generation of the
     *      alternative message.
     */
    public CmdLineException( final String message, final int messageKey, final Object... messageArguments )
    {
        this( null, message, messageKey, messageArguments );
    }   //  CmdLineException()

    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  cliDefinition   The CLI definition for the argument/option that
     *      caused this exception.
     *  @param  message The error message.
     *  @param  messageKey  The resource bundle key for an alternative message.
     *  @param  messageArguments   The arguments for the generation of the
     *      alternative message.
     */
    public CmdLineException( final CLIDefinition cliDefinition, final String message, final int messageKey, final Object... messageArguments )
    {
        super( format( requireNonNullArgument( message, "message" ), messageArguments ) );

        m_CLIDefinition = Optional.ofNullable( cliDefinition );

        m_MessageArguments = messageArguments.clone();
        m_MessageKey = messageKey;
    }   //  CmdLineException()

    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  message The error message.
     *  @param  cause   The exception that caused this exception.
     *  @param  messageKey  The resource bundle key for an alternative message.
     *  @param  messageArguments   The arguments for the generation of the
     *      alternative message.
     */
    public CmdLineException( final String message, final Throwable cause, final int messageKey, final Object... messageArguments )
    {
        this( null, message, cause, messageKey, messageArguments );
    }   //  CmdLineException()

    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  cliDefinition   The CLI definition for the argument/option that
     *      caused this exception.
     *  @param  message The error message.
     *  @param  cause   The exception that caused this exception.
     *  @param  messageKey  The resource bundle key for an alternative message.
     *  @param  messageArguments   The arguments for the generation of the
     *      alternative message.
     */
    public CmdLineException( final CLIDefinition cliDefinition, final String message, final Throwable cause, final int messageKey, final Object... messageArguments )
    {
        super( format( requireNonNullArgument( message, "message" ), messageArguments ), cause );

        m_CLIDefinition = Optional.ofNullable( cliDefinition );

        m_MessageArguments = messageArguments.clone();
        m_MessageKey = messageKey;
    }   //  CmdLineException()

    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  cause   The exception that caused this exception.
     */
    public CmdLineException( final Throwable cause )
    {
        this( Optional.empty(), cause );
    }   //  CmdLineException()

    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  cliDefinition   The CLI definition for the argument/option that
     *      caused this exception.
     *  @param  cause   The exception that caused this exception.
     */
    public CmdLineException( final CLIDefinition cliDefinition, final Throwable cause )
    {
        this( Optional.of( requireNonNullArgument( cliDefinition, "cliDefinition" ) ), cause );
    }   //  CmdLineException()

    /**
     *  Creates a new {@code CmdLineException} instance.
     *
     *  @param  cliDefinition   An instance of
     *      {@link Optional}
     *      that holds the CLI definition for the argument/option that caused
     *      this exception.
     *  @param  cause   The exception that caused this exception.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public CmdLineException( final Optional<CLIDefinition> cliDefinition, final Throwable cause )
    {
        super( format( MSG_Aborted, nonNull( cause ) ? cause.getClass().getName() : "unknown" ), cause );

        m_CLIDefinition = requireNonNullArgument( cliDefinition, "cliDefinition" );

        m_MessageKey = MSGKEY_Aborted;
        m_MessageArguments = new Object [] {nonNull( cause ) ? cause.getClass().getName() : "unknown"};
    }   //  CmdLineException()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final synchronized Throwable fillInStackTrace()
    {
        final var retValue = Boolean.getBoolean( PROPERTY_IS_DEBUG )
            ? super.fillInStackTrace()
            : this;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fillInStackTrace()

    /**
     *  Returns the
     *  {@link CLIDefinition}
     *  that triggered the exception.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the CLI definition.
     */
    public final Optional<CLIDefinition> getCLIDefinition() { return m_CLIDefinition; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getLocalizedMessage() { return Commons.retrieveMessage( m_MessageKey, false, m_MessageArguments ); }

    /**
     *  Returns the message arguments.
     *
     *  @return The message arguments.
     */
    public final Object [] getMessageArguments() { return m_MessageArguments.clone(); }

    /**
     *  Returns the resource bundle key for the alternative message.
     *
     *  @return The message key.
     */
    public final int getMessageKey() { return m_MessageKey; }
}
//  class CmdLineException

/*
 *  End of File
 */