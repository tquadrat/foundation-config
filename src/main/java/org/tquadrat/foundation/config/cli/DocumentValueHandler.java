/*
 * ============================================================================
 *  Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.config.cli;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.config.CmdLineException.MSGKEY_InvalidFileName;
import static org.tquadrat.foundation.config.CmdLineException.MSG_InvalidFileName;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.util.stringconverter.FileStringConverter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *  An implementation of
 *  {@link CmdLineValueHandler}
 *  for
 *  {@link Document}
 *  values.<br>
 *  <br>The {@code Document} will be identified by the name of the respective
 *  {@link java.io.File}
 *  object that of course has to exist and needs to be accessible.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DocumentValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: DocumentValueHandler.java 896 2021-04-05 20:25:33Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
@SuppressWarnings( "exports" )
public final class DocumentValueHandler extends CmdLineValueHandler<Document>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for a failed read attempt for the XML file: {@value}.
     */
    public static final String MSG_ReadFailed = "Reading/parsing of XML file '%1$s' failed";

    /**
     *  The resource bundle key for the message about a failed read attempt for
     *  the XML file.
     */
    @Message
    (
        description = "The error message about a failed read attempt for the XML file.",
        translations =
        {
            @Translation( language = "en", text = MSG_ReadFailed ),
            @Translation( language = "de", text = "Die XML-Datei '%1$s' kann nicht gelesen oder geparst werden" )
        }
    )
    public static final int MSGKEY_ReadFailed = 27;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code DocumentValueHandler} instance.
     *
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public DocumentValueHandler( final BiConsumer<String,Document> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter );
    }   //  DocumentValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( {"ThrowCaughtLocally", "OverlyBroadCatchBlock"} )
    @Override
    protected final Collection<Document> translate( final Parameters params ) throws CmdLineException
    {
        Collection<Document> retValue = List.of();
        var fileName = requireNonNullArgument( params, "params" ).getParameter( 0 );
        try
        {
            final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
            final var builder = documentBuilderFactory.newDocumentBuilder();
            final var documentFile = FileStringConverter.INSTANCE.fromString( fileName ).getCanonicalFile().getAbsoluteFile();
            fileName = documentFile.getAbsolutePath();
            if( !documentFile.exists() ) throw new FileNotFoundException( fileName );
            if( !documentFile.isFile() ) throw new IOException( "'%s' is not a file".formatted( fileName ) );
            if( !documentFile.canRead() ) throw new IOException( "Cannot read '%s'".formatted( fileName ) );
            final var document = builder.parse( documentFile );
            retValue = List.of( document );
        }
        catch( final IllegalArgumentException e )
        {
            throw new CmdLineException( MSG_InvalidFileName, e, MSGKEY_InvalidFileName, fileName );
        }
        catch( final IOException | ParserConfigurationException | SAXException e )
        {
            throw new CmdLineException( MSG_ReadFailed, e, MSGKEY_ReadFailed, fileName );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class DocumentValueHandler

/*
 *  End of File
 */