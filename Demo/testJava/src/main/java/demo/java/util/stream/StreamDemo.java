package demo.java.util.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import demo.vo.City;
import demo.vo.Person;

/**
 * 流底层核心其实是Spliterator接口的一个实现，而这个Spliterator接口其实本身就是Fork/Join并行框架的一个实现，所以归根结底要明白流的工作方式，就要明白一下Fork/Join框架的基本思想，
 * 即：以递归的方式将可以并行的任务拆分成更小的子任务，然后将每个子任务的结果合并起来生成整体的最后结果，
 *
 */
public class StreamDemo {

    public static void main(String[] args) {
        String a = "14760823,14704511,26297560,26353111,26330483,14748527,14789142,14803102,26329673,26224813,26339810,26350622,26350506,18053728,26315865,26284926,26268081,26299694,26296457,26295642,26298156,24898274,26309506,26311897,26307246,24557150,26351410,26088584,26356558,26343275,26351586,25957385,20148196,26306808,26310377,25286301,26307486,26010832,26276798,21139842,25834179,26307476,26279818,26351337,26330914,26306368,24717359,22581736,26351054,26309382,26308245,26336835,26307310,23541052,26335248,25939043,26354825,24650250,26311058,26331069,24924235,26354237,26301920,26301858,26347345,24509325,24955154,24482906,24432714,26295781,24498159,26308382,26355109,26283406,26341643,24953076,26335435,25315780,26343411,26304686,26271111,26335195,25144613,26300249,25113659,26310761,26355089,26342515,26339377,26309522,26307959,26309296,26331334,26309247,25495419,25544059,26343498,25669802,26300035,26338321,26350203,26351670,25926538,26307597,26302021,26314300,26342337,26322604,26336752,26356461,26319234,26032795,26032813,26032818,26033089,26349101,26093363,26344353,26351433,26345253,26342039,26347280,26295505,26269920,26275386,26276871,26277097,26278197,26283353,26341636,26286903,26342055,26293634,26294006,26294054,26294330,26295071,26295462,26296043,26295748,26296401,26297418,26298197,26341026,26299988,26301105,26301960,26302713,26303662,26303903,26305902,26338043,26305342,26306072,26327717,26307211,26307812,26309094,26309584,26347076,26310764,26311236,26311686,26311646,26312302,26339966,26314421,26317211,26317270,26327901,26352505,26318655,26323330,26354954,26324773,26345493,26348081,26330102,26330905,26353913,26331362,26355502,26336691,26347878,26344064,26339636,26340313,26341105,26342740,26345290,26346098,26346070,26348291,26347634,26350370,26350472,26350571,26349937,26350328,26349863,26350055,26350271,26350485,26350423,26350574,26353072,26351236,26351512,26352201,26353106,26353205,26356336,26354177,26354255,26355466,26355978";
        String[] array = a.split(",");
        System.out.println(array.length);
        Set<String> set = new HashSet<>(Arrays.asList(array));
        System.out.println(set.size());
    }

    static void testToArray() {
        String a = "1,2,3,4,5,6";
        Integer[] array = Stream.of(a.split(",")).map(Integer::valueOf).toArray(Integer[]::new);
        System.out.println(Arrays.toString(array));
    }

