package demo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class SerializeUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

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
}
