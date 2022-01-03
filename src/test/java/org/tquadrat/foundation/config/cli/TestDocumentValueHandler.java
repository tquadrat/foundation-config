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

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.w3c.dom.Document;

/**
 *  Tests for the class
 *  {@link DocumentValueHandler}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDocumentValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestDocumentValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.cli.TestDocumentValueHandler" )
public class TestDocumentValueHandler extends ValueHandlerTestBase<Document>
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final CmdLineValueHandler<Document> createCandidate()
    {
        final var retValue = new DocumentValueHandler( this::valueSetter );
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

        DocumentValueHandler candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new DocumentValueHandler( null );
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
        final Map<String,Document> values = new TreeMap<>();
        //noinspection UnusedAssignment
        candidate = new DocumentValueHandler( values::put );
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

        final var candidate = (DocumentValueHandler) createCandidate();
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

        location = "./src/test/data/TestDocument.xml";
        final var testFile = new File( location );
        assertTrue( testFile.exists() );
        candidate.parseCmdLine( createParameters( location ) );

        final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final var builder = documentBuilderFactory.newDocumentBuilder();
        final var expected = builder.parse( testFile );
        checkDocumentValue( expected );
    }   //  testParseCmdLine()
}
//  class TestDocumentValueHandler

/*
 *  End of File
 */