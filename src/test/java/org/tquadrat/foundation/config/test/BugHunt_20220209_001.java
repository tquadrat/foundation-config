/*
 * ============================================================================
 *  Copyright © 2002-2022 by Thomas Thrien.
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

package org.tquadrat.foundation.config.test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  {@link org.tquadrat.foundation.config.CmdLineException}
 *  cannot read the resources …
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: BugHunt_20220209_001.java 1016 2022-02-09 16:51:08Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.test.BugHunt_20220209_001" )
public class BugHunt_20220209_001 extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Some tests with
     *  {@link org.tquadrat.foundation.config.CmdLineException}.
     *
     *  @throws Exception   Something went unexpectedly wrong.
     */
    @Test
    final void testForBugHunt() throws Exception
    {
        skipThreadTest();

        final var candidate = new CmdLineException( "%s", 10, "option" );
        assertNotNull( candidate );
        final var message = candidate.getLocalizedMessage();
        assertNotNull( message );
        assertFalse( message.isBlank() );

        out.println( message );
    }   //  testForBugHunt()
}
//  class BugHunt_20220209_001

/*
 *  End of File
 */