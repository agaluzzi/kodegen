# KodeGen
A [Kotlin](https://kotlinlang.org/) library for generating Java source code, which leverages [type-safe builders](https://kotlinlang.org/docs/reference/type-safe-builders.html) to provide a fluent DSL for code generation.

## Table of contents

- [Example](#example)
- [License](#license)

## Example
The following Kotlin code demonstrates the code generation DSL.
```
val generator = JavaCodeGenerator(outputDir = "/some/directory")
val pkg = JavaPackage("com.example")

generator.generate(

        JavaClass.create("HelloWorld", pkg) {

            javadoc {
                +"This is a simple example class."
            }

            val msg = field("message", Type.String, Scope.PRIVATE) {
                static()
                final()
                value = string("Hello World!")
            }

            method("main", Type.void) {
                static()
                param("args", Type.String.array())
                body {
                    +"System.out.println($msg);"
                }
            }
        })
```
which generates ...
```
package com.example;

/**
 * This is a simple example class.
 */
public class HelloWorld
{
    private static final String message = "Hello World!";

    public static void main(String[] args)
    {
        System.out.println(HelloWorld.message);
    }
}
```

## License
This project is licensed under the terms of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).