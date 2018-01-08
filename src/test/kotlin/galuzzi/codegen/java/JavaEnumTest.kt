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

package galuzzi.codegen.java

import galuzzi.codegen.java.control.switch
import galuzzi.codegen.java.template.BasicConstructor
import galuzzi.codegen.java.template.Getter
import galuzzi.codegen.java.template.ToString

internal class JavaEnumTest : JavaGenTest("expect/Enum.java")
{
    class Protocol(val name: String,
                   val description: String,
                   val numberHex: String,
                   val headerLength: Int)

    override fun build(): JavaTypeElement
    {
        val protocols = listOf(Protocol("TCP", "Transmission Control Protocol", "0x06", 20),
                               Protocol("UDP", "User Datagram Protocol", "0x11", 8))

        return JavaEnum.create("TransportProtocol", JavaPackage("com.example")) {

            javadoc {
                +"Some common transport layer protocols."
            }

            for (p in protocols)
            {
                constant(p.name) {
                    javadoc {
                        +p.description
                    }
                    params(string("${p.name} (${p.description})"),
                           { +p.numberHex },
                           number(p.headerLength))
                }
            }

            val displayName = field("displayName", Type.String) {
                final()
            }

            val number = field("number", Type.int, description = "The protocol number used in the IPv4 header") {
                final()
            }

            val headerLength = field("headerLength", Type.int, description = "The typical number of octets in a packet header") {
                final()
            }

            constructor(BasicConstructor(getFields(), Scope.PRIVATE))

            method(Getter(number))
            method(Getter(headerLength))

            method(ToString { +"return $displayName;" })

            method("get", Type.Optional.of(type)) {
                static()

                javadoc {
                    +"Retrieves the transport protocol that matches a given string."
                    returns("the matching protocol, or an empty Optional if there is none")
                }

                val input = param("str", Type.String, allowNull = false, description = "the protocol name (acronym) to resolve")

                body {
                    +switch("$input.toLowerCase().trim()") {
                        for (p in protocols)
                        {
                            case(string(p.name.toLowerCase())) {
                                +"return ${Type.Optional}.of(${p.name});"
                            }
                        }
                        default {
                            +"return ${Type.Optional}.empty();"
                        }
                    }
                }
            }
        }
    }
}