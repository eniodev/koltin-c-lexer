package org.example

class Scanner(val source: String) {
    var start: Int = 0
    var current: Int = 0
    var line: Int = 1
    var tokens: MutableList<Token> = arrayListOf()
    val keywords: Map<String, TokenType> = mapOf(
        "and" to TokenType.TAnd,
        "or" to TokenType.TOr,
        "true" to TokenType.TTrue,
        "false" to TokenType.TFalse,
        "if" to TokenType.TIf,
        "else" to TokenType.TElse,
        "switch" to TokenType.TSwitch,
        "case" to TokenType.TCase,
        "break" to TokenType.TBreak,
        "continue" to TokenType.TContinue,
        "default" to TokenType.TDefault,
        "for" to TokenType.TFor,
        "while" to TokenType.TWhile,
        "do" to TokenType.TDo,
        "int" to TokenType.TInt,
        "float" to TokenType.TFloat,
        "double" to TokenType.TDouble,
        "short" to TokenType.TShort,
        "long" to TokenType.TLong,
        "char" to TokenType.TChar,
        "auto" to TokenType.TAuto,
        "const" to TokenType.TConst,
        "volatile" to TokenType.TVolatile,
        "signed" to TokenType.TSigned,
        "unsigned" to TokenType.TUnsigned,
        "enum" to TokenType.TEnum,
        "void" to TokenType.TVoid,
        "static" to TokenType.TStatic,
        "register" to TokenType.TRegister,
        "extern" to TokenType.TExtern,
        "struct" to TokenType.TStruct,
        "typedef" to TokenType.TTypedef,
        "sizeof" to TokenType.TSizeof,
        "goto" to TokenType.TGoto,
        "union" to TokenType.TUnion,
        "return" to TokenType.TReturn
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

    fun error(line: Int, message: String) {
        println("$line -> $message")
    }

    fun advance(): Char {
        return source.get(current++)
    }

    fun peek(): Char {
        if (isAtEnd()) return Char(0)
        return source.get(current)
    }

    fun peekNext(): Char {
        if (current + 1 >= source.length) return Char(0)
        return source.get(current + 1)
    }

    fun match(ch: Char): Boolean {
        if (isAtEnd()) return false
        if (source.get(current) != ch) return false

        current++
        return true
    }

    fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            error(line, "Unterminated string")
            return
        }
        advance()
        val content = source.substring(start + 1, current - 1)
        addToken(TokenType.TString)
    }

    fun isDigit(ch: Char): Boolean {
        return ch in '0'..'9'
    }

    fun isAlpha(ch: Char): Boolean {
        return (ch in 'a'..'z') ||
                (ch in 'A'..'Z') ||
                ch == '_';
    }

    fun identifier() {
        while (isAlphaNumeric(peek())) advance()

        val value = source.substring(start, current)
        var ttype = keywords.get(value)
        if (ttype == null) ttype = TokenType.TIdentifier
        addToken(ttype)
    }

    fun isAlphaNumeric(ch: Char): Boolean {
        return isAlpha(ch) || isDigit(ch)
    }

    fun number() {
        while (isDigit(peek())) advance()

        if (peek() == '.' && isDigit(peekNext())) {
            advance()
            while (isDigit(peek())) advance()

        }
        addToken(TokenType.TNumber)
    }

    fun addToken(ttype: TokenType) {
        val lexeme = source.substring(start, current)
        tokens.add(Token(line, lexeme, ttype))
    }

    fun scanToken() {
        val ch = advance()
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
            '!' -> addToken(if (match('=')) TokenType.TBangEqual else TokenType.TBang)
            '=' -> addToken(if (match('=')) TokenType.TBangEqual else TokenType.TEqual)
            '<' -> addToken(if (match('=')) TokenType.TLessEqual else TokenType.TLess)
            '>' -> addToken(if (match('=')) TokenType.TGreaterEqual else TokenType.TGreater)
            '/' -> if (match('/')) {
                while (peek() != '\n' && !isAtEnd()) advance()
            } else addToken(TokenType.TSlash)

            '"' -> string()
            else -> {
                if (isDigit(ch)) number()
                else if (isAlpha(ch)) identifier() else error(line, "Unexpected character")
            }
        }
    }
}