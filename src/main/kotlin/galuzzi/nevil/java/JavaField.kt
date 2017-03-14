package galuzzi.nevil.java

import galuzzi.nevil.GenScope
import galuzzi.nevil.java.type.TypeRef
import java.util.*

/**
 * @author Aaron Galuzzi (3/9/2017)
 */
@GenScope
class JavaField(val name:String,
                val scope:Scope,
                val type:TypeRef):JavaElement
{
    private val modifiers = LinkedHashSet<Modifier>()
    private val javadoc = Javadoc()

    fun static()
    {
        modifiers += Modifier.STATIC
    }

    fun final()
    {
        modifiers += Modifier.FINAL
    }

    fun volatile()
    {
        modifiers += Modifier.VOLATILE
    }

    fun javadoc(init:Javadoc.() -> Unit)
    {
        init.invoke(javadoc)
    }

    override fun writeTo(out:JavaWriter)
    {
        out += javadoc
        out += scope
        out += modifiers
        out += type
        out += " $name;\n"
    }

    override fun toString():String = name
}