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

package galuzzi.codegen.java.template

import galuzzi.codegen.CodeGen
import galuzzi.codegen.java.*
import galuzzi.codegen.java.stmt
import java.util.*

/**
 * TODO...
 */
interface MethodTemplate
{
    fun buildMethod(): JavaMethod
}

private object Types
{
    val Objects = Type.from(Objects::class)
    val Collections = Type.from(Collections::class)
}

class ToString(private val body: CodeGen) : MethodTemplate
{
    override fun buildMethod(): JavaMethod
    {
        return JavaMethod(Scope.PUBLIC, Type.String, "toString").apply {
            annotation(Type.Override)
            body(body)
        }
    }
}

class Equals(private val type: Type,
             private val body: CodeGen) : MethodTemplate
{
    override fun buildMethod(): JavaMethod
    {
        return JavaMethod(Scope.PUBLIC, Type.boolean, "equals").apply {
            annotation(Type.Override)
            param("o", Type.Object, allowNull = true)
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
                body.invoke(this)
            }
        }
    }
}

fun standardEquals(type: Type, fields: List<JavaField>): MethodTemplate
{
    return Equals(type) {
        fields.forEach { f ->
            if (f.type.isPrimitive())
            {
                +"if (${f.name} != other.${f.name}) return false;\n"
            }
            else
            {
                +"if (!"
                +Types.Objects
                +".equals(${f.name}, other.${f.name})) return false;\n"
            }
        }
        +"return true;"
    }
}

class HashCode(private val body: CodeGen) : MethodTemplate
{
    override fun buildMethod(): JavaMethod
    {
        return JavaMethod(Scope.PUBLIC, Type.int, "hashCode").apply {
            annotation(Type.Override)
            body(body)
        }
    }
}

fun standardHashCode(fields: List<JavaField>): MethodTemplate
{
    return HashCode {
        +"int result = 0;\n"
        fields.forEach { f ->
            when
            {
                f.type == Type.boolean -> +"result = 31 * result + (${f.name} ? 1 : 0);\n"
                f.type == Type.long    -> +"result = 31 * result + (int) (${f.name} ^ (${f.name} >>> 32));\n"
                f.type.isPrimitive()   -> +"result = 31 * result + ${f.name};\n"
                else                   -> +"result = 31 * result + (${f.name} != null ? ${f.name}.hashCode() : 0);\n"
            }
        }
        +"return result;"
    }
}

class Getter(private val field: JavaField,
             private val scope: Scope = Scope.PUBLIC,
             private val nonNullCollections: Boolean = false) : MethodTemplate
{
    override fun buildMethod(): JavaMethod
    {
        val type = field.type
        val prefix = if (type.toObjectType() == Type.Boolean) "is" else "get"

        return JavaMethod(scope, type, prefix + publicName(field)).apply {
            body {
                if (nonNullCollections && type is ParameterizedType && (type.base == Type.List || type.base == Type.Set))
                {
                    +"if (${field.name} == null) return "
                    +Types.Collections
                    +".empty${type.base.simpleName()}();\n"
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

class Setter(private val field: JavaField,
             private val scope: Scope = Scope.PUBLIC,
             private val returnType: Type = Type.void) : MethodTemplate
{
    override fun buildMethod(): JavaMethod
    {
        return JavaMethod(scope, returnType, "set${publicName(field)}").apply {
            param(field.name, field.type, field.nullable, field.description)
            body {
                +stmt("this.${field.name} = ${field.name}")
                if (this@Setter.returnType != Type.void)
                {
                    +"return this;"
                }
            }
            javadoc {
                if (this@Setter.returnType != Type.void)
                {
                    returns("this object (for method chaining)")
                }
            }
        }
    }
}

private fun publicName(field: JavaField): String
{
    return field.name.trim('_').capitalize()
}