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

package org.tquadrat.foundation.config;

import static org.apiguardian.api.API.Status.STABLE;

import java.io.Serial;
import java.util.EventObject;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.Objects;

/**
 *  The event object that is thrown each time a property of a configuration
 *  bean is changed.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ConfigurationChangeEvent.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.1
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ConfigurationChangeEvent.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.1" )
public final class ConfigurationChangeEvent extends EventObject
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The new value of the property.
     */
    private final Object m_NewValue;

    /**
     *  The old value of the property.
     */
    private final Object m_OldValue;

    /**
     *  The name of the property.
     */
    private final String m_PropertyName;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ConfigurationChangeEvent} instance.
     *
     *  @param  sourceBean  The reference to the configuration bean that fired
     *      the change event.
     *  @param  propertyName    The name of the property that was modified.
     *  @param  oldValue    The property's value before the change; obviously,
     *      this can be {@code null}, depending on the property.
     *  @param  newValue    The new value of the property; if allowed by the
     *      property, this can be {@code null} also.
     */
    public ConfigurationChangeEvent( final ConfigBeanSpec sourceBean, final String propertyName, final Object oldValue, final Object newValue )
    {
        super( sourceBean );    //  Parent does the null check
        m_PropertyName = propertyName;
        m_OldValue = oldValue;
        m_NewValue = newValue;
    }   //  ConfigurationChangeEvent()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Gets the new value for the property, expressed as an instance of
     *  {@link Object}.
     *
     *  @return The new value for the property, expressed as an instance of
     *      {@code Object}. May be {@code null}.
     */
    public final Object getNewValue() { return m_NewValue; }

    /**
     *  Gets the old value for the property, expressed as an instance of
     *  {@link Object}.
     *
     *  @return The old value for the property, expressed as an instance of
     *      {@code Object}. May be {@code null}.
     */
    public final Object getOldValue() { return m_OldValue; }

    /**
     *  Gets the name of the configuration property that was changed.
     *
     *  @return The name of the property that was changed.
     */
    public final String getPropertyName() { return m_PropertyName; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final ConfigBeanSpec getSource() { return (ConfigBeanSpec) super.getSource(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var retValue =
            "%1$s [propertyName=%3$s; oldValue=%4$s; newValue=%5$s; source=%2$s]".formatted(
                getClass().getName(),
                getSource().getClass().getName(),
                getPropertyName(),
                Objects.toString( getOldValue() ),
                Objects.toString( getNewValue() ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ConfigurationChangeEvent

/*
 *  End of File
 */