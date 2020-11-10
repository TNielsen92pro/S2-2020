import java.lang.Math;
import java.util.Stack;
import java.util.ArrayList;

public class Evaluator {
  private int degree = 90; // Degree 0 is from (0, 0) to (0, 1). Starts at 90 degrees
  private double x = 0;
  private double y = 0;
  private boolean penDown = false;
  private String color = "#0000FF";
  private ArrayList<ParseTree> currentChildren;
  private int currentChildIdx = 0;
  private StringBuilder result; // Needed? Maybe better than concat on regular strings.

  public Evaluator(ParseTree tree) {
    currentChildren = tree.children;
  }

  private double formatNumber(double num) {
    return Math.round(num * 10000) / 10000;
  }

  public void evaluate() {
    System.out.println("children trees size: " + currentChildren.size());
    while(currentChildIdx < currentChildren.size()) {
      ParseTree instruction = currentChildren.get(currentChildIdx);
      // System.out.println("TokenType of a node: " + instruction.type);
      TokenType currentType = instruction.type;
      System.out.println("Now going through instruction: " + currentType);
      double tempX = x;
      double tempY = y;
      String outputLine = color + " " + formatNumber(x) + formatNumber(y) + " "; // Prepare first part of string for potential output
      if(currentType != null) {
        switch(currentType) {
          case DOWN:
            penDown = true;
          break;
          case UP:
            penDown = false;
          break;
          case FORW:
            x += instruction.value * Math.sin(degree * Math.PI / 180);
            y += instruction.value * Math.cos(degree * Math.PI / 180);
          break;
          case BACK:
            x += instruction.value * Math.sin((degree + 180) * Math.PI / 180);
            y += instruction.value * Math.cos((degree + 180) * Math.PI / 180);
          break;
          case RIGHT:
            degree += instruction.value;
          break;
          case LEFT:
            degree -= instruction.value;
          break;
          default:
            System.out.println("WENT INTO EVALUATE DEFAULT??");
        }
        if(penDown && (tempX != x || tempY != y)) {
          outputLine = outputLine.concat(formatNumber(x) + " " + formatNumber(y));
          System.out.println(outputLine);
        }
        currentChildIdx++;
      }
    }
  }
}