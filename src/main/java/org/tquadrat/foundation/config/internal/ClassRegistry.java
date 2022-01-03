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

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.config.cli.BooleanValueHandler;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.config.cli.DateValueHandler;
import org.tquadrat.foundation.config.cli.DocumentValueHandler;
import org.tquadrat.foundation.config.cli.ImageValueHandler;
import org.tquadrat.foundation.config.cli.InstantValueHandler;
import org.tquadrat.foundation.config.cli.LocalDateTimeValueHandler;
import org.tquadrat.foundation.config.cli.LocalDateValueHandler;
import org.tquadrat.foundation.config.cli.StringValueHandler;
import org.tquadrat.foundation.config.cli.YearMonthValueHandler;
import org.tquadrat.foundation.config.cli.YearValueHandler;
import org.tquadrat.foundation.config.cli.ZonedDateTimeValueHandler;
import org.tquadrat.foundation.config.spi.prefs.BooleanAccessor;
import org.tquadrat.foundation.config.spi.prefs.ByteAccessor;
import org.tquadrat.foundation.config.spi.prefs.DateAccessor;
import org.tquadrat.foundation.config.spi.prefs.DoubleAccessor;
import org.tquadrat.foundation.config.spi.prefs.FloatAccessor;
import org.tquadrat.foundation.config.spi.prefs.ImageAccessor;
import org.tquadrat.foundation.config.spi.prefs.IntegerAccessor;
import org.tquadrat.foundation.config.spi.prefs.LongAccessor;
import org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor;
import org.tquadrat.foundation.config.spi.prefs.PrimitiveBooleanAccessor;
import org.tquadrat.foundation.config.spi.prefs.PrimitiveByteAccessor;
import org.tquadrat.foundation.config.spi.prefs.PrimitiveDoubleAccessor;
import org.tquadrat.foundation.config.spi.prefs.PrimitiveIntAccessor;
import org.tquadrat.foundation.config.spi.prefs.PrimitiveLongAccessor;
import org.tquadrat.foundation.config.spi.prefs.PrimitiveShortAccessor;
import org.tquadrat.foundation.config.spi.prefs.ShortAccessor;
import org.tquadrat.foundation.config.spi.prefs.StringAccessor;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.w3c.dom.Document;

/**
 *  This class is meant as a place to hold the predefined implementations of
 *  {@link CmdLineValueHandler}
 *  for classes that are not covered by
 *  {@link org.tquadrat.foundation.config.cli.SimpleCmdLineValueHandler},
 *  either because there is no implementation of
 *  {@link org.tquadrat.foundation.lang.StringConverter}
 *  for that class, or because the {@code CmdLineValueHandler} allows
 *  additional settings.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ClassRegistry.java 914 2021-05-07 21:22:12Z tquadrat $
 *
 *  @UMLGraph.link
 *  @since 0.1.0
 */
@SuppressWarnings( {"UseOfObsoleteDateTimeApi", "OverlyCoupledClass"} )
@UtilityClass
@ClassVersion( sourceVersion = "$Id: ClassRegistry.java 914 2021-05-07 21:22:12Z tquadrat $" )
@API( status = INTERNAL, since = "0.1.0" )
public final class ClassRegistry
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The handler classes.
     */
    @API( status = INTERNAL, since = "0.1.0" )
    public static final Map<String,Class<? extends CmdLineValueHandler<?>>> m_HandlerClasses;

    /**
     *  The preferences accessor classes.
     */
    @API( status = INTERNAL, since = "0.1.0" )
    public static final Map<String,Class<? extends PreferenceAccessor<?>>> m_PrefsAccessorClasses;

    static
    {
        m_HandlerClasses = Map.ofEntries
        (
            entry( boolean.class.getName(), BooleanValueHandler.class ),
            entry( Boolean.class.getName(), BooleanValueHandler.class ),
            entry( BufferedImage.class.getName(), ImageValueHandler.class ),
            entry( Date.class.getName(), DateValueHandler.class ),
            entry( Document.class.getName(), DocumentValueHandler.class ),
            entry( Instant.class.getName(), InstantValueHandler.class ),
            entry( LocalDate.class.getName(), LocalDateValueHandler.class ),
            entry( LocalDateTime.class.getName(), LocalDateTimeValueHandler.class ),
            entry( LocalTime.class.getName(), LocalDateTimeValueHandler.class ),
            entry( String.class.getName(), StringValueHandler.class ),
            entry( YearMonth.class.getName(), YearMonthValueHandler.class ),
            entry( Year.class.getName(), YearValueHandler.class ),
            entry( ZonedDateTime.class.getName(), ZonedDateTimeValueHandler.class )
        );

        m_PrefsAccessorClasses = Map.ofEntries
        (
            entry( boolean.class.getName(), PrimitiveBooleanAccessor.class ),
            entry( Boolean.class.getName(), BooleanAccessor.class ),
            entry( BufferedImage.class.getName(), ImageAccessor.class ),
            entry( byte.class.getName(), PrimitiveByteAccessor.class ),
            entry( Byte.class.getName(), ByteAccessor.class ),
            entry( Date.class.getName(), DateAccessor.class ),
            entry( double.class.getName(), PrimitiveDoubleAccessor.class ),
            entry( Double.class.getName(), DoubleAccessor.class ),
            entry( Float.class.getName(), FloatAccessor.class ),
            entry( int.class.getName(), PrimitiveIntAccessor.class ),
            entry( Integer.class.getName(), IntegerAccessor.class ),
            entry( long.class.getName(), PrimitiveLongAccessor.class ),
            entry( Long.class.getName(), LongAccessor.class ),
            entry( short.class.getName(), PrimitiveShortAccessor.class ),
            entry( Short.class.getName(), ShortAccessor.class ),
            entry( String.class.getName(), StringAccessor.class )
        );
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private ClassRegistry() { throw new PrivateConstructorForStaticClassCalledError( ClassRegistry.class ); }
}
//  class ClassRegistry

/*
 *  End of File
 */