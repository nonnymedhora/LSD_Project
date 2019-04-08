/**
 * 
 */
package org.bawaweb.lsd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Navroz
 * Generates a LSD for a grid with placed values
 * 
 * SampleOutput
 * ----------------
 * 		Starting with LSD[9] having placed values:

			LSD [9] is
			
			 0 0 2 0 0 0 5 0 0
			 0 0 0 0 7 0 0 0 0
			 0 0 0 0 0 0 0 0 0
			 0 0 0 0 0 0 0 0 0
			 0 0 0 0 0 0 0 0 0
			 0 0 0 0 0 0 0 0 0
			 0 0 0 0 0 0 0 0 0
			 0 9 0 0 0 0 0 0 0
			 0 0 0 0 3 0 0 0 0
			 
			 
			LSD [9] is

			 7 1 2 9 6 3 5 4 8
			 6 4 5 3 7 8 1 9 2
			 9 7 3 8 4 6 2 1 5
			 5 3 9 2 8 1 6 7 4
			 1 8 6 4 9 2 7 5 3
			 4 6 1 5 2 9 3 8 7
			 3 2 4 1 5 7 8 6 9
			 2 9 8 7 1 5 4 3 6
			 8 5 7 6 3 4 9 2 1
			
			LSD_ALPHA [9] is
			
			 G A B I F C E D H
			 F D E C G H A I B
			 I G C H D F B A E
			 E C I B H A F G D
			 A H F D I B G E C
			 D F A E B I C H G
			 C B D A E G H F I
			 B I H G A E D C F
			 H E G F C D I B A
			 
			 

 * 
 */
public class LSDPlaced extends LSD {
	
	// sample taken from bigfish
	/*
	 *			1		2		3 
	 * 	
	 * A	0 0 2	9 8 0	5 0 0
	 * 		4 0 0	0 7 0	0 1 3
	 * 		0 3 9 	6 0 4	0 7 0
	 * 
	 * B	2 0 0 	0 5 6 	4 0 0
	 * 		8 4 0	3 0 0	2 0 1
	 * 		9 0 7	0 0 1	0 8 6
	 * 
	 * C	6 0 0	7 0 5	1 3 0
	 * 		0 9 1	4 0 0	0 0 5
	 * 		0 2 0	0 3 0	6 0 8
	 * 
	 * 
	 * */
	
	
	class Cell {
		private int row;
		private int col;
		private int val;
		
		public Cell(){}
		
		public Cell(int r, int c) {
			super();
			this.row = r;
			this.col = c;
		}
		
		public Cell(int r, int c, int v) {
			super();
			this.row = r;
			this.col = c;
			this.val = v;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getCol() {
			return col;
		}

		public void setCol(int col) {
			this.col = col;
		}

		public int getVal() {
			return val;
		}

		public void setVal(int val) {
			this.val = val;
		}
		
		
	}
	
	
	
	
	// map of rows and values placed
	private Map<Integer,List<Cell>> rowsPlMap = new HashMap<Integer,List<Cell>>();
	// map of cols and values placed
	private Map<Integer,List<Cell>> colsPlMap = new HashMap<Integer,List<Cell>>();

	public LSDPlaced(int b) {
		super(b);
	}
	
	public LSDPlaced(int b, List<Integer> rowC, List<Integer> colC, List<Integer> vals) {
		super(b);
		if (doBoundaryCheck(b,rowC,colC)) {
			this.matrix = setUpMatrix(rowC, colC, vals);
			System.out.println("Starting with LSD["+b+"] having placed values:");
			this.printMatrix(this.matrix);
			this.setUpValsMap(rowC, colC, vals);
			this.generateLSD(this.rowsPlMap, this.colsPlMap);
		}
		
	}
	
	protected boolean doBoundaryCheck(int b, List<Integer> rowC, List<Integer> colC) {
		for (int row : rowC) {
			if (row < 0 || row > b - 1) {
				return false;
			}
		}
		for (int col : colC) {
			if (col < 0 || col > b - 1) {
				return false;
			}
		}
		return true;
	}

