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

package galuzzi.kodegen

import java.io.Writer
import java.lang.Integer.max

/**
 * A language-agnostic output mechanism for generating code.
 *
 * This class allows for recursively writing a tree-like structure of code elements,
 * as well as helping out with indentation and newlines.
 */
class CodeWriter(private val output: Writer) : AutoCloseable
{
    private var indent = 0
    private var trailingNewlines = 0

    /**
     * Increments the current indentation level.
     */
    fun indent()
    {
        indent++
    }

    /**
     * Decrements the current indentation level.
     */
    fun outdent()
    {
        indent = max(0, indent - 1)
    }

    /**
     * Ensures that the next [non-newline] character written will be at the beginning of a new line.
     */
    fun newline()
    {
        if (trailingNewlines == 0)
        {
            write('\n')
        }
    }

    /**
     * Ensures that the next [non-newline] character written will be at the beginning of a new line,
     * and also preceded by a blank line.
     */
    fun pad()
    {
        while (trailingNewlines < 2)
        {
            write('\n')
        }
    }

    /**
     * Writes a single character.
     */
    fun write(c: Char)
    {
        if (c == '\n')
        {
            output.append('\n')
            trailingNewlines++
        }
        else
        {
            if (trailingNewlines > 0)
            {
                for (i in 0 until indent)
                {
                    output.append("    ")
                }
            }
            output.append(c)
            trailingNewlines = 0
        }
    }

    /**
     * Writes a single character. (for DSL)
     */
    operator fun Char.unaryPlus()
    {
        write(this)
    }

    /**
     * Writes a String.
     */
    fun write(str: String)
    {
        str.forEach(::write)
    }

    /**
     * Writes a String. (for DSL)
     */
    operator fun String.unaryPlus()
    {
        write(this)
    }

    /**
     * Writes a number, by calling its toString().
     */
    fun write(num: Number)
    {
        write(num.toString())
    }

    /**
     * Writes a number, by calling its toString(). (for DSL)
     */
    operator fun Number.unaryPlus()
    {
        write(this)
    }

    /**
     * Writes a piece of [CodeGen].
     */
    fun write(code: CodeGen)
    {
        code.invoke(this)
    }

    /**
     * Writes a piece of [CodeGen]. (for DSL)
     */
    operator fun CodeGen.unaryPlus()
    {
        write(this)
    }

    /**
     * Writes a code element, by calling its [CodeElement.build] function.
     */
    fun write(element: CodeElement)
    {
        write(element.build())
    }

    /**
     * Writes a code element, by calling its [CodeElement.build] function. (for DSL)
     */
    operator fun CodeElement.unaryPlus()
    {
        write(this)
    }

    /**
     * Writes a collection of code elements, by calling their [CodeElement.build] functions.
     */
    fun write(elements: Iterable<CodeElement>)
    {
        elements.forEach(this::write)
    }

    /**
     * Writes a collection of code elements, by calling their [CodeElement.build] functions. (for DSL)
     */
    operator fun Iterable<CodeElement>.unaryPlus()
    {
        write(this)
    }

    /**
     * Writes a code embeddable, by calling its [CodeEmbeddable.toString] function.
     */
    fun write(embeddable: CodeEmbeddable)
    {
        write(embeddable.toString())
    }

    /**
     * Writes a code fragment, by calling its [CodeEmbeddable.toString] function. (for DSL)
     */
    operator fun CodeEmbeddable.unaryPlus()
    {
        write(this)
    }

    override fun close()
    {
        output.flush()
        output.close()
    }
}

/**
 * A code generation function, which is invoked with a [CodeWriter] receiver to provide a fluent syntax.
 */
typealias CodeGen = CodeWriter.() -> Unit


