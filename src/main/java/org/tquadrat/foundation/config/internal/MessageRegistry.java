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

package org.tquadrat.foundation.config.internal;

import static java.util.Map.entry;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.config.cli.CmdLineValueHandler.MSGKEY_InvalidParameter;
import static org.tquadrat.foundation.config.cli.CmdLineValueHandler.MSG_InvalidParameter;

import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.util.stringconverter.BigDecimalStringConverter;
import org.tquadrat.foundation.util.stringconverter.BigIntegerStringConverter;
import org.tquadrat.foundation.util.stringconverter.ByteStringConverter;
import org.tquadrat.foundation.util.stringconverter.CharacterStringConverter;
import org.tquadrat.foundation.util.stringconverter.CharsetStringConverter;
import org.tquadrat.foundation.util.stringconverter.ClassStringConverter;
import org.tquadrat.foundation.util.stringconverter.DoubleStringConverter;
import org.tquadrat.foundation.util.stringconverter.DurationStringConverter;
import org.tquadrat.foundation.util.stringconverter.FileStringConverter;
import org.tquadrat.foundation.util.stringconverter.FloatStringConverter;
import org.tquadrat.foundation.util.stringconverter.InetAddressStringConverter;
import org.tquadrat.foundation.util.stringconverter.IntegerStringConverter;
import org.tquadrat.foundation.util.stringconverter.LongStringConverter;
import org.tquadrat.foundation.util.stringconverter.NumberStringConverter;
import org.tquadrat.foundation.util.stringconverter.PathStringConverter;
import org.tquadrat.foundation.util.stringconverter.PatternStringConverter;
import org.tquadrat.foundation.util.stringconverter.PeriodStringConverter;
import org.tquadrat.foundation.util.stringconverter.ShortStringConverter;
import org.tquadrat.foundation.util.stringconverter.TimeZoneStringConverter;
import org.tquadrat.foundation.util.stringconverter.URIStringConverter;
import org.tquadrat.foundation.util.stringconverter.URLStringConverter;
import org.tquadrat.foundation.util.stringconverter.UUIDStringConverter;
import org.tquadrat.foundation.util.stringconverter.ZoneIdStringConverter;

