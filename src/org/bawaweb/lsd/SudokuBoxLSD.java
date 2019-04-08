/**
 * 
 */
package org.bawaweb.lsd;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Navroz
 * A randomly generated 3 X 3 grid
 * containing numbers 1 to 9
 *
 */
public class SudokuBoxLSD extends LSD {

	public SudokuBoxLSD(int b) {
		super(b);
	}

	private static List<Integer> numList = new ArrayList<Integer>();
	static {
		for (int i = 0; i < 9; i++)
			numList.add(i + 1);
	}

	private int getRandomValue() {	// folows SRSWoR
		int index = new Random().nextInt(numList.size());
		int rand = numList.get(index);
		numList.remove(index);	//WoR
		return rand;
	}

	public static void main(String[] args) {
		SudokuBoxLSD s = new SudokuBoxLSD(3);
		int[][] matrix = s.generateLSD();

		final int length = matrix.length;
		assert (length == (matrix[0].length));

		for (int row = 0; row < length; row++) {
			for (int col = 0; col < length; col++) {
				matrix[row][col] = s.getRandomValue();
			}
		}
		System.out.println("\n-------------\nSudoku Box LSD");
		s.printMatrix(matrix);
	}

}
