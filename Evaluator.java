import java.lang.Math;
import java.util.ArrayList;

public class Evaluator {
  private double degree = 90.0; // Degree 0 is from (0, 0) to (0, 1). Starts at 90 degrees
  private double x;
  private double y;
  private boolean penDown;
  private String color;
  private ArrayList<ParseTree> currentChildren;
  private int currentChildIdx;
  // private StringBuilder result; // Needed? Maybe better than concat on regular strings.

  public Evaluator(ParseTree tree) {
    currentChildren = tree.children;
    color = "#0000FF";
    x = 0;
    y = 0;
    penDown = false;
    currentChildIdx = 0;
  }

  private double formatNumber(double num) {
    double multiplied = Math.round(num * 10000);
    double divided = multiplied / 10000;
    return divided;
  }

  public void evaluate() {
    while(currentChildIdx < currentChildren.size()) {
      ParseTree instruction = currentChildren.get(currentChildIdx);
      TokenType currentType = instruction.type;
      double tempX = x;
      double tempY = y;
      String outputLine = formatNumber(x) + " " + formatNumber(y) + " "; // Prepare first part of string for potential output
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
          case COLOR:
            color = instruction.hex;
          break;
          case REP:
            ArrayList<ParseTree> childrenCopy = currentChildren;
            int childIdxCopy = currentChildIdx;
            currentChildren = instruction.children;
            for(int i = 0; i < instruction.value; i++) {
              currentChildIdx = 0;
              evaluate();
            }
            currentChildren = childrenCopy;
            currentChildIdx = childIdxCopy;
          break;
          default:
            System.out.println("Somehow ended up in evaluation default?");
        }
        if(penDown && (tempX != x || tempY != y) && instruction.type != TokenType.REP) {
          String colorString = color + " ";
          outputLine = colorString.concat(outputLine + formatNumber(x) + " " + formatNumber(y));
          System.out.println(outputLine);
        }
        currentChildIdx++;
      }
    }
  }
}