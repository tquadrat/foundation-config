/*
 * ============================================================================
 * Copyright © 2002-2018 by Thomas Thrien.
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

package org.tquadrat.foundation.config.cli;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.stringconverter.BooleanStringConverter;

/**
 *  Tests for an instance of
 *  {@link CmdLineValueHandler}
 *  that handles
 *  {@link Boolean}
 *  and {@code boolean} properties.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestBooleanValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestBooleanValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestBooleanValueHandler" )
public class TestBooleanValueHandler extends ValueHandlerTestBase<Boolean>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  A primitive value.
     */
    private boolean m_Value;

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected final void checkValue( final boolean expected ) throws AssertionError
    {
        checkValue( Boolean.valueOf( expected ) );
    }   //  checkValue()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final CmdLineValueHandler<Boolean> createCandidate()
    {
        final var retValue = new SimpleCmdLineValueHandler<>( this::valueSetter, BooleanStringConverter.INSTANCE );
        createDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCandidate()

    /**
     *  {@inheritDoc}
     */
    @Override
    @Test
    public final void testParseCmdLine()
    {
        skipThreadTest();

        var candidate = createCandidate();

        var expected = TRUE;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        expected = FALSE;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        candidate = new SimpleCmdLineValueHandler<>( ($,v) -> m_Value = v, BooleanStringConverter.INSTANCE );
        createDefinition( candidate );

        expected = TRUE;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        assertTrue( m_Value );

        expected = FALSE;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        assertFalse( m_Value );
    }   //  testParseCmdLine()
}
//  class TestBooleanValueHandler

/*
 *  End of File
 */