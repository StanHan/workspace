package demo.java.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class IteratorDemo {

    public static void main(String[] args) {
//        demoScanner();
        demoIterator();
    }
    
    static void demoIterator(){
        String s = "a,b,c,d,e,f";
        String[] array = s.split(",");
        List<String> list = Arrays.asList(array);
        Iterator<String> iterator = list.iterator();
        while(iterator.hasNext()){
            String t = iterator.next();
            System.err.println(t);
        }
        
        while(iterator.hasNext()){
            String t = iterator.next();
            System.out.println(t);
        }
    }
    
    static void demoScanner(){
        try(Scanner scanner = new Scanner(System.in);){
            System.out.println("请输入：");
            while(true){
                String line = scanner.nextLine();
                System.out.println(line);
                if(line.equalsIgnoreCase("QUIT")){
                    break;
                }
            }
            String line = scanner.nextLine();
            System.out.println(line);
            System.out.println("Bye bye!");
        }
    }

}
