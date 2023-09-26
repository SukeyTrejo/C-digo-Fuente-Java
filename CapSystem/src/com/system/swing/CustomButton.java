package com.system.swing;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;


public class CustomButton extends JButton {
	public static final long serialVersionUID = 1L;
	private Color color;
	private Color colorOver;
	private int radius;
	
	public CustomButton(Color color, Color colorOver) {
		setBackground(color);
		this.radius = 40;
		this.color = color;
		this.colorOver = colorOver;
		
		setContentAreaFilled(false);
		setBorder(null);
		setFocusable(false);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor( new Cursor(Cursor.HAND_CURSOR));
				setBackground(colorOver);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(color);
			}
		});
		
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		g2.setColor(getBackground());
		g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
		super.paintComponent(g);
	}


	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		setBackground(color);
	}

	public Color getColorOver() {
		return colorOver;
	}

	public void setColorOver(Color colorOver) {
		this.colorOver = colorOver;
	}


	public int getRadius() {
		return radius;
	}


	public void setRadius(int radius) {
		this.radius = (radius >= 0) ? radius : 0;
	}	
	
	
	
}
