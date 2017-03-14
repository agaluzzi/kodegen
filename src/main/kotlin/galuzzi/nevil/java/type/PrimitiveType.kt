package galuzzi.nevil.java.type

import galuzzi.nevil.java.JavaWriter

/**
 * @author Aaron Galuzzi (2/12/2017)
 */
class PrimitiveType(val keyword:String):TypeRef
{
    override fun isPrimitive():Boolean = true

    override fun writeTo(out:JavaWriter)
    {
        out += keyword
    }
}