	private void setUpValsMap(List<Integer> rows, List<Integer> cols, List<Integer> vals) {
		assert (rows.size() == cols.size());
		assert (rows.size() == vals.size());

		Map<Integer, List<Cell>> rowMap = new HashMap<Integer, List<Cell>>();
		for (int i = 0; i < rows.size(); i++) {
			int row = rows.get(i);
			int col = cols.get(i);
			int val = vals.get(i);

			if (rowMap.get(row) == null) {
				List<Cell> rowList = new ArrayList<Cell>();
				rowList.add(new Cell(row, col, val));
				rowMap.put(row, rowList);
			} else {
				List<Cell> rowList = rowMap.get(row);
				rowList.add(new Cell(row, col, val));
				rowMap.put(row, rowList);
			}
		}

		Map<Integer, List<Cell>> colMap = new HashMap<Integer, List<Cell>>();
		for (int i = 0; i < cols.size(); i++) {
			int col = cols.get(i);
			int row = rows.get(i);
			int val = vals.get(i);

			if (colMap.get(col) == null) {
				List<Cell> colList = new ArrayList<Cell>();
				colList.add(new Cell(row, col, val));
				colMap.put(col, colList);
			} else {
				List<Cell> colList = colMap.get(col);
				colList.add(new Cell(row, col, val));
				colMap.put(col, colList);
			}
		}

		this.rowsPlMap = rowMap;
		this.colsPlMap = colMap;

	}

	public int[][] generateLSD(Map<Integer,List<Cell>> rowMap, Map<Integer,List<Cell>> colMap) {
		final List<Integer> startList = getRowList(rowMap, colMap, 0);

		if (canAdd2Matrix(this.matrix, startList, rowMap, colMap, 0)) {
			this.matrix[0] = toIntArray(startList);
		}

		for (int row = 1; row < 9; row++) {
			List<Integer> list2Add = getRowList(rowMap, colMap, row);
			if (canAdd2Matrix(this.matrix, list2Add, rowMap, colMap, row)) {
				this.matrix[row] = toIntArray(list2Add);
			} else {
				int count = 0;
				list2Add = getRowList(rowMap, colMap, row);
				while (!canAdd2Matrix(this.matrix, list2Add, rowMap, colMap, row) && count < 1000000000) {
					list2Add = getRowList(rowMap, colMap, row);
					count += 1;//System.out.println("count--gld "+count+" row is "+row);
				}
				this.matrix[row] = toIntArray(list2Add);
			}
		}
		if (checkMatrix(this.matrix, rowMap)) {// System.out.println("CHECK");
			printMatrix(this.matrix);
			return this.matrix;
		}

		return null;

	}

