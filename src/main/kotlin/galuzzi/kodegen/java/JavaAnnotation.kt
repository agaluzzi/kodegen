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

import galuzzi.kodegen.CodeElement
import galuzzi.kodegen.CodeGen

class JavaAnnotation internal constructor(val type: Type) : CodeElement
{
    private val attributes = mutableMapOf<String, CodeGen>()

    fun attribute(name: String, value: CodeGen)
    {
        attributes[name] = value
    }

    fun value(value: CodeGen)
    {
        attribute("value", value)
    }

    override fun build(): CodeGen
    {
        return {
            +'@'
            +type
            if (attributes.isNotEmpty())
            {
                +'('
                val value = attributes["value"]
                if (value != null && attributes.size == 1)
                {
                    +value
                }
                else
                {
                    attributes.entries.forEachIndexed { i, entry ->
                        if (i > 0) +", "
                        +"${entry.key}="
                        +entry.value
                    }
                }
                +')'
            }
            newline()
        }
    }
}