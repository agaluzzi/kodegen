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

package galuzzi.kodegen.java

import galuzzi.kodegen.CodeElement
import galuzzi.kodegen.CodeEmbeddable
import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.java.support.*
import galuzzi.kodegen.join

/**
 * A generated Java enum.
 */
class JavaEnum private constructor(override val name: TypeName,
                                   override val type: ObjectType,
                                   val scope: Scope) : JavaTypeElement,
                                                       Annotated by Annotated.Impl(),
                                                       Documented by Documented.Impl(),
                                                       FieldHolder by FieldHolder.Impl(),
                                                       MethodHolder by MethodHolder.Impl(type),
                                                       Constructable by Constructable.Impl(type),
                                                       TypeContainer by TypeContainer.Impl(name)
{
    companion object
    {
        fun create(name: TypeName,
                   scope: Scope = Scope.PUBLIC,
                   init: JavaEnum.() -> Unit = {}): JavaEnum
        {
            return JavaEnum(name, Type.from(name), scope).apply(init)
        }

        fun create(name: String,
                   pkg: JavaPackage,
                   scope: Scope = Scope.PUBLIC,
                   init: JavaEnum.() -> Unit = {}): JavaEnum
        {
            return create(TypeName.create(name, pkg), scope, init)
        }
    }

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