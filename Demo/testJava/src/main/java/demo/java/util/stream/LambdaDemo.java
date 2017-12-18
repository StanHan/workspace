package demo.java.util.stream;

import java.util.Collections;
import java.util.List;

/**
 * lambda表达式: a function (or a subroutine) defined, and possibly called, without being bound to an identifier。
 * 简单点说就是：一个不用被绑定到一个标识符上，并且可能被调用的函数。
 * <h5>Lambda语法详解</h5>
 * <li>0. 抽象一下lambda表达式的一般语法： <code>
 * (Type1 param1, Type2 param2, ..., TypeN paramN) -> {
  statment1;
  statment2;
  //.............
  return statmentM;
}
 * </code> 下面陆续介绍一下lambda表达式的各种简化版
 * <li>1. 参数类型省略–绝大多数情况，编译器都可以从上下文环境中推断出lambda表达式的参数类型。这样lambda表达式就变成了： <code>
 (param1,param2, ..., paramN) -> {
  statment1;
  statment2;
  //.............
  return statmentM;
}
 *</code>
 * <li>2. 当lambda表达式的参数个数只有一个，可以省略小括号。lambda表达式简写为： <code>
 param1 -> {
  statment1;
  statment2;
  //.............
  return statmentM;
}
 *</code>
 * <li>3. 当lambda表达式只包含一条语句时，可以省略大括号、return和语句结尾的分号。lambda表达式简化为：param1 -> statment
 * <li>4. 使用Method Reference
 *
 * lambda表达式访问外部变量有一个非常重要的限制：变量不可变（只是引用不可变，而不是真正的不可变）。
 * 
 * <h5>方法引用（Method reference）和构造器引用（construct reference）</h5>
 * <p>
 * 方法引用语法格式有以下三种：
 * <li>objectName::instanceMethod
 * <li>ClassName::staticMethod
 * <li>ClassName::instanceMethod
 * 
 * 前两种方式类似，等同于把lambda表达式的参数直接当成instanceMethod|staticMethod的参数来调用。
 * 比如System.out::println等同于x->System.out.println(x)；Math::max等同于(x, y)->Math.max(x,y)。
 * 最后一种方式，等同于把lambda表达式的第一个参数当成instanceMethod的目标对象，其他剩余参数当成该方法的参数。 比如String::toLowerCase等同于x->x.toLowerCase()。
 * <p>
 * 构造器引用:
 * <li>构造器引用语法如下：ClassName::new，把lambda表达式的参数当成ClassName构造器的参数 。例如BigDecimal::new等同于x->new BigDecimal(x)。
 */
public class LambdaDemo {

    public static void main(String[] args) {

    }

    static void demo(List<String> names) {
        Collections.sort(names, (o1, o2) -> o1.compareTo(o2));
    }

}
