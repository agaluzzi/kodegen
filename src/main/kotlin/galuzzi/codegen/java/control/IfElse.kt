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

package galuzzi.codegen.java.control

import galuzzi.codegen.CodeElement
import galuzzi.codegen.CodeGen
import galuzzi.codegen.java.block

fun ifThen(test: CodeGen, then: CodeGen): IfElse
{
    return IfElse(test, then)
}

fun ifThen(test: String, then: CodeGen): IfElse
{
    return ifThen({ +test }, then)
}

/**
 * An if-then-else statement.
 */
class IfElse internal constructor(private val test: CodeGen,
                                  private val then: CodeGen) : CodeElement
{
    private var elseBlock: CodeGen? = null

    fun orElse(block: CodeGen): IfElse
    {
        elseBlock = block
        return this
    }

    override fun build(): CodeGen
    {
        return {
            newline()
            +"if ("
            +test
            +")"
            +block(then)

            val e = elseBlock
            if (e != null)
            {
                newline()
                +"else"
                +block(e)
            }
        }
    }
}