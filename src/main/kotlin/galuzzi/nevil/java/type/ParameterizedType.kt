package galuzzi.nevil.java.type

import galuzzi.nevil.java.JavaWriter

/**
 * @author Aaron Galuzzi (3/13/2017)
 */
class ParameterizedType(val base:TypeRef,
                        vararg parameters:TypeRef):ObjectType
{
    private val params:Array<out TypeRef> = parameters

    override fun writeTo(out:JavaWriter)
    {
        out += base
        out += '<'
        params.forEachIndexed { i, type ->
            if (i > 0)
            {
                out += ','
            }
            out += type
        }
        out += '>'
    }
}