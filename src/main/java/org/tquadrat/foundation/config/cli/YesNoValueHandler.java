/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.config.cli;

import static java.lang.Boolean.FALSE;
import static java.util.Locale.ROOT;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.lang.StringConverter;

/**
 *  An implementation of
 *  {@link CmdLineValueHandler}
 *  for {@code boolean} and
 *  {@link Boolean}
 *  values that does accept also &quot;yes&quot;, &quot;qui&quot;,
 *  &quot;ja&quot;, &quot;sí&quot;, &quot;sì&quot;, &quot;да&quot;,
 *  &quot;sim&quot;, &quot;tak&quot; and more as {@code true}. Still any other
 *  phrase and {@code null} are taken as {@code false}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: YesNoValueHandler.java 884 2021-03-22 18:02:51Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: YesNoValueHandler.java 884 2021-03-22 18:02:51Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class YesNoValueHandler extends SimpleCmdLineValueHandler<Boolean>
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  <p>{@summary An implementation of
     *  {@link StringConverter}
     *  that translates 'yes' in various languages into {@code true}.}</p>
     *  <p>'yes', 'true' and 'ok' will always be taken as {@code true}, the
     *  other variants only when valid for the current locale/language.</p>
     *
     *  @see Locale#getDefault()
     *
     *  @author Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: YesNoValueHandler.java 884 2021-03-22 18:02:51Z tquadrat $
     *  @since 0.0.1
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: YesNoValueHandler.java 884 2021-03-22 18:02:51Z tquadrat $" )
    @API( status = STABLE, since = "0.0.1" )
    private static final class YesNoStringConverter implements StringConverter<Boolean>
    {
            /*------------------------*\
        ====** Static Initialisations **=======================================
            \*------------------------*/
        /**
         *  The one and only instance for this
         *  {@link StringConverter}.
         */
        public static final YesNoStringConverter INSTANCE;

        /**
         *  The various forms of 'yes'.
         */
        @SuppressWarnings( "StaticCollection" )
        public static final Map<Locale,Set<String>> YES;

        static
        {
            final Map<Locale,Set<String>> yes = new HashMap<>();

            yes.put( ROOT, Set.of( "ok", "true", "yes" ) );
            yes.put( new Locale( "ar" ), Set.of( "naʿam", "نعم" ) );
            yes.put( new Locale( "cs" ), Set.of( "ano" ) );
            yes.put( new Locale( "el" ), Set.of( "ne", "ναι" ) );
            yes.put( new Locale( "es" ), Set.of( "sí" ) );
            yes.put( new Locale( "et" ), Set.of( "jah" ) );
            yes.put( new Locale( "fa" ), Set.of( "baleh", "بله" ) );
            yes.put( new Locale( "fi" ), Set.of( "kyllä" ) );
            yes.put( new Locale( "fr" ), Set.of( "oui" ) );
            yes.put( new Locale( "he" ), Set.of( "ken", "כֵּן", "כן" ) );
            yes.put( new Locale( "hi" ), Set.of( "hā̃ ", "हाँ", "jī", "जी", "jī hā̃ ", "जी हाँ" ) );
            yes.put( new Locale( "hu" ), Set.of( "igen" ) );
            yes.put( new Locale( "id" ), Set.of( "ya" ) );
            yes.put( new Locale( "is" ), Set.of( "já" ) );
            yes.put( new Locale( "it" ), Set.of( "sì" ) );
            yes.put( new Locale( "jp" ), Set.of( "hai", "はい", "ee", "ええ", "un", "うん" ) );
            yes.put( new Locale( "ko" ), Set.of( "ne", "네" ) );
            yes.put( new Locale( "ku" ), Set.of( "are" ) );
            yes.put( new Locale( "lt" ), Set.of( "taip" ) );
            yes.put( new Locale( "pl" ), Set.of( "tak" ) );
            yes.put( new Locale( "pt" ), Set.of( "sim" ) );
            yes.put( new Locale( "sq" ), Set.of( "po" ) );
            yes.put( new Locale( "sw" ), Set.of( "ndiyo", "naam" ) );
            yes.put( new Locale( "ta" ), Set.of( "ām", "ஆம்", "āmām", "ஆமாம்", "ōm", "ஓம்" ) );
            yes.put( new Locale( "tg" ), Set.of( "bale", "бале", "ore", "оре" ) );
            yes.put( new Locale( "tr" ), Set.of( "evet" ) );
            yes.put( new Locale( "uk" ), Set.of( "tak", "так" ) );

            var set = Set.of( "ja" );
            yes.put( new Locale( "af" ), set );
            yes.put( new Locale( "da" ), set );
            yes.put( new Locale( "de" ), set );
            yes.put( new Locale( "no" ), set );
            yes.put( new Locale( "sv" ), set );

            set = Set.of( "da", "да" );
            yes.put( new Locale( "bg" ), set );
            yes.put( new Locale( "hr" ), set );
            yes.put( new Locale( "mk" ), set );
            yes.put( new Locale( "ro" ), set );
            yes.put( new Locale( "ru" ), set );
            yes.put( new Locale( "sh" ), set );
            yes.put( new Locale( "sl" ), set );
            yes.put( new Locale( "sr" ), set );

            YES = Map.copyOf( yes );

            INSTANCE = new YesNoStringConverter();
        }

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new instance of {@code YesNoValueStringConverter}.
         */
        public YesNoStringConverter() { super(); }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public Boolean fromString( final CharSequence source ) throws IllegalArgumentException
        {
            var retValue = FALSE;
            if( isNotEmptyOrBlank( source ) )
            {
                final Collection<String> yes = new HashSet<>( YES.get( ROOT ) );
                final var language = new Locale( Locale.getDefault().getLanguage() );
                if( YES.containsKey( language ) ) yes.addAll( YES.get( language ) );
                retValue = Boolean.valueOf( yes.contains( source.toString().toLowerCase( Locale.getDefault() ) ) );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  fromString()
    }
    //  class YesNoStringConverter

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code YesNoValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public YesNoValueHandler( final CLIDefinition context, final BiConsumer<String,Boolean> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( context, valueSetter, YesNoStringConverter.INSTANCE );
    }   //  YesNoValueHandler()

    /**
     *  Creates a new {@code YesNoValueHandler} instance.
     *
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public YesNoValueHandler( final BiConsumer<String,Boolean> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter, YesNoStringConverter.INSTANCE );
    }   //  YesNoValueHandler()
}
//  class YesNoValueHandler

/*
 *  End of File
 */