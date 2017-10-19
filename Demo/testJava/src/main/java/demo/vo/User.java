package demo.vo;

public class User {
    /**
     * 身份证号
     */
    private Integer id;
    // 年龄
    public int age;
    /* 姓名 */
    String name;
    protected String address;

    public User() {
        System.out.println(" 实例化 ");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
