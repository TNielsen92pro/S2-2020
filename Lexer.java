import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

  private int row = 1;
  private int currentToken = 0;
  private List<Token> tokens;

  public List<Token> generateTokens(String input) throws java.io.IOException {
    // Maybe switch numbers to \d etc.
    Pattern tokenPattern = Pattern.compile("[1-9][0-9]*[\t ]*.|[1-9][0-9]* +|[A-Za-z] +|#[A-Fa-f0-9]{6}|\\.|\"|\\R|[ \t]*|%.*\\n");

    Matcher m = tokenPattern.matcher(input);
    int inputPos = 0;
    tokens = new ArrayList<Token>(10000);
    int idx = 0;
    while(m.find()) {
      idx++;
      Token result = null;
	    if (m.start() != inputPos) {
	        tokens.add(new Token(TokenType.INVALID, row)); // throw error? Speed up error findings
	    }
      String matchGroup = m.group().toUpperCase();
      
      if (matchGroup.matches("FORW +")) {
        result = new Token(TokenType.FORW, row);
        System.out.println("MATCHING FOWR!");
      }
	    else if (matchGroup.matches("BACK +"))
		    result = new Token(TokenType.BACK, row);
	    else if (matchGroup.matches("LEFT +"))
		    result = new Token(TokenType.LEFT, row);
	    else if (matchGroup.matches("RIGHT +"))
        result = new Token(TokenType.RIGHT, row);
	    else if (matchGroup.matches("UP"))
        result = new Token(TokenType.UP, row);
      else if (matchGroup.matches("DOWN"))
        result = new Token(TokenType.DOWN, row);
	    else if (matchGroup.matches("#[A-F0-9]{6}"))
        result = new Token(TokenType.HEX, row, matchGroup.toString());
      else if (matchGroup.matches("[1-9][0-9]*[\t ]*\\.")) {
        // Find number that ends with dot
        String number = matchGroup.replaceAll("[^0-9]", "");
        result = new Token(TokenType.NUMBER, row, Integer.parseInt(number), true);
      }
      else if (matchGroup.matches("[1-9][0-9]* +")) {
        // REP-number, with space (and no dot since that would be caught by other number check)
        String number = matchGroup.replaceAll("[^0-9]", "");  
        result = new Token(TokenType.NUMBER, row, Integer.parseInt(number), false);
      }
      else if (matchGroup.matches("\\R"))
        row++;
      else if (matchGroup.matches("\\."))
        result = new Token(TokenType.DOT, row);
      
        String printToken = idx + ":" + matchGroup;

      // Skip whitespaces
      if(result != null) {
        
        printToken = printToken.concat("-" + result.type);
        if(result.type == TokenType.NUMBER) {
          if(result.hasDot) {
            printToken = printToken.concat(" with dot");
          }  else {
            printToken = printToken.concat(" without dot");
          }
        }
          tokens.add(result);
      } else {
        printToken = printToken.concat("-" + "Whitespace");
      }
      printToken = printToken.concat("-" + "Row " + row);
      System.out.println(printToken);
      // System.out.println("From " + m.start() + " to " + m.end());
      inputPos = m.end();
    }

    if (inputPos != input.length()) {
	    tokens.add(new Token(TokenType.INVALID, row));
    }
    tokens.add(new Token(TokenType.EOF, row));
    
    return tokens;
  }

  public List<Token> getTokenList() {
    return tokens;
  }

  // Kika på nuvarande token i indata, utan att gå vidare
  public Token peekToken() {
    return tokens.get(currentToken);
  }
  
  // Gå framåt i indata och hämta nästa token
  public Token nextToken() {
    ++currentToken;
    Token res = peekToken();
    return res;
  }

  public boolean hasMoreTokens() {
    return currentToken < tokens.size();
  }

}
