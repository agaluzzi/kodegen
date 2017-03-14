package galuzzi.nevil.java

/**
 * @author Aaron Galuzzi (2/11/2017)
 */
enum class Scope(val prefix:String):JavaElement
{
    PUBLIC("public "),
    PROTECTED("protected "),
    PRIVATE("private "),
    PACKAGE("");

    override fun writeTo(out:JavaWriter)
    {
        out += prefix
    }
}