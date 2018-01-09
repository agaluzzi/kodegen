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
import galuzzi.codegen.CodeGen
import galuzzi.codegen.CodeGenScope
import galuzzi.codegen.java.support.Annotated
import galuzzi.codegen.java.support.BodyHolder
import galuzzi.codegen.java.support.Documented
import galuzzi.codegen.java.support.ParamHolder
import galuzzi.codegen.join


/**
 * @author Aaron Galuzzi (2/11/2017)
 */
@CodeGenScope
class JavaConstructor internal constructor(val type: Type,
                                           val scope: Scope) : CodeElement,
                                                               Annotated by Annotated.Impl(),
                                                               Documented by Documented.Impl(),
                                                               ParamHolder by ParamHolder.Impl(),
                                                               BodyHolder by BodyHolder.Impl()
{
    override fun build(): CodeGen
    {
        addParamJavadoc(getParams(), getJavadoc())

        return {
            pad()
            +getJavadoc()
            +getAnnotations()
            newline()
            +scope
            +type.simpleName()
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

