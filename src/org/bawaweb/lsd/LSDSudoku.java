/**
 * 
 */
package org.bawaweb.lsd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bawaweb.lsd.LSDPlaced.Cell;

/**
 * @author Navroz
 *
 */
public class LSDSudoku extends LSDPlaced {

	public LSDSudoku(int b, List<Integer> rowC, List<Integer> colC, List<Integer> vals) {
		super(b,rowC,colC,vals);
	}
	
	@Override
	public int[][] generateLSD(Map<Integer, List<Cell>> rowMap, Map<Integer, List<Cell>> colMap) {
		System.out.println("Here");
		List<Integer> startList = this.getRowList(rowMap, colMap, 0);
		boolean added0 = false;
		while (!added0) {
			if (!canAdd2Matrix(this.matrix, startList, rowMap, colMap, 0)) {
				startList = this.getRowList(rowMap, colMap, 0);
			}
			added0 = true;
			System.out.println("Can-add-0");
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
					count += 1;// System.out.println("count--gld "+count+"
								// row is "+row);
				}
				this.matrix[row] = toIntArray(list2Add);
			}
		}
		if (checkMatrix(this.matrix, rowMap)) {
			System.out.println("CHECK");
			printMatrix(this.matrix);
			return this.matrix;
		}

		System.out.println("Here2");
		return null;

	}
	
	@Override
	protected List<Integer> getRowList(Map<Integer, List<Cell>> rowM, Map<Integer, List<Cell>> colM,int row) {
		Random rnd = new Random(this.boundary | System.currentTimeMillis());
		List<Integer> rowList = null;
		boolean check = false;
		int count = 1;
		while (!check && count < 1000000000) {
			System.out.println("count in getRL "+count+" row is "+row);
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
			System.out.println(" cannot add for row " + rowS);
			printList(aList);
			return false;
		}

		return true;
	}

	
	@Override
	protected boolean isOk(List<Integer> list, Map<Integer, List<Cell>> rowMap, Map<Integer, List<Cell>> colMap,
			int rowSel) {
		if (super.isOk(list, rowMap, colMap, rowSel)) {
			System.out.println("super1 ok for row [" + rowSel + "]");
			
			// check-boxes
			for (int col = 0; col < 9; col++) {
				Cell aCell = new Cell(rowSel, col);
				if (this.getSudokuBoxValues(aCell).contains(list.get(col))) {
					if (this.matrix[rowSel][col] != 0 && this.matrix[rowSel][col] == list.get(col)) { // isplaced-cell
						System.out.println(
								"isplaced [" + rowSel + "," + col + "] and val is " + this.matrix[rowSel][col]);
						continue;
					}
					System.out.println("SudokuBox contains "+list.get(col));
					System.out.println("matrix[rc] is "+this.matrix[rowSel][col]);
					return false;
				}
			}

		}
		return true;

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

	public static void main(String[] args) {
		List<Integer> rows = new ArrayList<Integer>();
		rows.add(0);
		rows.add(1);
		rows.add(7);
		rows.add(0);
		rows.add(8);

		List<Integer> cols = new ArrayList<Integer>();
		cols.add(2);
		cols.add(4);
		cols.add(1);
		cols.add(6);
		cols.add(4);

		List<Integer> vals = new ArrayList<Integer>();
		vals.add(2);
		vals.add(7);
		vals.add(9);
		vals.add(5);
		vals.add(3);

		LSDSudoku lsdS = new LSDSudoku(9, rows, cols, vals);

	}

}
