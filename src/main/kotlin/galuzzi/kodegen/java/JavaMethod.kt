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
import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.CodeGenScope
import galuzzi.kodegen.java.support.Annotated
import galuzzi.kodegen.java.support.BodyHolder
import galuzzi.kodegen.java.support.Documented
import galuzzi.kodegen.java.support.ParamHolder
import galuzzi.kodegen.join


/**
 * @author Aaron Galuzzi (2/11/2017)
 */
@CodeGenScope
class JavaMethod internal constructor(val scope: Scope,
                                      val returnType: Type,
                                      val name: String) : CodeElement,
                                                          Annotated by Annotated.Impl(),
                                                          Documented by Documented.Impl(),
                                                          ParamHolder by ParamHolder.Impl(),
                                                          BodyHolder by BodyHolder.Impl()
{
    private val modifiers = Modifiers()

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

    fun synchronized()
    {
        modifiers += Modifier.SYNCHRONIZED
    }

    override fun build(): CodeGen
    {
        addParamJavadoc(getParams(), getJavadoc())

        return {
            pad()
            +getJavadoc()
            +getAnnotations()
            newline()
            +scope
            +modifiers
            +returnType
            +' '
            +name
            +'('
            +join(getParams(), ", ")
            +')'
            +block {
                +nullChecks(getParams())
                +getBody()
            }
        }
    }
}

internal fun addParamJavadoc(params: List<Param>, javadoc: Javadoc)
{
    params.filter { it.description.isNotBlank() }
            .forEach { javadoc.param(it.name, it.description) }
}

internal fun nullChecks(params: List<Param>): CodeGen
{
    return {
        params.filter { !it.allowNull && !it.type.isPrimitive() }
                .forEach { p ->
                    newline()
                    +Type.Objects
                    +".requireNonNull(${p.name}, \"${p.name}\");\n"
                }
    }
}