	protected boolean checkMatrix(int[][] matrix, Map<Integer, List<Cell>> rowMap) {
		if (super.checkMatrix(matrix)) {
			for (int row : rowMap.keySet()) {
				for (Cell aCell : rowMap.get(row)) {
					int col = aCell.getCol();
					int val = aCell.getVal();

					if (matrix[row][col] != val) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	protected List<Integer> getRowList(Map<Integer, List<Cell>> rowM, Map<Integer, List<Cell>> colM,int row) {
		Random rnd = new Random(this.boundary | System.currentTimeMillis());
		List<Integer> rowList = null;
		boolean check = false;
		int count = 1;
		while (!check && count < 1000000000) {//System.out.println("count--gSL "+count+" row is "+row);
			rowList = this.shuffle(getBaseStartList(), rnd);
			check = this.isOk(rowList, rowM, colM, row);
			count++;
		}
		return rowList;
	}

	protected boolean isOk(List<Integer> list, Map<Integer, List<Cell>> rowMap, Map<Integer, List<Cell>> colMap, int rowSel) {
		boolean isOk = true;
		for (int row : rowMap.keySet()) {
			for (Cell aCell : rowMap.get(row)) {
				int comp = list.get(aCell.getCol());
				if (row == rowSel) {
					if (aCell.getVal() != comp) {
						return false;
					}
				} else if (aCell.getVal() == comp) {
					return false;
				}
			}
		}

		for (int col : colMap.keySet()) {
			for (Cell aCell : colMap.get(col)) {
				int row = aCell.getRow();
				int comp = list.get(col);
				if (row == rowSel) {
					if (aCell.getVal() != comp) {
						return false;
					}
				} else if (aCell.getVal() == comp) {
					return false;
				}
			}
		}

		for (int row = 0; row < rowSel; row++) {
			for (int col = 0; col < 9; col++) {
				if (matrix[row][col] == list.get(col)) {
					return false;
				}
			}
		}
		return isOk;
	}

//	private Map<Integer, List<Integer>> add2Map(Map<Integer, List<Integer>> map, int key, int val) {
//		List<Integer> list = map.get(key);
//		if(list==null){
//			list = new ArrayList<Integer>();
//		}
//		list.add(val);
//		map.put(key,list);
//		return map;
//	}

	private int[][] setUpMatrix(List<Integer> rowL, List<Integer> colL, List<Integer> valL) {
		int[][] cells = new int[this.boundary][this.boundary];
		for (int i = 0; i < this.boundary; i++) {
			for (int j = 0; j < this.boundary; j++) {
				cells[i][j] = 0;
			}
		}
		assert (rowL.size() == colL.size());
		assert (rowL.size() == valL.size());
		for (int k = 0; k < rowL.size(); k++) {
			int row = rowL.get(k);
			int col = colL.get(k);
			int val = valL.get(k);

			cells[row][col] = val;
		}
		return cells;
	}

	
	protected boolean canAdd2Matrix(int[][] aMatrix, List<Integer> aList, 
									Map<Integer, List<Cell>> rowMap,
									Map<Integer, List<Cell>> colMap, int rowS) {
		if(aList==null){return false;}
		final int size = aList.size();
		for (int col = 0; col < size; col++) {
			int listValue = aList.get(col);
			List<Integer> colList = toIntList(getColumn(aMatrix, col));
			if (colList.contains(listValue)) {
				if (colMap.get(col) != null) {
					// since we are adding rows to matrix
					for (Cell aCell : colMap.get(col)) {
						int row = aCell.getRow();
						if (row == rowS) {// row-same-in-cell&colList--values-must-be-same
							if (aCell.getVal() != listValue) {
								return false;
							}
							continue;
						} else {// row-in-colMap-is-diff---values-must-be-different
							if (aCell.getVal() == listValue) {
								return false;
							}
							continue;
						}
					}
				}
			}
		}

		for (int row = 0; row < size; row++) {
			int listValue = aList.get(row);
			List<Integer> rowList = toIntList(getRow(aMatrix, row));
			if (rowList.get(row) == (listValue)) { // traverse along the columns
				if (rowMap.get(row) != null) {
					for (Cell aCell : rowMap.get(row)) {
						if (aCell.getRow() == rowS) {
							if (aCell.getVal() != listValue) {
								return false;
							}
							continue;
						} else {
							if (aCell.getVal() == listValue) {
								return false;
							}
							continue;
						}
					}
				}

			}
		}
		if (!this.isOk(aList, rowMap, colMap, rowS)) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		List<Integer> rows = new ArrayList<Integer>();
		rows.add(0);//	rows.add(0);	rows.add(0);
		rows.add(1);//	rows.add(1);	rows.add(1);	rows.add(1);
		rows.add(2);//	rows.add(2);	rows.add(2);	rows.add(2);	rows.add(2);
		rows.add(7);
		rows.add(0);
		rows.add(8);

		List<Integer> cols = new ArrayList<Integer>();
		cols.add(2);//	cols.add(3);	cols.add(4);
		cols.add(4);//	cols.add(0);	cols.add(7);	cols.add(8);
		cols.add(1);//	cols.add(2);	cols.add(3);	cols.add(5);	cols.add(7);
		cols.add(1);
		cols.add(6);
		cols.add(4);

		List<Integer> vals = new ArrayList<Integer>();
		vals.add(2);//	vals.add(9);	vals.add(8);
		vals.add(7);//	vals.add(4);	vals.add(1);	vals.add(3);
		vals.add(3);//	vals.add(9);	vals.add(6);	vals.add(4);	vals.add(7);
		vals.add(9);
		vals.add(5);
		vals.add(3);

		LSDPlaced lsdP = new LSDPlaced(9, rows, cols, vals);
		// lsdP.printMatrix(lsdP.matrix);
		// lsdP.generateLSD();
		
		
	}

}
