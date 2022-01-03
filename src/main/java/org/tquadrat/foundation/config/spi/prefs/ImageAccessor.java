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

package org.tquadrat.foundation.config.spi.prefs;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.spi.InvalidPreferenceValueException;
import org.tquadrat.foundation.function.Getter;
import org.tquadrat.foundation.function.Setter;

/**
 *  The implementation of
 *  {@link org.tquadrat.foundation.config.spi.prefs.PreferenceAccessor}
 *  for instances of
 *  {@link BufferedImage}.
 *
 *  @note The image will be stored to the preferences as PNG, no matter what
 *      the original image format was!
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ImageAccessor.java 911 2021-05-06 22:07:00Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ImageAccessor.java 911 2021-05-06 22:07:00Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
@SuppressWarnings( "exports" )
public final class ImageAccessor extends BulkDataAccessorBase<BufferedImage>
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code MapAccessor} instance.
     *
     *  @param  propertyName    The name of the property.
     *  @param  getter  The property getter.
     *  @param  setter  The property setter.
     */
    public ImageAccessor( final String propertyName, final Getter<BufferedImage> getter, final Setter<BufferedImage> setter )
    {
        super( propertyName, getter, setter );
    }   //  ImageAccessor()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    protected final BufferedImage fromByteArray( final Preferences node, final byte [] source ) throws InvalidPreferenceValueException
    {
        BufferedImage retValue = null;
        if( nonNull( source ) )
        {
            try( final var inputStream = new ByteArrayInputStream( source ) )
            {
                retValue = ImageIO.read( inputStream );
            }
            catch( final IOException e )
            {
                throw new InvalidPreferenceValueException( node, getPropertyName(), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromByteArray()

    /**
     *  {@inheritDoc}
     */
    @Override
    protected final byte [] toByteArray( final Preferences node, final BufferedImage source ) throws InvalidPreferenceValueException
    {
        byte [] retValue = null;
        if( nonNull( source ) )
        {
            final var outputStream = new ByteArrayOutputStream();
            try( outputStream )
            {
                ImageIO.write( source, "PNG", outputStream );
            }
            catch( final IOException e )
            {
                throw new InvalidPreferenceValueException( node, getPropertyName(), e );
            }
            retValue = outputStream.toByteArray();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toByteArray()
}
//  class ImageAccessor

/*
 *  End of File
 */