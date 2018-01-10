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

class TypeName private constructor(val className: String, val pkg: JavaPackage)
{
    val fqcn: String = if (pkg.isDefault())
    {
        className
    }
    else
    {
        "${pkg.name}.$className"
    }

    fun simpleName(): String
    {
        return className.substringAfterLast('.')
    }

    fun resolveNested(name: String): TypeName
    {
        if (name.isBlank()) throw IllegalArgumentException("name cannot be blank")
        return TypeName("$className.$name", pkg)
    }

    fun isNestedChildOf(parent: TypeName): Boolean
    {
        return this != parent &&
               pkg == parent.pkg &&
               className.contains('.') &&
               className.startsWith(parent.className)
    }

    override fun toString(): String
    {
        return fqcn
    }

    companion object
    {
        fun create(className: String, pkg: JavaPackage): TypeName
        {
            if (className.isBlank()) throw IllegalArgumentException("className cannot be blank")
            return TypeName(className.trim(), pkg)
        }

        fun create(className: String, packageName: String): TypeName
        {
            return create(className, JavaPackage(packageName))
        }

        fun create(clazz: Class<*>): TypeName
        {
            val outer = clazz.declaringClass
            val simpleName = clazz.simpleName

            return if (outer == null)
            {
                val packageName = clazz.`package`?.name ?: ""
                TypeName(simpleName, JavaPackage(packageName))
            }
            else
            {
                val outerType = create(outer)
                TypeName("${outerType.className}.$simpleName", outerType.pkg)
            }
        }

        fun create(fqcn: String): TypeName
        {
            return if (fqcn.contains('.'))
            {
                val packageName = fqcn.substringBeforeLast('.')
                val className = fqcn.substringAfterLast('.')
                return create(className, packageName)
            }
            else
            {
                TypeName(fqcn, JavaPackage.DEFAULT)
            }
        }
    }
}