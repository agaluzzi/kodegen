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

/**
 * Joins multiple code generation functions with a delimiter.
 *
 * @return a single aggregated code generation function
 */
fun join(functions: Array<out CodeGen>, delimiter: String): CodeGen
{
    return {
        functions.forEachIndexed { i, function ->
            if (i > 0) +delimiter
            +function
        }
    }
}

/**
 * Joins multiple code elements with a delimiter.
 *
 * @return a single aggregated code generation function
 */
fun join(elements: Iterable<CodeElement>, delimiter: String): CodeGen
{
    return {
        elements.forEachIndexed { i, elem ->
            if (i > 0) +delimiter
            +elem
        }
    }
}