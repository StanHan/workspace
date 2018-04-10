package demo.java.lang.enumeration;

/* 还可以有构造方法 */
public enum LoanCard_State {
    /*
     * 通过括号赋值,而且必须有带参构造器和一属性跟方法， 否则编译出错 赋值必须是都赋值或都不赋值，不能一部分赋值一部分不赋值 如果不赋值则不能写构造器，赋值编译也出错
     */
    正常(1), 冻结(2), 止付(3), 销户(4), 呆帐(5), 未激活(6), 核销(8), 转出(7);

    private final int value;

    public int getValue() {
        return value;
    }

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    LoanCard_State(int value) {
        this.value = value;
    }

    public static void main(String[] args) {
        System.out.println(LoanCard_State.冻结);
        System.out.println(LoanCard_State.冻结.value);
        // System.out.println(LoanCard_State.valueOf("1"));
        System.out.println(LoanCard_State.valueOf("冻结"));
        System.out.println(LoanCard_State.冻结.compareTo(正常));
        System.out.println(LoanCard_State.冻结.compareTo(冻结));
        System.out.println(LoanCard_State.冻结.compareTo(止付));
        System.out.println(LoanCard_State.冻结.compareTo(核销));

        for (LoanCard_State t : LoanCard_State.values()) {
            /* 通过getValue()取得相关枚举的值 */
            System.out.printf("%s getValue=%s,t.value=%s,t.ordinal=%s,t.name=%s,t.toString=%s.%n", t, t.getValue(),
                    t.value, t.ordinal(), t.name(), t.toString());

        }
        System.out.println();
    }
}
