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

package galuzzi.kodegen.java.support

import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.java.Type

/**
 * A code element that may throw exceptions.
 */
interface Thrower
{
    fun throws(type: Type, description: String = "")

    fun getThrows(): CodeGen

    fun addThrowsDoc(target: Documented)

    class Impl : Thrower
    {
        private val types = mutableListOf<ThrowType>()

        override fun throws(type: Type, description: String)
        {
            types += ThrowType(type, description)
        }

        override fun getThrows(): CodeGen
        {
            return {
                if (types.isNotEmpty())
                {
                    +" throws "
                    types.forEachIndexed { i, type ->
                        if (i > 0) +", "
                        +type.type
                    }
                }
            }
        }

        override fun addThrowsDoc(target: Documented)
        {
            target.javadoc {
                types.filter { it.description.isNotBlank() }
                        .forEach { type -> throws("${type.type}", type.description) }
            }
        }
    }

    private data class ThrowType(val type: Type, val description: String)
}