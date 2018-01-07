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

package galuzzi.codegen.java.support

import galuzzi.codegen.java.JavaClass
import galuzzi.codegen.java.JavaTypeElement
import galuzzi.codegen.java.Scope
import galuzzi.codegen.java.TypeName

/**
 * TODO...
 */
interface TypeContainer
{
    fun getNestedTypes(): List<JavaTypeElement>

    fun nestedClass(name: String,
                    scope: Scope = Scope.PUBLIC,
                    init: JavaClass.() -> Unit = {}): JavaClass

    class Impl(private val baseName: TypeName) : TypeContainer
    {
        private val types = mutableListOf<JavaTypeElement>()

        override fun getNestedTypes(): List<JavaTypeElement>
        {
            return types
        }

        override fun nestedClass(name: String, scope: Scope, init: JavaClass.() -> Unit): JavaClass
        {
            val clazz = JavaClass.create(baseName.resolveNested(name), scope, init)
            types += clazz
            return clazz
        }
    }
}