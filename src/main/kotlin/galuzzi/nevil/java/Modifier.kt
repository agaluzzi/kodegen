package galuzzi.nevil.java

/**
 * @author Aaron Galuzzi (2/11/2017)
 */
enum class Modifier(val prefix:String):JavaElement
{
    STATIC("static "),
    FINAL("final "),
    ABSTRACT("abstract "),
    SYNCHRONIZED("synchronized "),
    VOLATILE("volatile ");

    override fun writeTo(out:JavaWriter)
    {
        out += prefix
    }
}