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

import galuzzi.kodegen.java.JavaMethod
import galuzzi.kodegen.java.Scope
import galuzzi.kodegen.java.Type
import galuzzi.kodegen.java.template.MethodTemplate

/**
 * A code element that may contain methods.
 */
interface MethodHolder
{
    fun getMethods(): List<JavaMethod>

    fun method(name: String,
               returnType: Type = Type.void,
               scope: Scope = Scope.PUBLIC,
               override: Boolean = false,
               init: JavaMethod.() -> Unit = {}): JavaMethod

    fun method(template: MethodTemplate): JavaMethod

    class Impl(private val type: Type) : MethodHolder
    {
        private val methods = mutableListOf<JavaMethod>()

        override fun getMethods(): List<JavaMethod>
        {
            return methods
        }

        override fun method(name: String, returnType: Type, scope: Scope, override: Boolean, init: JavaMethod.() -> Unit): JavaMethod
        {
            val method = JavaMethod(scope, returnType, name).apply(init)
            if (override)
            {
                method.annotation(Type.Override)
            }
            methods += method
            return method
        }

        override fun method(template: MethodTemplate): JavaMethod
        {
            val method = template.build(type)
            methods += method
            return method
        }
    }
}