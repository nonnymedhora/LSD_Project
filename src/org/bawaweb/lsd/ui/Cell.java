package org.bawaweb.lsd.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * @author Navroz
 *
 */
public class Cell extends JComponent {

	private static final long serialVersionUID = 4957160200129021594L;

	private int row;
	private int column;

	private final int height = 25;
	private final int width = 25;

	private Color color;

	public Cell() {
		super();
	}

	public Cell(int x, int y) {
		super();
		this.row = x;
		this.column = y;
	}

	public Cell(Color coll) {
		super();
		this.setColor(coll);
	}

	public Cell(int row, int column, Color color) {
		super();
		this.row = row;
		this.column = column;
		this.color = color;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(this.color);

		g2.fillRect(row, column, width, height);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
