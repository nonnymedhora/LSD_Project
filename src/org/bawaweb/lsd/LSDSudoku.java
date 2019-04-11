/**
 * 
 */
package org.bawaweb.lsd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bawaweb.lsd.LSDPlaced.Cell;

/**
 * @author Navroz
 * A Brute-force Random Sudoku Solver for a 9 * 9 grid
 * 	==	each row contains values 1 - 9
 * 	==	each col contains values 1 - 9
 * 	==	each 3*3 grid contains values 1 - 9
 * 
 * 
 * <strike>No Recursive Backtracking;
 * just brute force random shuffling employed</strike>
 * 
 * Implemented Backtracking
 * 
 * Output
 * -------------
 * Starting with LSD[9] having placed values:

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

		 1 7 2 3 9 8 5 6 4
		 4 8 3 5 7 6 2 1 9
		 9 5 6 1 4 2 8 7 3
		 5 2 7 9 1 3 4 8 6
		 6 3 9 4 8 5 7 2 1
		 8 4 1 6 2 7 9 3 5
		 7 6 4 8 5 1 3 9 2
		 3 9 8 2 6 4 1 5 7
		 2 1 5 7 3 9 6 4 8
		
		LSD_ALPHA [9] is
		
		 A G B C I H E F D
		 D H C E G F B A I
		 I E F A D B H G C
		 E B G I A C D H F
		 F C I D H E G B A
		 H D A F B G I C E
		 G F D H E A C I B
		 C I H B F D A E G
		 B A E G C I F D H
		 
		 =======================
		 
		 LSD [9] is

		 3 7 2 1 6 8 5 9 4
		 9 1 5 2 7 4 8 3 6
		 6 8 4 5 9 3 2 7 1
		 2 3 9 6 4 7 1 5 8
		 4 5 7 3 8 1 6 2 9
		 1 6 8 9 2 5 7 4 3
		 8 4 3 7 5 6 9 1 2
		 7 9 6 4 1 2 3 8 5
		 5 2 1 8 3 9 4 6 7
		
		LSD_ALPHA [9] is
		
		 C G B A F H E I D
		 I A E B G D H C F
		 F H D E I C B G A
		 B C I F D G A E H
		 D E G C H A F B I
		 A F H I B E G D C
		 H D C G E F I A B
		 G I F D A B C H E
		 E B A H C I D F G
		 
		 
		 Note-- if more values were placed
		 time to solve increases exponentially	--	( but the puzzle does get eventually solved)
		 so not adding all places
		 for lack of patience	^__^

 *
 */
public class LSDSudoku extends LSDPlaced {
	
	public LSDSudoku(int b, List<Cell> placedCells) {
		this(b, getRows(placedCells), getCols(placedCells), getVals(placedCells));
	}
	
	public LSDSudoku(int b, List<Integer> rowC, List<Integer> colC, List<Integer> vals) {
		super(b,rowC,colC,vals);
	}
	
	@Override
	public int[][] generateLSD(Map<Integer, List<Cell>> rowMap, Map<Integer, List<Cell>> colMap) {
		List<Integer> startList = this.getRowList(rowMap, colMap, 0);
		boolean added0 = false;
		while (!added0) {
			if (!canAdd2Matrix(this.matrix, startList, rowMap, colMap, 0)) {
				startList = this.getRowList(rowMap, colMap, 0);
			}
			added0 = true;
		}

		this.matrix[0] = toIntArray(startList);
		for (int row = 1; row < 9; row++) {
			List<Integer> list2Add = getRowList(rowMap, colMap, row);
			if (canAdd2Matrix(this.matrix, list2Add, rowMap, colMap, row)) {
				this.matrix[row] = toIntArray(list2Add);
			} else {
				int count = 0;
				list2Add = getRowList(rowMap, colMap, row);
				while (!canAdd2Matrix(this.matrix, list2Add, rowMap, colMap, row) && count < 1000000000) {
					list2Add = getRowList(rowMap, colMap, row);
					count += 1;
				}
				
				if (list2Add != null) {
					this.matrix[row] = toIntArray(list2Add);
				}
//				System.out.println("list2Add is null -- Call==remove"+row);
				this.removeRow(row,rowMap);
				row -= 1;
			}
		}
		if (checkMatrix(this.matrix, rowMap)) {
			System.out.println("CHECK");
			printMatrix(this.matrix);
			return this.matrix;
		}

		return null;

	}
	
