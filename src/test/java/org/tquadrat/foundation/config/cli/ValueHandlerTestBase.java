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

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.CLIOptionDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.UnexpectedExceptionError;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.w3c.dom.Document;

/**
 *  Base class for tests with implementations of
 *  {@link org.tquadrat.foundation.config.cli.CmdLineValueHandler}.
 *
 *  @param  <T> The type that is handled by the implementation of
 *      {@link CmdLineValueHandler}
 *      to be tested with the implementation of this class.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@SuppressWarnings( {"ClassWithTooManyMethods", "UseOfObsoleteDateTimeApi"} )
@ClassVersion( sourceVersion = "$Id: ValueHandlerTestBase.java 1076 2023-10-03 18:36:07Z tquadrat $" )
public abstract class ValueHandlerTestBase<T> extends TestBaseClass
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  An implementation of
     *  {@link Parameters}
     *  for test purposes.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: ValueHandlerTestBase.java 1076 2023-10-03 18:36:07Z tquadrat $
     */
    private static class ParametersImpl implements Parameters
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The arguments.
         */
        private final String [] m_Arguments;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code ParametersImpl} instance.
         *
         *  @param  arguments   The arguments.
         */
        public ParametersImpl( final String... arguments )
        {
            m_Arguments = requireNonNullArgument( arguments, "arguments" );
        }   //  ParametersImpl()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @SuppressWarnings( "ProhibitedExceptionCaught" )
        @Override
        public String getParameter( final int index ) throws CmdLineException
        {
            final String retValue;
            try
            {
                retValue = m_Arguments [index];
            }
            catch( final ArrayIndexOutOfBoundsException e )
            {
                throw new CmdLineException( "Illegal index: %d", e, 0, index );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  getParameter()

        @Override
        public final boolean isParameter( final int index )
        {
            final var retValue = index < m_Arguments.length;

            //---* Done *----------------------------------------------------------
            return retValue;
        }   //  isParameter()
    }
    //  class ParametersImpl

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The option names.
     */
    public static final List<String> OPTION_NAMES = List.of( "--option", "-o" );

    /**
     *  The name of the property: {@value}.
     */
    public static final String PROPERTY_NAME = "property";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The value container.
     */
    private final Map<String,T> m_ValueContainer = new TreeMap<>();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Empties the value container before each test.
     */
    @BeforeEach
    public final void beforeEach() { m_ValueContainer.clear(); }

    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkDocumentValue( final Document expected ) throws AssertionError
    {
        checkDocumentValue( PROPERTY_NAME, expected );
    }   //  checkDocumentValue()

    /**
     *  Checks the value.
     *
     *  @param  property    The name of the property.
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkDocumentValue( final String property, final Document expected ) throws AssertionError
    {
        assertEquals( toString( expected ), toString( ((Document) m_ValueContainer.get( property )) ) );
    }   //  checkDocumentValue()

    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkDateValue( final Date expected ) throws AssertionError
    {
        checkDateValue( PROPERTY_NAME, expected );
    }   //  checkDateValue()

    /**
     *  Checks the value.
     *
     *  @param  property    The name of the property.
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkDateValue( final String property, final Date expected ) throws AssertionError
    {
        assertEquals( expected.getTime(), ((Date) m_ValueContainer.get( property )).getTime() );
    }   //  checkDateValue()

    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkPatternValue( final Pattern expected ) throws AssertionError
    {
        checkPatternValue( PROPERTY_NAME, expected );
    }   //  checkPatternValue()

    /**
     *  Checks the value.
     *
     *  @param  property    The name of the property.
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkPatternValue( final String property, final Pattern expected ) throws AssertionError
    {
        final Object value = m_ValueContainer.get( property );
        assertTrue( value instanceof Pattern );
        assertEquals( expected.toString(), value.toString() );
    }   //  checkPatternValue()

    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkTemporalValue( final Temporal expected ) throws AssertionError
    {
        checkTemporalValue( PROPERTY_NAME, expected );
    }   //  checkTemporalValue()

    /**
     *  Checks the value.
     *
     *  @param  property    The name of the property.
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    @SuppressWarnings( "unchecked" )
    protected void checkTemporalValue( final String property, final Temporal expected ) throws AssertionError
    {
        final Object value = m_ValueContainer.get( property );
        assertTrue( value instanceof Temporal );
        if( !value.equals( expected ) )
        {
            if( ((Comparable<Temporal>) value).compareTo( expected ) < 0 ) throw new AssertionError( format( "%s before %s", value.toString(), expected.toString() ) );
        }
    }   //  checkTemporalValue()

    /**
     *  Checks the value for the property &quot;{@value #PROPERTY_NAME}&quot;.
     *
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkValue( final T expected ) throws AssertionError
    {
        checkValue( PROPERTY_NAME, expected );
    }   //  checkValue()

    /**
     *  Checks the value.
     *
     *  @param  property    The name of the property.
     *  @param  expected    The expected value.
     *  @throws AssertionError  The check failed.
     */
    protected void checkValue( final String property, final T expected ) throws AssertionError
    {
        assertEquals( expected, m_ValueContainer.get( property ) );
    }   //  checkValue()


    /**
     *  Creates a test candidate.
     *
     *  @return The test candidate.
     */
    protected abstract CmdLineValueHandler<T> createCandidate();

    /**
     *  Creates an instance of
     *  {@link CLIDefinition}
     *  to be used for the test.
     *
     *  @param  valueHandler    The value handler.
     *  @return The CLI definition instance.
     */
    protected CmdLineValueHandler<T> createDefinition( final CmdLineValueHandler<T> valueHandler )
    {
        return createDefinition( valueHandler, null );
    }   //  createDefinition

    /**
     *  Creates an instance of
     *  {@link CLIDefinition}
     *  to be used for the test.
     *
     *  @param  valueHandler    The value handler.
     *  @param  format  The format as defined by the annotation; can be
     *      {@code null}.
     *  @return The given value handler with the CLI definition applied to it.
     */
    protected CmdLineValueHandler<T> createDefinition( final CmdLineValueHandler<T> valueHandler, final String format )
    {
        final var retValue = requireNonNullArgument( valueHandler, "valueHandler" );
        final var definition = new CLIOptionDefinition( PROPERTY_NAME, OPTION_NAMES, "usage", "USAGE_property", "META_VAR", false, valueHandler, false, format );
        retValue.setContext( definition );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createDefinition

    /**
     *  Creates an instance of
     *  {@link Parameters}
     *  from the given arguments to be used for a test.
     *
     *  @param  args    The arguments.
     *  @return The parameters.
     */
    protected static final Parameters createParameters( final String... args )
    {
        return new ParametersImpl( args );
    }   //  createParameters()

    /**
     *  Returns the property.
     *
     *  @return The property.
     */
    protected final T getProperty() { return m_ValueContainer.get( PROPERTY_NAME ); }

    /**
     *  Tests the method
     *  {@link CmdLineValueHandler#parseCmdLine(org.tquadrat.foundation.ui.spi.Parameters)}.
     *
     *  @throws Exception   Something went wrong unexpectedly.
     */
    @Test
    protected abstract void testParseCmdLine() throws Exception;

    /**
     *  Tests the method
     *  {@link CmdLineValueHandler#setContext(org.tquadrat.foundation.ui.spi.CLIDefinition)}.
     */
    @Test
    public final void testSetContext_Null()
    {
        skipThreadTest();

        final var candidate = createCandidate();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.setContext( null );
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
    }   //  testSetContext_Null()

    /**
     *  Tests the method {@code CmdLineValueHandler#translate(Parameters)}.
     */
    @Test
    protected void testTranslate()
    {
        skipThreadTest();

        assertTrue( true );
    }   //  testTranslate()

    /**
     *  Converts the given
     *  {@link Document}
     *  instance to a String.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    private static final String toString( final Document source )
    {
        String retValue = null;
        if( nonNull( source ) )
        {
            final var encoding = source.getXmlEncoding();
            try
            {
                //---* Obtain the transformer *--------------------------------
                final var transformerFactory = TransformerFactory.newInstance();
                final var transformer = transformerFactory.newTransformer();

                //---* Configure the transformer *-----------------------------
                transformer.setOutputProperty( "method", "xml" );
                transformer.setOutputProperty( "indent", "no" );
                transformer.setOutputProperty( "standalone", source.getXmlStandalone() ? "yes" : "no" );
                if( nonNull( encoding ) ) transformer.setOutputProperty( "encoding", encoding );
                final var doctype = source.getDoctype();
                if( nonNull( doctype) )
                {
                    final var systemId = doctype.getSystemId();
                    if( isNotEmptyOrBlank( systemId ) ) transformer.setOutputProperty( "doctype-system", systemId );
                    final var publicId = doctype.getPublicId();
                    if( isNotEmptyOrBlank( publicId ) ) transformer.setOutputProperty( "doctype-public", publicId );
                }

                final var documentSource = new DOMSource( source );
                final var outputStream = new ByteArrayOutputStream();
                final var result = new StreamResult( outputStream );
                transformer.transform( documentSource, result );
                retValue = outputStream.toString( isNull( encoding ) ? UTF8.name() : encoding );
            }
            catch( final TransformerConfigurationException e )
            {
                throw new UnexpectedExceptionError( "Cannot instantiate Transformer", e );
            }
            catch( final TransformerException e )
            {
                throw new IllegalArgumentException( "Unrecoverable error on transformation", e );
            }
            catch( final UnsupportedEncodingException e )
            {
                throw new UnexpectedExceptionError( format( "Invalid encoding: %s", encoding ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  The value setter.
     *
     *  @param  property    The name of the property that is used as the key
     *      for the container.
     *  @param  value   The value.
     */
    public final void valueSetter( final String property, final T value ) { m_ValueContainer.put( property, value ); }
}
//  class TestStringValueHandler

/*
 *  End of File
 */