import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

  private int row = 1;
  private int previousRow = 1;
  private int currentToken = 0;
  private ArrayList<Token> tokens;

  public ArrayList<Token> generateTokens(String input) throws java.io.IOException {
    // Maybe switch numbers to \d etc.
    String whiteSpace = "([ \\t\\n]|(%.*\\n))";
    Pattern tokenPattern = Pattern.compile(
        "[1-9][0-9]*\\." + "|" + 
        "[1-9][0-9]*" + whiteSpace + "|" +
        "[A-Za-z]+" + whiteSpace + "|" +
        "[A-Za-z]+" + "|" +
        "#[A-Fa-f0-9]{6}" + "|" +
        "\\." + "|" +
        "\"" + "|" +
        "[ \\t\\n]+" + "|" + 
        "%.*\\n"
    );
    Matcher m = tokenPattern.matcher(input);
    int inputPos = 0;
    tokens = new ArrayList<Token>();
    int idx = 0;

    String forwMatch = "FORW" + whiteSpace + "+";
    String backMatch = "BACK" + whiteSpace + "+";
    String leftMatch = "LEFT" + whiteSpace + "+";
    String rightMatch = "RIGHT" + whiteSpace + "+";
    String colorMatch = "COLOR" + whiteSpace + "+";
    String upMatch = "UP" + whiteSpace + "*";
    String downMatch = "DOWN" + whiteSpace + "*";
    String hexMatch = "#[A-F0-9]{6}";
    String dotNumberMatch = "[1-9][0-9]*\\.";
    String numberMatch = "[1-9][0-9]*" + whiteSpace + "+";

    while (m.find()) {
      idx++;
      Token result = null;
      if (m.start() != inputPos) {
        tokens.add(new Token(TokenType.INVALID, row));
      }
      String matchGroup = m.group().toUpperCase();

      if (matchGroup.matches(forwMatch)) {
        result = new Token(TokenType.FORW, row);
      } else if (matchGroup.matches(backMatch))
        result = new Token(TokenType.BACK, row);
      else if (matchGroup.matches(leftMatch))
        result = new Token(TokenType.LEFT, row);
      else if (matchGroup.matches(rightMatch))
        result = new Token(TokenType.RIGHT, row);
      else if (matchGroup.matches(colorMatch))
        result = new Token(TokenType.COLOR, row);
      else if (matchGroup.matches(upMatch))
        result = new Token(TokenType.UP, row);
      else if (matchGroup.matches(downMatch))
        result = new Token(TokenType.DOWN, row);
      else if (matchGroup.matches(hexMatch))
        result = new Token(TokenType.HEX, row, matchGroup.toString());
      else if (matchGroup.matches(dotNumberMatch)) {
        // Find number that ends with dot
        String number = matchGroup.replaceAll("[^0-9]", "");
        if (number.length() > 6) {
          result = new Token(TokenType.INVALID, row);
        } else {
          result = new Token(TokenType.NUMBER, row, Integer.parseInt(number), true);
        }
      } else if (matchGroup.matches(numberMatch)) {
        // Number with following whitespace
        String number = matchGroup.replaceAll("[^0-9]", "");
        if (number.length() > 6) {
          result = new Token(TokenType.INVALID, row);
        } else {
          result = new Token(TokenType.NUMBER, row, Integer.parseInt(number), false);
        }
      }
      else if (matchGroup.matches("\\."))
        result = new Token(TokenType.DOT, row);
      else if (matchGroup.matches("\""))
        result = new Token(TokenType.CIT, row);
      else if (matchGroup.matches("REP" + whiteSpace + "+"))
        result = new Token(TokenType.REP, row);
      // else if (matchGroup.matches("\\n"))
      // row++;

      String printToken = idx + ":    " + matchGroup;

      // Skip whitespaces
      if (result == null) {
        if (matchGroup.matches(whiteSpace + "+")) {
          printToken = printToken.concat("-" + "Whitespace");
        } else {
          tokens.add(new Token(TokenType.INVALID, row));
          printToken = printToken.concat("-ERROR");
        }
      } else if (result.type == TokenType.INVALID) {
        tokens.add(result);
        printToken = printToken.concat("-INVALID ERROR");
      } else {
        previousRow = row;
        printToken = printToken.concat("-" + result.type);
        if (result.type == TokenType.NUMBER) {
          if (result.hasDot) {
            printToken = printToken.concat(" with dot");
          } else {
            printToken = printToken.concat(" with whitespace");
          }
        }
        tokens.add(result);
      }

      printToken = printToken.concat("-" + "Row " + row);

      String newLines = matchGroup.replaceAll("[^\\n]", "");
      for (int i = 0; i < newLines.length(); i++) {
        row++;
      }

      // System.out.println(printToken);
      // System.out.println("From " + m.start() + " to " + m.end());

      inputPos = m.end();
    }

    if (inputPos != input.length()) {
      // System.out.println("Input != Token.length");
      tokens.add(new Token(TokenType.INVALID, row));
    }
    tokens.add(new Token(TokenType.EOF, previousRow));

    return tokens;
  }

  public List<Token> getTokenList() {
    return tokens;
  }

  // Look at current token without proceeding
  public Token peekToken() {
    return tokens.get(currentToken);
  }

  // Proceed and fetch next token
  public Token nextToken() {
    ++currentToken;
    Token res = peekToken();
    return res;
  }

  public boolean hasMoreTokens() {
    return currentToken < tokens.size();
  }

}
