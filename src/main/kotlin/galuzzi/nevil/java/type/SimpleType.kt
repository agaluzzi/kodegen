package galuzzi.nevil.java.type

import galuzzi.nevil.java.JavaWriter

/**
 * @author Aaron Galuzzi (3/13/2017)
 */
class SimpleType(val fqcn:String,
                 val shortName:String):ObjectType
{
    override fun writeTo(out:JavaWriter)
    {
        if (out.import(shortName, fqcn))
        {
            out += shortName
        }
        else
        {
            out += fqcn
        }
    }
}