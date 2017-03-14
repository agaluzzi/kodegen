package galuzzi.nevil.java

import java.io.Writer
import java.lang.Integer.max

/**
 * @author Aaron Galuzzi (2/12/2017)
 */
class JavaWriter(val pkg:String)
{
    private val buffer = StringBuilder()
    private val imports = mutableMapOf<String, String>()
    private var indent = 0
    private var column = 0
    private var padNeeded = false
    private var trailingNewlines = 0

    fun import(shortName:String, fqcn:String):Boolean
    {
        // check for same package (no import required)
        if ("$pkg.$shortName" == fqcn) return true

        val existing:String? = imports[shortName]
        if (existing == null)
        {
            imports[shortName] = fqcn
            return true
        }
        else
        {
            return existing == fqcn
        }
    }

    fun indent()
    {
        indent++
    }

    fun outdent()
    {
        indent = max(0, indent - 1)
    }

    operator fun plusAssign(c:Char)
    {
        if (padNeeded)
        {
            padNeeded = false
            while (trailingNewlines < 2)
            {
                buffer.append('\n')
                trailingNewlines++
                column = 0
            }
        }

        if (c == '\n')
        {
            buffer.append('\n')
            trailingNewlines++
            column = 0
        }
        else
        {
            if (column == 0)
            {
                for (i in 0 until indent)
                {
                    buffer.append("    ")
                }
            }
            buffer.append(c)
            trailingNewlines = 0
            column++
        }
    }

    operator fun plusAssign(obj:Any)
    {
        obj.toString().forEach(this::plusAssign)
    }

    operator fun plusAssign(elem:JavaElement)
    {
        elem.writeTo(this)
    }

    operator fun plusAssign(collection:Collection<Any>)
    {
        collection.forEach {
            if (it is JavaElement)
            {
                it.writeTo(this)
            }
            else
            {
                plusAssign(it)
            }
        }
    }

    operator fun plusAssign(array:Array<out Any>)
    {
        array.forEach {
            if (it is JavaElement)
            {
                it.writeTo(this)
            }
            else
            {
                plusAssign(it)
            }
        }
    }

    fun block(content:JavaWriter.() -> Unit)
    {
        this += "{\n"
        indent()
        content.invoke(this)
        outdent()
        this += "}\n"
    }

    fun pad()
    {
        padNeeded = true
    }

    fun writeTo(output:Writer)
    {
        output.write("package $pkg;\n\n")
        imports.forEach { _, fqcn -> output.write("import $fqcn;\n") }
        output.write("\n")
        output.write(buffer.toString())
        output.flush()
    }
}