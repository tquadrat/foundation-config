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

package org.tquadrat.foundation.config.cli;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.stringconverter.BigIntegerStringConverter;

/**
 *  Tests for an instance of
 *  {@link CmdLineValueHandler}
 *  that handles
 *  {@link BigDecimal}
 *  properties.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestBigIntegerValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestBigIntegerValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestBigIntegerValueHandler" )
public class TestBigIntegerValueHandler extends ValueHandlerTestBase<BigInteger>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final CmdLineValueHandler<BigInteger> createCandidate()
    {
        final var retValue = new SimpleCmdLineValueHandler<>( this::valueSetter, BigIntegerStringConverter.INSTANCE );
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

        final var candidate = createCandidate();

        var expected = BigInteger.ONE;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        expected = BigInteger.ZERO;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        expected = BigInteger.TEN;
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );

        expected = new BigInteger(
            """
                3141592653589793238462643383279502884197169399375105820974944\
                59230781640628620899862803482534211706798214808651328230664709\
                38446095505822317253594081284811174502841027019385211055596446\
                22948954930381964428810975665933446128475648233786783165271201\
                90914564856692346034861045432664821339360726024914127372458700\
                66063155881748815209209628292540917153643678925903600113305305\
                48820466521384146951941511609433057270365759591953092186117381\
                93261179310511854807446237996274956735188575272489122793818301\
                194912""" );
        candidate.parseCmdLine( createParameters( expected.toString() ) );
        checkValue( expected );
    }   //  testParseCmdLine()
}
//  class TestBigIntegerValueHandler

/*
 *  End of File
 */