	private void removeRow(final int row, final Map<Integer, List<Cell>> rowMap) {
		if (rowMap.get(row) == null) {
			for (int col = 0; col < 9; col++) {
				this.matrix[row][col] = 0;
			}
		} else {
			if (rowMap.get(row).size() > 0) {
				List<Integer> placedCols = new ArrayList<Integer>();
				Map<Integer, Integer> placedVals = new HashMap<Integer, Integer>();
				for (Cell aCell : rowMap.get(row)) {
					placedCols.add(aCell.getCol());
					placedVals.put(aCell.getCol(), aCell.getVal());
				}
				for (int col = 0; col < 9; col++) {
					if (!placedCols.contains(col)) {
						this.matrix[row][col] = 0;
					} else {
						this.matrix[row][col] = placedVals.get(col);
					}
				}
			}
		}
//		System.out.println("removed row == " + row);
	}

	@Override
	protected List<Integer> getRowList(Map<Integer, List<Cell>> rowM, Map<Integer, List<Cell>> colM,int row) {
		Random rnd = new Random(this.boundary | System.currentTimeMillis());
		List<Integer> rowList = null;
		boolean check = false;
		int count = 1;
		while (!check && count < 1000000000) {
//			System.out.println("count in getRL "+count+" row is "+row);
			rowList = this.shuffle(getBaseStartList(), rnd);
			check = this.isOk(rowList, rowM, colM, row);
			count+=1;
		}
		return rowList;
	}

	protected boolean canAdd2Matrix(int[][] aMatrix, List<Integer> aList, Map<Integer, List<Cell>> rowMap,
			Map<Integer, List<Cell>> colMap, int rowS) {
		if (aList == null) {
			return false;
		}
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
//			System.out.println(" cannot add for row " + rowS);
//			printList(aList);
			return false;
		}

