package galuzzi.nevil.java.type

import galuzzi.nevil.java.JavaElement
import java.lang.Byte
import java.lang.Double
import java.lang.Float
import java.lang.Long
import java.lang.Short
import java.lang.String
import java.util.*
import java.util.List
import java.util.Map
import java.util.Set
import kotlin.reflect.KClass

/**
 * @author Aaron Galuzzi (2/12/2017)
 */

interface TypeRef:JavaElement
{
    fun isPrimitive():Boolean
}

val boolean = PrimitiveType("boolean")
val byte = PrimitiveType("byte")
val char = PrimitiveType("char")
val short = PrimitiveType("short")
val int = PrimitiveType("int")
val long = PrimitiveType("long")
val float = PrimitiveType("float")
val double = PrimitiveType("double")
val void = PrimitiveType("void")

val BOOLEAN = type(java.lang.Boolean::class)
val BYTE = type(Byte::class)
val CHAR = type(Character::class)
val SHORT = type(Short::class)
val INTEGER = type(Integer::class)
val LONG = type(Long::class)
val FLOAT = type(Float::class)
val DOUBLE = type(Double::class)

val OBJECT:ObjectType = type(Object::class)
val STRING:ObjectType = type(String::class)
val LIST:ObjectType = type(List::class)
val SET:ObjectType = type(Set::class)
val MAP:ObjectType = type(Map::class)
val OPTIONAL:ObjectType = type(Optional::class)

fun type(clazz:KClass<*>):ObjectType
{
    return type(clazz.java)
}

fun type(clazz:Class<*>):ObjectType
{
    return SimpleType(clazz.name, clazz.simpleName)
}
