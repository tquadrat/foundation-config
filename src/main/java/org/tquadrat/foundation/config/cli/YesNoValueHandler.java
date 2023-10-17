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
 *  @version $Id: YesNoValueHandler.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: YesNoValueHandler.java 1061 2023-09-25 16:32:43Z tquadrat $" )
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
     *  @version $Id: YesNoValueHandler.java 1061 2023-09-25 16:32:43Z tquadrat $
     *  @since 0.0.1
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: YesNoValueHandler.java 1061 2023-09-25 16:32:43Z tquadrat $" )
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
            yes.put( Locale.of( "ar" ), Set.of( "naʿam", "نعم" ) );
            yes.put( Locale.of( "cs" ), Set.of( "ano" ) );
            yes.put( Locale.of( "el" ), Set.of( "ne", "ναι" ) );
            yes.put( Locale.of( "es" ), Set.of( "sí" ) );
            yes.put( Locale.of( "et" ), Set.of( "jah" ) );
            yes.put( Locale.of( "fa" ), Set.of( "baleh", "بله" ) );
            yes.put( Locale.of( "fi" ), Set.of( "kyllä" ) );
            yes.put( Locale.of( "fr" ), Set.of( "oui" ) );
            yes.put( Locale.of( "he" ), Set.of( "ken", "כֵּן", "כן" ) );
            yes.put( Locale.of( "hi" ), Set.of( "hā̃ ", "हाँ", "jī", "जी", "jī hā̃ ", "जी हाँ" ) );
            yes.put( Locale.of( "hu" ), Set.of( "igen" ) );
            yes.put( Locale.of( "id" ), Set.of( "ya" ) );
            yes.put( Locale.of( "is" ), Set.of( "já" ) );
            yes.put( Locale.of( "it" ), Set.of( "sì" ) );
            yes.put( Locale.of( "jp" ), Set.of( "hai", "はい", "ee", "ええ", "un", "うん" ) );
            yes.put( Locale.of( "ko" ), Set.of( "ne", "네" ) );
            yes.put( Locale.of( "ku" ), Set.of( "are" ) );
            yes.put( Locale.of( "lt" ), Set.of( "taip" ) );
            yes.put( Locale.of( "pl" ), Set.of( "tak" ) );
            yes.put( Locale.of( "pt" ), Set.of( "sim" ) );
            yes.put( Locale.of( "sq" ), Set.of( "po" ) );
            yes.put( Locale.of( "sw" ), Set.of( "ndiyo", "naam" ) );
            yes.put( Locale.of( "ta" ), Set.of( "ām", "ஆம்", "āmām", "ஆமாம்", "ōm", "ஓம்" ) );
            yes.put( Locale.of( "tg" ), Set.of( "bale", "бале", "ore", "оре" ) );
            yes.put( Locale.of( "tr" ), Set.of( "evet" ) );
            yes.put( Locale.of( "uk" ), Set.of( "tak", "так" ) );

            var set = Set.of( "ja" );
            yes.put( Locale.of( "af" ), set );
            yes.put( Locale.of( "da" ), set );
            yes.put( Locale.of( "de" ), set );
            yes.put( Locale.of( "no" ), set );
            yes.put( Locale.of( "sv" ), set );

            set = Set.of( "da", "да" );
            yes.put( Locale.of( "bg" ), set );
            yes.put( Locale.of( "hr" ), set );
            yes.put( Locale.of( "mk" ), set );
            yes.put( Locale.of( "ro" ), set );
            yes.put( Locale.of( "ru" ), set );
            yes.put( Locale.of( "sh" ), set );
            yes.put( Locale.of( "sl" ), set );
            yes.put( Locale.of( "sr" ), set );

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
                final var language = Locale.of( Locale.getDefault().getLanguage() );
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