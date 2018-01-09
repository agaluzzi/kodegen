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

import galuzzi.codegen.java.JavaConstructor
import galuzzi.codegen.java.JavaField
import galuzzi.codegen.java.Scope
import galuzzi.codegen.java.Type
import galuzzi.codegen.java.support.FieldHolder

/**
 * TODO...
 */
interface ConstructorTemplate
{
    fun build(type: Type): JavaConstructor
}

private class BasicConstructor(val scope: Scope,
                               val fields: List<JavaField>,
                               val allowNulls: Boolean) : ConstructorTemplate
{
    override fun build(type: Type): JavaConstructor
    {
        return JavaConstructor(type, scope).apply {
            javadoc {
                +"Constructs a new ${type.simpleName()}."
            }

            // Add parameter for each field
            fields.forEach { field ->
                param(field.name, field.type, allowNulls, field.description)
            }

            // Add a statement to set each field
            body {
                fields.forEach { field ->
                    +"$field = ${field.name};\n"
                }
            }
        }
    }
}

fun basicConstructor(fields: List<JavaField>,
                     scope: Scope = Scope.PUBLIC,
                     allowNulls: Boolean = true): ConstructorTemplate
{
    return BasicConstructor(scope, fields, allowNulls)
}

fun basicConstructor(target: FieldHolder,
                     scope: Scope = Scope.PUBLIC,
                     allowNulls: Boolean = true): ConstructorTemplate
{
    return BasicConstructor(scope, target.getFields(), allowNulls)
}