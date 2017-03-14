package galuzzi.nevil.java.type

import galuzzi.nevil.java.JavaWriter

/**
 * @author Aaron Galuzzi (3/13/2017)
 */
class NestedType(val name:String,
                 val parent:ObjectType):ObjectType
{
    override fun writeTo(out:JavaWriter)
    {
        parent.writeTo(out)
        out += ".$name"
    }
}