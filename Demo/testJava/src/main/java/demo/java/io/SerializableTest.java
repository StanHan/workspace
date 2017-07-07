package demo.java.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.util.SerializeUtil;

public class SerializableTest {

	public static void main(String[] args) {
		testTransient();
	}

	/**
	 * 测试 transient关键字
	 */
	public static void testTransient() {
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

	public static void test2() {

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
		SerializeUtil.serialize(bean, path);
		Bean tmp_bean = SerializeUtil.deSerialize(path);
	}

	public static void test1() {
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

class Bean implements Serializable {
	private static final long serialVersionUID = 967226365385508006L;
	private String name;
	private Date today;
	private List<String> list;
	private int age;
	private Double money;
	private transient String passwd;
	

	/////////////////////
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
