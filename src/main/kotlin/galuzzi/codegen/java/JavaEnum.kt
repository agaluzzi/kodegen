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
import galuzzi.codegen.java.support.*
import galuzzi.codegen.join

/**
 * TODO...
 */
class JavaEnum private constructor(override val name: TypeName,
                                   val scope: Scope) : JavaTypeElement,
                                                       Annotated by Annotated.Impl(),
                                                       Documented by Documented.Impl(),
                                                       FieldHolder by FieldHolder.Impl(),
                                                       MethodHolder by MethodHolder.Impl(),
                                                       Constructable by Constructable.Impl(name),
                                                       TypeContainer by TypeContainer.Impl(name)
{
    companion object
    {
        fun create(name: String, pkg: JavaPackage, scope: Scope = Scope.PUBLIC, init: JavaEnum.() -> Unit = {}): JavaEnum
        {
            return JavaEnum(TypeName.create(name, pkg), scope).apply(init)
        }

        fun create(name: TypeName, scope: Scope = Scope.PUBLIC, init: JavaEnum.() -> Unit = {}): JavaEnum
        {
            return JavaEnum(name, scope).apply(init)
        }
    }

    val type: ObjectType = Type.from(name)
    private val constants = ArrayList<Constant>()

    fun constant(name: String, init: Constant.() -> Unit = {}): Constant
    {
        val constant = Constant(name).apply(init)
        constants += constant
        return constant
    }

    override fun build(): CodeGen
    {
        return {
            +getJavadoc()
            +getAnnotations()
            newline()
            +scope
            +"enum "
            +name.simpleName()
            +block {
                +join(constants, ",\n")
                +";"
                pad()
                +getFields()
                +getConstructors()
                +getMethods()
                +getNestedTypes()
            }
        }
    }

    class Constant internal constructor(val name: String) : CodeElement,
                                                            Documented by Documented.Impl(),
                                                            CodeEmbeddable
    {
        private var params: Array<out CodeGen> = emptyArray()

        fun params(vararg params: CodeGen)
        {
            this.params = params
        }

        override fun build(): CodeGen
        {
            return {
                +getJavadoc()
                newline()
                +name
                if (params.isNotEmpty())
                {
                    +'('
                    +join(params, ", ")
                    +')'
                }
            }
        }

        /**
         * Returns the enum constant name.
         */
        override fun toString(): String
        {
            return name
        }
    }
}