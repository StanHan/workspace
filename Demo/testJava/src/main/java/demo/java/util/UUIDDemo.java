package demo.java.util;

import java.util.UUID;

public class UUIDDemo {

    public static void main(String[] args) {
        while(true) {
            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);
            System.out.println(uuid.toString().length());
        }
        
    }

}
