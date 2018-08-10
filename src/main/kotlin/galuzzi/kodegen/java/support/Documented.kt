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

import galuzzi.kodegen.java.Javadoc

/**
 * A code element that supports Javadoc.
 */
interface Documented
{
    fun javadoc(block: Javadoc.() -> Unit)

    fun getJavadoc(): Javadoc

    class Impl : Documented
    {
        private val javadoc = Javadoc()

        override fun javadoc(block: Javadoc.() -> Unit)
        {
            block.invoke(javadoc)
        }

        override fun getJavadoc(): Javadoc
        {
            return javadoc
        }
    }
}