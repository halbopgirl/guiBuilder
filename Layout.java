/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guibuildr;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

/**
 *
 * @author halbo
 */
public class Layout implements LayoutManager  {
    
 //method to create grid layout
    public GridLayout gridLayout(int rows, int cols, int horzGap, int vertGap) {
        //System.out.println("Grid Layout Created with " + rows + " rows, " + cols + " cols, " + horzGap + "px horz gap, and " + vertGap + "px vert gap.");
        GridLayout layout = new GridLayout(rows, cols);
        layout.setHgap(horzGap);
        layout.setVgap(vertGap);
        return layout;
    }
    
    public GridLayout gridLayout(int rows, int cols) {
        //System.out.println("Grid Layout Created with " + rows + " rows and " + cols + " cols.");
        GridLayout layout = new GridLayout(rows, cols);
        return layout;
    }

    //method to create flow layout
    public FlowLayout flowLayout() {
        //System.out.println("Flow Layout Created");
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.CENTER);
        return layout;
    }
    
    @Override
    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void layoutContainer(Container parent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
