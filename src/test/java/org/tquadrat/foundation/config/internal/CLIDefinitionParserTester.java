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

import static java.lang.System.err;
import static java.lang.System.out;
import static org.tquadrat.foundation.config.ConfigUtil.printUsage;
import static org.tquadrat.foundation.config.internal.CLIDefinitionParser.parse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.PlaygroundClass;
import org.tquadrat.foundation.config.spi.CLIDefinition;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;

/**
 *  Tester for
 *  {@link CLIDefinitionParser}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 */
@PlaygroundClass
@ClassVersion( sourceVersion = "$Id: CLIDefinitionParserTester.java 895 2021-04-05 12:40:34Z tquadrat $" )
public final class CLIDefinitionParserTester
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class!
     */
    private CLIDefinitionParserTester() { throw new PrivateConstructorForStaticClassCalledError( CLIDefinitionParserTester.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Dumps the CLI definition to the console.
     *
     *  @param  cli The CLI definition.
     *  @throws IOException     Dump failed.
     */
    private static final void dump( final List<? extends CLIDefinition> cli ) throws IOException
    {
        printUsage( out, Optional.empty(), ">", cli );
    }   //  dump()

    /**
     *  The program entry point.
     *
     *  @param  args    The command line arguments.
     */
    @SuppressWarnings( "NestedTryStatement" )
    public static void main( final String... args )
    {
        final Map<String,Object> propertyMap = Map.of();
        try
        {
            //---* Parse the DTD version *-------------------------------------
            out.println( "Test with the DTD version of the XML file" );
            try( final var xml = CLIDefinitionParserTester.class.getResourceAsStream( "/TestCLIConfigDTD.xml" ) )
            {
                final var cliDefinition = parse( xml, propertyMap, false );
                dump( cliDefinition );
            }

            out.println( "=".repeat( 40 ) );
            out.println( "Test with the DTD with Schema version of the XML file" );
            try( final var xml = CLIDefinitionParserTester.class.getResourceAsStream( "/TestCLIConfigDTDwithSchema.xml" ) )
            {
                final var cliDefinition = parse( xml, propertyMap, true );
                dump( cliDefinition );
            }

            //---* Parse the XSD version *-------------------------------------
            out.println( "=".repeat( 40 ) );
            out.println( "Test with the XSD/Namespace version of the XML file" );
            try( final var xml = CLIDefinitionParserTester.class.getResourceAsStream( "/TestCLIConfigXSD_Namespace.xml" ) )
            {
                final var cliDefinition = parse( xml, propertyMap, true );
                dump( cliDefinition );
            }

            out.println( "=".repeat( 40 ) );
            out.println( "Test with the XSD version of the XML file" );
            try( final var xml = CLIDefinitionParserTester.class.getResourceAsStream( "/TestCLIConfigXSD.xml" ) )
            {
                final var cliDefinition = parse( xml, propertyMap, true );
                dump( cliDefinition );
            }
            out.println( "=".repeat( 40 ) );
        }
        catch( final Throwable t )
        {
            //---* Handle any previously unhandled exceptions *----------------
            t.printStackTrace( err );
        }
    }   //  main()
}
//  class CLIDefinitionParserTester

/*
 *  End of File
 */