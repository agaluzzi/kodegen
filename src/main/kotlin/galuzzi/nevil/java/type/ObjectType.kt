package galuzzi.nevil.java.type

/**
 * @author Aaron Galuzzi (3/9/2017)
 */
interface ObjectType:TypeRef
{
    override fun isPrimitive():Boolean = false

    fun of(vararg params:TypeRef):TypeRef
    {
        return ParameterizedType(this, *params)
    }
}



