/*
 * Copyright 2018 Aaron Galuzzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package galuzzi.kodegen.java

import galuzzi.kodegen.java.control.ifThen
import galuzzi.kodegen.java.template.*
import java.io.Serializable

internal class JavaClassTest : JavaGenTest("expect/Class.java")
{
    override fun build(): JavaTypeElement
    {
        return JavaClass.create("Person", JavaPackage("com.example")) {

            javadoc { +"A human being." }

            implements(Type.from(Serializable::class))

            field("JOHN_DOE", type, Scope.PUBLIC) {
                static()
                final()
                value = { +"new $type(\"John\", \"Doe\", 999)" }
            }

            val first = field("first", Type.String, description = "The given name") {
                final()
            }

            val last = field("last", Type.String, description = "The family name") {
                final()
            }

            field("timeOfBirth", Type.long, Scope.PRIVATE) {
                final()
            }

            val age = field("age", Type.int, Scope.PROTECTED, description = "The number of years lived") {
                volatile()
            }

            constructor(basicConstructor(this, allowNulls = false))

            constructor(Scope.PROTECTED) {
                val pFirst = param(first)
                val pLast = param(last)
                body {
                    +"$first = $pFirst.trim();\n"
                    +"$last = $pLast.trim();\n"
                }
            }

            for (f in getMemberFields())
            {
                method(getter(f))
            }

            for (f in getMemberFields().filter { !it.isFinal() })
            {
                method(setter(f))
            }

            method("happyBirthday") {
                javadoc {
                    +"Adds another year lived."
                    pad()
                    +"This should only be invoked annually."
                }
                synchronized()
                throws(Type.IllegalStateException, "if the age rolls over")
                throws(Type.from(UnsupportedOperationException::class))
                body {
                    +"$age++;\n"
                    +ifThen("$age < 0") {
                        +"throw new ${Type.IllegalStateException}(\"Invalid age.\");"
                    }
                }
            }

            method(equalsBasic(this))
            method(hashCodeBasic(this))
            method(toStringBasic(this))
        }
    }
}