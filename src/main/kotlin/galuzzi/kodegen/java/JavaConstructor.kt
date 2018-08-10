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
import galuzzi.kodegen.java.support.*
import galuzzi.kodegen.join


/**
 * A constructor, which is similar to a [JavaMethod], but has no modifiers or return type.
 */
@CodeGenScope
class JavaConstructor internal constructor(val type: Type,
                                           val scope: Scope) : CodeElement,
                                                               Annotated by Annotated.Impl(),
                                                               Documented by Documented.Impl(),
                                                               ParamHolder by ParamHolder.Impl(),
                                                               BodyHolder by BodyHolder.Impl(),
                                                               Thrower by Thrower.Impl()
{
    override fun build(): CodeGen
    {
        addParamDoc(this)
        addThrowsDoc(this)

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
            +getThrows()
            +block {
                +nullChecks(getParams())
                +getBody()
            }
        }
    }
}

