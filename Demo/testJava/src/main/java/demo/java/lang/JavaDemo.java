package demo.java.lang;

public class JavaDemo {

	public static void main(String[] args) {
		// Son son = new Son();
		C c = new C();
		c.pX();

	}

	public void testOverload() {
		JavaDemo t = new JavaDemo();
		t.myMethod(null);
	}

	public void myMethod(Object o) {
		System.out.println("My Object");
	}

	public void myMethod(String s) {
		System.out.println("My String");
	}

	public static void testAdd() {
		// byte b = 2, e = 3;
		// byte f = b + e;
		// System.out.println(f);
	}

	public static void testASCII() {
		for (byte i = 0; i < Byte.MAX_VALUE; i++) {
			System.out.println(i + " = " + Character.toString((char) i));
		}
	}

	public static void testTryCatchFinally() {
		String a = null;
		try {

			System.out.println("try block.");
			System.out.println(a.indexOf("b"));
			throw new Exception("throwException.");
		} catch (Exception e) {
			System.out.println("catch block.");
			System.out.println(e);
			return;
		} finally {
			System.out.println("finally block.");
		}
		// System.out.println("end.");
	}

}

// ---------------------------------------------------------
/**
 * 泛型
 *
 * @param <T1>
 * @param <T2>
 * @param <Tn>
 */
class Name<T1, T2, Tn> {
	/* ... */
}

/**
 * Generic version of the Box class.
 * 
 * @param <T>
 *            the type of the value being boxed
 */
class Box<T> {
	// T stands for "Type"
	private T t;

	public void set(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}
}

class Box2<T> {

	private T t;

	public void set(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}

	public <U extends Number> void inspect(U u) {
		System.out.println("T: " + t.getClass().getName());
		System.out.println("U: " + u.getClass().getName());
	}

	public static void testMain(String[] args) {
		Box<Integer> integerBox = new Box<Integer>();
		integerBox.set(new Integer(10));
		// integerBox.inspect("some text"); // error: this is still String!
	}

	public static <U> void addBox(U u, java.util.List<Box<U>> boxes) {
		Box<U> box = new Box<>();
		box.set(u);
		boxes.add(box);
	}

	public static <U> void outputBoxes(java.util.List<Box<U>> boxes) {
		int counter = 0;
		for (Box<U> box : boxes) {
			U boxContents = box.get();
			System.out.println("Box #" + counter + " contains [" + boxContents.toString() + "]");
			counter++;
		}
	}
}

class Father {
	public Father() {
		System.out.println("father.");
	}
}

class Son extends Father {
	public Son() {
		System.out.println("son.");
	}
}

interface A {
	int x = 0;
}

class B {
	int x = 1;
}

class C extends B implements A {
	public void pX() {
		// System.out.println(x);//The field x is ambiguous
		System.out.println(super.x);
		// A.x = 3;// The final field A.x cannot be assigned
		System.out.println(A.x);

	}
}