package galuzzi.nevil.java

import galuzzi.nevil.GenScope
import galuzzi.nevil.java.type.PrimitiveType
import galuzzi.nevil.java.type.TypeRef
import java.util.*


/**
 * @author Aaron Galuzzi (2/11/2017)
 */
@GenScope
open class JavaMethod(val name:String,
                      val scope:Scope,
                      val returnType:TypeRef):JavaElement
{
    private val modifiers = LinkedHashSet<Modifier>()
    private val params = ArrayList<MethodParam>()
    private val body = CodeBlock()
    private val javadoc = Javadoc()

    fun static()
    {
        modifiers += Modifier.STATIC
    }

    fun final()
    {
        modifiers += Modifier.FINAL
    }

    fun abstract()
    {
        modifiers += Modifier.ABSTRACT
    }

    fun synchronized()
    {
        modifiers += Modifier.SYNCHRONIZED
    }

    fun javadoc(init:Javadoc.() -> Unit)
    {
        init.invoke(javadoc)
    }

    fun param(type:TypeRef, name:String, nullability:Nullability, description:String = ""):MethodParam
    {
        val param = MethodParam(type, name, nullability, description)
        params += param

        var d = description
        if (nullability == Nullability.ALLOW_NULL) d += " (optional, may be null)"
        javadoc {
            param(name, d)
        }
        return param
    }

    fun param(type:PrimitiveType, name:String, description:String = ""):MethodParam
    {
        return param(type, name, Nullability.NON_NULL, description)
    }

    fun body(init:CodeBlock.() -> Unit)
    {
        init.invoke(body)
    }

    override fun writeTo(out:JavaWriter)
    {
        out.pad()
        out += javadoc
        out += scope
        out += modifiers
        out += returnType
        out += " $name("
        params.forEachIndexed { i, param ->
            if (i > 0) out += ", "
            out += param.type
            out += " ${param.name}"
        }
        out += ")\n"
        out.block {
            params.filter { it.nullability == Nullability.NON_NULL && !it.type.isPrimitive() }
                    .forEach {
                        out += "if( $it == null ) throw new NullPointerException(\"The '$it' argument must not be null\");\n"
                    }
            out += body
        }
        out.pad()
    }
}

class MethodParam(val type:TypeRef,
                  val name:String,
                  val nullability:Nullability,
                  val description:String?)
{
    override fun toString():String = name
}