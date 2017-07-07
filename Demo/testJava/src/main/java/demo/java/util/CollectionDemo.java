package demo.java.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

public class CollectionDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testTreeSet();
		testHashSet();
	}

	public static void testCollection(){
		ArrayList<String> arrayList = new ArrayList<String>();
		LinkedList<String> linkedList = new LinkedList<String>();
		Vector<String> vector = new Vector<String>();
		Stack<String> stack = new Stack<String>();
		
		
		ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
		Map<String,Date> map = Collections.synchronizedMap(new HashMap<String,Date>());
	}
	
	public static void testTreeSet(){
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.add("han");
		treeSet.add("jun");
		treeSet.add("ying");
		treeSet.add("Stan");
		treeSet.add("abc");
		
		Iterator<String> iterator = treeSet.iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
		
		/*Object[] array = treeSet.toArray();
		for (Object string : array) {
			System.out.println(string);
		}*/
	}
	
	public static void testHashSet(){
		HashSet<String> set = new HashSet();
		set.add("han");
		set.add("jun");
		set.add("ying");
		set.add("Stan");
		set.add("abc");
		
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			System.out.println(string);
		}
		
		Object[] array = set.toArray();
		for (Object string : array) {
			System.out.println(string);
		}
	}
	
	public static void testSort(){
		List<Double> list = new ArrayList<Double>();
		list.add(7.5);
		list.add(8.5);
		list.add(6.5);
		for (Double string : list) {
			System.out.print(string + " ");
		}
		Collections.sort(list);
		System.out.println();
		for (Double string : list) {
			System.err.print(string + " ");
		}
		Collections.reverse(list);
		System.out.println();
		for (Double string : list) {
			System.err.print(string + " ");
		}
	}
	
	public static void testItorator() {
		List<String> list = new ArrayList<String>();
		list.add("6");
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		for (String string : list) {
			System.out.print(string + " ");
		}
		System.out.println();
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			System.err.print(string + " ");
		}

	}

	public static void testDeque() {
		Deque<String> deque = new ArrayDeque<String>();
		deque.addFirst("Fist1");
		deque.addFirst("Fist2");
		deque.addLast("Last1");
		deque.addLast("Last2");
		deque.offer("offer1");
		while (!deque.isEmpty()) {
			String tmpFirst = deque.poll();
			System.out.println(tmpFirst);
		}

	}

	public void testQueue() {
		Queue<String> queue;
	}

}
