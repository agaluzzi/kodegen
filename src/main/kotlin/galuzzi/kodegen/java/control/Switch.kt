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

package galuzzi.kodegen.java.control

import galuzzi.kodegen.CodeElement
import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.java.block

fun switch(value: CodeGen, init: Switch.() -> Unit): Switch
{
    return Switch(value).apply(init)
}

fun switch(value: String, init: Switch.() -> Unit): Switch
{
    return switch({ +value }, init)
}

/**
 * A switch statement.
 */
class Switch internal constructor(private val value: CodeGen) : CodeElement
{
    private val cases = mutableListOf<Case>()

    fun case(value: CodeGen, block: CodeGen)
    {
        cases += Case(value, block)
    }

    fun default(block: CodeGen)
    {
        cases += Case(null, block)
    }

    override fun build(): CodeGen
    {
        return {
            newline()
            +"switch ("
            +value
            +")"
            +block {
                for (case in cases)
                {
                    newline()
                    if (case.value == null)
                    {
                        +"default"
                    }
                    else
                    {
                        +"case "
                        +case.value
                    }
                    +":\n"
                    indent()
                    +case.block
                    outdent()
                }
            }
        }
    }

    private class Case(val value: CodeGen?, val block: CodeGen)
}