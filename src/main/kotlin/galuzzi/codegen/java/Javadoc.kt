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

import galuzzi.codegen.CodeGen
import galuzzi.codegen.CodeElement
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

    fun add(text: String)
    {
        description.append(text)
    }

    fun summary(text: String?)
    {
        if (text != null && text.isNotBlank())
        {
            p(text)
        }
    }

    operator fun String.unaryPlus()
    {
        add(this)
    }

    fun p(text: String)
    {
        add("<p>$text</p>\n")
    }

    fun pre(text: String)
    {
        add("<pre>\n$text\n</pre>\n")
    }

    fun ul(vararg items: String)
    {
        add("<ul>\n")
        items.forEach { add("<li>$it</li>\n") }
        add("</ul>\n")
    }

    fun ul(items: Iterable<String>)
    {
        add("<ul>\n")
        items.forEach { add("<li>$it</li>\n") }
        add("</ul>\n")
    }

    fun ol(vararg items: String)
    {
        add("<ol>\n")
        items.forEach { add("<li>$it</li>\n") }
        add("</ol>\n")
    }

    fun ol(items: Iterable<String>)
    {
        add("<ol>\n")
        items.forEach { add("<li>$it</li>\n") }
        add("</ol>\n")
    }

    fun link(name: TypeName)
    {
        add("{@link ${name.fqcn}}")
    }

    fun tag(name: String, value: String)
    {
        tags += Tag(name, value)
    }

    fun param(name: String, description: String)
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
            tags.filter { it.value.isNotBlank() }.forEach { +" * @${it.name} ${it.value}\n" }
            +" */\n"
        }
    }
}

private class Tag(val name:String, val value:String)

