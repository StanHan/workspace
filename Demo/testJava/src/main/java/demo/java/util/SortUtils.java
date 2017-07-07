package demo.java.util;

/**
 * 排序工具类
 *
 */
public class SortUtils {

	public static void main(String[] args) {
//		sort();
		index();
 	}
	
	/**
	 * 排序测试
	 */
	public static void sort(){
		int[] a = {3,14,1,5,9,2,6,53,58,9,7,93,0};
		pubbleSort(a);
	}

	/**
	 * 查找测试
	 */
	public static void index(){
		int[] array = {0 ,1 ,2 ,3 ,5 ,6 ,7 ,9 ,9 ,14 ,53 ,58 ,93};
		int index = indexOf(array, 58);
		System.out.println(index);
	}
	
	/**
	 * 冒泡排序
	 */
	public static void pubbleSort(int[] array){
		for (int i = 0; i < array.length -1; i++) {//外层循环
			for (int j = 0; j < array.length-1 -i; j++) {//内层循环
				if(array[j] > array[j+1]){
					int tmp = array[j];
					array[j] = array[j+1];
					array[j+1] = tmp;
				}
			}
			print(i, array);
		}
	}
	
	/**
	 * 二叉查找算法，用于查找排好序的数组
	 * @param array
	 * @param value
	 * @return
	 */
	public static int indexOf(int[] array,int value){
		int low = 0;
		int high = array.length -1;
		int middle;
		
		while (low < high) {
			middle = (low + high)/2;
			printIndex(array, middle);
			if(value == array[middle]){
				return middle;
			}else if(value > array[middle] ){
				low = middle;
			}else{
				high = middle;
			}
		}
		
		return -1;
	}
	
	public static void print(int i,int[] array){
		System.out.print("第"+i +"趟排序后的结果为：");
		for (int j = 0; j < array.length; j++) {
			System.out.print(array[j] +" ,");
		}
		System.out.println();
	}
	
	public static void printIndex(int[] array,int middle){
		for (int i = 0; i < array.length; i++) {
			System.out.print(array[i]);
			if(i == middle){
				System.out.print("*");
			}else {
				System.out.print(" ");
			}
		}
		System.out.println();
	}
}