		return true;
	}

	
	@Override
	protected boolean isOk(List<Integer> list, Map<Integer, List<Cell>> rowMap, Map<Integer, List<Cell>> colMap,
			int rowSel) {
		if (super.isOk(list, rowMap, colMap, rowSel)) {
			
			// check-boxes
			for (int col = 0; col < 9; col++) {
				Cell aCell = new Cell(rowSel, col);
				if (this.getSudokuBoxValues(aCell).contains(list.get(col))) {
					if (this.matrix[rowSel][col] != 0 && this.matrix[rowSel][col] == list.get(col)) { // isplaced-cell
//						System.out.println("isplaced [" + rowSel + "," + col + "] and val is " + this.matrix[rowSel][col]);
						continue;
					}
//					System.out.println("SudokuBox contains "+list.get(col));
//					System.out.println("matrix[rc] is "+this.matrix[rowSel][col]);
					return false;
				}
			}
			return true;

		}
		return false;

	}


	private List<Integer> getSudokuBoxValues(Cell aCell) {
		int row = aCell.getRow();
		int col = aCell.getCol();
		List<Integer> boxList = new ArrayList<Integer>();

		int rowMin = -1;
		int rowMax = -1;
		int colMin = -1;
		int colMax = -1;

		if (row >= 0 && row < 3) {
			rowMin = 0;
			rowMax = 2;
			if (col >= 0 && col < 3) {
				colMin = 0;
				colMax = 2;
			} else if (col >= 3 && col < 6) {
				colMin = 3;
				colMax = 5;
			} else if (col >= 6 && col < 9) {// this.boundary){
				colMin = 6;
				colMax = 8;
			}
		} else if (row >= 3 && row < 6) {
			rowMin = 3;
			rowMax = 5;
			if (col >= 0 && col < 3) {
				colMin = 0;
				colMax = 2;
			} else if (col >= 3 && col < 6) {
				colMin = 3;
				colMax = 5;
			} else if (col >= 6 && col < 9) {// this.boundary){
				colMin = 6;
				colMax = 8;
			}
		} else if (row >= 6 && row < 9) {// this.boundary) {
			rowMin = 6;
			rowMax = 8;// this.boundary-1;
			if (col >= 0 && col < 3) {
				colMin = 0;
				colMax = 2;
			} else if (col >= 3 && col < 6) {
				colMin = 3;
				colMax = 5;
			} else if (col >= 6 && col < 9) {// this.boundary){
				colMin = 6;
				colMax = 8;
			}
		}

		for (int r = rowMin; r <= rowMax; r++) {
			for (int c = colMin; c <= colMax; c++) {
				if ((r != row && c != col) && (this.matrix[r][c] != 0)) {
					boxList.add(this.matrix[r][c]);
				}
			}
		}
		return boxList;
	}
	private static List<Integer> getVals(List<Cell> cells) {
		if(cells != null ){
			List<Integer> rows = new ArrayList<Integer>();
			for(Cell aCell : cells) {
				rows.add(aCell.getRow());
			}
			return rows;
		}
		return null;
	}

	private static List<Integer> getCols(List<Cell> cells) {
		if(cells != null ){
			List<Integer> cols = new ArrayList<Integer>();
			for(Cell aCell : cells) {
				cols.add(aCell.getCol());
			}
			return cols;
		}
		return null;
	}

	private static List<Integer> getRows(List<Cell> cells) {
		if(cells != null ){
			List<Integer> vals = new ArrayList<Integer>();
			for(Cell aCell : cells) {
				vals.add(aCell.getVal());
			}
			return vals;
		}
		return null;
	}

	public static void main(String[] args) {
		List<Integer> rows = new ArrayList<Integer>();
		rows.add(0);	rows.add(0);	rows.add(0);	rows.add(0);
		rows.add(1);	rows.add(1);	rows.add(1);	rows.add(1);
		rows.add(2);	rows.add(2);	rows.add(2);	rows.add(2);	rows.add(2);
		rows.add(3);	rows.add(3);	rows.add(3);	rows.add(3);
		rows.add(4);	rows.add(4);	rows.add(4);	rows.add(4);	rows.add(4);
		rows.add(5);	rows.add(5);	rows.add(5);	rows.add(5);	rows.add(5);
		rows.add(6);	rows.add(6);	rows.add(6);	rows.add(6);	rows.add(6);
		rows.add(7);	rows.add(7);	rows.add(7);	rows.add(7);
		rows.add(8);	rows.add(8);	rows.add(8);	rows.add(8);

		List<Integer> cols = new ArrayList<Integer>();
		cols.add(2);	cols.add(3);	cols.add(4);	cols.add(6);
		cols.add(0);	cols.add(4);	cols.add(7);	cols.add(8);
		cols.add(1);	cols.add(2);	cols.add(3);	cols.add(5);	cols.add(7);	
		cols.add(0);	cols.add(4);	cols.add(5);	cols.add(6);	
		cols.add(0);	cols.add(1);	cols.add(3);	cols.add(6);	cols.add(8);
		cols.add(0);	cols.add(2);	cols.add(5);	cols.add(7);	cols.add(8);
		cols.add(0);	cols.add(3);	cols.add(5);	cols.add(6);	cols.add(7);
		cols.add(1);	cols.add(2);	cols.add(3);	cols.add(8);
		cols.add(1);	cols.add(4);	cols.add(6);	cols.add(8);

		List<Integer> vals = new ArrayList<Integer>();
		vals.add(2);	vals.add(9);	vals.add(8);	vals.add(5);
		vals.add(4);	vals.add(7);	vals.add(1);	vals.add(3);
		vals.add(3);	vals.add(9);	vals.add(6);	vals.add(4);	vals.add(7);
		vals.add(2);	vals.add(5);	vals.add(6);	vals.add(4);
		vals.add(8);	vals.add(4);	vals.add(3);	vals.add(2);	vals.add(1);
		vals.add(9);	vals.add(7);	vals.add(1);	vals.add(8);	vals.add(6);
		vals.add(6);	vals.add(7);	vals.add(5);	vals.add(1);	vals.add(3);
		vals.add(9);	vals.add(1);	vals.add(4);	vals.add(5);
		vals.add(2);	vals.add(3);	vals.add(6);	vals.add(8);

		LSDSudoku lsdS = new LSDSudoku(9, rows, cols, vals);
		
		//v2
		/*List<Cell> cells = new ArrayList<Cell>();
		Cell acell = new Cell(0,2,2);
		cells.add(acell);
		
		LSDSudoku lsdS2 = new LSDSudoku(9,cells);*/
		
		

	}

}

//sample taken from bigfish
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
