import java.util.ArrayList;

public class Parser {
  private Lexer lexer;
  private ParseTree tree;

  Parser(Lexer lexer) {
    this.lexer = lexer;
    tree = new ParseTree();
    tree.children = new ArrayList<ParseTree>(1024);
  }

  public ParseTree parse() throws CustomError {
    if (lexer.peekToken().type == TokenType.EOF) {
      return null;
    } else {
      tree.children.add(evaluate());
      tree.children.add(parse());
    }
    return tree;
  }

  public ParseTree evaluate() throws CustomError {
    ParseTree tree = new ParseTree();
    switch(lexer.peekToken().type) {
      case FORW:
        getNumberNode(tree);
      break;
      case BACK:
        getNumberNode(tree);
      break;
      case LEFT:
        getNumberNode(tree);
      break;
      case RIGHT:
        getNumberNode(tree);
      break;
      case DOWN:
        getStateNode(tree);
      break;
      case UP:
        getStateNode(tree);
      break;
      default:
        throwError();
    }
    // Move on to next token every time one is parsed
    lexer.nextToken();
    return tree;
  }

  private void getNumberNode(ParseTree tree) throws CustomError {
    tree.type = lexer.peekToken().type;
    Token next = lexer.nextToken();
    if(next.type != TokenType.NUMBER) {
      System.out.println("NaN error");
      throwError();
    }

    tree.value = next.value;
    if(!next.hasDot) {
      if(lexer.nextToken().type != TokenType.DOT) {
        System.out.println("No dot error");
        throwError();
      }
    }
  }

  private void getStateNode(ParseTree tree) throws CustomError {
    tree.type = lexer.peekToken().type;
    if(lexer.nextToken().type != TokenType.DOT) {
      throwError();
    }
  }

  public void throwError() throws CustomError {
    throw new CustomError("Syntaxfel på rad " + lexer.peekToken().row);
  }

}