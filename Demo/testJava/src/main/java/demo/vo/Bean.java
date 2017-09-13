package demo.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Bean implements Serializable {
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
