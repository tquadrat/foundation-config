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

package org.tquadrat.foundation.config.test;

import static java.lang.System.err;
import static java.lang.System.out;

import java.time.Clock;
import java.time.Instant;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.PlaygroundClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  Compares the different clocks from
 *  {@link Clock}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@PlaygroundClass
@ClassVersion( sourceVersion = "$Id: ClockTester.java 895 2021-04-05 12:40:34Z tquadrat $" )
public final class ClockTester
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private ClockTester() { throw new PrivateConstructorForStaticClassCalledError( ClockTester.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    public static void main( final String... args )
    {
        try
        {
            out.printf( "Default now()                       : %s\n", Instant.now() );
            out.printf( "now() with Clock.systemUTC()        : %s\n", Instant.now( Clock.systemUTC() ) );
            out.printf( "now() with Clock.systemDefaultZone(): %s\n", Instant.now( Clock.systemDefaultZone() ) );
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()
}
//  class ClockTester

/*
 *  End of File
 */