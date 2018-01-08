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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

/**
 * Base class for code generation unit tests.
 */
internal abstract class JavaGenTest(val expectPath: String)
{
    abstract fun build(): JavaTypeElement

    @Test
    fun testGenerate()
    {
        val tempDir = Files.createTempDirectory("JavaGenTest")

        val generator = JavaCodeGenerator(tempDir)
        generator.header = "Please do not modify this file.\nThis is generated code."

        val element = build()
        val outputFile = generator.generate(element)
        val result = outputFile.readString()
        val expect = loadResource(expectPath)

        assertEquals(expect, result)
    }
}

fun Path.readString(): String
{
    return String(Files.readAllBytes(this)).trim()
}

fun loadResource(path: String): String
{
    return ClassLoader.getSystemResource(path).readText().trim()
}