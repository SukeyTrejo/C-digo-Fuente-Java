package com.system.swing;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegularButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Color overColor;
	private Color bgColor;
	private Color overText;
	private Color bgText;
	
	public RegularButton(Color overColor, Color bgColor, Color overText, Color bgText) {
		this.overColor = overColor;
		this.bgColor = bgColor;
		this.overText = overText;
		this.bgText = bgText;
		
		setBackground(bgColor);
		setForeground(bgText);
		setContentAreaFilled(false);
		setBorder(null);
		setFocusable(false);
		setOpaque(true);
		
		MouseManager mouseManager = new MouseManager();
		addMouseListener(mouseManager);
	}

	public Color getOverColor() {
		return overColor;
	}

	public void setOverColor(Color overColor) {
		this.overColor = overColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public Color getOverText() {
		return overText;
	}

	public void setOverText(Color overText) {
		this.overText = overText;
	}

	public Color getBgText() {
		return bgText;
	}

	public void setBgText(Color bgText) {
		this.bgText = bgText;
	}
	
	
	private class MouseManager extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			setBackground(overColor);
			setForeground(overText);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			setBackground(bgColor);
			setForeground(bgText);
		}
	}
}
