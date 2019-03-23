package org.bawaweb.lsd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;

/**
 * @author Navroz
 * This class creates the LSD
 *
 */

public class LSD {
	
	private int boundary;

	public int[][] generateLSD() {
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < this.boundary; i++) {
			list.add(i+1);
		}
		/*System.out.println("Initial List:");
		printList(list);*/
		
		Random rnd = new Random(this.boundary|System.currentTimeMillis());
		final List<Integer> startList = shuffle(list, rnd );

		return process(startList,rnd);
	}
	
	private int[][] process(final List<Integer> startList, Random rn) {
		final int size = startList.size();
		int[][] matrix = new int[size][size];
		matrix[0] = toIntArray(startList);
		for (int i = 1; i < size; i++) {
			List<Integer> list2Add = shuffle(startList, rn);
			if ( canAdd2Matrix(matrix, list2Add) ) {
				matrix[i] = toIntArray(list2Add);
			} else {
				int count = 0;
				list2Add = shuffle(startList, rn);
				while(!canAdd2Matrix(matrix, list2Add) && count<1000000000) {
					list2Add = shuffle(startList, rn);
					count += 1;
				}/*System.out.println("count = "+ count);*/
				matrix[i] = toIntArray(list2Add);
			}
		}
		if ( checkMatrix(matrix) ) {
			printMatrix(matrix);
			return matrix;
		}
		return null;
	}
	
	//	uses sum of row = (n*(n+1))/2 formula.... n = boundary
	private boolean checkMatrix(int[][] matrix) {
		boolean checked = true;
		
		int rowSum, colSum = 0;
		
		final int length = matrix.length;
		assert( this.boundary == length);
		
		final int target = ((length)*(length+1))/2;
		
		for(int row = 0; row < length; row++) {
			checked = checked && ( target == getRowSum(matrix,row) );
			if( !checked ) return false;
		}
		
		for(int col = 0; col < length; col++) {
			checked = checked && ( target == getColSum(matrix,col) );
			if( !checked ) return false;
		}
		
		
		return checked;
		
	}

	private int getColSum(int[][] matrix, int col) {
		int sum = 0;
		for (int r = 0; r < this.boundary; r++) {
			sum += matrix[r][col];
		}
		return sum;
	}

	private int getRowSum(int[][] matrix, int row) {
		int sum = 0;
		for (int c = 0; c < this.boundary; c++) {
			sum += matrix[row][c];
		}
		return sum;
	}

	private boolean checkMatrix_O(int[][] matrix) {
		int length = matrix.length;
		Set<Integer> checkSet = new HashSet<Integer>();		
		for(int i = 1; i <= length; i++) {
			checkSet.add(i);
		}
		
		int[] row = matrix[0];
		List<Integer> rowList = new ArrayList<Integer>();
		for(int i = 0; i < row.length; i++) {
			rowList.add( row[i] );
		}

		
		List<Integer> colList = new ArrayList<Integer>();
		for(int i = 0; i < length; i++) {
			colList.add(matrix[i][0]);
		}
		
		
		for(int i = 0; i < row.length; i++) {
			int aVal = row[i];
			
			if( checkSet.contains(aVal) && colList.contains(aVal) ) {
				checkSet.remove(aVal);
			} else {
				return false;
			}
		}		
		return true;
	}

	private boolean canAdd2Matrix(int[][] aMatrix, List<Integer> aList) {
		 final int size = aList.size();
		 for(int i = 0; i < size; i++) {
			 int listValue = aList.get(i);
			 List<Integer> colList = toIntList( getColumn(aMatrix,i) );
			 if( colList.contains(listValue)) {		// since we are adding rows to matrix
				 return false;
			 }
		 }
		 
		 for(int i = 0; i < size; i++) {
			 int listValue = aList.get(i);
			 List<Integer> rowList = toIntList( getRow(aMatrix,i) );
			 if ( rowList.get(i) == ( listValue ) ) {	//	traverse along the columns
				 return false;
			 }
		 }
		 
		 return true;
	}
	 
	private Set<Integer> getSetFromArray(int[] anArray) {
		Set<Integer> theSet = new HashSet<Integer>();
		final int length = anArray.length;
		for(int i = 0; i < length; i++) {
			if( anArray[i] != 0 && !theSet.contains(anArray[i]) )
				theSet.add(anArray[i]);
		}
		return theSet;
	}

	private int[] getColumn(int[][] someMatrix, final int aCol) {
		final int length = someMatrix[0].length;
		int[] theColumn = new int[length];
		for(int row = 0; row < length; row++) {
			theColumn[row] = someMatrix[row][aCol];
		}
		return theColumn;
	}
	
	private int[] getRow(int[][] someMatrix, final int aRow) {
		final int length = someMatrix[0].length;
		int[] theRow = new int[length];
		for(int col = 0; col < length; col++) {
			theRow[col] = someMatrix[aRow][col];
		}
		return theRow;
	}
	
	private void printSet(Set<Integer> aSet) {
		Iterator<Integer> iter = aSet.iterator();
		StringBuilder sb = new StringBuilder();
		while (iter.hasNext()) {
			sb.append(" " + iter.next() + " ");
		}
		System.out.println("\n\nSet is\n"+sb.toString()+"\n_______\n");
		
	}
		
	
	private void printList(List<Integer> list) {
		Iterator<Integer> it = list.iterator();
		StringBuilder x = new StringBuilder();
		while (it.hasNext()) {
			x.append(" " + it.next() + " ");
		}
		System.out.println(/*"List is\n"+*/x.toString()+"\n_______\n");
		
	}
	
	private void printArray(final int[] anArray) {
		StringBuilder x = new StringBuilder();
		for(int i = 0; i < anArray.length; i++) {
			x.append(" "+anArray[i]);
		}
		System.out.println(x.toString());
	}
	
	private void printMatrix(final int[][] matrix) {
		System.out.println("\nLSD [" + matrix[0].length + "] is\n");
		for (int row = 0; row < matrix[0].length; row++) {
			printArray(matrix[row]);
		}
		printAlphaMatrix(matrix);
	}


	private void printAlphaMatrix(final int[][] matrix) {
		Map<Integer,Character> aMap = new HashMap<Integer,Character>();
		aMap.put(	1,	'A'	);
		aMap.put(	2,	'B'	);
		aMap.put(	3,	'C'	);
		aMap.put(	4,	'D'	);
		aMap.put(	5,	'E'	);
		aMap.put(	6,	'F'	);
		aMap.put(	7,	'G'	);
		aMap.put(	8,	'H'	);
		aMap.put(	9,	'I'	);
		aMap.put(	10,	'J'	);
		aMap.put(	11,	'K'	);
		
		aMap = Collections.unmodifiableMap(aMap);	
		
		System.out.println("\nLSD_ALPHA [" + matrix[0].length + "] is\n");
		for (int row = 0; row < matrix[0].length; row++) {
			printAlphaArray(matrix[row], aMap);
		}		
	}

	private void printAlphaArray(final int[] anArray, final Map<Integer, Character> alphaMap) {
		
		StringBuilder x = new StringBuilder();
		for(int i = 0; i < anArray.length; i++) {
			x.append(" "+alphaMap.get(anArray[i]));
		}
		System.out.println(x.toString());		
	}

	/**
	  * adapted from @see java.util.ArrayList
	  * @param a
	  * @return
	  */
	/* public <T> T[] toArray(T[] a) {
	        if (a.length < size)
	            // Make a new array of a's runtime type, but my contents:
	            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
	        System.arraycopy(elementData, 0, a, 0, size);
	        if (a.length > size)
	            a[size] = null;
	        return a;
	    }*/
	private int[] toIntArray(List<Integer> aList) {
		int[] theArray = new int[aList.size()];
		Iterator<Integer> it = aList.iterator();
		int i = 0;
		while (it.hasNext()) {
			theArray[i] = it.next();
			i += 1;
		}
		return theArray;
	}

	private List<Integer> toIntList(int[] anArray) {
		List<Integer> theList = new ArrayList<Integer>();
		for(int i = 0; i < anArray.length; i++) {
			theList.add(anArray[i]);
		}
		return theList;
	}
	
	/**
	  * adapted-from---java.util.Collections
	 * Swaps the two specified elements in the specified array.
	 */
	private  void swap(Object[] arr, int i, int j) {
	    Object tmp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = tmp;
	}

	/**
	  * adapted-from--- @see java.util.Collections
	  */ 
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<Integer>  shuffle(List<Integer> list, Random rnd) {
	    int size = list.size();
	    if (size < 5 || list instanceof RandomAccess) {
	        for (int i=size; i>1; i--)
	            swap(list, i-1, rnd.nextInt(i));
	    } else {
	        Object arr[] = list.toArray();
	
	        // Shuffle array
	        for (int i=size; i>1; i--)
	            swap(arr, i-1, rnd.nextInt(i));
	
	        // Dump array back into list
	        // instead of using a raw type here, it's possible to capture
	        // the wildcard but it will require a call to a supplementary
	        // private method
	        ListIterator it = list.listIterator();
	        for (int i=0; i<arr.length; i++) {
	            it.next();
	            it.set(arr[i]);
	        }
	    }
	    return list;
	}

	/**
	  * adapted-from--- @see java.util.Collections
	  */ 
	@SuppressWarnings({"rawtypes", "unchecked"})
	private  void swap(List<?> list, int i, int j) {
	    // instead of using a raw type here, it's possible to capture
	    // the wildcard but it will require a call to a supplementary
	    // private method
	    final List l = list;
	    l.set(i, l.set(j, l.get(i)));
	}

	public LSD(int b) {
//		LSDGenerator srg = new LSDGenerator(b);
		super();
		this.boundary = b;
//		generateLSD(this.boundary);//
	}

	public static void main(String[] args) {
		int num = 5;//Integer.parseInt(args[0]);
		LSD lsd = new LSD(num);
		lsd.generateLSD();
	}

}


