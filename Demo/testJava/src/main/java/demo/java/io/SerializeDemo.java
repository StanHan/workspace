package demo.java.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import demo.vo.Bean;

public class SerializeDemo {

	public static void main(String[] args) {
		testTransient();
	}
	
	/**
	 * 序列化对象到文件
	 * @param t
	 * @param path
	 */
	public static <T> void serialize(T t, String path) {
        try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutput.writeObject(t);
            objectOutput.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件反序列化为Java对象
     * @param path
     * @return
     */
    public static <T> T deSerialize(String path) {
        // Deserialize a string and date from a file.
        try (FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

            T t = (T) objectInputStream.readObject();
            System.out.println(t);
            return t;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 测试 transient关键字
	 */
	static void testTransient() {
		Bean user = new Bean();
		user.setName("Alexia");
		user.setPasswd("123456");

		System.out.println("read before Serializable: ");
		System.out.println("username: " + user.getName());
		System.err.println("password: " + user.getPasswd());

		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("D:/cache/user.txt"));
			os.writeObject(user); // 将User对象写进文件
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream("D:/cache/user.txt"));
			user = (Bean) is.readObject(); // 从流中读取User的数据
			is.close();

			System.out.println("\nread after Serializable: ");
			System.out.println("username: " + user.getName());
			System.err.println("password: " + user.getPasswd());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	static void test2() {

		List<String> list = new ArrayList<String>();
		list.add("Stan");
		list.add("hello");
		list.add("world");
		Bean bean = new Bean();
		bean.setList(list);
		bean.setName("Stan");
		bean.setToday(new Date());
		bean.setAge(30);
		bean.setMoney(new Double(100000000000.00));
		String path = "d:/cache/today.txt";
		serialize(bean, path);
		Bean tmp_bean = deSerialize(path);
	}

	static void test1() {
		// Serialize today's date to a file.
		try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream("d:/cache/today.txt"))) {
			objectOutput.writeObject("Today");
			objectOutput.writeObject(new Date());
			objectOutput.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Deserialize a string and date from a file.
		try (FileInputStream fileInputStream = new FileInputStream("d:/cache/today.txt");
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

			String today = (String) objectInputStream.readObject();
			Date date = (Date) objectInputStream.readObject();
			System.out.println(today);
			System.out.println(date);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
