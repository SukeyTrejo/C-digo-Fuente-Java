package com.system.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public class CustomPasswordField extends JPasswordField {
	private static final long serialVersionUID = 1L;
	private Color color;
    private Color focusColor;
    private Icon prefixIcon;

    public CustomPasswordField(Color color, Color focusColor){
        setBorder(new EmptyBorder(5,5,5,5));
        this.color = color;
        this.focusColor = focusColor;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if (isFocusOwner()){
            paintPrefixImage(g, focusColor);
            paintCustomBorder(g, focusColor);
        } else {
            paintPrefixImage(g, color);
            paintCustomBorder(g, color);
        }
        
        repaint();
    }

   private void paintCustomBorder(Graphics g, Color color) {
       Graphics2D g2 = (Graphics2D) g;
       g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       g2.setColor(color);
       
       if (prefixIcon != null) {
    	   g2.fillRoundRect(prefixIcon.getIconWidth() + 25, getHeight() - 2, getWidth(), 2, 10, 10);
       } else {
    	   g2.fillRoundRect(0, getHeight() - 2, getWidth(), 2, 10, 10);
       }
       
   }
   
    private void paintPrefixImage(Graphics g, Color color){
        Graphics2D g2 = (Graphics2D) g;
        if (prefixIcon != null){
            Image prefix = ((ImageIcon) prefixIcon).getImage();
            int y = (getHeight() - prefixIcon.getIconHeight()) / 2;

            //Paint image
            g2.setColor( color );
            g2.fillRect(0,0, prefixIcon.getIconWidth() + 20, getHeight());
            g2.drawImage(prefix,10, y,this);
        }
    }

    private void initBorder(){
        int left = 5;

        if (prefixIcon != null){
            left = prefixIcon.getIconWidth() + 30;
        }

        setBorder(new EmptyBorder(5,left,5,5));
    }

    public Icon getPrefixIcon(){
        return this.prefixIcon;
    }

    public void setPrefixIcon(Icon prefixIcon){
        this.prefixIcon = prefixIcon;
        initBorder();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getFocusColor() {
        return this.focusColor;
    }

    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
    }
}
