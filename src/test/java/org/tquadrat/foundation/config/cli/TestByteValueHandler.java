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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.stringconverter.ByteStringConverter;

/**
 *  Tests for an instance of
 *  {@link CmdLineValueHandler}
 *  that handles
 *  {@link Byte}
 *  and {@code byte} properties.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestByteValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestByteValueHandler.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestByteValueHandler" )
public class TestByteValueHandler extends ValueHandlerTestBase<Byte>
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  A primitive value.
     */
    private byte m_Value;

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected final void checkValue( final byte expected ) throws AssertionError
    {
        checkValue( Byte.valueOf( expected ) );
    }   //  checkValue()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final CmdLineValueHandler<Byte> createCandidate()
    {
        final var retValue = new SimpleCmdLineValueHandler<>( this::valueSetter, ByteStringConverter.INSTANCE );
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

        byte expected = 42;
        candidate.parseCmdLine( createParameters( Byte.toString( expected ) ) );
        checkValue( expected );

        expected = 0x42;
        candidate.parseCmdLine( createParameters( "0x42" ) );
        checkValue( expected );

        //noinspection OctalInteger
        expected = 042;
        candidate.parseCmdLine( createParameters( "042" ) );
        checkValue( expected );
    }   //  testParseCmdLine()

    /**
     *  Set the primitive value.
     */
    @Test
    public final void testPrimitive()
    {
        skipThreadTest();

        m_Value = 0;
        assertEquals( 0, m_Value );

        final var candidate = new SimpleCmdLineValueHandler<>( ($,v) -> m_Value = v, ByteStringConverter.INSTANCE );
        createDefinition( candidate );
        final byte expected = 42;
        candidate.parseCmdLine( createParameters( Byte.toString( expected ) ) );
        assertEquals( expected, m_Value );
    }   //  testPrimitive()
}
//  class TestByteValueHandler

/*
 *  End of File
 */