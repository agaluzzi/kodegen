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

/**
 * Creates a code generation function for a string literal.
 */
fun literal(str: String): CodeGen
{
    return { +str }
}

/**
 * Creates a code generation function for a numeric literal.
 */
fun number(n: Number): CodeGen
{
    return { +n }
}

/**
 * Creates a code generation function for a double-quoted string literal.
 */
fun string(str: String): CodeGen
{
    return { +"\"$str\"" }
}

/**
 * Creates a code generation function for a statement, which is a piece of code that begins on a new line and ends
 * with a semicolon (and a newline).
 */
fun stmt(str: String): CodeGen
{
    return {
        newline()
        +str.trimEnd(';', '\n')
        +";\n"
    }
}

/**
 * Creates a code generation function for a comment -- single line or multi-line.
 *
 * @param text the comment, which may contain newline ('\n') characters for a multi-line comment
 */
fun comment(text: String): CodeGen
{
    return {
        newline()
        text.split('\n').forEach { line -> write("// $line\n") }
    }
}

/**
 * Creates a code generation function for a code block, which is surrounded by curly braces.
 */
fun block(content: CodeGen): CodeGen
{
    return {
        newline()
        +"{\n"
        indent()
        +content
        outdent()
        newline()
        +"}\n"
    }
}

/**
 * Creates a code generation function for an if-then statement.
 */
fun ifThen(test: CodeGen, then: CodeGen): CodeGen
{
    return {
        newline()
        +"if ("
        +test
        +')'
        +block(then)
    }
}

/**
 * Creates a code generation function for an if-then statement.
 */
fun ifThen(test: String, then: CodeGen): CodeGen
{
    return ifThen({ +test }, then)
}

/**
 * Creates a code generation function for an if-then-else statement.
 */
fun ifThenElse(test: CodeGen, then: CodeGen, default: CodeGen): CodeGen
{
    return {
        +ifThen(test, then)
        +"else"
        +block(default)
    }
}

/**
 * Creates a code generation function for an if-then-else statement.
 */
fun ifThenElse(condition: String, then: CodeGen, default: CodeGen): CodeGen
{
    return ifThenElse({ +condition }, then, default)
}
