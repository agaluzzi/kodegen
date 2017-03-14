package galuzzi.nevil.java.type

import galuzzi.nevil.java.JavaWriter

/**
 * @author Aaron Galuzzi (3/13/2017)
 */
class LocalType(val name:String):ObjectType
{
    override fun writeTo(out:JavaWriter)
    {
        out += name
    }
}