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

/**
 * A code element that contains a body.
 */
interface BodyHolder
{
    /**
     * @return the body code generation function
     */
    fun getBody(): CodeGen

    /**
     * Sets the body.
     *
     * This function should only be called once.
     * Calling this function multiple times will replace the previously set value.
     */
    fun body(block: CodeGen)

    class Impl : BodyHolder
    {
        private var body: CodeGen = {}

        override fun getBody(): CodeGen
        {
            return body
        }

        override fun body(block: CodeGen)
        {
            body = block
        }
    }
}