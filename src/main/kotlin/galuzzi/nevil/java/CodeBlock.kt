package galuzzi.nevil.java

import galuzzi.nevil.GenScope

/**
 * @author Aaron Galuzzi (3/13/2017)
 */
@GenScope
class CodeBlock:JavaElement
{
    private val elements = mutableListOf<JavaElement>()

    override fun writeTo(out:JavaWriter)
    {
        out += elements
    }

    operator fun JavaElement.unaryPlus()
    {
        elements += this
    }

    operator fun String.unaryPlus()
    {
        elements += text(this)
    }
}