import java.util.List;
import java.util.ArrayList;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {

  private String input;
  private int row = 1;
  private int currentToken = 0;
  private List<Token> tokens;

  /*
  private static String inputToString(InputStream f) throws java.io.IOException {
    Reader stdin = new InputStreamReader(f);
    StringBuilder buf = new StringBuilder();
    char input[] = new char[1024];
    int read = 0;
    while((read = stdin.read(input)) != -1) {
        buf.append(input, 0, read);
    }
    return buf.toString();
  }
  */

  public List<Token> generateTokens(String input) throws java.io.IOException {
    Pattern tokenPattern = Pattern.compile("[1-9][0-9]*|[A-Za-z]+| +|#[A-Fa-f0-9]{6}|\t|\\.|\"|\\n|%.*\\n");
    Matcher m = tokenPattern.matcher(input);
    int inputPos = 0;
    tokens = new ArrayList<Token>(10000);
    currentToken = 0;
    // Hitta tokens eller whitespaces
    // Token prev = new Token(TokenType.WHITESPACE, 0);

    while(m.find()) {
      Token result = null;
	    if (m.start() != inputPos) {
	        tokens.add(new Token(TokenType.INVALID, row));
	    }
      String matchGroup = m.group().toUpperCase();
      
      if (matchGroup.equals("FORW"))
		    result = new Token(TokenType.FORW, row);
	    else if (matchGroup.equals("BACK"))
		    result = new Token(TokenType.BACK, row);
	    else if (matchGroup.equals("UP"))
		    result = new Token(TokenType.UP, row);
	    else if (matchGroup.equals("LEFT"))
		    result = new Token(TokenType.LEFT, row);
	    else if (matchGroup.equals("RIGHT"))
		    result = new Token(TokenType.RIGHT, row);
      else if (matchGroup.equals("DOWN"))
        result = new Token(TokenType.DOWN, row);
	    else if (matchGroup.matches("#[A-F0-9]{6}"))
        result = new Token(TokenType.HEX, row, matchGroup.toString());
      else if (matchGroup.matches("0-9*"))
        result = new Token(TokenType.NUMBER, row);
      tokens.add(result);
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

   // Kika på nästa token i indata, utan att gå vidare
   public Token peekToken() {
    return tokens.get(currentToken);
  }
  // Hämta nästa token i indata och gå framåt i indata
  public Token nextToken() {
    ++currentToken;
    Token res = peekToken();
    return res;
  }

  public boolean hasMoreTokens() {
    return currentToken < tokens.size();
  }

}
