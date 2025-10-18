package org.example

import java.io.File

fun readFile(path: String): String {
    val file = File(path)
    val source = file.readText()
    return source
}

fun main() {
    val source: String = readFile("source.c")
    val lexer = Scanner(source)
    lexer.scanTokens()
    lexer.tokens.forEach { println("${it.line} :: ${it.lexeme} :: ${it.ttype.name}") }
}