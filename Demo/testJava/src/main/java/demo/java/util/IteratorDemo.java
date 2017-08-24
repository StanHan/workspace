package demo.java.util;

import java.util.Scanner;

public class IteratorDemo {

    public static void main(String[] args) {
        demoScanner();

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
