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

package galuzzi.codegen.java.support

import galuzzi.codegen.java.JavaAnnotation
import galuzzi.codegen.java.Type

/**
 * TODO...
 */
interface Annotated
{
    fun annotation(type: Type, init: JavaAnnotation.() -> Unit = { }): JavaAnnotation

    fun getAnnotations(): List<JavaAnnotation>

    fun hasAnnotations(): Boolean
    {
        return getAnnotations().isNotEmpty()
    }

    class Impl : Annotated
    {
        private val annotations = mutableListOf<JavaAnnotation>()

        override fun annotation(type: Type, init: JavaAnnotation.() -> Unit): JavaAnnotation
        {
            val annotation = JavaAnnotation(type)
            init.invoke(annotation)
            annotations += annotation
            return annotation
        }

        override fun getAnnotations(): List<JavaAnnotation>
        {
            return annotations
        }
    }
}