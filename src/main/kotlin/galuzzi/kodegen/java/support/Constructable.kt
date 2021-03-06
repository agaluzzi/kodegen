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

import galuzzi.kodegen.java.JavaConstructor
import galuzzi.kodegen.java.Scope
import galuzzi.kodegen.java.Type
import galuzzi.kodegen.java.template.ConstructorTemplate

/**
 * A code element that may contain constructors.
 */
interface Constructable
{
    /**
     * @return the list of constructors
     */
    fun getConstructors(): List<JavaConstructor>

    /**
     * Adds a constructor.
     */
    fun constructor(scope: Scope = Scope.PUBLIC,
                    init: JavaConstructor.() -> Unit = {}): JavaConstructor

    /**
     * Adds a constructor.
     */
    fun constructor(template: ConstructorTemplate): JavaConstructor

    class Impl(private val type: Type) : Constructable
    {
        private val constructors = mutableListOf<JavaConstructor>()

        override fun getConstructors(): List<JavaConstructor>
        {
            return constructors
        }

        override fun constructor(scope: Scope, init: JavaConstructor.() -> Unit): JavaConstructor
        {
            val constructor = JavaConstructor(type, scope).apply(init)
            constructors += constructor
            return constructor
        }

        override fun constructor(template: ConstructorTemplate): JavaConstructor
        {
            val constructor = template.build(type)
            constructors += constructor
            return constructor
        }
    }
}