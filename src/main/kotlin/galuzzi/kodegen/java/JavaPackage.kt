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

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Represents a valid Java package name.
 */
data class JavaPackage(val name: String)
{
    init
    {
        if (name != "" && !VALID_NAME.matches(name))
        {
            throw IllegalArgumentException("Invalid package name: $name")
        }
    }

    fun isDefault(): Boolean
    {
        return name == ""
    }

    fun resolve(packageName: String): JavaPackage
    {
        return if (isDefault()) JavaPackage(packageName) else JavaPackage("$name.$packageName")
    }

    fun toPath(): Path
    {
        return name.split('.')
                .map { Paths.get(it) }
                .reduce(Path::resolve)
    }

    companion object
    {
        private val VALID_NAME = Regex("^[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*$")

        val DEFAULT = JavaPackage("")
    }
}
