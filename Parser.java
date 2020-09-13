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
        System.out.println("I entered forw token!");
        getNumberNode(tree);
      break;
      case BACK:
        System.out.println("I entered BACK token!");
        getNumberNode(tree);
      break;
      case LEFT:
        System.out.println("I entered LEFT token!");
        getNumberNode(tree);
      break;
      case RIGHT:
        System.out.println("I entered RIGHT token!");
        getNumberNode(tree);
      break;
      case DOWN:
        System.out.println("I entered fDOWNorw token!");
        getStateNode(tree);
      break;
      case UP:
        System.out.println("I entered UP token!");
        getStateNode(tree);
      break;
      default:
        throwError();
    }
    return tree;
  }

  private void getNumberNode(ParseTree tree) throws CustomError {
    tree.type = lexer.peekToken().type;
    Token next = lexer.nextToken();
    if(next.type != TokenType.NUMBER) {
      throwError();
    }
    tree.value = next.value;
    if(!next.hasDot) {
      if(lexer.nextToken().type != TokenType.DOT) {
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