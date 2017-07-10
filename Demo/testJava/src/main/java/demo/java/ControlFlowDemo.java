package demo.java;

public class ControlFlowDemo {

	public static void main(String[] args) {
	}

	public static void continueWithLabelDemo() {

		String searchMe = "Look for a substring in me";
		String substring = "sub";
		boolean foundIt = false;

		int max = searchMe.length() - substring.length();

		test: for (int i = 0; i <= max; i++) {
			int n = substring.length();
			int j = i;
			int k = 0;
			while (n-- != 0) {
				if (searchMe.charAt(j++) != substring.charAt(k++)) {
					continue test;
				}
			}
			foundIt = true;
			break test;
		}
		System.out.println(foundIt ? "Found it" : "Didn't find it");
	}

	public static void continueDemo() {

		String searchMe = "peter piper picked a " + "peck of pickled peppers";
		int max = searchMe.length();
		int numPs = 0;

		for (int i = 0; i < max; i++) {
			// interested only in p's
			if (searchMe.charAt(i) != 'p') {
				continue;
			}
			// process p's
			numPs++;
		}
		System.out.println("Found " + numPs + " p's in the string.");
	}

	/**
	 * 跳出循环到 标签
	 */
	public static void breakWithLabelDemo() {

		int[][] arrayOfInts = { { 32, 87, 3, 589 }, { 12, 1076, 2000, 8 }, { 622, 127, 77, 955 } };
		int searchfor = 12;

		int i;
		int j = 0;
		boolean foundIt = false;

		search: for (i = 0; i < arrayOfInts.length; i++) {
			for (j = 0; j < arrayOfInts[i].length; j++) {
				if (arrayOfInts[i][j] == searchfor) {
					foundIt = true;
					break search;
				}
			}
		}

		if (foundIt) {
			System.out.println("Found " + searchfor + " at " + i + ", " + j);
		} else {
			System.out.println(searchfor + " not in the array");
		}
	}

	public static void enhancedForDemo() {
		int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		for (int item : numbers) {
			System.out.println("Count is: " + item);
		}
	}

	public static void infiniteLoop() {
		while (true) {
		}
	}

	public static void infiniteLoop2() {
		// infinite loop
		for (;;) {
			// your code goes here
		}
	}

	public static void doWhileDemo() {
		int count = 1;
		do {
			System.out.println("Count is: " + count);
			count++;
		} while (count < 11);
	}

	public static void whileDemo() {
		int count = 1;
		while (count < 11) {
			System.out.println("Count is: " + count);
			count++;
		}
	}

	
}
