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

package galuzzi.kodegen.java

import galuzzi.kodegen.CodeEmbeddable
import java.util.*

/**
 * @author Aaron Galuzzi (2/11/2017)
 */
enum class Modifier(val prefix: String)
{
    STATIC("static "),
    FINAL("final "),
    ABSTRACT("abstract "),
    SYNCHRONIZED("synchronized "),
    VOLATILE("volatile "),
    TRANSIENT("transient ")
}

class Modifiers : CodeEmbeddable
{
    private val set = EnumSet.noneOf(Modifier::class.java)

    operator fun plusAssign(modifier: Modifier)
    {
        set += modifier
    }

    override fun toString(): String
    {
        return set.joinToString("") { it.prefix }
    }
}

fun main(args: Array<String>)
{
    val set = emptySet<String>()//setOf("A", "B", "C")
    println("|" + set.joinToString(" ") + "|")
}