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

/**
 * A specialized (thread-local) code element that manages the imports and typenames for a Java file.
 */
class Imports(private val context: TypeName) : CodeElement
{
    companion object
    {
        private val THREAD_LOCAL: ThreadLocal<Imports> = ThreadLocal()

        fun get(): Imports
        {
            return THREAD_LOCAL.get() ?: throw IllegalStateException("Import context is not set")
        }

        fun setThreadLocal(imports: Imports)
        {
            THREAD_LOCAL.set(imports)
        }
    }

    private val classMap = mutableMapOf<String, String>()

    fun import(type: TypeName): String
    {
        if (type.isNestedChildOf(context))
        {
            return type.simpleName()
        }

        // Skip import for types that don't need it:
        if (type.pkg.isDefault() ||          // in the default package
            type.pkg == context.pkg ||       // in the same package
            type.pkg.name == "java.lang")    // in java.lang
        {
            return type.className
        }

        val mappedFQCN = classMap.getOrPut(type.className, type::fqcn)
        return if (mappedFQCN == type.fqcn) type.className else type.fqcn
    }

    override fun build(): CodeGen
    {
        return {
            classMap.values.sorted().forEach { fqcn -> +stmt("import $fqcn") }
        }
    }
}