    /**
     * 打印不同的version版本
     */
    static void testPrintDistinctVersion() {
        String[] andoidVersion = { "5.7.1", "5.7.0", "5.6.2", "5.6.0", "5.5.0", "5.4.3", "5.4.2", "5.4.1", "5.4.0",
                "5.3.2", "5.3.1", "5.2.1", "5.2.0", "5.1.2", "5.1.1", "5.1.0", "5.0.2", "5.0.1", "4.4.0", "4.3.0",
                "4.2.0", "4.1.0", "4.0.0" };

        String[] iosVersion = { "5.7.0", "5.6.2", "5.6.0", "5.5.0", "5.4.0", "5.3.2", "5.3.0", "5.2.1", "5.1.0",
                "5.0.1", "5.0.0", "4.4.0", "4.3.1", "4.3.0", "4.2.1", "4.2.0", "4.1.0", "4.0.2", "4.0.1", "4.0.0",
                "3.4.3", "3.4.2", "3.4.1", "3.4.0", "3.3.0" };

        List<String> list = new ArrayList<String>();
        List<String> list1 = Arrays.asList(andoidVersion);
        List<String> list2 = Arrays.asList(iosVersion);
        list.addAll(list1);
        list.addAll(list2);
        list.stream().distinct().sorted().peek(e -> System.out.println(e)).collect(Collectors.toList());
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
    static void 流的来源() {
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
    static void 中间操作() {
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
    static void 终止操作() {
    }

    /**
     * 把输入的元素们累积到一个可变的容器中，比如Collection或者StringBuilder；可变汇聚对应的只有一个方法：collect，正如其名字显示的，它可以把Stream中的要有元素收集到一个结果容器中。
     */
    static void 可变汇聚() {
    }

    /**
     * List 转MAP
     */
    static void testCollectors2Map() {
        String[] array = { "a", "b", "c" };
        Map<String, String> map = Arrays.stream(array).collect(Collectors.toMap(e -> e, e -> e + " hehe!"));
        System.out.println(map);
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
    static void testCollect() {
        Collection<String> strings = Stream.generate(new Supplier<Integer>() {
            Random random = new Random();

            public Integer get() {
                return random.nextInt(5000);
            }
        }).limit(50).map(String::valueOf).collect(Collectors.toList());

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

        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");

        Map<String, Long> result = items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(result);

        Collection<Person> people = new ArrayList<>();
        Map<City, Set<String>> lastNamesByCity = people.stream().collect(
                Collectors.groupingBy(Person::getCity, Collectors.mapping(Person::getLastName, Collectors.toSet())));

        Comparator<String> byLength = Comparator.comparing(String::length);

    }

    /**
     * 其他汇聚操作：
     * <p>
     * reduce方法：reduce方法非常的通用，后面介绍的count，sum等都可以使用其实现。reduce 这个方法的主要作用是把 Stream 元素组合起来。
     * 它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。 从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的
     * reduce。
     * <p>
     * 搜索相关
     * <li>allMatch：是不是Stream中的所有元素都满足给定的匹配条件
     * <li>anyMatch：Stream中是否存在任何一个元素满足匹配条件
     * <li>findFirst: 返回Stream中的第一个元素，如果Stream为空，返回空Optional
     * <li>noneMatch：是不是Stream中的所有元素都不满足给定的匹配条件
     * <li>max和min：使用给定的比较器（Operator），返回Stream中的最大|最小值
     * 
     */
    static void reduceDemo() {
        // reduce方法的第一种形式，其方法定义如下：Optional<T> reduce(BinaryOperator<T> accumulator);
        // reduce方法接受一个函数，这个函数有两个参数，第一个参数是上次函数执行的返回值（也称为中间结果），第二个参数是stream中的元素，这个函数把这两个值相加，得到的和会被赋值给下次执行这个函数的第一个参数。
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("ints sum is:" + ints.stream().reduce((sum, item) -> sum + item).get());
        // T reduce(T identity, BinaryOperator<T> accumulator);
        // 这个定义上上面已经介绍过的基本一致，不同的是：它允许用户提供一个循环计算的初始值，如果Stream为空，就直接返回该值。而且这个方法不会返回Optional，因为其不会出现null值。
        System.out.println("ints sum is:" + ints.stream().reduce(0, (sum, item) -> sum + item));
        // 搜索相关
        System.out.println(ints.stream().allMatch(item -> item < 100));
        ints.stream().max((o1, o2) -> o1.compareTo(o2)).ifPresent(System.out::println);

        // 字符串连接，concat = "ABCD"
        String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
        // 求最小值，minValue = -3.0
        double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
        // 求和，sumValue = 10, 有起始值
        int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        // 求和，sumValue = 10, 无起始值
        sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
        // 过滤，字符串连接，concat = "ace"
        concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);
    }

    static void demoSupplier() {
        Supplier<A> supplierA = new Supplier<A>() {
            Random random = new Random();

            @Override
            public A get() {
                int i = random.nextInt(1000);
                A a = new A();
                a.setA("A" + i);
                a.setB("B" + i);
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
        // peek: 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数；
        List<B> list = Stream.generate(supplierA).limit(10).map(function).peek(e -> System.out.println(e.getA()))
                .collect(Collectors.toList());
        for (B b : list) {
            System.out.println(b);
        }
    }

}

class SupplierB implements Supplier<B> {

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