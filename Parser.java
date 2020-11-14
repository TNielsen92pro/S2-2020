import java.util.ArrayList;

public class Parser {
  private Lexer lexer;
  private ParseTree tree;

  Parser(Lexer lexer) {
    this.lexer = lexer;
    tree = new ParseTree();
    tree.children = new ArrayList<ParseTree>();
  }

  public ParseTree parse() throws CustomError {
    if (lexer.peekToken().type == TokenType.EOF) {
      return null;
    } else {
      tree.children.add(parseCommand());
      lexer.nextToken();
      tree.children.add(parse());
    }
    return tree;
  }

  public ParseTree parseCommand() throws CustomError {
    ParseTree tempTree;
    TokenType type = lexer.peekToken().type;
    switch(type) {
      case FORW:
        tempTree = getNumberNode();
      break;
      case BACK:
        tempTree = getNumberNode();
      break;
      case LEFT:
        tempTree = getNumberNode();
      break;
      case RIGHT:
        tempTree = getNumberNode();
      break;
      case DOWN:
        tempTree = getStateNode();
      break;
      case UP:
        tempTree = getStateNode();
      break;
      case COLOR:
        tempTree = getColorNode();
      break;
      case REP:
        tempTree = getRepNode();
      break;
      default:
        throwError();
        tempTree = new ParseTree(); // To avoid initialization warning
    }
    return tempTree;
  }

  // Get node for positioning
  private ParseTree getNumberNode() throws CustomError {

    ParseTree tempTree = new ParseTree();
    tempTree.type = lexer.peekToken().type;
    Token next = lexer.nextToken();

    if(next.type != TokenType.NUMBER) {
      throwError();
    }

    tempTree.value = next.value;
    if(!next.hasDot) {
      if(lexer.nextToken().type != TokenType.DOT) {
        throwError();
      }
    }
    return tempTree;
  }

  // Get node for changing pen state
  private ParseTree getStateNode() throws CustomError {
    ParseTree tempTree = new ParseTree();
    tempTree.type = lexer.peekToken().type;
    if(lexer.nextToken().type != TokenType.DOT) {
      throwError();
    }
    return tempTree;
  }
  
  // Get node for color
  private ParseTree getColorNode() throws CustomError {
    ParseTree tempTree = new ParseTree();
    tempTree.type = lexer.peekToken().type;
    Token next = lexer.nextToken();
    if(next.type != TokenType.HEX) {
      throwError();
    }
    tempTree.hex = next.data;
    if(lexer.nextToken().type != TokenType.DOT) {
      throwError();
    }
    return tempTree;
  }

  // Get rep node
  private ParseTree getRepNode() throws CustomError {
    ParseTree tempTree = new ParseTree();
    tempTree.type = lexer.peekToken().type;
    Token next = lexer.nextToken();
    if(next.type != TokenType.NUMBER || next.hasDot) {
      throwError();
    }
    tempTree.value = next.value;
    next = lexer.nextToken();
    if(next.type == TokenType.CIT) {
      next = lexer.nextToken();
      if(next.type == TokenType.CIT) throwError();
      while(lexer.peekToken().type != TokenType.CIT) {
        tempTree.children.add(parseCommand());
        lexer.nextToken();
      }
    } else {
      tempTree.children.add(parseCommand());
    }
    return tempTree;
  }

  // Throw syntax error at corresponding row
  public void throwError() throws CustomError {
    throw new CustomError("Syntaxfel på rad " + lexer.peekToken().row);
  }
}

