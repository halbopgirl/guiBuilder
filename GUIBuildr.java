//Name: Haleigh Jayde Doetschman
//Class: CMSC 330
//Date: 04/08/2019
//Purpose: Create and build GUI based off of input file
package guibuildr;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GUIBuildr {

    private final JFileChooser fileChooser = new JFileChooser(".");
    private Lexer lexer;

    /*
    
    GRAMMAR
    
    gui ::=     
        Window STRING '(' NUMBER ',' NUMBER ')' layout widgets End '.' 
    layout ::=     
        Layout layout_type ':' 
    layout_type ::=     
        Flow |     
        Grid '(' NUMBER ',' NUMBER [',' NUMBER ',' NUMBER] ')' 
    widgets ::=     
        widget widgets |     
        widget 
    widget ::=     
        Button STRING ';' |     
        Group radio_buttons End ';' |     
        Label STRING ';' |     
        Panel layout widgets End ';' |     
        Textfield NUMBER ';' 
    radio_buttons ::=     
        radio_button radio_buttons |     
        radio_button 
    radio_button ::=     
        Radio STRING ';'
     */
    public GUIBuildr() {
        JFrame openFrame = new JFrame("Open File");
        int returnVal = fileChooser.showOpenDialog(openFrame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileChosen = fileChooser.getSelectedFile();
            lexer = new Lexer(fileChosen);
        } else {
            JOptionPane.showMessageDialog(null, "No File Selected. Please Try Again Later.");
            System.exit(0);
        }
    }

    public static void main(String[] args) throws IOException {
        GUIBuildr project1 = new GUIBuildr();
    }
}
