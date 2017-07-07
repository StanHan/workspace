package demo.java.lang.enumeration;

public enum Operation2 {
	PLUS {
		double eval(double x, double y) {
			return x + y;
		}
	},
	MINUS {
		double eval(double x, double y) {
			return x - y;
		}
	},
	TIMES {
		double eval(double x, double y) {
			return x * y;
		}
	},
	DIVIDE {
		double eval(double x, double y) {
			return x / y;
		}
	};

	// Do arithmetic op represented by this constant
	abstract double eval(double x, double y);

	public static void main(String[] args) {
		System.out.println(Operation2.PLUS.eval(7.0, 8.0));
		System.out.println(Operation2.MINUS.eval(7.0, 8.0));
		System.out.println(Operation2.TIMES.eval(7.0, 8.0));
		System.out.println(Operation2.DIVIDE.eval(7.0, 8.0));

		for (Operation2 op : Operation2.values()) {
			//java.util.Formatter a= null;
			System.out.printf("%f %s %f = %f%n", 7.0, op, 8.0, op.eval(7.0, 8.0));
		}
	}
}
