package demo.java.util.stream;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

    public static void main(String[] args) {
        demoCollection();
//        demoSupplier();
    }

    /**
     * JDK 中的流来源:
     * <li>Collection.stream() 使用一个集合的元素创建一个流。
     * <li>Stream.of(T...) 使用传递给工厂方法的参数创建一个流。
     * <li>Stream.of(T[]) 使用一个数组的元素创建一个流。
     * <li>Stream.empty() 创建一个空流。
     * <li>Stream.iterate(T first, BinaryOperator<T> f) 创建一个包含序列 first, f(first), f(f(first)), ... 的无限流
     * <li>Stream.iterate(T first, Predicate<T> test, BinaryOperator<T> f) （仅限 Java 9）类似于 Stream.iterate(T first,
     * BinaryOperator<T> f)，但流在测试预期返回 false 的第一个元素上终止。
     * <li>Stream.generate(Supplier<T> f) 使用一个生成器函数创建一个无限流。
     * <li>IntStream.range(lower, upper) 创建一个由下限到上限（不含）之间的元素组成的 IntStream。
     * <li>IntStream.rangeClosed(lower, upper) 创建一个由下限到上限（含）之间的元素组成的 IntStream。
     * <li>BufferedReader.lines() 创建一个有来自 BufferedReader 的行组成的流。
     * <li>BitSet.stream() 创建一个由 BitSet 中的设置位的索引组成的 IntStream。
     * <li>Stream.chars() 创建一个与 String 中的字符对应的 IntStream。
     */
    public static void 流的来源() {
    }

    /**
     * 中间操作负责将一个流转换为另一个流，中间操作包括 filter()（选择与条件匹配的元素）、map()（根据函数来转换元素）、distinct()（删除重复）、limit()（在特定大小处截断流）和
     * sorted()。一些操作（比如 mapToInt()）获取一种类型的流并返回一种不同类型的流；中间操作始终是惰性的：调用中间操作只会设置流管道的下一个阶段，不会启动任何操作。重建操作可进一步划分为无状态 和有状态
     * 操作。无状态操作（比如 filter() 或 map()）可独立处理每个元素，而有状态操作（比如 sorted() 或 distinct()）可合并以前看到的影响其他元素处理的元素状态。
     * <li>filter(Predicate<T>) 与预期匹配的流的元素
     * <li>map(Function<T, U>) 将提供的函数应用于流的元素的结果
     * <li>flatMap(Function<T, Stream<U>> 将提供的流处理函数应用于流元素后获得的流元素
     * <li>distinct() 已删除了重复的流元素
     * <li>sorted() 按自然顺序排序的流元素
     * <li>Sorted(Comparator<T>) 按提供的比较符排序的流元素
     * <li>limit(long) 截断至所提供长度的流元素
     * <li>skip(long) 丢弃了前 N 个元素的流元素
     * <li>takeWhile(Predicate<T>) （仅限 Java 9）在第一个提供的预期不是 true 的元素处阶段的流元素
     * <li>dropWhile(Predicate<T>) （仅限 Java 9）丢弃了所提供的预期为 true 的初始元素分段的流元素
     */
    public static void 中间操作() {
    }

    /**
     * 数据集的处理在执行终止操作时开始，比如缩减（sum() 或 max()）、应用 (forEach()) 或搜索 (findFirst())
     * 操作。终止操作会生成一个结果或副作用。执行终止操作时，会终止流管道，如果您想再次遍历同一个数据集，可以设置一个新的流管道。
     * 
     * <li>forEach(Consumer<T> action) 将提供的操作应用于流的每个元素。
     * <li>toArray() 使用流的元素创建一个数组。
     * <li>reduce(...) 将流的元素聚合为一个汇总值。
     * <li>collect(...) 将流的元素聚合到一个汇总结果容器中。
     * <li>min(Comparator<T>) 通过比较符返回流的最小元素。
     * <li>max(Comparator<T>) 通过比较符返回流的最大元素。
     * <li>count() 返回流的大小。
     * <li>{any,all,none}Match(Predicate<T>) 返回流的任何/所有元素是否与提供的预期相匹配。
     * <li>findFirst() 返回流的第一个元素（如果有）。
     * <li>findAny() 返回流的任何元素（如果有）。
     */
    public static void 终止操作() {

    }

    /**
     * <code>
     * <R> collect(Supplier<R> resultSupplier,
            BiConsumer<R, T> accumulator, 
            BiConsumer<R, R> combiner)
     * </code>
     * <li>一种生成空结果容器的途径
     * <li>一种将新元素合并到结果容器中的途径
     * <li>一种合并两个结果容器的途径
     * 
     * 
     */
    public static void demoCollection() {
        Collection<String> strings = Stream.generate(new Supplier<Integer>() {
            Random random = new Random();
            public Integer get(){
                return random.nextInt(5000);
            }
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        StringBuilder concat = strings.stream().collect(() -> new StringBuilder(), (sb, s) -> sb.append(s),
                (sb, sb2) -> sb.append(sb2));
        System.out.println(concat);
        /*
         * 此方法使用 StringBuilder 作为结果容器。传递给 collect() 的 3 个函数使用默认构造函数创建了一个空容器，append(String)
         * 方法将一个元素添加到容器中，append(StringBuilder) 方法将一个容器合并到另一个容器中。 使用方法引用可能可以比拉姆达表达式更好地表达此代码：
         */

        StringBuilder concat2 = strings.stream().collect(StringBuilder::new, StringBuilder::append,
                StringBuilder::append);
        System.out.println(concat2);

        Set<String> uniqueStrings = strings.stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
        
        String a = strings.stream().collect(Collectors.joining(","));
        System.out.println(a);
    }
    
    public static void demoSupplier(){
        Supplier<A> supplierA = new Supplier<A>() {
            Random random = new Random();
            @Override
            public A get() {
                int i = random.nextInt(1000);
                A a = new A();
                a.setA("A"+i);
                a.setB("B"+i);
                return a;
            }
        };
        
        Function<A, B> function = new Function<A, B>() {

            @Override
            public B apply(A a) {
                B b = new B();
                b.setA(a.getA());
                b.setB(a.getB());
                return b;
            }
        };
//        peek: 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数；
        List<B> list = Stream.generate(supplierA).limit(10).map(function).peek(e -> System.out.println(e.getA())).collect(Collectors.toList());
        for (B b : list) {
            System.out.println(b);
        }
    }
    
}

class SupplierB implements Supplier<B>{

    private A a;
    
    public SupplierB(A a) {
        super();
        this.a = a;
    }

    @Override
    public B get() {
        B b = new B();
        b.setA(a.getA());
        b.setB(a.getB());
        return b;
    }
    
}

class A {
    private String a;
    private String b;
    public String getA() {
        return a;
    }
    public void setA(String a) {
        this.a = a;
    }
    public String getB() {
        return b;
    }
    public void setB(String b) {
        this.b = b;
    }
}

class B {
    private String a;
    private String b;
    private int c;
    public int getC() {
        return c;
    }
    public void setC(int c) {
        this.c = c;
    }
    public String getA() {
        return a;
    }
    public void setA(String a) {
        this.a = a;
    }
    public String getB() {
        return b;
    }
    public void setB(String b) {
        this.b = b;
    }
    @Override
    public String toString() {
        return "B [a=" + a + ", b=" + b + ", c=" + c + "]";
    }
    
    
}