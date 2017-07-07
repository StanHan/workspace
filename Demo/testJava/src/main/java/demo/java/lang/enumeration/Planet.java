package demo.java.lang.enumeration;

public enum Planet {
	MERCURY(3.303e+23, 2.4397e6), VENUS(4.869e+24, 6.0518e6), EARTH(5.976e+24, 6.37814e6), MARS(6.421e+23,
			3.3972e6), JUPITER(1.9e+27, 7.1492e7), SATURN(5.688e+26, 6.0268e7), URANUS(8.686e+25,
					2.5559e7), NEPTUNE(1.024e+26, 2.4746e7), PLUTO(1.27e+22, 1.137e6);

	private final double mass; // in kilograms
	private final double radius; // in meters

	private Planet(double mass, double radius) {
		this.mass = mass;
		this.radius = radius;
	}

	public double mass() {
		return mass;
	}

	public double radius() {
		return radius;
	}

	// universal gravitational constant (m3 kg-1 s-2)
	public static final double G = 6.67300E-11;

	/**
	 * 万有引力: 两个物体之间的引力。 G:万有引力常量， m1: 物体1的质量， m2: 物体2的质量， r:
	 * 两个物体之间的距离(大小)(r表示径向矢量) 依照国际单位制，F的单位为牛顿(N)，m1和m2的单位为千克(kg)，r
	 * 的单位为米(m)，常数G近似地等于 G=6.67×10⁻¹¹ N·m²/kg²（牛顿平方米每二次方千克）。
	 * 
	 * F=GMm/rr
	 * 
	 * @return
	 */
	public double surfaceWeight(double otherMass) {
		return otherMass * G * mass / (radius * radius);
	}

	public static void main(String[] args) {
		System.out.println(Planet.EARTH.name());
		System.out.println(Planet.EARTH.toString());
		Planet[] planets = Planet.values();
		for (Planet planet : planets) {
			System.out.println(planet.name()+"["+planet.mass+","+planet.radius+"]");
		}
		System.out.println(Planet.EARTH.mass);
		System.out.println(Planet.EARTH.radius);
		System.out.println(Planet.EARTH.surfaceWeight(75.0));
	}
}