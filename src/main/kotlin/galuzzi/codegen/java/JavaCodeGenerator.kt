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

import galuzzi.codegen.CodeWriter
import java.nio.file.Files
import java.nio.file.Path

/**
 * TODO...
 */
class JavaCodeGenerator(private val outputDir: Path)
{
    var header = ""

    companion object
    {
        private val IMPORT_PLACEHOLDER = "__IMPORT_PLACEHOLDER__"
    }

    fun generate(elem: JavaTypeElement):Path
    {
        val name = elem.name
        val imports = Imports(name)
        Imports.setThreadLocal(imports)

        val dir = outputDir.resolve(name.pkg.toPath())

        Files.createDirectories(dir)

        val outputFile = dir.resolve("${name.simpleName()}.java")
        val tempFile = dir.resolve("${name.simpleName()}.tmp")

        try
        {
            Files.newBufferedWriter(tempFile).use {
                val output = CodeWriter(it)

                output.apply {
                    if (!header.isBlank())
                    {
                        +"/*\n$header\n*/\n\n"
                    }
                    +stmt("package ${name.pkg.name}")
                    pad()
                    +IMPORT_PLACEHOLDER
                    pad()
                    +elem
                }
            }

            Files.newBufferedWriter(outputFile).use {
                val writer = CodeWriter(it)

                Files.newBufferedReader(tempFile).useLines {
                    it.forEach { line ->
                        if (line == IMPORT_PLACEHOLDER)
                        {
                            writer.write(imports)
                        }
                        else
                        {
                            writer.write(line)
                        }
                        writer.write('\n')
                    }
                }
            }
        }
        finally
        {
            Files.delete(tempFile)
        }

        return outputFile
    }
}