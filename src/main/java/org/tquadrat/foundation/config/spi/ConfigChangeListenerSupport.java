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

package org.tquadrat.foundation.config.spi;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.config.ConfigBeanSpec;
import org.tquadrat.foundation.config.ConfigurationChangeEvent;
import org.tquadrat.foundation.config.ConfigurationChangeListener;
import org.tquadrat.foundation.lang.AutoLock;

/**
 *  Provides support for the event handling to the configuration beans.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigChangeListenerSupport.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ConfigChangeListenerSupport.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class ConfigChangeListenerSupport
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The owner of this support component.
     */
    private final ConfigBeanSpec m_Owner;

    /**
     *  The registry for the listeners.
     */
    private final Collection<WeakReference<ConfigurationChangeListener>> m_Listeners = new ArrayList<>();

    /**
     *  The &quot;read&quot; lock for the listener's registry.
     */
    private final AutoLock m_ReadLock;

    /**
     *  The thread pool for the notifier threads.
     */
    private final ExecutorService m_ThreadPool;

    /**
     *  The &quot;write&quot; lock for the listener's registry.
     */
    private final AutoLock m_WriteLock;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ConfigChangeListenerSupport} instance.
     *
     *  @param  owner   The owner of this instance of
     *      {@code ConfigChangeListenerSupport}.
     */
    public ConfigChangeListenerSupport( final ConfigBeanSpec owner )
    {
        m_Owner = requireNonNullArgument( owner, "owner" );

        //---* Create the locks *----------------------------------------------
        final var lock = new ReentrantReadWriteLock();
        m_ReadLock = AutoLock.of( lock.readLock() );
        m_WriteLock = AutoLock.of( lock.writeLock() );

        //---* Create the thread pool *----------------------------------------
        final var threadGroup = new ThreadGroup( format( "%s:Notifiers", getClass().getName()) );
        final var threadFactory = (ThreadFactory) r -> new Thread( threadGroup, r, format( "%s:Notifier", getClass().getName() ) );
        m_ThreadPool = newCachedThreadPool( threadFactory );
    }   //  ConfigChangeListenerSupport()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a new listener.
     *
     *  @param  listener    The new listener.
     */
    public final void addListener( final ConfigurationChangeListener listener )
    {
        requireNonNullArgument( listener, "listener" );
        try( final var ignored = m_WriteLock.lock() )
        {
            //---* Copy the current registry *---------------------------------
            final var copy = m_Listeners.stream()
                .filter( r -> nonNull( r.get() ) )
                .toList();

            //---* Clear the current registry *--------------------------------
            m_Listeners.clear();

            //---* Add the new listener *--------------------------------------
            if( copy.stream().map( Reference::get ).noneMatch( r -> r == listener ) )
            {
                m_Listeners.add( new WeakReference<>( listener ) );
            }

            //---* Put back the copy *-----------------------------------------
            m_Listeners.addAll( copy );
        }
    }   //  addListener()

    /**
     *  Fires the event to all registered listeners. Each listener will be
     *  called in its own thread.
     *
     *  @param  propertyName    The name of the property.
     *  @param  oldValue    The old value; can be {@code null}.
     *  @param  newValue    The new value; can be {@code null}.
     */
    public final void fireEvent( final String propertyName, final Object oldValue, final Object newValue )
    {
        requireNotEmptyArgument( propertyName, "propertyName" );

        try( final var ignored = m_ReadLock.lock() )
        {
            if( !m_Listeners.isEmpty() )
            {
                final var event = new ConfigurationChangeEvent( m_Owner, propertyName, oldValue, newValue );
                for( final var listenerRef : m_Listeners )
                {
                    final var listener = listenerRef.get();
                    if( nonNull( listener ) ) m_ThreadPool.submit( () -> listener.propertyChange( event ) );
                }
            }
        }
    }   //  fireEvent()

    /**
     *  Removes the given listener.
     *
     *  @param  listener    The listener to remove.
     */
    public final void removeListener( final ConfigurationChangeListener listener )
    {
        if( nonNull( listener ) )
        {
            try( final var ignored = m_WriteLock.lock() )
            {
                //---* Copy the current registry *-----------------------------
                final var copy = m_Listeners.stream()
                    .filter( r -> nonNull( r.get() ) )
                    .filter( r -> r.get() != listener )
                    .toList();

                //---* Clear the current registry *----------------------------
                m_Listeners.clear();

                //---* Put back the copy *-------------------------------------
                m_Listeners.addAll( copy );
            }
        }
    }   //  removeListener()
}
//  class ConfigChangeListenerSupport

/*
 *  End of File
 */