package demo.java.lang;

public class ObjectDemo {

    public static void main(String[] args) {
        demoHashCode();

    }

    /**
     * 哈希算法将任意长度的二进制值映射为固定长度的较小二进制值，这个小的二进制值称为哈希值。哈希值是一段数据唯一且极其紧凑的数值表示形式。
     * 如果散列一段明文而且哪怕只更改该段落的一个字母，随后的哈希都将产生不同的值。要找到散列为同一个值的两个不同的输入，在计算上来说基本上是不可能的。
     */
    static void demoHashCode() {
        Object object = new Object();
        int hashCode = object.hashCode();
        System.out.println(hashCode);
    }

}
