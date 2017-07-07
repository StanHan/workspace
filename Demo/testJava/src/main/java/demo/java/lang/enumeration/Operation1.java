package demo.java.lang.enumeration;

public enum Operation1 {
	PLUS, MINUS, TIMES, DIVIDE;

	// Do arithmetic op represented by this constant
	double eval(double x, double y) {
		switch (this) {
		case PLUS:
			return x + y;
		case MINUS:
			return x - y;
		case TIMES:
			return x * y;
		case DIVIDE:
			return x / y;
		}
		throw new AssertionError("Unknown op: " + this);
	}
	
	public static void main(String[] args) {
		System.out.println(Operation1.PLUS.eval(7.0, 8.0));
		System.out.println(Operation1.MINUS.eval(7.0, 8.0));
		System.out.println(Operation1.TIMES.eval(7.0, 8.0));
		System.out.println(Operation1.DIVIDE.eval(7.0, 8.0));
		
		for (Operation1 op : Operation1.values()) {
			//java.util.Formatter a= null;
			System.out.printf("%f %s %f = %f%n", 7.0, op, 8.0, op.eval(7.0, 8.0));
		}
	}
}
