package galuzzi.nevil.java

import galuzzi.nevil.GenScope
import galuzzi.nevil.java.Scope.PRIVATE
import galuzzi.nevil.java.Scope.PUBLIC
import galuzzi.nevil.java.type.LocalType
import galuzzi.nevil.java.type.OPTIONAL
import galuzzi.nevil.java.type.TypeRef


/**
 * @author Aaron Galuzzi (2/11/2017)
 */
@GenScope
open class JavaClass(val name:String,
                     val scope:Scope):JavaElement
{
    private val type = LocalType(name)
    private val modifiers = mutableSetOf<Modifier>()
    private val fields = mutableListOf<JavaField>()
    private val methods = mutableListOf<JavaMethod>()
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

    fun javadoc(init:Javadoc.() -> Unit)
    {
        init.invoke(javadoc)
    }

    fun field(scope:Scope,
              type:TypeRef,
              name:String,
              init:(JavaField.() -> Unit)? = null):JavaField
    {
        val field = JavaField(name, scope, type)
        init?.invoke(field)
        fields += field
        return field
    }

    fun property(name:String,
                 type:TypeRef,
                 nullability:Nullability,
                 description:String,
                 getter:Boolean = true,
                 setter:Boolean = true,
                 init:(JavaField.() -> Unit)? = null):JavaField
    {
        val field = field(PRIVATE, type, name, init = init)
        val nullable = nullability == Nullability.ALLOW_NULL

        if (getter)
        {
            val returnType = if (nullable) OPTIONAL.of(field.type) else field.type

            method(PUBLIC, returnType, "get" + field.name.capitalize())
            {
                final()
                javadoc {
                    returns(description)
                }
                body {
                    if (nullable)
                    {
                        +"return "
                        +OPTIONAL
                        +".ofNullable($field);\n"
                    }
                    else
                    {
                        +"return $field;\n"
                    }
                }
            }
        }

        if (setter)
        {
            method(PUBLIC, this@JavaClass.type, "set" + field.name.capitalize())
            {
                final()
                param(field.type, field.name, nullability, "the new value")
                javadoc {
                    +"Sets $description"
                    returns("this ${this@JavaClass.name} (for method chaining)")
                }
                body {
                    +"this.$field = $field;\n"
                    +"return this;\n"
                }
            }
        }

        return field
    }

    fun method(scope:Scope,
               returnType:TypeRef,
               name:String,
               init:(JavaMethod.() -> Unit)? = null):JavaMethod
    {
        val method = JavaMethod(name, scope, returnType)
        init?.invoke(method)
        methods += method
        return method
    }

    override fun writeTo(out:JavaWriter)
    {
        out += javadoc
        out += scope
        out += modifiers
        out += "class $name\n"
        out.block {
            out += fields
            out += methods

//            fields.forEach { field ->
//                if (field.getter)
//                {
//                    out += JavaMethod().apply {
//                        +"return ${field.name};"
//                    }
//                }
//            }
        }
    }
}