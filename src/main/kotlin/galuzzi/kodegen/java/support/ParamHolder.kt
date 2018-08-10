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

import galuzzi.kodegen.java.JavaField
import galuzzi.kodegen.java.Param
import galuzzi.kodegen.java.Type
import java.util.*

/**
 * A code element that may contain parameters. (i.e. a method or constructor)
 */
interface ParamHolder
{
    /**
     * @return the list of all parameters that have been added
     */
    fun getParams(): List<Param>

    /**
     * Adds a parameter.
     */
    fun param(name: String,
              type: Type,
              allowNull: Boolean = true,
              description: String = "",
              varargs: Boolean = false,
              init: Param.() -> Unit = {}): Param

    /**
     * Adds a parameter that corresponds to a field.
     * The name, type, and description of the parameter will be equal to that of the field.
     *
     * @param field the field from which to obtain the name, type, and description
     * @param allowNull specifies whether an argument may be null -- if false, a null check will be added to the method body
     */
    fun param(field: JavaField,
              allowNull: Boolean = true): Param
    {
        return param(field.name, field.type, allowNull, field.description)
    }

    fun addParamDoc(target: Documented)

    class Impl : ParamHolder
    {
        private val params = ArrayList<Param>()

        override fun getParams(): List<Param>
        {
            return params
        }

        override fun param(name: String, type: Type, allowNull: Boolean, description: String, varargs: Boolean, init: Param.() -> Unit): Param
        {
            val param = Param(type, name, allowNull, description, varargs).apply(init)
            params += param
            return param
        }

        override fun addParamDoc(target: Documented)
        {
            target.javadoc {
                params.filter { it.description.isNotBlank() }
                        .forEach { param(it.name, it.description) }
            }
        }
    }
}