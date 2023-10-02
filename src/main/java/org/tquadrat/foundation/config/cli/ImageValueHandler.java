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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.CmdLineException;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.config.spi.Parameters;
import org.tquadrat.foundation.i18n.Message;
import org.tquadrat.foundation.i18n.Translation;
import org.tquadrat.foundation.util.stringconverter.FileStringConverter;

/**
 *  <p>{@summary An implementation of
 *  {@link CmdLineValueHandler}
 *  for
 *  {@link BufferedImage}
 *  values.}</p>
 *  <p>The image will be identified by the name of the respective
 *  {@link java.io.File}
 *  object that of course has to exist and needs to be accessible.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ImageValueHandler.java 1061 2023-09-25 16:32:43Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ImageValueHandler.java 1061 2023-09-25 16:32:43Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.1" )
public final class ImageValueHandler extends CmdLineValueHandler<BufferedImage>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for a failed read attempt for the image: {@value}.
     */
    public static final String MSG_ReadFailed = "Reading of the image file '%1$s' failed";

    /**
     *  The resource bundle key for the message about a failed read attempt for
     *  the image.
     */
    @Message
    (
        description = "The error message about a failed read attempt for the image file.",
        translations =
        {
            @Translation( language = "en", text = MSG_ReadFailed ),
            @Translation( language = "de", text = "Die Bilddatei '%1$s' kann nicht gelesen werden" )
        }
    )
    public static final int MSGKEY_ReadFailed = 28;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ImageValueHandler} instance.
     *
     *  @param  context The CLI definition that provides the context for this
     *      value handler.
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public ImageValueHandler( final CLIDefinition context, final BiConsumer<String,BufferedImage> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( context, valueSetter );
    }   //  ImageValueHandler()

    /**
     *  Creates a new {@code ImageValueHandler} instance.
     *
     *  @param  valueSetter The function that places the translated value to
     *      the property.
     */
    public ImageValueHandler( final BiConsumer<String,BufferedImage> valueSetter )
    {
        //---* Daddy will do the null check *----------------------------------
        super( valueSetter );
    }   //  ImageValueHandler()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final Collection<BufferedImage> translate( final Parameters params ) throws CmdLineException
    {
        Collection<BufferedImage> retValue = List.of();
        var fileName = requireNonNullArgument( params, "params" ).getParameter( 0 );
        try
        {
            final var imageFile = FileStringConverter.INSTANCE.fromString( fileName ).getCanonicalFile().getAbsoluteFile();
            fileName = imageFile.getAbsolutePath();
            final var image = ImageIO.read( imageFile );
            retValue = List.of( image );
        }
        catch( final IllegalArgumentException e )
        {
            throw new CmdLineException( MSG_InvalidFileName, e, MSGKEY_InvalidFileName, fileName );
        }
        catch( final IOException e )
        {
            throw new CmdLineException( MSG_ReadFailed, e, MSGKEY_ReadFailed, fileName );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  translate()
}
//  class ImageValueHandler

/*
 *  End of File
 */