package galuzzi.nevil

import galuzzi.nevil.java.ClassFile
import galuzzi.nevil.java.Nullability.ALLOW_NULL
import galuzzi.nevil.java.Nullability.NON_NULL
import galuzzi.nevil.java.Scope.PRIVATE
import galuzzi.nevil.java.Scope.PUBLIC
import galuzzi.nevil.java.comment
import galuzzi.nevil.java.type.*
import java.nio.file.Paths

/**
 * @author Aaron Galuzzi (2/12/2017)
 */
fun main(args:Array<String>)
{
    ClassFile("Test", "galuzzi.test")
    {
        abstract()

        javadoc {
            +"My first generated class!"
            tag("author", "Aaron Galuzzi")
            tag("since", "1980")
        }

        method(PUBLIC, void, "doSomething")
        {
            static()
            synchronized()
            javadoc {
                +"Does something\n\nLine 3"
            }

            val label = param(STRING, "label", ALLOW_NULL, "A textual label of sorts")
            val count = param(int, "count", "The number of things")

            body {
                +"System.out.println(\"Hello World!\");\n"
                +"System.out.println( $label + \" = \" + $count );\n"
                +comment("This is a comment.\n" +
                                 " ... Continued on line two.")
            }
        }

        property("firstName", STRING, NON_NULL, "the user's first name")
        property("lastName", STRING, ALLOW_NULL, "the user's last name")
        property("doubles", LIST.of(DOUBLE), NON_NULL, "a magical list of number")
        property("cache", MAP.of(INTEGER, STRING), NON_NULL, "a cache of sorts", setter = false)

        for (i in 1..4)
        {
            field(PRIVATE, int, "field_$i")
            {
                volatile()
                javadoc { +"This is field #$i" }
            }
        }

        method(PRIVATE, STRING, "buildThings")
        {
            javadoc { +"Does some amazing things!" }
            body { +"return \"hello\";\n" }
        }

    }.writeTo(Paths.get("C:\\Temp"))
}