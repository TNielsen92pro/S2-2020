import java.util.Scanner;
import java.io.File;

public class MainLocal{
  public static void main(String[] args) throws java.io.IOException{
    try {
      Scanner in = new Scanner(new File("test2.txt"));
      StringBuilder sb  = new StringBuilder();

      while(in.hasNext()){
        sb.append(in.nextLine());
        sb.append("\n");
      }
      in.close();

      Lexer lexer = new Lexer();
      lexer.generateTokens(sb.toString());
      Parser parser = new Parser(lexer);
      ParseTree tree = parser.parse();
      
      Evaluator evaluator = new Evaluator(tree);
      evaluator.evaluate();

    } catch (Exception e) {
      if(e.getMessage() != null) System.out.println(e.getMessage());
    }
  }
}