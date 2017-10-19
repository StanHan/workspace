package demo.java.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;

import demo.vo.People;
import demo.vo.User;

/**
 * 要让Java程序能够运行，就得让Java类被JVM加载。Java类如果不被JVM加载就不能正常运行。正常情况下，我们运行的所有的程序在编译期时候就已经把那个类被加载了。
 * 
 * 但是在Java运行时环境中，对于任意一个类，能否知道这个类有哪些属性和方法？对于任意一个对象，能否调用它的任意一个方法？答案是肯定的。 这种动态获取类的信息以及动态调用对象的方法的功能来自于Java 反射（Reflection）机制。
 * 
 * Java反射机制容许程序在运行时加载、探知、使用编译期间完全未知的classes。 * 换言之，Java可以加载一个运行时才得知名称的class，获得其完整结构。
 * 
 * <li>静态编译：在编译时确定类型，绑定对象,即通过。
 * <li>动态编译：运行时确定类型，绑定对象。动态编译最大限度发挥了java的灵活性，体现了多态的应用，有以降低类之间的藕合性。
 * 
 * 一句话，反射机制的优点就是可以实现动态创建对象和编译，体现出很大的灵活性，特别是在J2EE的开发中.它的灵活性就表现的十分明显。
 * 
 * 比如，一个大型的软件，不可能一次就把把它设计的很完美，当这个程序编译后，发布了，当发现需要更新某些功能时，我们不可能要用户把以前的卸载，再重新安装新的版本，假如这样的话，这个软件肯定是没有多少人用的。
 * 采用静态的话，需要把整个程序重新编译一次才可以实现功能的更新，而采用反射机制的话，它就可以不用卸载，只需要在运行时才动态的创建和编译，就可以实现该功能。
 * 
 * 它的缺点是对性能有影响。使用反射基本上是一种解释操作，我们可以告诉JVM，我们希望做什么并且它满足我们的要求。这类操作总是慢于只直接执行相同的操作。
 * 
 * @author hanjy
 *
 */
public class JavaReflectDemo {

    public static void main(String[] args) throws Exception {
        // testAnnotation();
        // testField();
        // testDynamicLoadObject();
        // testConstructor();
        testParameterizedType();
    }

    static void testParameterizedType() {
        Collection<String> ids = new ArrayList<String>();
        Class<?> cls = ids.getClass();
        System.out.println(cls.toGenericString());
        String canonicalName = cls.getCanonicalName();
        System.out.println(canonicalName);
        TypeVariable<?>[] typeVariables = cls.getTypeParameters();
        for (TypeVariable<?> typeVariable : typeVariables) {
            System.out.println(typeVariable.getTypeName());
        }
        
        Type[] types = cls.getGenericInterfaces();
        for (Type type : types) {
            System.out.println(type.getTypeName());
            if (type instanceof ParameterizedType) {
                System.out.println("ok");
            }
        }
        
    }

    /**
     * 获取构造方法
     * 
     * @throws Exception
     */
    static void testConstructor() throws Exception {
        Class<?> c = null;
        c = Class.forName("java.lang.Long");

        Class array[] = { java.lang.String.class };

        System.out.println("\n-------------------------------\n");

        Constructor constructor1 = c.getConstructor(array);
        System.out.println("1、通过参数获取指定Class对象的构造方法：");
        System.out.println(constructor1.toString());

        Constructor constructor2 = c.getDeclaredConstructor(array);
        System.out.println("2、通过参数获取指定Class对象所表示的类或接口的构造方法：");
        System.out.println(constructor2.toString());

        Constructor constructor3 = c.getEnclosingConstructor();
        System.out.println("3、获取本地或匿名类Constructor 对象，它表示基础类的立即封闭构造方法。");
        if (constructor3 != null)
            System.out.println(constructor3.toString());
        else
            System.out.println("-- 没有获取到任何构造方法！");

        Constructor[] constructors = c.getConstructors();
        System.out.println("4、获取指定Class对象的所有构造方法：");
        for (int i = 0; i < constructors.length; i++) {
            System.out.println(constructors[i].toString());
        }

        System.out.println("\n-------------------------------\n");

        Type types1[] = c.getGenericInterfaces();
        System.out.println("1、返回直接实现的接口：");
        for (int i = 0; i < types1.length; i++) {
            System.out.println(types1[i].toString());
        }

        Type type1 = c.getGenericSuperclass();
        System.out.println("2、返回直接超类：");
        System.out.println(type1.toString());

        Class[] cis = c.getClasses();
        System.out.println("3、返回超类和所有实现的接口：");
        for (int i = 0; i < cis.length; i++) {
            System.out.println(cis[i].toString());
        }

        Class cs1[] = c.getInterfaces();
        System.out.println("4、实现的接口");
        for (int i = 0; i < cs1.length; i++) {
            System.out.println(cs1[i].toString());
        }

        System.out.println("\n-------------------------------\n");

        Field fs1[] = c.getFields();
        System.out.println("1、类或接口的所有可访问公共字段：");
        for (int i = 0; i < fs1.length; i++) {
            System.out.println(fs1[i].toString());
        }

        Field f1 = c.getField("MIN_VALUE");
        System.out.println("2、类或接口的指定已声明指定公共成员字段：");
        System.out.println(f1.toString());

        Field fs2[] = c.getDeclaredFields();
        System.out.println("3、类或接口所声明的所有字段：");
        for (int i = 0; i < fs2.length; i++) {
            System.out.println(fs2[i].toString());
        }

        Field f2 = c.getDeclaredField("serialVersionUID");
        System.out.println("4、类或接口的指定已声明指定字段：");
        System.out.println(f2.toString());

        System.out.println("\n-------------------------------\n");

        Method m1[] = c.getMethods();
        System.out.println("1、返回类所有的公共成员方法：");
        for (int i = 0; i < m1.length; i++) {
            System.out.println(m1[i].toString());
        }

        Method m2 = c.getMethod("longValue", new Class[] {});
        System.out.println("2、返回指定公共成员方法：");
        System.out.println(m2.toString());

    }

    /**
     * 动态加载，及运行时加载类
     */
    static void testDynamicLoadObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<People> clas = (Class<People>) Class.forName("demo.vo.People");
        People people = clas.newInstance();
        System.out.println(people.getId());
    }

    /**
     * 
     * @throws Exception
     */
    static void testField() throws Exception {
        System.out.println("User.class.getName() = " + User.class.getName());

        User user = User.class.newInstance();

        Field[] fields = User.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];
            System.out.println("field.toString() = " + field.toString());
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

    static void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        /*
         * 实列化类 方法 2
         */
        User user = new User();
        user.setId(100);
        user.setAddress(" 武汉 ");

        // 得到类对象
        Class<? extends User> class_user = user.getClass();

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
