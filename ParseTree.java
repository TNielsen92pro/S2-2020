import java.util.ArrayList;

public class ParseTree {
  public int value;
  public TokenType type;
  public String hex;

  public ArrayList<ParseTree> children;

  ParseTree() {
    children = new ArrayList<>();
  }

  public TokenType getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  public String getHex() {
    return hex;
  }
}
