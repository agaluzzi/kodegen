package galuzzi.nevil.java

/**
 * @author Aaron Galuzzi (3/7/2017)
 */
interface JavaElement
{
    fun writeTo(out:JavaWriter)
}

fun text(text:String):JavaElement
{
    return object:JavaElement
    {
        override fun writeTo(out:JavaWriter)
        {
            out += text
        }
    }
}

fun comment(text:String):JavaElement
{
    return object:JavaElement
    {
        override fun writeTo(out:JavaWriter)
        {
            text.split('\n').forEach { out += "// $it\n" }
        }
    }
}