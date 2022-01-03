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

package org.tquadrat.foundation.config.test;

import static javax.lang.model.element.ElementKind.METHOD;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.type.TypeKind.BOOLEAN;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.EnumSet;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.testutil.impl.NameTestImpl;
import org.tquadrat.foundation.util.JavaUtils;

/**
 *  Getters for {@code boolean} values are not recognised when the method name
 *  starts with &quot;is&hellip;&quot; instead of &quot;get&hellip;&quot; –
 *  but only the method
 *  {@link JavaUtils#isGetter(javax.lang.model.element.Element)}
 *  is called with a mock.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: BugHunt_20180912_001.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.config.test.BugHunt_20180912_001" )
public class BugHunt_20180912_001 extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for
     *  {@link JavaUtils#isGetter(javax.lang.model.element.Element)}
     *  when called with a mock.
     */
    @Test
    final void testMockIsGetter()
    {
        skipThreadTest();

        final var name = "isPresent";
        final ExecutableElement candidate = mock( name, ExecutableElement.class );
        expect( candidate.getSimpleName() ).andReturn( new NameTestImpl( name ) ).anyTimes();
        expect( candidate.getKind() ).andReturn( METHOD ).anyTimes();
        expect( candidate.getModifiers() ).andReturn( EnumSet.of( ABSTRACT, PUBLIC ) ).anyTimes();
        final TypeMirror returnType = mock( TypeMirror.class );
        expect( returnType.getKind() ).andReturn( BOOLEAN ).anyTimes();
        expect( candidate.getReturnType() ).andReturn( returnType ).anyTimes();
        expect( candidate.getParameters() ).andReturn( List.of() ).anyTimes();
        replayAll();

        assertTrue( JavaUtils.isGetter( candidate ) );
    }   //  testMockIsGetter()
}
//  class BugHunt_20180912_001

/*
 *  End of File
 */