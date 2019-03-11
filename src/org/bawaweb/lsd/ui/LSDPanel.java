/**
 * 
 */
package org.bawaweb.lsd.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.bawaweb.lsd.LSD;

/**
 * @author Navroz
 *
 */
public class LSDPanel extends JPanel {

	private int 				DIM;
	
	private  Map<Integer,Color> colorPaletteMap = new HashMap<Integer,Color>();
	
	private Cell[][]			theCells;
	private LSD					lsd;
	

	public LSDPanel(int dIM) {
		super();
		this.DIM = dIM;
		this.setVisible(true);
		this.setUpPalette();
		this.setLayout(new GridLayout(DIM,DIM));
		this.setBorder(new BevelBorder(3,Color.LIGHT_GRAY,Color.DARK_GRAY));
		this.theCells = new Cell[DIM][DIM];
		this.lsd = new LSD(this.DIM);

		int[][] matrix = lsd.generateLSD();
		this.fillLSDPanel(matrix);
	}

	private void fillLSDPanel(int[][] matrix) {
		final int size = matrix[0].length;
		for(int row = 0; row < size; row++) {
			for( int col = 0; col < size; col++) {
				theCells[row][col] = new Cell( row, col, this.colorPaletteMap.get(matrix[row][col]));
				
				this.add( theCells[row][col]);
			}
		}	
		repaint();
	}

	private void setUpPalette() {
		this.colorPaletteMap.put(	1,	Color.BLACK		);
		this.colorPaletteMap.put(	2,	Color.BLUE		);
		this.colorPaletteMap.put(	3,	Color.GREEN		);
		this.colorPaletteMap.put(	4,	Color.RED		);
		this.colorPaletteMap.put(	5,	Color.YELLOW	);
		this.colorPaletteMap.put(	6,	Color.ORANGE	);
		this.colorPaletteMap.put(	7,	Color.MAGENTA	);
		this.colorPaletteMap.put(	8,	Color.GRAY		);
		this.colorPaletteMap.put(	9,	Color.PINK		);
		this.colorPaletteMap.put(	10,	Color.CYAN		);
		
		this.colorPaletteMap	=	Collections.unmodifiableMap( this.colorPaletteMap );
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
