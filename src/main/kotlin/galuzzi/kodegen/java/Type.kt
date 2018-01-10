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

import galuzzi.kodegen.CodeEmbeddable
import kotlin.reflect.KClass

interface Type : CodeEmbeddable
{
    fun isPrimitive(): Boolean

    fun simpleName(): String

    fun toObjectType(): ObjectType

    fun baseType(): Type = this

    fun array(): Type
    {
        return ArrayType(this)
    }

    companion object
    {
        // Primitive wrappers
        val Boolean = from(java.lang.Boolean::class)
        val Byte = from(java.lang.Byte::class)
        val Character = from(java.lang.Character::class)
        val Short = from(java.lang.Short::class)
        val Integer = from(java.lang.Integer::class)
        val Long = from(java.lang.Long::class)
        val Float = from(java.lang.Float::class)
        val Double = from(java.lang.Double::class)
        val Void = from(java.lang.Void::class)

        // Primitive types
        val boolean = PrimitiveType("boolean", Boolean)
        val byte = PrimitiveType("byte", Byte)
        val char = PrimitiveType("char", Character)
        val short = PrimitiveType("short", Short)
        val int = PrimitiveType("int", Integer)
        val long = PrimitiveType("long", Long)
        val float = PrimitiveType("float", Float)
        val double = PrimitiveType("double", Double)
        val void = PrimitiveType("void", Void)

        // Basic building blocks
        val Object = from(java.lang.Object::class)
        val Number = from(java.lang.Number::class)
        val String = from(java.lang.String::class)
        val Optional = from(java.util.Optional::class)

        // Exceptions
        val Exception = from(java.lang.Exception::class)
        val RuntimeException = from(java.lang.RuntimeException::class)
        val NullPointerException = from(java.lang.NullPointerException::class)
        val IllegalArgumentException = from(java.lang.IllegalArgumentException::class)
        val IllegalStateException = from(java.lang.IllegalStateException::class)

        // Annotations
        val Override = from(java.lang.Override::class)

        // Utilities
        val Objects = from(java.util.Objects::class)
        val Collections = from(java.util.Collections::class)

        // Collections
        val List = from(java.util.List::class)
        val Set = from(java.util.Set::class)
        val Map = from(java.util.Map::class)

        fun from(clazz: KClass<*>): ObjectType
        {
            return from(clazz.java)
        }

        fun from(clazz: Class<*>): ObjectType
        {
            return from(TypeName.create(clazz))
        }

        fun from(name: TypeName): ObjectType
        {
            return BasicType(name)
        }

        fun from(className: String, packageName: String): ObjectType
        {
            return from(TypeName.create(className, packageName))
        }

        fun from(fqcn: String): ObjectType
        {
            return from(TypeName.create(fqcn))
        }
    }
}

data class PrimitiveType internal constructor(val keyword: String, val wrapperType: ObjectType) : Type
{
    override fun isPrimitive(): Boolean = true

    override fun simpleName(): String = keyword

    override fun toObjectType(): ObjectType = wrapperType

    override fun toString(): String
    {
        return keyword
    }
}

interface ObjectType : Type
{
    override fun isPrimitive(): Boolean = false

    override fun toObjectType(): ObjectType = this

    fun of(vararg params: ObjectType): ObjectType
    {
        return ParameterizedType(this, *params)
    }

    fun nested(name: String): ObjectType
    {
        return NestedType(this, name)
    }
}

//**********************************************************************
// ObjectTypes:
//**********************************************************************

data class BasicType(val name: TypeName) : ObjectType, CodeEmbeddable
{
    override fun simpleName(): String = name.simpleName()

    override fun toString(): String
    {
        return Imports.get().import(name)
    }
}

class ParameterizedType(val base: ObjectType, vararg parameters: ObjectType) : ObjectType
{
    private val params: Array<out ObjectType> = parameters

    override fun simpleName(): String
    {
        return "${base.simpleName()}<${params.joinToString { it.simpleName() }}>"
    }

    fun param(i: Int): ObjectType
    {
        return params[i]
    }

    override fun baseType(): Type = base

    override fun toString(): String
    {
        val sb = StringBuilder("$base<")
        params.forEachIndexed { i, type ->
            if (i > 0) sb.append(',')
            sb.append(type.toString())
        }
        return sb.append('>').toString()
    }
}

data class NestedType(val parent: ObjectType, val name: String) : ObjectType
{
    override fun simpleName(): String = name

    override fun toString(): String
    {
        return "$parent.$name"
    }
}

data class ArrayType(val itemType: Type) : ObjectType
{
    override fun simpleName(): String = "${itemType.simpleName()}[]"

    override fun toString(): String
    {
        return "$itemType[]"
    }
}



