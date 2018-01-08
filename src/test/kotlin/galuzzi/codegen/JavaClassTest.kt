package galuzzi.codegen

import galuzzi.codegen.java.*
import galuzzi.codegen.java.Scope.PRIVATE
import galuzzi.codegen.java.Scope.PUBLIC
import galuzzi.codegen.java.Type.Companion.String
import galuzzi.codegen.java.Type.Companion.boolean
import galuzzi.codegen.java.Type.Companion.byte
import galuzzi.codegen.java.Type.Companion.int
import galuzzi.codegen.java.template.*
import galuzzi.codegen.java.comment
import galuzzi.codegen.java.number
import galuzzi.codegen.java.stmt
import galuzzi.codegen.java.string
import jdk.nashorn.internal.ir.annotations.Immutable
import sun.misc.Contended
import java.nio.file.Paths

/**
 * @author Aaron Galuzzi (2/12/2017)
 */
fun main(args: Array<String>)
{
    val outputDir = Paths.get("/Users/aarongaluzzi/Projects/aaron/codegen/target/generated-sources")

    val codeGen = JavaCodeGenerator(outputDir)

    codeGen.header = "This is a generated file. Please do not edit."

    val pkg = JavaPackage("com.test")

    codeGen.generate(buildClass(pkg))
    codeGen.generate(buildEnum(pkg))
}

private fun buildClass(pkg: JavaPackage): JavaClass
{
    return JavaClass.create("Person", pkg)
    {
        abstract()

        javadoc {
            p("My first generated class!")
            p("Here's another paragraph.")
            +"This class should:"
            ul("Be awesome", "Be exemplary", "Be generated, obviously")
            pre("Code{\n" +
                "  example();\n" +
                "}")
            tag("author", "Aaron Galuzzi")
            tag("since", "1980")
        }

        annotation(Type.from(Contended::class)).value(string("heavyweight"))
        annotation(Type.from(Immutable::class))

        field("IDENTIFIER", byte.array(), PRIVATE) {
            static()
            final()
            value({ +"new byte[42]" })
        }

        val firstName = field("firstName", Type.String, PRIVATE) { final() }
        val lastName = field("lastName", Type.String, PUBLIC) {
            volatile()
            javadoc {
                +"Not to be reckoned with"
            }
        }
        field("age", Type.int, PRIVATE)

        method("getFirstName", returnType = firstName.type) {
            javadoc {
                +"Gets this person's first name"
                returns("the first name")
            }
            body {
                +stmt("return ${firstName.name}")
            }
        }

//        property("doubles", LIST.of(DOUBLE), "a magical list of numbers", mutable = true)
//        property("messageCache", MAP.of(INTEGER, STRING), "the cache of messages", mutable = true)

//        addGetter(firstName, useOptional = true)
//        addGetter(age, useOptional = true)

        for (i in 1..4)
        {
            val field = field("field_$i", boolean, PRIVATE, description = "Field number $i")
            {
                volatile()
            }

            method(Getter(field))
            method(Setter(field))
        }

        constructor {
            annotation(Type.from("java.lang.Deprecated"))
            annotation(Type.from("java.lang.SuppressWarnings")) { value(string("foobar")) }

            param("first", firstName.type)
            param("last", lastName.type)

            body {
                +comment("\nI am being created...\n")
                +stmt("this.${firstName.name} = first")
                +stmt("this.${lastName.name} = last")
            }
        }

        method("doSomething")
        {
            static()
            synchronized()

            javadoc {
                +"Does something\n\nLine 3"
            }

            val label = param("label", Type.String, description = "A textual label of sorts", allowNull = true) {
                annotation(Type.from(SuppressWarnings::class)) { value(string("XXXX")) }
                annotation(Type.from("java.lang.Deprecated"))
            }
            val count = param("count", Type.int, description = "The number of things")

            body {
                +"System.out.println(\"Hello World!\");\n"
                +"System.out.println( ${label.name} + \" = \" + ${count.name} );\n"
                +comment("This is a comment.\n" +
                         " ... Continued on line two.")
            }
        }

        method("buildThings", Type.String, PRIVATE)
        {
            javadoc { +"Does some amazing things!" }
            annotation(Type.from(SuppressWarnings::class)) { value(string("XXXX")) }
            annotation(Type.from("java.lang.Deprecated"))
            body { +"return \"hello\";\n" }
        }

        method(ToString {
            +"return \"asdf\";"
        })

        method(Equals(this.type) {
            +"return age == other.age;"
        })

        method(HashCode {
            +"return age;"
        })

        val builder = nestedClass("Builder") {
            static()
            constructor(PRIVATE) {
            }
        }

        method("newBuilder", returnType = builder.type) {
            static()
            body {
                +"return new ${builder.type}();"
            }
        }
    }
}

private fun buildEnum(pkg: JavaPackage): JavaEnum
{
    return JavaEnum.create("Protocol", pkg) {

        javadoc {
            +"An enumeration of the standard web protocols."
        }

        constant("HTTP") {
            params(number(80), string("https://"))
            javadoc {
                +"Hypertext Transfer Protocol (insecure)"
            }
        }

        constant("HTTPS") {
            params(number(443), string("http://"))
            javadoc {
                +"HTTP Secure"
            }
        }

        field("port", int, description = "The standard port number") { final() }
        field("urlPrefix", String, description = "Prefix for URLs") { final() }

        constructor(BasicConstructor(getFields(), PRIVATE))

        for (field in getFields())
        {
            method(Getter(field))
        }
    }
}