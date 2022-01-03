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

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.exception.NullArgumentException;

/**
 *  Tests for the class
 *  {@link ImageValueHandler}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestImageValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestImageValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestImageValueHandler" )
public class TestImageValueHandler extends ValueHandlerTestBase<BufferedImage>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CmdLineValueHandler<BufferedImage> createCandidate()
    {
        final var retValue = new ImageValueHandler( this::valueSetter );
        createDefinition( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createCandidate()

    /**
     *  Tests the constructor.
     */
    @Test
    public final void testConstructor()
    {
        skipThreadTest();

        ImageValueHandler candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new ImageValueHandler( null );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        @SuppressWarnings( "MismatchedQueryAndUpdateOfCollection" )
        final Map<String,BufferedImage> values = new TreeMap<>();
        //noinspection UnusedAssignment
        candidate = new ImageValueHandler( values::put );
    }   //  testConstructor()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "CastToConcreteClass" )
    @Override
    @Test
    protected final void testParseCmdLine() throws Exception
    {
        skipThreadTest();

        final var candidate = (ImageValueHandler) createCandidate();
        createDefinition( candidate );

        String location;

        location = ".";
        final Class<? extends Throwable> expectedException = CmdLineException.class;
        try
        {
            candidate.parseCmdLine( createParameters( location ) );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        location = "./src/test/data/tquadrat_logo.JPG";
        assertTrue( new File( location ).exists() );
        try
        {
            candidate.parseCmdLine( createParameters( location ) );
        }
        catch( final Error | Exception e )
        {
            e.printStackTrace( out );
            throw e;
        }

        assertNotNull( getProperty() );
    }   //  testParseCmdLine()
}
//  class TestDocumentValueHandler

/*
 *  End of File
 */