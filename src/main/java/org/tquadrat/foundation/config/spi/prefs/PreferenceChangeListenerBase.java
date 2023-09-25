/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import java.util.Map;
import java.util.Optional;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.AutoLock;

/**
 *  <p>{@summary This class provides the base for an implementation of a change
 *  listener for the connected USER {@code Preferences} as defined through the
 *  interfaces
 *  {@link NodeChangeListener}
 *  and
 *  {@link PreferenceChangeListener}.}</p>
 *  <p>All implementations must provided a {@code public} constructor with the
 *  same signature as
 *  {@linkplain #PreferenceChangeListenerBase(Map, AutoLock) that for this class}.</p>
 *  <p>The class
 *  {@link PreferenceChangeListenerImpl}
 *  provides a default implementation.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: PreferenceChangeListenerBase.java 944 2021-12-21 21:56:24Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: PreferenceChangeListenerBase.java 944 2021-12-21 21:56:24Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public abstract class PreferenceChangeListenerBase implements PreferenceChangeListener, NodeChangeListener
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     * The reference to the registry of the preferences accessors.
     */
    private final Map<String, PreferenceAccessor<?>> m_AccessorsRegistry;

    /**
     *  The &quot;write&quot; lock that guards the access to the properties.
     */
    private final AutoLock m_Lock;

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
    protected PreferenceChangeListenerBase( final Map<String, PreferenceAccessor<?>> registry, final AutoLock lock )
    {
        m_AccessorsRegistry = Map.copyOf( requireNonNullArgument( registry, "registry" ) );
        m_Lock = requireNonNullArgument( lock, "lock" );
    }   //  PreferenceChangeListenerImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract void childAdded( final NodeChangeEvent event );

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract void childRemoved( final NodeChangeEvent event );

    /**
     *  <p>{@summary Returns a reference to the lock instance.} This does
     *  <i>not</i> trigger the lock!</p>
     *  <p>Use this method like this:</p>
     *  <pre><code>  …
     *  final var lock = getLockInstance();
     *  lock.execute( () -> doSomething() );
     *  …</code></pre>
     *
     *  @return The lock instance.
     */
    protected final AutoLock getLockInstance() { return m_Lock; }

    /**
     *  <p>{@summary Locks the connected configuration bean for writing.}</p>
     *  <p>Use this method like this:</p>
     *  <pre><code>  …
     *  try( final var ignore = lock() )
     *  {
     *      // Add here whatever needs to be done …
     *  }
     *  …</code></pre>
     *
     *  @return The locked lock.
     */
    protected final AutoLock lock()
    {
        final var retValue = m_Lock.lock();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  lock()

    /**
     *  {@inheritDoc}
     */
    @Override
    public abstract void preferenceChange( final PreferenceChangeEvent event );

    /**
     *  Provides a reference to the registry for the
     *  {@linkplain PreferenceAccessor accessors}. The key for the map is the
     *  preferences key, <i>not</i> the property name!
     *
     *  @return A reference to the accessor registry.
     */
    protected final Map<String,PreferenceAccessor<?>> registry() { return m_AccessorsRegistry; }

    /**
     *  Retrieves the accessor for the given {@code Preferences} key.
     *
     *  @param  key The key.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the accessor.
     */
    protected final Optional<PreferenceAccessor<?>> retrieveAccessor( final String key )
    {
        Optional<PreferenceAccessor<?>> retValue = Optional.empty();
        if( isNotEmptyOrBlank( key ) )
        {
            retValue = Optional.ofNullable( m_AccessorsRegistry.get( key ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveAccessor()
}
//  class PreferenceChangeListenerImpl

/*
 *  End of File
 */