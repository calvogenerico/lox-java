package calvo.jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static calvo.jlox.TokenType.*;

public class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;

  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and", AND);
    keywords.put("class", CLASS);
    keywords.put("else", ELSE);
    keywords.put("false", FALSE);
    keywords.put("for", FOR);
    keywords.put("fun", FUN);
    keywords.put("if", IF);
    keywords.put("nil", NIL);
    keywords.put("or", OR);
    keywords.put("print", PRINT);
    keywords.put("return", RETURN);
    keywords.put("super", SUPER);
    keywords.put("this", THIS);
    keywords.put("true", TRUE);
    keywords.put("var", VAR);
    keywords.put("while", WHILE);
  }


  Scanner(String source) {
    this.source = source;
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '(':
        addEmptyToken(LEFT_PAREN);
        break;
      case ')':
        addEmptyToken(RIGHT_PAREN);
        break;
      case '{':
        addEmptyToken(LEFT_BRACE);
        break;
      case '}':
        addEmptyToken(RIGHT_BRACE);
        break;
      case ',':
        addEmptyToken(COMMA);
        break;
      case '.':
        addEmptyToken(DOT);
        break;
      case '-':
        addEmptyToken(MINUS);
        break;
      case '+':
        addEmptyToken(PLUS);
        break;
      case ';':
        addEmptyToken(SEMICOLON);
        break;
      case '*':
        addEmptyToken(STAR);
        break;
      case '!':
        addEmptyToken(match('=') ? BANG_EQUAL : BANG);
        break;
      case '=':
        addEmptyToken(match('=') ? EQUAL_EQUAL : EQUAL);
        break;
      case '<':
        addEmptyToken(match('=') ? LESS_EQUAL : LESS);
        break;
      case '>':
        addEmptyToken(match('=') ? GREATER_EQUAL : GREATER);
        break;
      case '/':
        if (match('/')) {
          // A comment goes until the end of the line.
          while (peek() != '\n' && !isAtEnd()) advance();
        } else {
          addEmptyToken(SLASH);
        }
        break;
      case ' ':
      case '\r':
      case '\t':
        // Ignore whitespace.
        break;
      case '\n':
        line += 1;
        break;
      case '"':
        string();
        break;

      default:
        if (isDigit(c)) {
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          Lox.error(line, "Unexpected character.");
        }
    }
  }

  private boolean isAlpha(char c) {
    return c >= 'a' && c <= 'z' ||
      c >= 'A' && c <= 'Z' ||
      c == '_';
  }

  private void identifier() {
    while (isAlphaNumeric(peek())) advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addEmptyToken(type);
  }

  private boolean isAlphaNumeric(char c) {
    return isDigit(c) || isAlpha(c);
  }


  private void number() {
    while (isDigit(peek())) advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      // Consume the "."
      advance();

      while (isDigit(peek())) advance();
    }

    addToken(NUMBER,
      Double.parseDouble(source.substring(start, current)));
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }


  private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }

    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    // The closing '"'.
    advance();

    // Trim the surrounding quotes.
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current += 1;
    return true;
  }

  private void addEmptyToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  private char advance() {
    char res = source.charAt(current);
    this.current += 1;
    return res;
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }
}