/*

Initial List:
 1  2  3  4  5 
_______


LSD [5] is

 3 1 2 4 5
 2 4 3 5 1
 1 3 5 2 4
 4 5 1 3 2
 5 2 4 1 3

_______________________________________________________
Initial List:
	 1  2  3  4  5  6  7 
	_______

	LSD [7] is

	 7 5 2 4 1 3 6
	 4 7 5 6 3 2 1
	 3 2 6 1 4 5 7
	 6 1 4 5 2 7 3
	 1 4 3 2 7 6 5
	 2 6 7 3 5 1 4
	 5 3 1 7 6 4 2
	 
--------------------------------------------------------

Initial List:
 1  2  3  4  5  6  7  8  9 
_______

LSD [9] is							LSD [9] is

 8 2 5 9 1 6 7 3 4					 6 3 4 1 9 5 2 7 8
 3 8 2 4 6 1 5 9 7					 7 5 8 9 1 3 6 4 2
 9 1 4 6 2 7 3 8 5					 5 6 1 4 2 9 8 3 7				
 6 7 1 8 9 3 4 5 2					 2 1 9 7 8 4 3 5 6
 4 9 6 2 5 8 1 7 3					 9 4 5 8 3 6 7 2 1
 1 5 8 3 7 4 2 6 9					 1 7 6 3 4 2 5 8 9
 2 6 7 5 3 9 8 4 1					 8 9 3 2 7 1 4 6 5
 5 3 9 7 4 2 6 1 8					 4 8 2 5 6 7 1 9 3
 7 4 3 1 8 5 9 2 6					 3 2 7 6 5 8 9 1 4

*/