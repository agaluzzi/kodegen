package galuzzi.nevil.java

import galuzzi.nevil.GenScope
import java.util.*

/**
 * @author Aaron Galuzzi (3/9/2017)
 */
@GenScope
class Javadoc:JavaElement
{
    private val description = StringBuilder()
    private val tags = ArrayList<Tag>()

    operator fun String.unaryPlus()
    {
        description.append(this)
    }

    fun tag(name:String, value:String)
    {
        tags += Tag(name, value)
    }

    fun param(name:String, description:String)
    {
        tag("param", "$name $description")
    }

    fun returns(description:String)
    {
        return tag("return", description)
    }

    fun throws(type:String, description:String)
    {
        tag("throws", "$type $description")
    }

    override fun writeTo(out:JavaWriter)
    {
        val _description = description.toString()

        val haveDescription = _description.isNotBlank()
        val haveTags = tags.isNotEmpty()

        if (!haveDescription && !haveTags)
        {
            return
        }

        out.pad()
        out += "/**\n"
        if (haveDescription)
        {
            _description.split('\n').forEach { out += " * $it\n" }
            if (haveTags) out += " *\n"
        }
        tags.forEach { out += " * @${it.name} ${it.value}\n" }
        out += " */\n"
    }
}

private class Tag(val name:String, val value:String)

