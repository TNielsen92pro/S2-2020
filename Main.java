import java.util.Scanner;
import java.io.IOException;
import java.io.File;

public class Main{
  public static void main(String[] args) throws java.io.IOException{

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
    // Parser parser = new Parser();
    System.out.println("Done with program. " + sb.toString());
  }
}