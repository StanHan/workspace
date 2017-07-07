package demo.java.lang.enumeration;

import java.util.EnumSet;

public enum Day {
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

	public static void main(String[] args) {
		for (Day d : EnumSet.range(Day.MONDAY, Day.FRIDAY)) {
			System.out.println(d);
		}
		
	}
}
