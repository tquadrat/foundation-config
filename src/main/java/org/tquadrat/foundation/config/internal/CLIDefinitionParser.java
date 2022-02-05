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

package org.tquadrat.foundation.config.internal;

import static java.lang.String.join;
import static java.lang.System.out;
import static java.util.Locale.ROOT;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.stream.XMLStreamConstants.ATTRIBUTE;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.COMMENT;
import static javax.xml.stream.XMLStreamConstants.DTD;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.ENTITY_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.NAMESPACE;
import static javax.xml.stream.XMLStreamConstants.NOTATION_DECLARATION;
import static javax.xml.stream.XMLStreamConstants.PROCESSING_INSTRUCTION;
import static javax.xml.stream.XMLStreamConstants.SPACE;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.config.internal.ClassRegistry.m_HandlerClasses;
import static org.tquadrat.foundation.config.spi.CLIDefinition.validateOptionName;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Name;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.JavaUtils.isValidName;
import static org.tquadrat.foundation.util.JavaUtils.retrieveMethod;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.cli.CmdLineValueHandler;
import org.tquadrat.foundation.config.cli.EnumValueHandler;
import org.tquadrat.foundation.config.cli.SimpleCmdLineValueHandler;
import org.tquadrat.foundation.config.spi.CLIArgumentDefinition;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.CLIOptionDefinition;
import org.tquadrat.foundation.lang.StringConverter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *  Parses an XML CLI definition file.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: CLIDefinitionParser.java 1005 2022-02-03 12:40:52Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"OverlyComplexClass", "ClassWithTooManyMethods"} )
@ClassVersion( sourceVersion = "$Id: CLIDefinitionParser.java 1005 2022-02-03 12:40:52Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class CLIDefinitionParser
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  An implementation of
     *  {@link Location}.
     *  that is based on an instance of
     *  {@link SAXParseException}.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: CLIDefinitionParser.java 1005 2022-02-03 12:40:52Z tquadrat $
     *  @since 0.0.1
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: CLIDefinitionParser.java 1005 2022-02-03 12:40:52Z tquadrat $" )
    @API( status = INTERNAL, since = "0.0.1" )
    public static final class ExceptionLocation implements Location
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The exception that provides the data.
         */
        private final SAXParseException m_Exception;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code ExceptionLocation} instance from the given
         *  instance of
         *  {@link SAXParseException}.
         *
         *  @param  exception   The exception.
         */
        public ExceptionLocation( final SAXParseException exception ) { m_Exception = requireNonNullArgument( exception, "exception" ); }

            /*---------*\
        ====** Methods **==========================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final int getCharacterOffset() { return -1; }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final int getColumnNumber() {return m_Exception.getColumnNumber(); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final int getLineNumber() {return m_Exception.getLineNumber(); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String getPublicId() {return m_Exception.getPublicId(); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final String getSystemId()  {return m_Exception.getSystemId(); }
    }
    //  class ExceptionLocation

    /**
     *  An implementation of
     *  {@link XMLResolver}
     *  for this parser.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: CLIDefinitionParser.java 1005 2022-02-03 12:40:52Z tquadrat $
     *  @since 0.0.1
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: CLIDefinitionParser.java 1005 2022-02-03 12:40:52Z tquadrat $" )
    @API( status = INTERNAL, since = "0.0.1" )
    private static class CLIDefinitionResolver implements XMLResolver
    {
        /**
         *  {@inheritDoc}
         */
        @Override
        public final Object resolveEntity( final String publicID, final String systemID, final String baseURI, final String namespace ) throws XMLStreamException
        {
            Object retValue = null;

            if( CLI_DEFINITION_DTD.equals( systemID ) )
            {
                retValue = retrieveCLIDefinitionDTD();
            }
            else
            {
                out.printf( "PublicID = %s%n", publicID );
                out.printf( "SystemID = %s%n", systemID );
                out.printf( "BaseURI = %s%n", baseURI );
                out.printf( "Namespace = %s%n", namespace );
            }

            //---* Done *------------------------------------------------------
            return retValue;
        } // resolveEntity()
    }
    //  class XMLResolver

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The name for the CLI definition DTD file: {@value}.
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @API( status = INTERNAL, since = "0.0.1" )
    public static final String CLI_DEFINITION_DTD = "CLIDefinition.dtd";

    /**
     *  The name for the CLI definition XSD file: {@value}.
     */
    @API( status = INTERNAL, since = "0.0.1" )
    public static final String CLI_DEFINITION_XSD = "CLIDefinition.xsd";

    /**
     *  The message indicating that a start element was expected, but another
     *  event was encountered: {@value}.
     */
    public static final String MSG_ExpectedStartEvent = "Start event expected: %s";

    /**
     *  The message indicating that a provided
     *  {@link StringConverter}
     *  implementation is invalid:
     *  {@value}.
     */
    public static final String MSG_InvalidStringConverter = "Invalid StringConverter implementation: %s";

    /**
     *  The message indicating that an attribute value is invalid:
     *  {@value}.
     */
    public static final String MSG_InvalidValue = "Value '%2$s' for attribute '%1$s' is invalid";

    /**
     *  The message indicating that an attribute does not have a value:
     *  {@value}.
     */
    public static final String MSG_MissingValue = "Value for attribute '%1$s' is missing";

    /**
     *  The message indicating that a specific end element was expected, but
     *  another end element was encountered: {@value}.
     */
    public static final String MSG_UnexpectedEndEvent =
        """
        Unexpected End event: %1$s
        '%3$s' does not match the expected '%2$s'
        """;

    /**
     *  The message indicating that an unexpected event was encountered:
     *  {@value}.
     */
    public static final String MSG_UnexpectedEvent = "Unexpected event: %1$s";

    /**
     *  The message indicating that an unexpected attribute was
     *  encountered: {@value}.
     */
    public static final String MSG_WrongAttribute = "Unexpected attribute: %1$s";

    /**
     *  The message indicating that an unexpected start element was
     *  encountered: {@value}.
     */
    public static final String MSG_WrongElement1 = "Expected '%1$s', but encountered '%2$s'";

    /**
     *  The message indicating that an unexpected start element was
     *  encountered: {@value}.
     */
    public static final String MSG_WrongElement2 = "Encountered '%2$s' while expected one of: %1$s";

    /**
     *  The name for the XML attribute 'handler': {@value}.
     */
    public static final String XMLATTRIBUTE_Handler = "handler";

    /**
     *  The name for the XML attribute 'index': {@value}.
     */
    public static final String XMLATTRIBUTE_Index = "index";

    /**
     *  The name for the XML attribute 'isMultiValue': {@value}.
     */
    public static final String XMLATTRIBUTE_IsMultiValue = "isMultiValue";

    /**
     *  The name for the XML attribute 'isRequired': {@value}.
     */
    public static final String XMLATTRIBUTE_IsRequired = "isRequired";

    /**
     *  The name for the XML attribute 'key': {@value}.
     */
    public static final String XMLATTRIBUTE_Key = "key";

    /**
     *  The name for the XML attribute 'metaVar': {@value}.
     */
    public static final String XMLATTRIBUTE_MetaVar = "metaVar";

    /**
     *  The name for the XML attribute 'propertyName': {@value}.
     */
    public static final String XMLATTRIBUTE_PropertyName = "propertyName";

    /**
     *  The name for the XML attribute 'stringConversion': {@value}.
     */
    public static final String XMLATTRIBUTE_StringConversion = "stringConversion";

    /**
     *  The name for the XML attribute 'type': {@value}.
     */
    public static final String XMLATTRIBUTE_Type = "type";

    /**
     *  The name for the XML element 'alias': {@value}.
     */
    public static final String XMLELEMENT_Alias = "alias";

    /**
     *  The name for the XML element 'argument': {@value}.
     */
    public static final String XMLELEMENT_CLIArgument = "argument";

    /**
     *  The name for the XML element 'cliDefinition': {@value}.
     */
    public static final String XMLELEMENT_CLIDefinition = "cliDefinition";

    /**
     *  The name for the XML element 'option': {@value}.
     */
    public static final String XMLELEMENT_CLIOption = "option";

    /**
     *  The name for the XML element 'format': {@value}.
     */
    public static final String XMLELEMENT_Format = "format";

    /**
     *  The name for the XML element 'usage': {@value}.
     */
    public static final String XMLELEMENT_Usage = "usage";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The XML event reader that provides the CLI definition.
     */
    private final XMLEventReader m_EventReader;

    /**
     *  The property map.
     */
    private final Map<String,Object> m_PropertyMap;

    /**
     *  The XML stream.
     */
    private final String m_XMLContents;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code CLIDefinitionParser} instance.
     *
     *  @param  inputStream The input stream that should contain the XML CLI
     *      definition.
     *  @param  propertyMap The target data structure for the values from the
     *      command line.
     *  @throws XMLStreamException  Cannot create a
     *      {@link XMLEventReader}
     *      instance for the given input stream.
     *  @throws IOException Cannot read the given input stream.
     */
    @SuppressWarnings( "resource" )
    private CLIDefinitionParser( final InputStream inputStream, final Map<String,Object> propertyMap ) throws XMLStreamException, IOException
    {
        m_PropertyMap = requireNonNullArgument( propertyMap, "propertyMap" );

        //---* Get XML contents *----------------------------------------------
        m_XMLContents = new String( requireNonNullArgument( inputStream, "inputStream" ).readAllBytes(), UTF8 );

        //---* Create the event reader for parsing *---------------------------
        final var xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setXMLResolver( new CLIDefinitionResolver() );
        m_EventReader = xmlInputFactory.createXMLEventReader( new StringReader( m_XMLContents ) );
    }   //  CLIDefinitionParser()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Creates the instance for the command line value handler
     *  based on the given class for the type and class for the handler
     *  implementation.} If {@code processClass} is {@code null}, the method
     *  will search for a specialised handler class in the internal registry;
     *  if none can be found, and {@code stringConverter} is not {@code null},
     *  it creates an instance of
     *  {@link org.tquadrat.foundation.config.cli.SimpleCmdLineValueHandler}
     *  with it. But if {@code stringConverter} is {@code null}, an exception
     *  will be thrown.</p>
     *  <p>If {@code processClass} is not {@code null}, it has to be a class
     *  that implements
     *  {@link CmdLineValueHandler}.
     *  In that case that class will be instantiated.</p>
     *
     *  @param  <T> The type of the property the set.
     *  @param  type    The class for the property to set.
     *  @param  processClass    This is either the handler class or
     *      {@code null}.
     *  @param  stringConverter The
     *      {@link StringConverter}
     *      instance for the property; can be {@code null}.
     *  @return The command line value handler instance.
     *  @throws IllegalArgumentException    A command line value handler
     *      instance cannot be created.
     */
    @SuppressWarnings( {"unchecked", "rawtypes", "CastToConcreteClass"} )
    private final <T> CmdLineValueHandler<T> createHandler( final Class<? extends T> type, final Class<?> processClass, final StringConverter<? extends T> stringConverter ) throws IllegalArgumentException
    {
        final var propertyTypeIsEnum = requireNonNullArgument( type, "type" ).isEnum();
        @SuppressWarnings( "RedundantExplicitVariableType" )
        final BiConsumer<String, T> valueSetter = m_PropertyMap::put;
        CmdLineValueHandler<T> retValue = null;
        Class<? extends CmdLineValueHandler<?>> handlerClass = null;
        if( isNull( processClass ) )
        {
            if( propertyTypeIsEnum )
            {
                retValue = new EnumValueHandler( type, valueSetter );
            }
            else
            {
                //---* Infer the handler class, if necessary *-----------------
                final var foundHandlerClass = retrieveValueHandlerClass( type );
                if( foundHandlerClass.isPresent() )
                {
                    handlerClass = foundHandlerClass.get();
                }
                else if( nonNull( stringConverter ) )
                {
                    retValue = new SimpleCmdLineValueHandler<>( valueSetter, stringConverter );
                }
            }
        }
        else
        {
            if( !CmdLineValueHandler.class.isAssignableFrom( processClass ) )
            {
                throw new IllegalArgumentException( format( "'%s' is neither a StringConverter nor a CmdLineValueHandler", processClass.getName() ) );
            }

            //---* We got a command line value handler *-----------------------
            handlerClass = (Class<? extends CmdLineValueHandler<?>>) processClass;
        }

        if( isNull( retValue ) )
        {
            if( isNull( handlerClass ) )
            {
                throw new IllegalArgumentException( "Could not determine a class for the Command Line Value Handler" );
            }
            try
            {
                final var constructor = handlerClass.getConstructor( BiConsumer.class );
                retValue = (CmdLineValueHandler<T>) constructor.newInstance( valueSetter );
            }
            catch( final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e )
            {
                throw new IllegalArgumentException( format( "Unable to create value handler from '%s'", handlerClass.getName() ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createHandler()

    /**
     *  <p>{@summary Creates the instance for the string converter based on the
     *  given class for the type and class for the string converter
     *  implementation.} If {@code stringConverterClass} is {@code null}, the
     *  method will search for an implementation class in the internal
     *  registry; if none can be found, {@code null} will be returned.</p>
     *
     *  @param  <T> The type of the property to convert.
     *  @param  type    The class for the property to convert.
     *  @param  stringConverterClass    The String converter class or
     *      {@code null}.
     *  @return The String converter instance.
     */
    @SuppressWarnings( {"unchecked", "rawtypes"} )
    private final <T> StringConverter<T> createStringConverter( final Class<?> type, final Class<? extends StringConverter<?>> stringConverterClass )
    {
        requireNonNullArgument( type, "type" );

        final StringConverter<T> retValue;
        if( isNull( stringConverterClass ) )
        {
            if( type.isEnum() )
            {
                retValue = (StringConverter<T>) StringConverter.forEnum( (Class<? extends Enum>) type );
            }
            else
            {
                retValue = (StringConverter<T>) StringConverter.forClass( type ).orElse( null );
            }
        }
        else
        {
            final var foundProvider = retrieveMethod( stringConverterClass, "provider" );
            if( foundProvider.isPresent() )
            {
                try
                {
                    retValue = (StringConverter<T>) foundProvider.get().invoke( null );
                }
                catch( final IllegalAccessException | InvocationTargetException e )
                {
                    throw new IllegalArgumentException( format( MSG_InvalidStringConverter, stringConverterClass.getName() ), e );
                }
            }
            else
            {
                try
                {
                    @SuppressWarnings( "unchecked" )
                    final var constructor = (Constructor<StringConverter<T>>) stringConverterClass.getConstructor();
                    retValue = constructor.newInstance();
                }
                catch( final NoSuchMethodException e )
                {
                    throw new IllegalArgumentException( format( "No default constructor for StringConverter: %s", stringConverterClass.getName() ), e );
                }
                catch( final InstantiationException | InvocationTargetException | IllegalAccessException e )
                {
                    throw new IllegalArgumentException( format( MSG_InvalidStringConverter, stringConverterClass.getName() ), e );
                }
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createStringConverter()

    /**
     *  Executes the parsing.
     *
     *  @return The parsed CLI definition.
     *  @throws XMLStreamException  Cannot parse the given input stream.
     */
    @SuppressWarnings( "SwitchStatementWithTooManyBranches" )
    private final List<CLIDefinition> execute() throws XMLStreamException
    {
        List<CLIDefinition> retValue = List.of();
        try
        {
            while( m_EventReader.hasNext() )
            {
                final var event = m_EventReader.nextEvent();
                switch( event.getEventType() )
                {
                    case ATTRIBUTE:
                    case CDATA:
                    case CHARACTERS:
                    case END_ELEMENT:
                        out.printf( "%d: %s%n", event.getEventType(), event );
                        throw new XMLStreamException( format( MSG_ExpectedStartEvent, event.toString() ), event.getLocation() );

                    case START_ELEMENT:
                    {
                        final var startElement = event.asStartElement();
                        final var name = startElement.getName();
                        if( name.getLocalPart().equals( XMLELEMENT_CLIDefinition ) )
                        {
                            retValue = handleCLIDefinition( startElement );
                        }
                        else
                        {
                            throw new XMLStreamException( format( MSG_WrongElement1, XMLELEMENT_CLIDefinition, name.toString() ), event.getLocation() );
                        }
                        break;
                    }

                    case COMMENT:
                    case DTD:
                    case END_DOCUMENT:
                    case ENTITY_DECLARATION:
                    case NAMESPACE:
                    case NOTATION_DECLARATION:
                    case PROCESSING_INSTRUCTION:
                    case SPACE:
                    case START_DOCUMENT:
                        break;

                    default:
                        out.printf( "%d: %s%n", event.getEventType(), event );
                        throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
                }
            }
        }
        finally
        {
            m_EventReader.close();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  execute()

    /**
     *  Retrieves the URL for the CLI definition DTD file from the resources.
     *
     *  @return The URL for the file.
     */
    @API( status = INTERNAL, since = "0.0.1" )
    public static final URL getCLIDefinitionDTDURL()
    {
        final var retValue = CLIDefinitionParser.class.getResource( format( "/%s", CLI_DEFINITION_DTD ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getCLIDefinitionDTDURL()

    /**
     *  Retrieves the URL for the CLI definition XSD file from the resources.
     *
     *  @return The URL for the file.
     */
    @API( status = INTERNAL, since = "0.0.1" )
    public static final URL getCLIDefinitionXSDURL()
    {
        final var retValue = CLIDefinitionParser.class.getResource( format( "/%s", CLI_DEFINITION_XSD ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getCLIDefinitionXSDURL()

    /**
     *  Handles the {@value #XMLELEMENT_Alias} element.
     *
     *  @param  element The current element.
     *  @return The option alias.
     *  @throws XMLStreamException  A problem occurred while parsing the
     *      element.
     */
    @SuppressWarnings( {"SwitchStatementWithTooManyBranches", "SwitchStatementWithTooFewBranches"} )
    private final String handleAlias( final StartElement element ) throws XMLStreamException
    {
        String retValue = null;

        //---* Get the attributes *--------------------------------------------
        final var attributes = element.getAttributes();
        while( attributes.hasNext() )
        {
            final var attribute = attributes.next();
            final var name = attribute.getName();
            retValue = switch( name.getLocalPart() )
            {
                case XMLATTRIBUTE_Name -> processAttrName( attribute );
                default -> throw new XMLStreamException( format( MSG_WrongAttribute, name.toString() ), element.getLocation() );
            };
        }

        //---* Proceed parsing ... *-------------------------------------------
        var proceed = true;
        while( m_EventReader.hasNext() && proceed )
        {
            final var event = m_EventReader.nextEvent();
            switch( event.getEventType() )
            {
                case ATTRIBUTE:
                case CDATA:
                case DTD:
                case END_DOCUMENT:
                case ENTITY_DECLARATION:
                case NAMESPACE:
                case NOTATION_DECLARATION:
                case PROCESSING_INSTRUCTION:
                case START_DOCUMENT:
                case START_ELEMENT:
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );

                case END_ELEMENT:
                {
                    final var endElement = event.asEndElement();
                    final var name = endElement.getName();
                    if( !name.equals( element.getName() ) )
                    {
                        throw new XMLStreamException( format( MSG_UnexpectedEndEvent, event.toString(), element.getName(), name ), event.getLocation() );
                    }
                    proceed = false;
                    break;
                }

                case CHARACTERS:
                case COMMENT:
                case SPACE:
                    break;

                default:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
            }
        }

        if( isEmpty( retValue ) )
        {
            throw new XMLStreamException( format( MSG_MissingValue, XMLATTRIBUTE_Name ), element.getLocation() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleAlias()

    /**
     *  Handles the {@value #XMLELEMENT_CLIArgument} element.
     *
     *  @param  element The current element.
     *  @return The parsed CLI definition.
     *  @throws XMLStreamException  A problem occurred while parsing the
     *      element.
     */
    @SuppressWarnings( {"unchecked", "rawtypes", "SwitchStatementWithTooManyBranches", "NestedSwitchStatement"} )
    private CLIDefinition handleArgument( final StartElement element ) throws XMLStreamException
    {
        String format = null;
        Class<? extends CmdLineValueHandler<?>> processorClass = null;
        Class<? extends StringConverter<?>> stringConverterClass = null;
        var index = -1;
        var isMultiValue = false;
        var isRequired = false;
        String metaVar = null;
        String propertyName = null;
        Class<?> type = null;
        String usage = null;
        String usageKey = null;
        CLIArgumentDefinition retValue = null;

        //---* Get the attributes *--------------------------------------------
        final var attributes = element.getAttributes();
        while( attributes.hasNext() )
        {
            final var attribute = attributes.next();
            final var name = attribute.getName();
            switch( name.getLocalPart() )
            {
                case XMLATTRIBUTE_Handler -> processorClass = processAttrHandler( attribute );
                case XMLATTRIBUTE_Index -> {
                    try
                    {
                        index = Integer.parseInt( attribute.getValue() );
                        if( index < 0 )
                        {
                            throw new XMLStreamException( format( MSG_InvalidValue, XMLATTRIBUTE_Index, Integer.toString( index ) ), attribute.getLocation() );
                        }
                    }
                    catch( final NumberFormatException e )
                    {
                        final var xse = new XMLStreamException( format( MSG_InvalidValue, XMLATTRIBUTE_Index, attribute.getValue() ), attribute.getLocation(), e );
                        xse.initCause( e );
                        throw xse;
                    }
                }

                case XMLATTRIBUTE_IsMultiValue -> isMultiValue = Boolean.parseBoolean( attribute.getValue() );
                case XMLATTRIBUTE_IsRequired -> isRequired = Boolean.parseBoolean( attribute.getValue() );
                case XMLATTRIBUTE_MetaVar -> metaVar = attribute.getValue();
                case XMLATTRIBUTE_PropertyName -> propertyName = processAttrPropertyName( attribute );
                case XMLATTRIBUTE_StringConversion -> stringConverterClass = processAttrStringConversion( attribute );
                case XMLATTRIBUTE_Type -> type = processAttrType( attribute );
                default -> throw new XMLStreamException( format( MSG_WrongAttribute, name.toString() ), element.getLocation() );
            }
        }

        //---* Get the StringConverter instance *------------------------------
        final StringConverter<?> stringConverter = createStringConverter( type, stringConverterClass );

        //---* Get the handler instance *--------------------------------------
        final CmdLineValueHandler<?> handler;
        try
        {
            handler = createHandler( type, processorClass, stringConverter );
        }
        catch( final IllegalArgumentException e )
        {
            throw new XMLStreamException( "Cannot create Command Line Value Handler", e );
        }

        //---* Proceed parsing ... *-------------------------------------------
        var proceed = true;
        while( m_EventReader.hasNext() && proceed )
        {
            final var event = m_EventReader.nextEvent();
            switch( event.getEventType() )
            {
                case ATTRIBUTE:
                case CDATA:
                case DTD:
                case END_DOCUMENT:
                case ENTITY_DECLARATION:
                case NAMESPACE:
                case NOTATION_DECLARATION:
                case PROCESSING_INSTRUCTION:
                case START_DOCUMENT:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_ExpectedStartEvent, event.toString() ), event.getLocation() );

                case END_ELEMENT:
                {
                    final var endElement = event.asEndElement();
                    final var name = endElement.getName();
                    if( !name.equals( element.getName() ) )
                    {
                        throw new XMLStreamException( format( MSG_UnexpectedEndEvent, event.toString(), element.getName(), name ), event.getLocation() );
                    }
                    proceed = false;
                    break;
                }

                case START_ELEMENT:
                {
                    final var startElement = event.asStartElement();
                    final var name = startElement.getName();
                    switch( name.getLocalPart() )
                    {
                        case XMLELEMENT_Format -> format = handleFormat( startElement );

                        case XMLELEMENT_Usage ->
                        {
                            final var result = handleUsage( startElement );
                            usageKey = result.get( XMLATTRIBUTE_Key );
                            usage = result.get( XMLELEMENT_Usage );
                        }

                        default -> throw new XMLStreamException( format( MSG_WrongElement2, join( ", ", XMLELEMENT_Alias, XMLELEMENT_Format, XMLELEMENT_Usage ), name.toString() ), event.getLocation() );
                    }
                    break;
                }

                case CHARACTERS:
                case COMMENT:
                case SPACE:
                    break;

                default:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
            }
        }

        //---* Create the return value *---------------------------------------
        if( isEmptyOrBlank( metaVar ) )
        {
            metaVar = type.getSimpleName().toUpperCase( ROOT );
        }
        retValue = new CLIArgumentDefinition( propertyName, index, usage, usageKey, metaVar, isRequired, handler, isMultiValue, format );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleArgument()

    /**
     *  Handles the {@value #XMLELEMENT_CLIDefinition} element.
     *
     *  @param  element The current element.
     *  @return The parsed CLI definition.
     *  @throws XMLStreamException  A problem occurred while parsing the
     *      element.
     */
    @SuppressWarnings( {"SwitchStatementWithTooManyBranches", "NestedSwitchStatement"} )
    private final List<CLIDefinition> handleCLIDefinition( final StartElement element ) throws XMLStreamException
    {
        final List<CLIDefinition> retValue = new ArrayList<>();
        var proceed = true;
        while( m_EventReader.hasNext() && proceed )
        {
            final var event = m_EventReader.nextEvent();
            switch( event.getEventType() )
            {
                case ATTRIBUTE:
                case CDATA:
                case DTD:
                case END_DOCUMENT:
                case ENTITY_DECLARATION:
                case NAMESPACE:
                case NOTATION_DECLARATION:
                case PROCESSING_INSTRUCTION:
                case START_DOCUMENT:
                    throw new XMLStreamException( format( MSG_ExpectedStartEvent, event.toString() ), event.getLocation() );

                case END_ELEMENT:
                {
                    final var endElement = event.asEndElement();
                    final var name = endElement.getName();
                    if( !name.equals( element.getName() ) )
                    {
                        throw new XMLStreamException( format( MSG_UnexpectedEndEvent, event.toString(), element.getName(), name ), event.getLocation() );
                    }
                    proceed = false;
                    break;
                }

                case START_ELEMENT:
                {
                    final var startElement = event.asStartElement();
                    final var name = startElement.getName();
                    switch( name.getLocalPart() )
                    {
                        case XMLELEMENT_CLIArgument -> retValue.add( handleArgument( startElement ) );
                        case XMLELEMENT_CLIOption -> retValue.add( handleOption( startElement ) );
                        default -> throw new XMLStreamException( format( MSG_WrongElement2, join( ", ", XMLELEMENT_CLIOption, XMLELEMENT_CLIArgument ), name.toString() ), event.getLocation() );
                    }
                    break;
                }

                case CHARACTERS:
                case COMMENT:
                case SPACE:
                    break;

                default:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleCLIDefinition()

    /**
     *  Handles the {@value #XMLELEMENT_Format} element.
     *
     *  @param  element The current element.
     *  @return The format.
     *  @throws XMLStreamException  A problem occurred while parsing the
     *      element.
     */
    @SuppressWarnings( {"SwitchStatementWithTooManyBranches", "AssignmentToNull"} )
    private final String handleFormat( final StartElement element ) throws XMLStreamException
    {
        String retValue = null;

        var proceed = true;
        while( m_EventReader.hasNext() && proceed )
        {
            final var event = m_EventReader.nextEvent();
            switch( event.getEventType() )
            {
                case ATTRIBUTE:
                case DTD:
                case END_DOCUMENT:
                case ENTITY_DECLARATION:
                case NAMESPACE:
                case NOTATION_DECLARATION:
                case PROCESSING_INSTRUCTION:
                case START_DOCUMENT:
                case START_ELEMENT:
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );

                case END_ELEMENT:
                {
                    final var endElement = event.asEndElement();
                    final var name = endElement.getName();
                    if( !name.equals( element.getName() ) )
                    {
                        throw new XMLStreamException( format( MSG_UnexpectedEndEvent, event.toString(), element.getName(), name ), event.getLocation() );
                    }
                    proceed = false;
                    break;
                }

                case CDATA:
                case CHARACTERS:
                {
                    final var characters = event.asCharacters();
                    retValue = characters.getData();
                    break;
                }

                case COMMENT:
                case SPACE:
                    break;

                default:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
            }
        }

        if( isEmpty( retValue ) ) retValue = null;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleFormat()

    /**
     *  Handles the {@value #XMLELEMENT_CLIOption} element.
     *
     *  @param  element The current element.
     *  @return The parsed CLI definition.
     *  @throws XMLStreamException  A problem occurred while parsing the
     *      element.
     */
    @SuppressWarnings( {"unchecked", "rawtypes", "SwitchStatementWithTooManyBranches", "NestedSwitchStatement"} )
    private final CLIDefinition handleOption( final StartElement element ) throws XMLStreamException
    {
        String format = null;
        Class<? extends CmdLineValueHandler<?>> processorClass = null;
        Class<? extends StringConverter<?>> stringConverterClass = null;
        var isMultiValue = false;
        var isRequired = false;
        String metaVar = null;
        final List<String> names = new ArrayList<>();
        String propertyName = null;
        Class<?> type = null;
        String usage = null;
        String usageKey = null;
        CLIOptionDefinition retValue = null;

        //---* Get the attributes *--------------------------------------------
        final var attributes = element.getAttributes();
        while( attributes.hasNext() )
        {
            final var attribute = attributes.next();
            final var name = attribute.getName();
            switch( name.getLocalPart() )
            {
                case XMLATTRIBUTE_Handler -> processorClass = processAttrHandler( attribute );
                case XMLATTRIBUTE_IsMultiValue -> isMultiValue = Boolean.parseBoolean( attribute.getValue() );
                case XMLATTRIBUTE_IsRequired -> isRequired = Boolean.parseBoolean( attribute.getValue() );
                case XMLATTRIBUTE_MetaVar -> metaVar = attribute.getValue();
                case XMLATTRIBUTE_Name -> names.add( processAttrName( attribute ) );
                case XMLATTRIBUTE_PropertyName -> propertyName = processAttrPropertyName( attribute );
                case XMLATTRIBUTE_StringConversion -> stringConverterClass = processAttrStringConversion( attribute );
                case XMLATTRIBUTE_Type -> type = processAttrType( attribute );
                default -> throw new XMLStreamException( format( MSG_WrongAttribute, name.toString() ), element.getLocation() );
            }
        }

        //---* Get the StringConverter instance *------------------------------
        final StringConverter<?> stringConverter = createStringConverter( type, stringConverterClass );

        //---* Get the handler instance *--------------------------------------
        final CmdLineValueHandler<?> handler;
        try
        {
            handler = createHandler( type, processorClass, stringConverter );
        }
        catch( final IllegalArgumentException e )
        {
            throw new XMLStreamException( "Cannot create Command Line Value Handler", e );
        }

        //---* Proceed parsing ... *-------------------------------------------
        var proceed = true;
        while( m_EventReader.hasNext() && proceed )
        {
            final var event = m_EventReader.nextEvent();
            switch( event.getEventType() )
            {
                case ATTRIBUTE:
                case CDATA:
                case DTD:
                case END_DOCUMENT:
                case ENTITY_DECLARATION:
                case NAMESPACE:
                case NOTATION_DECLARATION:
                case PROCESSING_INSTRUCTION:
                case START_DOCUMENT:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_ExpectedStartEvent, event.toString() ), event.getLocation() );

                case END_ELEMENT:
                {
                    final var endElement = event.asEndElement();
                    final var name = endElement.getName();
                    if( !name.equals( element.getName() ) )
                    {
                        throw new XMLStreamException( format( MSG_UnexpectedEndEvent, event.toString(), element.getName(), name ), event.getLocation() );
                    }
                    proceed = false;
                    break;
                }

                case START_ELEMENT:
                {
                    final var startElement = event.asStartElement();
                    final var name = startElement.getName();
                    switch( name.getLocalPart() )
                    {
                        case XMLELEMENT_Alias -> names.add( handleAlias( startElement ) );
                        case XMLELEMENT_Format -> format = handleFormat( startElement );
                        case XMLELEMENT_Usage ->
                        {
                            final var result = handleUsage( startElement );
                            usageKey = result.get( XMLATTRIBUTE_Key );
                            usage = result.get( XMLELEMENT_Usage );
                        }
                        default -> throw new XMLStreamException( format( MSG_WrongElement2, join( ", ", XMLELEMENT_Alias, XMLELEMENT_Format, XMLELEMENT_Usage ), name.toString() ), event.getLocation() );
                    }
                    break;
                }

                case CHARACTERS:
                case COMMENT:
                case SPACE:
                    break;

                default:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
            }
        }

        //---* Create the return value *---------------------------------------
        if( names.size() != new HashSet<>( names ).size() )
        {
            throw new XMLStreamException( "Duplicate option names", element.getLocation() );
        }
        if( isEmptyOrBlank( metaVar ) )
        {
            metaVar = type.getSimpleName().toUpperCase( ROOT );
        }
        retValue = new CLIOptionDefinition( propertyName, names, usage, usageKey, metaVar, isRequired, handler, isMultiValue, format );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleOption()

    /**
     *  Handles the {@value #XMLELEMENT_Usage} element.
     *
     *  @param  element The current element.
     *  @return The usage and the usage key.
     *  @throws XMLStreamException  A problem occurred while parsing the
     *      element.
     */
    @SuppressWarnings( {"SwitchStatementWithTooFewBranches", "SwitchStatementWithTooManyBranches"} )
    private final Map<String,String> handleUsage( final StartElement element ) throws XMLStreamException
    {
        final Map<String,String> retValue = new HashMap<>();

        //---* Get the attributes *--------------------------------------------
        final var attributes = element.getAttributes();
        while( attributes.hasNext() )
        {
            final var attribute = attributes.next();
            final var name = attribute.getName();
            switch( name.getLocalPart() )
            {
                case XMLATTRIBUTE_Key -> retValue.put( XMLATTRIBUTE_Key, attribute.getValue() );
                default -> throw new XMLStreamException( format( MSG_WrongAttribute, name.toString() ), element.getLocation() );
            }
        }

        //---* Proceed parsing ... *-------------------------------------------
        var proceed = true;
        while( m_EventReader.hasNext() && proceed )
        {
            final var event = m_EventReader.nextEvent();
            switch( event.getEventType() )
            {
                case ATTRIBUTE:
                case DTD:
                case END_DOCUMENT:
                case ENTITY_DECLARATION:
                case NAMESPACE:
                case NOTATION_DECLARATION:
                case PROCESSING_INSTRUCTION:
                case START_DOCUMENT:
                case START_ELEMENT:
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );

                case END_ELEMENT:
                {
                    final var endElement = event.asEndElement();
                    final var name = endElement.getName();
                    if( !name.equals( element.getName() ) )
                    {
                        throw new XMLStreamException( format( MSG_UnexpectedEndEvent, event.toString(), element.getName(), name ), event.getLocation() );
                    }
                    proceed = false;
                    break;
                }

                case CDATA:
                case CHARACTERS:
                {
                    final var characters = event.asCharacters();
                    retValue.put( XMLELEMENT_Usage, characters.getData() );
                    break;
                }

                case COMMENT:
                case SPACE:
                    break;

                default:
                    out.printf( "%d: %s\n", event.getEventType(), event );
                    throw new XMLStreamException( format( MSG_UnexpectedEvent, event.toString() ), event.getLocation() );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  handleUsage()

    /**
     *  Parses the given
     *  {@link InputStream}.
     *
     *  @param  inputStream The input stream that should contain the XML CLI
     *      definition.
     *  @param  propertyMap The target data structure for the values from the
     *      command line.
     *  @param  validate    {@code true} if the given XML should be validated
     *      against the schema {@code CLIDefinition.xsd} previous to parsing
     *      it, {@code false} if the validation can be omitted.
     *
     *  @return The parsed CLI definition.
     *  @throws XMLStreamException  Cannot parse the given input stream.
     *  @throws IOException Cannot read the given input stream.
     */
    public static final List<CLIDefinition> parse( final InputStream inputStream, final Map<String,Object> propertyMap, final boolean validate ) throws XMLStreamException, IOException
    {
        final var parser = new CLIDefinitionParser( inputStream, propertyMap );
        if( validate ) parser.validate();
        final var retValue = parser.execute();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parse()

    /**
     *  Processes the attribute {@value #XMLATTRIBUTE_Handler}.
     *
     *  @param  attribute   The attribute.
     *  @return The handler class.
     *  @throws XMLStreamException  The attribute is somehow invalid.
     */
    @SuppressWarnings( "unchecked" )
    private static final Class<? extends CmdLineValueHandler<?>> processAttrHandler( final Attribute attribute ) throws XMLStreamException
    {

        final var name = attribute.getName();

        final var value = attribute.getValue();
        final Class<?> result;
        if( isNotEmptyOrBlank( value ) )
        {
            try
            {
                result = Class.forName( value );
            }
            catch( final ClassNotFoundException e )
            {
                final var xse = new XMLStreamException( format( MSG_InvalidValue, name.toString(), value ), attribute.getLocation(), e );
                xse.initCause( e );
                throw xse;
            }
            if( !CmdLineValueHandler.class.isAssignableFrom( result ) )
            {
                throw new XMLStreamException( format( MSG_InvalidValue, name.toString(), result.getName() ), attribute.getLocation() );
            }
        }
        else
        {
            throw new XMLStreamException( format( MSG_MissingValue, name.toString() ), attribute.getLocation() );
        }

        @SuppressWarnings( "RedundantExplicitVariableType" )
        final Class<? extends CmdLineValueHandler<?>> retValue = (Class<? extends CmdLineValueHandler<?>>) result;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processAttrHandler()

    /**
     *  Processes the attribute {@value org.tquadrat.foundation.lang.CommonConstants#XMLATTRIBUTE_Name}.
     *
     *  @param  attribute   The attribute.
     *  @return The property name.
     *  @throws XMLStreamException  The attribute is somehow invalid.
     */
    private static final String processAttrName( final Attribute attribute ) throws XMLStreamException
    {
        final var name = attribute.getName();

        final var retValue = attribute.getValue();
        try
        {
            validateOptionName( retValue );
        }
        catch( final IllegalArgumentException e )
        {
            final var xse = new XMLStreamException( join( "\n", format( MSG_InvalidValue, name.toString(), retValue ), e.getMessage() ), attribute.getLocation(), e );
            xse.initCause( e );
            throw xse;
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processAttrPropertyName()

    /**
     *  Processes the attribute {@value #XMLATTRIBUTE_PropertyName}.
     *
     *  @param  attribute   The attribute.
     *  @return The property name.
     *  @throws XMLStreamException  The attribute is somehow invalid.
     */
    private static final String processAttrPropertyName( final Attribute attribute ) throws XMLStreamException
    {
        final var name = attribute.getName();

        final var retValue = attribute.getValue();
        if( isNotEmptyOrBlank( retValue ) )
        {
            if( !isValidName( retValue ) )
            {
                throw new XMLStreamException( format( MSG_InvalidValue, name.toString(), retValue ), attribute.getLocation() );
            }
        }
        else
        {
            throw new XMLStreamException( format( MSG_MissingValue, name.toString() ), attribute.getLocation() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processAttrPropertyName()

    /**
     *  Processes the attribute {@value #XMLATTRIBUTE_StringConversion}.
     *
     *  @param  attribute   The attribute.
     *  @return The
     *      {@link StringConverter}
     *      implementation class.
     *  @throws XMLStreamException  The attribute is somehow invalid.
     */
    private static final Class<? extends StringConverter<?>> processAttrStringConversion( final Attribute attribute ) throws XMLStreamException
    {
        final var name = attribute.getName();

        final var value = attribute.getValue();
        final Class<?> result;
        if( isNotEmptyOrBlank( value ) )
        {
            try
            {
                result = Class.forName( value );
            }
            catch( final ClassNotFoundException e )
            {
                final var xse = new XMLStreamException( format( MSG_InvalidValue, name.toString(), value ), attribute.getLocation(), e );
                xse.initCause( e );
                throw xse;
            }
            if( !StringConverter.class.isAssignableFrom( result ) )
            {
                throw new XMLStreamException( format( MSG_InvalidStringConverter, result.getName(), attribute.getLocation() ) );
            }
        }
        else
        {
            throw new XMLStreamException( format( MSG_MissingValue, name.toString() ), attribute.getLocation() );
        }

        @SuppressWarnings( "unchecked" )
        final var retValue = (Class<? extends StringConverter<?>>) result;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processAttrStringConversion()

    /**
     *  Processes the attribute {@value #XMLATTRIBUTE_Type}.
     *
     *  @param  attribute   The attribute.
     *  @return The class for the type.
     *  @throws XMLStreamException  The attribute is somehow invalid.
     */
    private static final Class<?> processAttrType( final Attribute attribute ) throws XMLStreamException
    {
        final Class<?> retValue;

        final var name = attribute.getName();

        final var value = attribute.getValue();
        if( isNotEmptyOrBlank( value ) )
        {
            try
            {
                retValue = Class.forName( value );
            }
            catch( final ClassNotFoundException e )
            {
                final var xse = new XMLStreamException( format( MSG_InvalidValue, name.toString(), value ), attribute.getLocation(), e );
                xse.initCause( e );
                throw xse;
            }
        }
        else
        {
            throw new XMLStreamException( format( MSG_MissingValue, name.toString() ), attribute.getLocation() );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  processAttrType()

    /**
     *  Retrieves the CLI definition DTD file from the resources.
     *
     *  @return The input stream for the file.
     */
    @SuppressWarnings( "StaticMethodOnlyUsedInOneClass" )
    @API( status = INTERNAL, since = "0.0.1" )
    public static final InputStream retrieveCLIDefinitionDTD()
    {
        final var retValue = CLIDefinitionParser.class.getResourceAsStream( format( "/%s", CLI_DEFINITION_DTD ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveCLIDefinitionDTD()

    /**
     *  Retrieves the CLI definition XSD file from the resources.
     *
     *  @return The input stream for the file.
     */
    @API( status = INTERNAL, since = "0.0.1" )
    public static final InputStream retrieveCLIDefinitionXSD()
    {
        final var retValue = CLIDefinitionParser.class.getResourceAsStream( format( "/%s", CLI_DEFINITION_XSD ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveCLIDefinitionXSD()

    /**
     *  <p>{@summary Retrieves the class for the value handler if the property
     *  class is not an {@code enum} type.}</p>
     *  <p>{@code enum} types have to be handled separately.</p>
     *
     *  @param  propertyClass   The class of the property that should be set.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the effective handler class if the property class is not
     *      an {@code enum}.
     */
    private final Optional<Class<? extends CmdLineValueHandler<?>>> retrieveValueHandlerClass( final Class<?> propertyClass )
    {
        final Optional<Class<? extends CmdLineValueHandler<?>>> retValue = requireNonNullArgument( propertyClass, "propertyClass" ).isEnum()
            ? Optional.empty()
            : Optional.ofNullable( m_HandlerClasses.get( propertyClass ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveValueHandlerClass()

    /**
     *  Validates the given XML.
     *
     *  @throws XMLStreamException  The validation failed.
     *  @throws IOException Cannot read the given input stream.
     */
    private final void validate() throws IOException, XMLStreamException
    {
        //---* Create the stream reader for validation *-----------------------
        final var xmlInputFactory = XMLInputFactory.newInstance();
        final var streamReader = xmlInputFactory.createXMLStreamReader( new StringReader( m_XMLContents ) );

        //---* Create the schema factory for the validation *------------------
        final var schemaFactory = SchemaFactory.newInstance( W3C_XML_SCHEMA_NS_URI );
        try
        {
            final var schema = schemaFactory.newSchema( getCLIDefinitionXSDURL() );

            //---* Create the validator *--------------------------------------
            final var validator = schema.newValidator();

            //---* Validate *--------------------------------------------------
            validator.validate( new StAXSource( streamReader ) );
        }
        catch( @SuppressWarnings( "CaughtExceptionImmediatelyRethrown" ) final IOException e )
        {
            //---* Should not happen ... *-------------------------------------
            throw e;
        }
        catch( final SAXParseException e )
        {
            final var xse = new XMLStreamException( e.getMessage(), new ExceptionLocation( e ), e );
            xse.initCause( e );
            throw xse;
        }
        catch( final SAXException e )
        {
            throw new XMLStreamException( e.getMessage(), e );
        }
    }   //  validate()
}
//  class CLIDefinitionParser

/*
 *  End of File
 */