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

package galuzzi.codegen.java

import galuzzi.codegen.CodeElement
import galuzzi.codegen.CodeEmbeddable
import galuzzi.codegen.CodeGen
import galuzzi.codegen.CodeGenScope
import galuzzi.codegen.java.support.Annotated
import galuzzi.codegen.java.support.Documented

/**
 * @author Aaron Galuzzi (3/9/2017)
 */
@CodeGenScope
open class JavaField internal constructor(val scope: Scope,
                                          val type: Type,
                                          val name: String,
                                          val description: String) : CodeElement,
                                                                     Annotated by Annotated.Impl(),
                                                                     Documented by Documented.Impl(),
                                                                     CodeEmbeddable
{
    private val modifiers = Modifiers()
    private var value: CodeGen? = null

    fun static()
    {
        modifiers += Modifier.STATIC
    }

    fun final()
    {
        modifiers += Modifier.FINAL
    }

    fun volatile()
    {
        modifiers += Modifier.VOLATILE
    }

    fun value(value: CodeGen)
    {
        this.value = value
    }

    override fun build(): CodeGen
    {
        return {
            +getJavadoc()
            +getAnnotations()
            newline()
            +scope
            +modifiers
            +type
            +' '
            +name
            if (value != null)
            {
                +" = "
                +value!!
            }
            +";\n"
        }
    }

    fun isPrimitive(): Boolean = type.isPrimitive()

    /**
     * @return a reference to this field, in the form: `this.{name}`
     */
    override fun toString(): String = "this." + name
}