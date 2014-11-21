import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**Programming Assignment 4
 * Demonstrate an understanding of inheritance, polymorphism, and abstract base classes.
 *
 * Java console application that reads drawing commands from a text file and writes 
 * Scalable Vector Graphics (SVG) to Standard Output.
 * @author Tyler_Atiburcio
 */
public class AtiburcioTyler4 {

    private static String fileName;

    public static void main(String[] args) {
        try {
            inputFile();
            readFile();

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private static void readFile() throws Exception {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            StringTokenizer token = new StringTokenizer(fileScanner.nextLine());
            String temp = token.nextToken();
            if (!temp.equalsIgnoreCase("SVG")) {
                throw new Exception("No 'SVG' Header");
            } else {
                Svg svg = new Svg(Double.parseDouble(token.nextToken()), Double.parseDouble(token.nextToken()));
                while (fileScanner.hasNext()) {
                    temp = fileScanner.next();
                    //System.out.println(temp);
                    Shape shape = null;
                    switch (temp) {
                        case "line": {
                            shape = new Line(Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()));
                            break;
                        }
                        case "rect": {
                            shape = new Rectangle(Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()));
                            break;
                        }
                        case "circle": {
                            shape = new Circle(Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()), Double.parseDouble(fileScanner.next()));
                            break;
                        }
                        default: {
                            throw new Exception("No such shape called \"" + temp + "\"");
                        }

                    }
                    String tokenOne = "";
                    do {
                        tokenOne = fileScanner.next();
                        if (!tokenOne.equalsIgnoreCase("end")) {
                            shape.addStyle(tokenOne, fileScanner.next());
                        }
                    } while (!tokenOne.equalsIgnoreCase("end"));
                    svg.addShape(shape);
                    
                }
                svg.render(System.out);
            }
        } catch (NumberFormatException e) {

            throw new Exception("Invalid input " + e.toString().split(" ")[e.toString().split(" ").length - 1]);
            //To just return the bad token of input and not the full error message
        }

    }

    /**
     * inputFile method requests the name of the file that is going to be sorted
     * if fileName is valid store the name of the file into fileName variable.
     *
     * @throws Exception If there are too many command-line arguments
     */
    private static void inputFile() throws Exception {
        Scanner scan = new Scanner(System.in);
        String input = "";
        System.out.print("\nPlease input the file which contains numbers you want to read\n---->");
        input = scan.nextLine();
        StringTokenizer token = new StringTokenizer(input, " ", true);
        if (input.isEmpty() || input.contains(" ") || token.countTokens() != 1) //If line is empty or if it contains spaces or has more then one argument throw an error
        {
            throw new Exception("Invalid command-line arguments!");
        }
        fileName = input;
    }

    static class Svg {

        private ArrayList<Shape> shapes = new ArrayList<Shape>();
        private double height, width;

        public Svg(double height, double width) {
            this.height = height;
            this.width = width;
        }

        void addShape(Shape shape) {
            this.shapes.add(shape);
        }

        void render(PrintStream out) {
            out.println("<svg width='"+ this.width+"' height='"+this.height+"'>");
            for (Shape s : shapes) {
                s.renderAttributes(out);
                out.print("style='");
                for(String mod : s.styles ) out.print(mod);                     //Print the styles that included for each shape
                out.print("'/>");                                               //Close the tag
                out.println();
            }
            out.println("</svg>");                                              //Print closing svg tag
        }

    }

    static abstract class Shape {

        private ArrayList<String> styles = new ArrayList<String>();

        public void addStyle(String key, String value) {
            //System.out.println(key + " " + value);
            styles.add(key + ":" + value + ";");
        }

        abstract void renderAttributes(PrintStream out);
    }

    static class Line extends Shape {

        private double x1, x2, y1, y2;

        public Line(double x1, double x2, double y1, double y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }

        @Override
        void renderAttributes(PrintStream out) {
            out.print("<line x1='"+this.x1 +"' y1='"+ this.y1 +"' x2='"+this.x2 +"' y2='"+ this.y2+"' ");
        }

    }

    static class Rectangle extends Shape {

        private double x, y, height, width;

        public Rectangle(double x, double y, double height, double width) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.width = width;
        }

        @Override
        void renderAttributes(PrintStream out) { 
            out.print("<rect x='"+this.x +"' y='"+ this.y +"' height='"+this.height +"' width='"+ this.width+"' ");
        }

    }

    static class Circle extends Shape {

        private double cx, cy, r;

        public Circle(double cx, double cy, double r) {
            this.cx = cx;
            this.cy = cy;
            this.r = r;
        }

        @Override
        void renderAttributes(PrintStream out) {
            out.print("<circle cx='"+this.cx +"' cy='"+ this.cy +"' r='"+this.r +"' ");
        }

    }

}
