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

import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.AutoLock;

/**
 *  This class provides the implementation for the preferences change listeners
 *  as defined through the interfaces
 *  {@link NodeChangeListener}
 *  and
 *  {@link PreferenceChangeListener}. For that, it extends
 *  {@link PreferenceChangeListenerBase}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferenceChangeListenerImpl.java 944 2021-12-21 21:56:24Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PreferenceChangeListenerImpl.java 944 2021-12-21 21:56:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class PreferenceChangeListenerImpl extends PreferenceChangeListenerBase
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code PreferenceChangeListenerImpl} instance.
     *
     *  @param  registry    The reference to the registry of the preferences
     *      accessors.
     *  @param  lock    The lock that guards the access to the properties.
     */
    public PreferenceChangeListenerImpl( final Map<String, PreferenceAccessor<?>> registry, final AutoLock lock )
    {
        super( registry, lock );
    }   //  PreferenceChangeListenerImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public void childAdded( final NodeChangeEvent event )
    {
        final var accessor = retrieveAccessor( event.getChild().name() );
        if( accessor.isPresent() )
        {
            try( final var ignore = lock() )
            {
                accessor.get().readPreference( event.getParent() );
            }
            catch( final BackingStoreException e )
            {
                throw new PreferencesException( e );
            }
        }
    }   //  childAdded()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void childRemoved( final NodeChangeEvent event )
    {
        final var accessor = retrieveAccessor( event.getChild().name() );
        if( accessor.isPresent() )
        {
            try( final var ignore = lock() )
            {
                accessor.get().readPreference( event.getParent() );
            }
            catch( final BackingStoreException e )
            {
                throw new PreferencesException( e );
            }
        }
    }   //  childRemoved()

    /**
     *  {@inheritDoc}
     */
    @Override
    public void preferenceChange( final PreferenceChangeEvent event )
    {
        final var accessor = retrieveAccessor( event.getKey() );
        if( accessor.isPresent() )
        {
            try( final var ignore = lock() )
            {
                accessor.get().readPreference( event.getNode() );
            }
            catch( final BackingStoreException e )
            {
                throw new PreferencesException( e );
            }
        }
    }   //  preferenceChange()
}
//  class PreferenceChangeListenerImpl

/*
 *  End of File
 */