/*
 * Copyright 2018 Aaron Galuzzi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package galuzzi.codegen.java

import galuzzi.codegen.CodeElement
import galuzzi.codegen.CodeGen
import galuzzi.codegen.CodeGenScope
import java.util.*

/**
 * @author Aaron Galuzzi (3/9/2017)
 */
@CodeGenScope
class Javadoc : CodeElement
{
    private val description = StringBuilder()
    private val tags = ArrayList<Tag>()

    operator fun String.unaryPlus()
    {
        description.append(this)
    }

    fun pad()
    {
        if (description.isBlank())
        {
            return
        }
        if (!description.endsWith('\n'))
        {
            +"\n"
        }
        if (!description.endsWith(PARAGRAPH_SEPARATOR))
        {
            +PARAGRAPH_SEPARATOR
        }
    }

    fun pre(text: String)
    {
        +"<pre>\n$text\n</pre>\n"
    }

    fun code(text: String)
    {
        +"<code>$text</code>"
    }

    fun list(items: Iterable<String>, ordered: Boolean = false)
    {
        val tag = if (ordered) "ol" else "ul"
        +"<$tag>\n"
        items.forEach { +"  <li>$it</li>\n" }
        +"</$tag>\n"
    }

    fun list(vararg items: String, ordered: Boolean = false)
    {
        list(items.asIterable(), ordered)
    }

    fun link(name: TypeName)
    {
        +"{@link ${name.fqcn}}"
    }

    fun author(value: String)
    {
        tags += Tag("author", value, 1)
    }

    fun version(value: String)
    {
        tags += Tag("version", value, 2)
    }

    fun param(name: String, description: String)
    {
        tags += Tag("param", "$name $description", 3)
    }

    fun returns(description:String)
    {
        tags += Tag("return", description, 4)
    }

    fun throws(type:String, description:String)
    {
        tags += Tag("throws", "$type $description", 5)
    }

    fun see(value: String)
    {
        tags += Tag("see", value, 6)
    }

    fun since(value: String)
    {
        tags += Tag("since", value, 7)
    }

    fun deprecated(description: String)
    {
        tags += Tag("deprecated", description, 9)
    }

    fun tag(name: String, value: String)
    {
        tags += Tag(name, value, 10)
    }

    fun isEmpty(): Boolean = (description.isBlank() && tags.isEmpty())

    override fun build(): CodeGen
    {
        val _description = description.toString()
        val haveDescription = _description.isNotBlank()
        val haveTags = tags.isNotEmpty()

        if (!haveDescription && !haveTags)
        {
            return {}
        }

        return {
            pad()
            +"/**\n"
            if (haveDescription)
            {
                _description.split('\n').forEach { +" * $it\n" }
                if (haveTags) +" *\n"
            }
            tags.sorted().forEach { +" * @${it.name} ${it.value}\n" }
            +" */\n"
        }
    }
}

private data class Tag(val name: String,
                       val value: String,
                       val order: Int) : Comparable<Tag>
{
    override fun compareTo(other: Tag): Int
    {
        return order.compareTo(other.order)
    }
}

private const val PARAGRAPH_SEPARATOR = "<p>\n"
