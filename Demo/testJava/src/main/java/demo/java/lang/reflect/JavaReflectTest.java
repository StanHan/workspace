package demo.java.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavaReflectTest {

	public static void main(String[] args) throws Exception {
		testField();
	}

	public static void testAnnotation() throws Exception {
		Annotation[] annotations = User.class.getAnnotations();
		for (Annotation annotation : annotations) {
			Class _class = annotation.annotationType();
			System.out.println(_class.getName());
		}
	}

	public static void testField() throws Exception {
		System.out.println("User.class.getName() = " + User.class.getName());

		User user = User.class.newInstance();

		Field[] fields = User.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			
			Field field = fields[i];
			System.out.println("field.toString() = "+field.toString());
			field.setAccessible(true);

			String field_name = field.getName();
			Class<?> field_type = field.getType();
			String field_modifier = Modifier.toString(field.getModifiers()) + "_" + field.getModifiers();
			Object field_value = null;
			field_value = field.get(user);

			System.out.println(
					"fields[" + i + "] = " + field_modifier + " " + field_type + " " + field_name + "=" + field_value);

			fillFieldWithDefaultValue(user, field);

			field_value = field.get(user);
			System.out.println(
					"fields[" + i + "] = " + field_modifier + " " + field_type + " " + field_name + "=" + field_value);

			field.toGenericString();
			
			Annotation[] annotations = field.getDeclaredAnnotations();
			for (Annotation annotation : annotations) {
				System.out.println(annotation.toString());
			}
		}

	}

	public static void fillFieldWithDefaultValue(Object obj, Field field) {
		Class<?> field_type = field.getType();
		try {
			if (field_type.equals(int.class)) {
				field.setInt(obj, 100);
			} else if (field_type.equals(boolean.class)) {
				field.setBoolean(obj, true);
			} else if (field_type.equals(byte.class)) {
				field.setByte(obj, (byte) 100);
			} else if (field_type.equals(double.class)) {
				field.setDouble(obj, 100.00);
			} else if (field_type.equals(long.class)) {
				field.setLong(obj, 1000L);
			} else if (field_type.equals(short.class)) {
				field.setShort(obj, (short) 100);
			} else if (field_type.equals(char.class)) {
				field.setChar(obj, 'a');
			} else if (field_type.equals(float.class)) {
				field.setFloat(obj, 100.00f);
			} else if (field_type.equals(String.class)) {
				field.set(obj, "StanHan");
			} else {
				field.set(obj, null);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		/*
		 * 实列化类 方法 2
		 */
		User user = new User();
		user.setId(100);
		user.setAddress(" 武汉 ");

		// 得到类对象
		Class class_user = (Class) user.getClass();

		/*
		 * 得到类中的所有属性集合
		 */
		Field[] fs = class_user.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			Object val = f.get(user); // 得到此属性的值

			System.out.println("name:" + f.getName() + "\t value = " + val);

			String type = f.getType().toString(); // 得到此属性的类型
			if (type.endsWith("String")) {
				System.out.println(f.getType() + "\t 是 String");
				f.set(user, "12"); // 给属性设值
			} else if (type.endsWith("int") || type.endsWith("Integer")) {
				System.out.println(f.getType() + "\t 是 int");
				f.set(user, 12); // 给属性设值
			} else {
				System.out.println(f.getType() + "\t");
			}

		}

		/*
		 * 得到类中的方法
		 */
		Method[] methods = class_user.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getName().startsWith("get")) {
				System.out.print("methodName:" + method.getName() + "\t");
				System.out.println("value:" + method.invoke(user)); // 得到
																	// get方法的值
			}
		}
	}
}

class User {
	/**
	 * 身份证号
	 */
	private Integer id;
	// 年龄
	public int age;
	/* 姓名 */
	String name;
	protected String address;

	public User() {
		System.out.println(" 实例化 ");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
