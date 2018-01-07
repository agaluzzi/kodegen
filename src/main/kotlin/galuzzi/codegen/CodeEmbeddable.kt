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

package galuzzi.codegen

/**
 * An object that can be "embedded" in a String for code generation. In other words, the [toString] for a
 * [CodeEmbeddable] will produce generated code of some sort.
 */
interface CodeEmbeddable
{
    /**
     * Produces generated code that represents this object. Implementers should define more specifically what this
     * function will produce.
     */
    override fun toString(): String
}