import java.lang.Math;
import java.util.Stack;
import java.util.ArrayList;

public class Evaluator {
  private int degree;
  private double x;
  private double y;
  private boolean penDown;
  private String color;
  private ArrayList<ParseTree> currentChildren;
  private int currentChildIdx = 0;
  private StringBuilder result;

  public Evaluator(ParseTree tree) {
    currentChildren = tree.children;
  }

  public void evaluate() {
    while(currentChildIdx < currentChildren.size()) {
      ParseTree instruction = currentChildren.get(currentChildIdx);
      System.out.println("TokenType of a node: " + instruction.toString());
      if(instruction.type != null) {

      }
      currentChildIdx++;
    }
  }

}
