import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main{
  public static void main(String[] args) throws java.io.IOException{
    try {
      //Scanner in = new Scanner(System.in);
      Scanner in = new Scanner(new File("test.txt"));
      StringBuilder sb  = new StringBuilder();

      while(in.hasNext()){
        sb.append(in.nextLine());
        sb.append("\n");
      }
      in.close();

      Lexer lexer = new Lexer();
      lexer.generateTokens(sb.toString());
      Parser parser = new Parser(lexer);
      ParseTree tree = parser.parse(); // Error here!

      // Evaluator evaluator = new Evaluator(tree);
      // evaluator.evaluate();

      /* // TESTING HERE
      Pattern tokenPattern = Pattern.compile("[1-9][0-9]*[\t ]*\\.|[1-9][0-9]* +|[A-Za-z]+ +|#[A-Fa-f0-9]{6}|\\.|\"|[\t ]*|%.*\n|\n");

      Matcher m = tokenPattern.matcher(sb.toString());
      System.out.println("Patterns found: \n");
      while(m.find()) {
        System.out.println("'" + m.group().toUpperCase() + "'");
        if( m.group().toUpperCase().matches("[1-9][0-9]*[\t ]*\\.")) {
          System.out.println("FOUND DOTTED NUMBER");
        } else if(m.group().toUpperCase().matches("[1-9][0-9]* +")) {
          System.out.println("NOT DOTTED NUMBER");

        }
      }
      // STOPPED TESTING */

      System.out.println("Done with program. " + sb.toString());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("WTFFFF?");
    }
  }
}