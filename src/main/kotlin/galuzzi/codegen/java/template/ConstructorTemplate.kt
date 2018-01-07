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

package galuzzi.codegen.java.template

import galuzzi.codegen.java.*

/**
 * TODO...
 */
interface ConstructorTemplate
{
    fun buildConstructor(typeName: TypeName): JavaConstructor
}

class BasicConstructor(private val fields: List<JavaField>,
                       private val scope: Scope = Scope.PUBLIC) : ConstructorTemplate
{
    override fun buildConstructor(typeName: TypeName): JavaConstructor
    {
        return JavaConstructor(typeName, scope).apply {
            fields.forEach { f ->
                param(f.name, f.type, f.nullable, f.description)
            }
            body {
                fields.forEach { f ->
                    +stmt("$f = ${f.name}")
                }
            }
        }
    }
}