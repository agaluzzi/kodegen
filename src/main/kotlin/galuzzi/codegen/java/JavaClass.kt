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

import galuzzi.codegen.CodeGen
import galuzzi.codegen.CodeGenScope
import galuzzi.codegen.java.support.*


/**
 * @author Aaron Galuzzi (2/11/2017)
 */
@CodeGenScope
class JavaClass private constructor(override val name: TypeName,
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
        fun create(name: String,
                   pkg: JavaPackage,
                   scope: Scope = Scope.PUBLIC,
                   init: JavaClass.() -> Unit = {}): JavaClass
        {
            return JavaClass(TypeName.create(name, pkg), scope).apply(init)
        }

        fun create(name: TypeName,
                   scope: Scope = Scope.PUBLIC,
                   init: JavaClass.() -> Unit = {}): JavaClass
        {
            return JavaClass(name, scope).apply(init)
        }
    }

    val type: ObjectType = Type.from(name)

    private val modifiers = Modifiers()
    private val extends = mutableListOf<Type>()
    private val implements = mutableListOf<Type>()

    fun static()
    {
        modifiers += Modifier.STATIC
    }

    fun final()
    {
        modifiers += Modifier.FINAL
    }

    fun abstract()
    {
        modifiers += Modifier.ABSTRACT
    }

    fun extends(vararg types: Type)
    {
        extends.addAll(types)
    }

    fun implements(vararg types: Type)
    {
        implements.addAll(types)
    }

    override fun build(): CodeGen
    {
        return {
            pad()
            +getJavadoc()
            +getAnnotations()
            newline()
            +scope
            +modifiers
            +"class ${name.simpleName()}\n"

            if (extends.isNotEmpty())
            {
                +" extends "
                +extends.joinToString(", ")
            }

            if (implements.isNotEmpty())
            {
                +" implements "
                +implements.joinToString(", ")
            }

            +block {
                +getFields()
                +getConstructors()
                +getMethods()
                +getNestedTypes()
            }
        }
    }
}