//Name: Haleigh Jayde Doetschman
//Class: CMSC 330
//Date: 04/08/2019
//Purpose: Contains lexical analyzer and parser that returns a GUI

package guibuildr;

//returns tokens from input file on each call to getNextTokens
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class Lexer {

    private String token = new String();
    private final JFrame frameA;
    private Scanner fileIn;
    private Scanner scanLine;
    JFrame successFrame = new JFrame("Build Failed");
    private int dim1 = 200;
    private int dim2 = 200;
    private LayoutManager layout = null;
    private JPanel panel = new JPanel();

    //constructor that takes file as parameter
    public Lexer(File fileName) {
        frameA = createGui(fileName);
        frameA.setVisible(true);
    }

    //tokenizer
    private StringTokenizer tokens;

    //createGui() method that creates interface by reading line by line
    public final JFrame createGui(File fileName) {
        JFrame frame = new JFrame("Build Failed");
        try {
            fileIn = new Scanner(new BufferedReader(new FileReader(fileName)));
            if (fileIn.hasNextLine()) {
                scanLine = new Scanner(fileIn.nextLine());
                frame = createWindow(fileIn, scanLine);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File Read Failed");
        }
        return frame;
    }

    //createWindow(Scanner) method that ensures syntax is valid or gives error
    public JFrame createWindow(Scanner fileIn, Scanner scanIn) {
        try {
        scanLine = scanIn;
        if (scanLine.hasNext()) {
            token = scanLine.next();
            //System.out.println(token);
            if (token.equals("Window")) {
                while (scanLine.hasNext()) {
                    token = scanLine.next();
                    //System.out.println(token);
                    if (token.matches("\"" + "[a-zA-Z0-9]+" + "\"")) {
                        //System.out.println("Name should be " + token);
                        successFrame = new JFrame(getString(token));
                    } else if (token.matches("\\(" + "[0-9]+" + ",")) {
                        dim1 = Integer.parseInt((token.subSequence(1, (token.lastIndexOf(","))).toString()));
                        //System.out.println("dim1 is now " + dim1);
                    } else if (token.matches("[0-9]+" + "\\)")) {
                        dim2 = Integer.parseInt((token.subSequence(0, (token.length() - 1)).toString()));
                        //System.out.println("dim2 is now " + dim2);
                    } else if (token.matches("Layout")) {
                        panel.setLayout(createFrameLayout(scanLine, token));
                    }
                }
            } else if (token.matches("End" + "\\.+")) {
                //System.out.println("End found");
                Dimension dim = new Dimension(dim1, dim2);
                successFrame.setPreferredSize(dim);
                successFrame.setMaximumSize(dim);
                successFrame.setMinimumSize(dim);
                successFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                panel.setVisible(true);
                panel.setOpaque(true);
                successFrame.pack();
                successFrame.add(panel);
                return successFrame;
            } else {
                //System.out.println("Widget found");
                //send line to widget method
                panel = createWidget(fileIn, scanLine, panel, token);
            }
        }
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Syntax Incorrect. Build Failed.");
            System.exit(0);
        }
        while (fileIn.hasNextLine()) {
            //System.out.println("Next Line found");
            scanLine = new Scanner(fileIn.nextLine());
            createWindow(fileIn, scanLine);
        }
        //System.out.println("No more lines found");
        return successFrame;
    }

    //getString Method which takes quotes off string
    private String getString(String token) {
        String returnString = token.replaceAll("\"", "");
        return returnString;
    }

    //createFrameLayout(Scanner) method that ensures layout of frame is valid or gives error
    public LayoutManager createFrameLayout(Scanner scanIn, String token) {
        scanLine = scanIn;
        Layout layout = new Layout();
        token = scanLine.next();
        //System.out.println(token + " in create frame layout");
        if (token.matches("Flow:")) {
            //System.out.println("Flow layout");
            return layout.flowLayout();
        } else if (token.matches("Grid" + "\\(" + "[0-9]+" + ",")) {
            //System.out.println("Grid Layout");
            token = token.substring(5, (token.length() - 1));
            //System.out.println("Rows: " + token);
            int rows = Integer.valueOf(token);
            int cols = 0;
            int horzgap = 0;
            int vertgap = 0;
            if (scanLine.hasNext()) {
                token = scanLine.next();
                //System.out.println("column string: " + token);
                token = token.replace(")", "");
                token = token.replace(",", "");
                token = token.replace(":", "");
                cols = Integer.valueOf(token);
                //System.out.println("Cols: " + cols);
                try {
                    if (scanLine.hasNext()) {
                        token = scanLine.next();
                        //System.out.println("horzgap string: " + token);
                        String sub = token.substring(0, (token.length() - 1));
                        //System.out.println("HorzGap: " + sub);
                        horzgap = Integer.valueOf(sub);
                    }
                    if (scanLine.hasNext()) {
                        token = scanLine.next();
                        //System.out.println("vertgap string: " + token);
                        String sub = (token.replace(",", ""));
                        sub = (sub.replace(")", ""));
                        sub = (sub.replace(":", ""));
                        vertgap = Integer.valueOf(sub);
                        //System.out.println("VartGap:  " + sub);
                    }
                } catch (NoSuchElementException f) {
                   JOptionPane.showMessageDialog(null, "Dimension Missing. Gaps Not Added.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Dimension Missing.");
            }

            return layout.gridLayout(rows, cols, horzgap, vertgap);
        }
        //System.out.println("No layout created");
        JOptionPane.showMessageDialog(null, "No layout created! Defaulting to flow layout.");
        return layout.flowLayout();
    }

    //create widget method
    public JPanel createWidget(Scanner fileIn, Scanner scanIn, JPanel parent, String token) {
        scanLine = scanIn;
        //System.out.println(token + " in create widget");

        if (token.equals("Panel")) {
            //System.out.println("Token equaled panel");
            parent.add(createPanel(fileIn, scanLine));
            //System.out.println("Panel added");
        } else if (token.equals("Group")) {
            createGroup(fileIn, scanLine, parent);
        } else if (token.equalsIgnoreCase("Button")) {
            String buttonName = "";
            if (scanLine.hasNext()) {
                token = scanLine.next();
                token = token.replaceAll("\\;", "");
                buttonName = getString(token);
            }
            parent.add(new JButton(buttonName));

            //System.out.println("Button added");
        } else if (token.matches("Label")) {
            String labelString = "";
            while (scanLine.hasNext()) {
                token = scanLine.next();
                token = token.replaceAll("\"", "");
                token = token.replaceAll(";", "");
                labelString += token;
            }
            parent.add(new JLabel(labelString));
            //System.out.println("Label added");
        } else if (token.matches("Textfield")) {
            JTextField field = new JTextField();
            if (scanLine.hasNext()) {
                token = scanLine.next();
                token = token.substring(0, (token.length() - 1));
                //System.out.println("Token is now " + token);
                //System.out.println("token: ");
                Dimension d = new Dimension ((Integer.valueOf(token)), 20);
                field.setMinimumSize(d);
                field.setMinimumSize(d);
                field.setPreferredSize(d);
            }
            parent.add(field);
            //System.out.println("Text field added");
        } else if (token.equals("End" + ";")) {
            //do nothing
        } else if (token.equals("End" + ".")) {
            JOptionPane.showMessageDialog(null, "End prematurely found. Build Failed.");
            System.exit(0);
        } else {
            JOptionPane.showMessageDialog(null, token + " is not a valid input");
        }
        return parent;
    }

    /* //add widgets to panel method
    public JPanel addWidgetsToPanel(ArrayList<JComponent> panelWidgets) {
       while (!panelWidgets.isEmpty()) {
        JComponent component = panelWidgets.get(0);
        nestedPanel.add(component);
        panelWidgets.remove(0);
       }
       return nestedPanel;
    }*/
    
    //method to create panel
    public JPanel createPanel(Scanner fileIn, Scanner scanLine) {
        JPanel thisPanel = new JPanel();
        if (scanLine.hasNext()) {
        token = scanLine.next();
        } else {
            if (fileIn.hasNextLine()) {
                
            }
        }
        //System.out.println(token);
        if (token.matches("Layout")) {
            thisPanel.setLayout(createFrameLayout(scanLine, token));
        }
        while ((!token.equals("End" + ";")) && fileIn.hasNext()) {
            System.out.println("Token 1: " + token);
            Scanner scanIn = new Scanner(fileIn.nextLine());
            token = scanIn.next();
            System.out.println("Token 2: " + token);
            thisPanel = createWidget(fileIn, scanIn, thisPanel, token);
        }
        if (token.equals("End" + ";")) {
            //System.out.println("End; found in createPanel");
            token = "";
            return thisPanel;
        } else {
            JOptionPane.showMessageDialog(null, "Panel End Not Found. Panel not added.");
        }
        return thisPanel;
    }

    //method to create group
    public void createGroup(Scanner fileIn, Scanner scanLine, JPanel parent) {
        ButtonGroup thisGroup = new ButtonGroup();
        token = "";
        while ((!token.equals("End" + ";")) && fileIn.hasNext()) {
            if (scanLine.hasNext()) {
        token = scanLine.next();
            } else {
                scanLine = new Scanner(fileIn.nextLine());
                token = scanLine.next();
            }
        //System.out.println("In create group: " + token);
        if (token.matches("Radio")) {
            token = scanLine.next();
            token = token.replace(";", "");
            token = token.replace("\"", "");
            JRadioButton newButton = new JRadioButton(token);
            thisGroup.add(newButton);
            parent.add(newButton);
        } else {
            //System.out.println("No More Radio Buttons to add");
        }
        }
        if (token.equals("End" + ";")) {
            //System.out.println("End; found in createGroup");
            token = "";
        } else {
            JOptionPane.showMessageDialog(null, "Group End Not Found. Group Not Created.");
        }
    }
}
