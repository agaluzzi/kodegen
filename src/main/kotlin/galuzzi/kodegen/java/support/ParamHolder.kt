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

import galuzzi.kodegen.java.Param
import galuzzi.kodegen.java.Type
import java.util.*

/**
 * TODO...
 */
interface ParamHolder
{
    fun getParams(): List<Param>

    fun param(name: String,
              type: Type,
              allowNull: Boolean = true,
              description: String = "",
              varargs: Boolean = false,
              init: Param.() -> Unit = {}): Param

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
    }
}