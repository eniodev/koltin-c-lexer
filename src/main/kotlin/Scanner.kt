package org.example

class Scanner(val source: String) {
    var start: Int = 0
    var current: Int = 0
    var line: Int = 1
    var tokens: MutableList<Token> = arrayListOf()
    val keywords: Map<String, TokenType> = mapOf(
        "int" to TokenType.TInt
    )

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }
        tokens.add(Token(line, "", TokenType.TEof))
        return tokens
    }

    fun isAtEnd(): Boolean {
        return current >= source.length
    }

    fun advance(): Char {
        return source.get(current++)
    }

    fun addToken(ttype: TokenType) {
        val lexeme = source.substring(start, current)
        tokens.add(Token(line, lexeme, ttype))
    }

    fun scanToken() {
        val ch = advance()
        print(ch)
        when (ch) {
            '(' -> addToken(TokenType.TLeftParen)
            ')' -> addToken(TokenType.TRightParen)
            '{' -> addToken(TokenType.TLeftBrace)
            '}' -> addToken(TokenType.TRightBrace)
            ',' -> addToken(TokenType.TComma)
            '.' -> addToken(TokenType.TDot)
            '-' -> addToken(TokenType.TMinus)
            '+' -> addToken(TokenType.TPlus)
            ';' -> addToken(TokenType.TSemicolon)
            '*' -> addToken(TokenType.TStar)
            else -> print("Unexpected character")
        }
    }
}