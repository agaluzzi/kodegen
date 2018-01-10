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

package galuzzi.kodegen.java.template

import galuzzi.kodegen.CodeGen
import galuzzi.kodegen.java.*
import galuzzi.kodegen.java.support.FieldHolder

/**
 * A template for building a method.
 */
interface MethodTemplate
{
    fun build(type: Type): JavaMethod
}

private class ToString(val init: JavaMethod.(Type) -> Unit) : MethodTemplate
{
    override fun build(type: Type): JavaMethod
    {
        return JavaMethod(Scope.PUBLIC, Type.String, "toString").apply {
            annotation(Type.Override)
            init.invoke(this, type)
        }
    }
}

private class Equals(val init: JavaMethod.(Type) -> Unit) : MethodTemplate
{
    override fun build(type: Type): JavaMethod
    {
        return JavaMethod(Scope.PUBLIC, Type.boolean, "equals").apply {
            annotation(Type.Override)
            param("o", Type.Object, allowNull = true)
            init.invoke(this, type)
        }
    }
}

private class HashCode(val init: JavaMethod.(Type) -> Unit) : MethodTemplate
{
    override fun build(type: Type): JavaMethod
    {
        return JavaMethod(Scope.PUBLIC, Type.int, "hashCode").apply {
            annotation(Type.Override)
            init.invoke(this, type)
        }
    }
}

fun toStringWithBody(body: CodeGen): MethodTemplate
{
    return ToString { body(body) }
}

fun equalsWithBody(body: CodeGen): MethodTemplate
{
    return Equals { body(body) }
}

fun hashCodeWithBody(body: CodeGen): MethodTemplate
{
    return HashCode { body(body) }
}

fun toStringBasic(target: FieldHolder): MethodTemplate
{
    return ToString { type ->
        body {
            +"return \"${type.simpleName()}[\" +\n"
            target.getFields().forEachIndexed { i, field ->
                val label = if (i > 0) ", ${field.name}" else field.name
                +"\"$label=\" + $field +\n"
            }
            +"']';\n"
        }
    }
}

fun equalsBasic(target: FieldHolder): MethodTemplate
{
    return Equals { type ->
        body {
            +"if (o == null) return false;\n"
            +"if (o == this) return true;\n"
            +"if (!(o instanceof "
            +type
            +")) return false;\nfinal "
            +type
            +" other = ("
            +type
            +") o;\n"
            target.getFields().forEach { f ->
                if (f.type.isPrimitive())
                {
                    +"if (${f.name} != other.${f.name}) return false;\n"
                }
                else
                {
                    +"if (!"
                    +Type.Objects
                    +".equals(${f.name}, other.${f.name})) return false;\n"
                }
            }
            +"return true;\n"
        }

    }
}

fun hashCodeBasic(target: FieldHolder): MethodTemplate
{
    return HashCode {
        body {
            +"int result = 0;\n"
            for (f in target.getFields())
            {
                when
                {
                    f.type == Type.boolean -> +"result = 31 * result + (${f.name} ? 1 : 0);\n"
                    f.type == Type.long    -> +"result = 31 * result + (int) (${f.name} ^ (${f.name} >>> 32));\n"
                    f.type.isPrimitive()   -> +"result = 31 * result + ${f.name};\n"
                    else                   -> +"result = 31 * result + (${f.name} != null ? ${f.name}.hashCode() : 0);\n"
                }
            }
            +"return result;\n"
        }
    }
}

private class Getter(val field: JavaField,
                     val scope: Scope,
                     val useEmptyCollections: Boolean) : MethodTemplate
{
    override fun build(type: Type): JavaMethod
    {
        val prefix = if (field.type.toObjectType() == Type.Boolean) "is" else "get"

        return JavaMethod(scope, field.type, prefix + field.publicName()).apply {
            body {
                if (useEmptyCollections)
                {
                    val collectionType = field.collectionType()
                    if (collectionType != null)
                    {
                        +"if (${field.name} == null) return ${Type.Collections}.empty$collectionType();\n"
                    }
                }
                +stmt("return ${field.name}")
            }
            javadoc {
                if (field.description.isNotBlank())
                {
                    returns(field.description)
                }
            }
        }
    }
}

private class Setter(val field: JavaField,
                     val scope: Scope,
                     val allowNull: Boolean,
                     val returnSelf: Boolean) : MethodTemplate
{
    override fun build(type: Type): JavaMethod
    {
        val returnType = if (returnSelf) type else Type.void

        return JavaMethod(scope, returnType, "set${field.publicName()}").apply {
            val param = param(field.name, field.type, allowNull, field.description)
            body {
                +"$field = $param;\n"
                if (returnSelf)
                {
                    +"return this;\n"
                }
            }
            javadoc {
                if (returnSelf)
                {
                    returns("this ${type.simpleName()} (for method chaining)")
                }
            }
        }
    }
}

private fun JavaField.publicName(): String = name.trim('_').capitalize()

private fun JavaField.collectionType(): String?
{
    return when (type.baseType())
    {
        Type.List -> "List"
        Type.Set  -> "Set"
        Type.Map  -> "Map"
        else      -> null
    }
}

fun getter(field: JavaField,
           scope: Scope = Scope.PUBLIC,
           useEmptyCollections: Boolean = false): MethodTemplate
{
    return Getter(field, scope, useEmptyCollections)
}

fun setter(field: JavaField,
           scope: Scope = Scope.PUBLIC,
           allowNull: Boolean = true,
           returnSelf: Boolean = false): MethodTemplate
{
    return Setter(field, scope, allowNull, returnSelf)
}