/**
 *  This class is meant as a place to hold the specific error messages for the
 *  failed conversion of command line entries.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: MessageRegistry.java 1042 2022-12-26 14:05:06Z tquadrat $
 *
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@SuppressWarnings( "OverlyCoupledClass" )
@UtilityClass
@ClassVersion( sourceVersion = "$Id: MessageRegistry.java 1042 2022-12-26 14:05:06Z tquadrat $" )
@API( status = INTERNAL, since = "0.1.0" )
public final class MessageRegistry
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  An entry for the registry.
     *
     *  @param  message The default message.
     *  @param  key The key for the alternative/localised message.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: MessageRegistry.java 1042 2022-12-26 14:05:06Z tquadrat $
     *
     *  @UMLGraph.link
     *  @since 0.1.0
     */
    public record MessageRegistryEntry( String message, int key ) {}

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message key for the error message about an illegal
     *  {@link java.nio.charset.Charset}
     *  name on the command line.
     *
     *  @see CharsetStringConverter#MSG_IllegalCharsetName
     */
    @Message
    (
        description = "The error message about an illegal charset name on the command line.",
        translations =
        {
            @Translation( language = "en", text = CharsetStringConverter.MSG_IllegalCharsetName ),
            @Translation( language = "de", text = "'%1$s' ist kein bekanntes Charset" )
        }
    )
    public static final int MSGKEY_IllegalCharsetName = 14;

    /**
     *  The message key for the error message about an invalid IP address or an
     *  unknown host name on the command line.
     *
     *  @see InetAddressStringConverter#MSG_InvalidAddress
     */
    @Message
    (
        description = "The error message about an invalid IP address or an unknown host name on the command line.",
        translations =
        {
            @Translation( language = "en", text = InetAddressStringConverter.MSG_InvalidAddress ),
            @Translation( language = "de", text = "'%1$s' ist ungültig oder unbekannt" )
        }
    )
    public static final int MSGKEY_InvalidAddress = 15;

    /**
     *  The message key for the error message about an invalid character on
     *  the command line.
     *
     *  @see CharacterStringConverter#MSG_InvalidCharacter
     */
    @Message
    (
        description = "The error message about an invalid character on the command line.",
        translations =
        {
            @Translation( language = "en", text = CharacterStringConverter.MSG_InvalidCharacter ),
            @Translation( language = "de", text = "Kein gültiges Zeichen: %1$s" )
        }
    )
    public static final int MSGKEY_InvalidCharacter = 16;

    /**
     *  The message key for the error message about an invalid duration String
     *  on the command line.
     *
     *  @see DurationStringConverter#MSG_InvalidDuration
     */
    @Message
    (
        description = "The error message about an invalid duration String on the command line.",
        translations =
        {
            @Translation( language = "en", text = DurationStringConverter.MSG_InvalidDuration ),
            @Translation( language = "de", text = "Keine gültige Zeitdauer: %1$s" )
        }
    )
    public static final int MSGKEY_InvalidDuration = 17;

    /**
     *  The message key for the error message about an invalid regular
     *  expression on the command line.
     *
     *  @see PatternStringConverter#MSG_InvalidExpression
     */
    @Message
    (
        description = "The error message about an invalid regular expression on the command line.",
        translations =
        {
            @Translation( language = "en", text = PatternStringConverter.MSG_InvalidExpression ),
            @Translation( language = "de", text = "'%1$s' ist kein gültiger regulärer Ausdruck" )
        }
    )
    public static final int MSGKEY_InvalidExpression = 18;

    /**
     *  The message key for the error message about an invalid number format on
     *  the command line.
     *
     *  @see NumberStringConverter#MSG_InvalidNumberFormat
     */
    @Message
    (
        description = "The error message about an invalid number format on the command line.",
        translations =
        {
            @Translation( language = "en", text = NumberStringConverter.MSG_InvalidNumberFormat ),
            @Translation( language = "de", text = "'%1$s' ist kein gültiges Zahlenformat" )
        }
    )
    public static final int MSGKEY_InvalidNumberFormat = 19;

    /**
     *  The message key for the error message about an invalid period on the
     *  command line.
     *
     *  @see PeriodStringConverter#MSG_InvalidPeriod
     */
    @Message
    (
        description = "The error message about an invalid period on the command line.",
        translations =
        {
            @Translation( language = "en", text = PeriodStringConverter.MSG_InvalidPeriod ),
            @Translation( language = "de", text = "Kein gültiger Zeitraum: %1$s" )
        }
    )
    public static final int MSGKEY_InvalidPeriod = 20;

    /**
     *  The message key for the error message about an invalid URI on the
     *  command line.
     *
     *  @see URIStringConverter#MSG_InvalidURI
     */
    @Message
    (
        description = "The error message about an invalid URI on the command line.",
        translations =
        {
            @Translation( language = "en", text = URIStringConverter.MSG_InvalidURI ),
            @Translation( language = "de", text = "Ungültige URI: %1$s" )
        }
    )
    public static final int MSGKEY_InvalidURI = 21;

    /**
     *  The message key for the error message about an invalid URL on the
     *  command line.
     *
     *  @see URLStringConverter#MSG_InvalidURL
     */
    @Message
    (
        description = "The error message about an invalid URL on the command line.",
        translations =
        {
            @Translation( language = "en", text = URLStringConverter.MSG_InvalidURL ),
            @Translation( language = "de", text = "Ungültige URL: %1$s" )
        }
    )
    public static final int MSGKEY_InvalidURL = 22;

    /**
     *  The message key for the error message about an invalid UUID on the
     *  command line.
     *
     *  @see UUIDStringConverter#MSG_InvalidUUIDFormat
     */
    @Message
        (
            description = "The error message about an invalid UUID on the command line.",
            translations =
                {
                    @Translation( language = "en", text = UUIDStringConverter.MSG_InvalidUUIDFormat ),
                    @Translation( language = "de", text = "Keine gültige UUID: %1$s" )
                }
        )
    public static final int MSGKEY_InvalidUUIDFormat = 23;

    /**
     *  The message key for the error message about an invalid zone id on the
     *  command line.
     *
     *  @see ZoneIdStringConverter#MSG_InvalidZoneId
     */
    @Message
    (
        description = "The error message about an invalid zone id on the command line.",
        translations =
        {
            @Translation( language = "en", text = ZoneIdStringConverter.MSG_InvalidZoneId ),
            @Translation( language = "de", text = "Keine gültige Id für eine Zeitzone: %1$s" )
        }
    )
    public static final int MSGKEY_InvalidZoneId = 24;

    /**
     *  The message key for the error message about an unknown class name on
     *  the command line.
     *
     *  @see ClassStringConverter#MSG_UnknownClass
     */
    @Message
    (
        description = "The error message about an unknown class name on the command line.",
        translations =
        {
            @Translation( language = "en", text = ClassStringConverter.MSG_UnknownClass ),
            @Translation( language = "de", text = "'%1$s' ist kein gültiges Zahlenformat" )
        }
    )
    public static final int MSGKEY_UnknownClass = 25;

    /**
     *  The message key for the error message about an unknown timezone name on
     *  the command line.
     *
     *  @see TimeZoneStringConverter#MSG_UnknownTimeZone
     */
    @Message
        (
            description = "The error message about an unknown timezone name on the command line.",
            translations =
                {
                    @Translation( language = "en", text = TimeZoneStringConverter.MSG_UnknownTimeZone ),
                    @Translation( language = "de", text = "Unbekannte Zeitzone: %1$s" )
                }
        )
    public static final int MSGKEY_UnknownTimeZone = 26;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The fall-back entry.
     */
    @API( status = INTERNAL, since = "0.1.0" )
    public static final MessageRegistryEntry MSG_REGISTRY_FALLBACK;

    /**
     *  The message registry.
     */
    @SuppressWarnings( {"rawtypes", "StaticCollection"} )
    @API( status = INTERNAL, since = "0.1.0" )
    public static final Map<Class<? extends StringConverter>,MessageRegistryEntry> m_MessageRegistry;

    static
    {
        final var numberMessage = new MessageRegistryEntry( NumberStringConverter.MSG_InvalidNumberFormat, MSGKEY_InvalidNumberFormat );
        final var fileMessage = new MessageRegistryEntry( FileStringConverter.MSG_InvalidFileName, CmdLineException.MSGKEY_InvalidFileName );

        MSG_REGISTRY_FALLBACK = new MessageRegistryEntry( MSG_InvalidParameter, MSGKEY_InvalidParameter );

        m_MessageRegistry = Map.ofEntries
        (
            entry( BigDecimalStringConverter.class, numberMessage ),
            entry( BigIntegerStringConverter.class, numberMessage ),
            entry( ByteStringConverter.class, numberMessage ),
            entry( CharacterStringConverter.class, new MessageRegistryEntry( CharacterStringConverter.MSG_InvalidCharacter, MSGKEY_InvalidCharacter ) ),
            entry( CharsetStringConverter.class, new MessageRegistryEntry( CharsetStringConverter.MSG_IllegalCharsetName, MSGKEY_IllegalCharsetName ) ),
            entry( ClassStringConverter.class, new MessageRegistryEntry( ClassStringConverter.MSG_UnknownClass, MSGKEY_UnknownClass ) ),
            entry( DoubleStringConverter.class, numberMessage ),
            entry( DurationStringConverter.class, new MessageRegistryEntry( DurationStringConverter.MSG_InvalidDuration, MSGKEY_InvalidDuration ) ),
            entry( FileStringConverter.class, fileMessage ),
            entry( FloatStringConverter.class, numberMessage ),
            entry( InetAddressStringConverter.class, new MessageRegistryEntry( InetAddressStringConverter.MSG_InvalidAddress, MSGKEY_InvalidAddress ) ),
            entry( IntegerStringConverter.class, numberMessage ),
            entry( LongStringConverter.class, numberMessage ),
            entry( PathStringConverter.class, fileMessage ),
            entry( PatternStringConverter.class, new MessageRegistryEntry( PatternStringConverter.MSG_InvalidExpression, MSGKEY_InvalidExpression ) ),
            entry( PeriodStringConverter.class, new MessageRegistryEntry( PeriodStringConverter.MSG_InvalidPeriod, MSGKEY_InvalidPeriod ) ),
            entry( ShortStringConverter.class, numberMessage ),
            entry( TimeZoneStringConverter.class, new MessageRegistryEntry( TimeZoneStringConverter.MSG_UnknownTimeZone, MSGKEY_UnknownTimeZone ) ),
            entry( URIStringConverter.class, new MessageRegistryEntry( URIStringConverter.MSG_InvalidURI, MSGKEY_InvalidURI ) ),
            entry( URLStringConverter.class, new MessageRegistryEntry( URLStringConverter.MSG_InvalidURL, MSGKEY_InvalidURL ) ),
            entry( UUIDStringConverter.class, new MessageRegistryEntry( UUIDStringConverter.MSG_InvalidUUIDFormat, MSGKEY_InvalidUUIDFormat ) ),
            entry( ZoneIdStringConverter.class, new MessageRegistryEntry( ZoneIdStringConverter.MSG_InvalidZoneId, MSGKEY_InvalidZoneId ) )
        );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private MessageRegistry() { throw new PrivateConstructorForStaticClassCalledError( MessageRegistry.class ); }
}
//  class MessageRegistry

/*
 *  End of File
 */