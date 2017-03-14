package galuzzi.nevil.java

import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Aaron Galuzzi (2/11/2017)
 */
class ClassFile(name:String, val pkg:String, init:(ClassFile.() -> Unit)? = null)
    :JavaClass(name, Scope.PUBLIC)
{
    init
    {
        init?.invoke(this)
    }

    fun writeTo(directory:Path)
    {
        val out:JavaWriter = JavaWriter(pkg)
        super.writeTo(out)

        val file = directory.resolve("$name.java")
        val writer = Files.newBufferedWriter(file)
        writer.use {
            out.writeTo(writer)
        }
    }
}