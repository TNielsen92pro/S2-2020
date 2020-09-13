enum TokenType {
  NUMBER, HEX, FORW, BACK, UP, DOWN, COLOR, REP, DOT, CIT, INVALID, EOF, RIGHT, LEFT, WHITESPACE
}

public class Token {
  public TokenType type;
  public String data;
  public int row;
  public int value;
  public boolean hasDot = false;

  public Token(TokenType type, int row) {
    this.type = type;
    this.data = null;
    this.row = row;
  }

  public Token(TokenType type, int row, String data) {
    this.type = type;
    this.data = data;
    this.row = row;
  }

  public Token(TokenType type, int row, int value, boolean hasDot) {
    this.type = type;
    this.value = value;
    this.row = row;
    this.hasDot = hasDot;
  }

  public TokenType getTokenType(){
    return type;
  }

  public String getData(){
    return data;
  }

  public int getRow(){
    return row;
  }
  
  public int getValue(){
    return value;
  }
  
  public String toString(){
    return "Token " + type;
  }

}
