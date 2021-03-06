package demo.vo;

import demo.spring.service.IActions;

public class People implements IActions {
    
    private int id;
    private String name;

    @Override
    public String say(String msg) {
        System.out.println(" hello "+name+"," + msg);
        return " hello "+name+"," + msg;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
