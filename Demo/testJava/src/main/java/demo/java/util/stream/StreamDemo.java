package demo.java.util.stream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import demo.vo.City;
import demo.vo.Person;

/**
 * Stream定义:A sequence of elements supporting sequential and parallel aggregate operations. 支持顺序和并行聚合操作的一系列元素。
 * 
 * Java 8的Stream API充分利用Lambda表达式的特性，极大的提高编程效率和程序可读性。
 * 同时它提供串行和并行两种模式进行汇聚操作，并发模式能够充分利用多核处理器的优势，使用fork/join并行方式来拆分任务和加速处理过程。
 * 流底层核心其实是Spliterator接口的一个实现，而这个Spliterator接口其实本身就是Fork/Join并行框架的一个实现，所以归根结底要明白流的工作方式，就要明白一下Fork/Join框架的基本思想，
 * 即：以递归的方式将可以并行的任务拆分成更小的子任务，然后将每个子任务的结果合并起来生成整体的最后结果，
 *
 */
public class StreamDemo {

    static final Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            String a = String.format("%010d%08d%032d", System.currentTimeMillis() / 1000, Long.valueOf("002220"),
                    Math.abs(random.nextLong()));
            System.out.println(a);
            if (a.length() != 50) {
                break;
            }

        }
    }

    /**
     * 从结果来看，串行能保证输出的顺序性，并行不行。从内存利用率来看，并发内存占用高。如果你都CPU核更多，将会有更多的线程参与运算，进一步加快计算，符合现代的多核计算思想。 看看cpu使用率，并发情况下cpu使用率比较均匀。
     */
    @Test
    public void parallel() throws InterruptedException {
        long t1 = System.currentTimeMillis();
        IntStream.range(0, 100).forEach(i -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + i);
        });
        System.err.println("串行执行耗时：" + (System.currentTimeMillis() - t1));
        Thread.sleep(1000);
        long t2 = System.currentTimeMillis();
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":" + i);
        });
        System.err.println("并行执行耗时：" + (System.currentTimeMillis() - t2));
    }

    /**
     * peek方法生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例）， 新Stream每个元素被消费的时候都会执行给定的消费函数，并且消费函数优先执行
     */
    @Test
    public void demoPeek() {
        Stream.of(1, 2, 3, 4, 5).peek(integer -> System.out.println("accept:" + integer)).filter(e -> e > 2)
                .forEach(System.out::println);
    }

    /**
     * concat方法将两个Stream连接在一起，合成一个Stream。
     * <li>若两个输入的Stream都时排序的，则新Stream也是排序的；
     * <li>若输入的Stream中任何一个是并行的，则新的Stream也是并行的；
     * <li>若关闭新的Stream时，原两个输入的Stream都将执行关闭处理。
     */
    static void demoConcat() {
        Stream.concat(Stream.of(1, 2, 3), Stream.of(4, 5)).forEach(integer -> System.out.print(integer + "  "));
    }

    /**
     * 执行终止操作时，会终止流管道，如果您想再次遍历同一个数据集，可以设置一个新的流管道。
     */
    static void testSteam() {
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");
        Stream<String> stream = items.stream();
        long count = stream.collect(Collectors.counting());
        System.out.println("个数：" + count);
        // Stream已经关闭，不能再次计算
        count = stream.collect(Collectors.counting());
        System.out.println("再次计算个数：" + count);
    }

    @Test
    public void demoOptional() {
        String text = null;
        Optional.ofNullable(text).ifPresent(System.out::println);
        int length = Optional.ofNullable(text).map(String::length).orElse(-1);
        System.out.println(length);
    }

    /**
     * 
     */
    static void testGroup() {
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya", "banana");
        Map<String, Long> result = items.stream().limit(1000L)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(result);
        String maxCountCity = result.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getKey();
        System.out.println(maxCountCity);
        Map<Long, String> unitMap = result.entrySet().stream().map(e -> {
            Map<Long, String> m = new HashMap<>();
            m.put(e.getValue(), e.getKey());
            return m;
        }).reduce((m1, m2) -> {
            m2.forEach((k, v) -> {
                if (m2.containsKey(k)) {
                    String tmp = m2.get(k);
                    tmp = tmp + "," + v;
                    m2.put(k, tmp);
                } else {
                    m2.put(k, v);
                }
            });
            return m2;
        }).orElse(null);
        System.out.println(unitMap);

        // 会返回一个空的结果集，而不是null.
        result = items.stream().limit(1000L).filter(e -> {
            return "Stan".equals(e);
        }).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(result);
    }

    /**
     * 测试分组时key值能否为空，结果：element cannot be mapped to a null key
     */
    static void testGroupingByNull() {
        String[] array = { "a", "b", "a", null, null };
        Map<String, List<String>> map = Stream.of(array).collect(Collectors.groupingBy(e -> e));
        System.out.println(map);
    }

    /**
     * 测试FOR循环的替代方法
     */
    @Test
    public void testForCircle() {
        for (int i = 1; i < 4; i++) {
            System.out.print(i + "...");
        }
        System.out.println("");
        IntStream.range(1, 4).forEach(i -> System.out.print(i + "..."));

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++) {
            int temp = i;

            executorService.submit(new Runnable() {
                public void run() {
                    // If uncommented the next line will result in an error
                    // System.out.println("Running task " + i);
                    // local variables referenced from an inner class must be final or effectively final

                    System.out.println("Running task " + temp);
                }
            });
        }

        IntStream.range(0, 5).forEach(i -> executorService.submit(new Runnable() {
            public void run() {
                System.out.println("Running task " + i);
            }
        }));
        System.out.println("将内部类替换为拉姆达表达式");
        IntStream.range(0, 5).forEach(i -> executorService.submit(() -> System.out.println("Running task " + i)));

        executorService.shutdown();

        IntStream.rangeClosed(0, 5).forEach(e -> System.out.println("封闭范围" + e));

        System.out.println("跳过值");
        int total = 0;
        for (int i = 1; i <= 100; i = i + 3) {
            total += i;
        }

        IntStream.iterate(1, e -> e + 3).limit(34).sum();

        // 有条件的迭代
        /**
         * 与 takeWhile 方法相反的是 dropWhile，它跳过满足给定条件前的值，这两个方法都是 JDK 中非常需要的补充方法。takeWhile 方法类似于 break，而 dropWhile 则类似于
         * continue。从 Java 9 开始，它们将可用于任何类型的 Stream。
         */
        IntStream.iterate(1, e -> e + 3)
                // .takeWhile(i -> i <= 100) //available in Java 9
                .sum();

        // 使用 iterate 的逆向迭代
        IntStream.iterate(7, e -> e - 1).limit(7);
    }

    @Test
    public void demoIntStream() {
        IntStream intStream = random.ints(98);
        // 计算列表中的元素数
        IntStream.range(0, 10).forEach(value -> System.out.println(value));

        List<Integer> list = IntStream.range(1, 100).boxed().collect(Collectors.toList());
        // 计算列表中的元素数
        System.out.println(list.stream().count());
        // 计算列表中元素的平均数
        Double avarage = list.stream().collect(Collectors.averagingInt(item -> item));
        System.out.println(avarage);
        // 对列表元素进行统计
        IntSummaryStatistics iss = list.stream().collect(Collectors.summarizingInt(value -> value));
        System.out.println(iss);
        // 根据List创建Map
        Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(p -> p, q -> q * 3));
        System.out.println(map);
        // 求列表元素的最大数
        Optional<Integer> max = list.stream().reduce(Math::max);
        max.ifPresent(value -> System.out.println(value));
        // 从一堆姓名列表中找出以字母“C”开头的姓名
        String[] names = { "Fred Edwards", "Anna Cox", "Deborah Patterson", "Ruth Torres", "Shawn Powell",
                "Rose Thompson", "Rachel Barnes", "Eugene Ramirez", "Earl Flores", "Janice Reed", "Sarah Miller",
                "Patricia Kelly", "Carl Hall", "Craig Wright", "Martha Phillips", "Thomas Howard", "Steve Martinez",
                "Diana Bailey", "Kathleen Hughes", "Russell Anderson", "Theresa Perry" };
        List<String> ls = Arrays.asList(names).stream().filter(s -> s.startsWith("C")).collect(Collectors.toList());
        System.out.println(ls.toString());
        // 把所有的姓名大写、排序，再输出
        Arrays.asList(names).stream().map(String::toUpperCase).sorted().forEach(System.out::println);
    }

    /**
     * 流的元素可以是对象引用 (Stream<String>)，也可以是原始整数 (IntStream)、长整型 (LongStream) 或双精度 (DoubleStream)。 当然我们也可以用
     * Stream<Integer>、Stream<Long> >、Stream<Double>，但是 boxing 和 unboxing 会很耗时，所以特别为这三种基本数值型提供了对应的 Stream。
     * <h2>JDK 中的流来源:</h2>
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
        IntStream intStream = random.ints(98);

        BitSet bitSet = new BitSet();
        intStream = bitSet.stream();

        try {
            JarFile jarFile = new JarFile(new File(""));
            jarFile.stream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * <h2>中间操作</h2>
     * <p>
     * Intermediate主要是用来对Stream做出相应转换及限制流，实际上是将源Stream转换为一个新的Stream，以达到需求效果。
     * <p>
     * 一些操作（比如 mapToInt()）获取一种类型的流并返回一种不同类型的流；<br>
     * 中间操作始终是惰性的：调用中间操作只会设置流管道的下一个阶段，不会启动任何操作。 <br>
     * 中间操作可进一步划分为无状态 和有状态 操作。无状态操作（比如filter() 或 map()）可独立处理每个元素， 而有状态操作（比如 sorted() 或 distinct()）可合并以前看到的影响其他元素处理的元素状态。
     * <li>filter(Predicate<T>) 与预期匹配的流的元素
     * 
     * <li>map(Function<T, U>) map方法将对于Stream中包含的元素使用给定的转换函数进行转换操作，新生成的Stream只包含转换生成的元素。
     * 为了提高处理效率，官方已封装好了，三种变形：mapToDouble，mapToInt，mapToLong。
     * map传入的Lambda表达式必须是Function实例，参数可以为任意类型，而其返回值也是任性类型，javac会根据实际情景自行推断。
     * 
     * <li>flatMap(Function<T, Stream<U>>，将提供的流处理函数应用于流元素后获得的流元素。
     * 与map不同的是，该换转函数的对象是一个Stream，也不会再创建一个新的Stream，而是将原Stream的元素取代为转换的Stream。
     * flatMap有三个对于原始类型的变种方法，分别是：flatMapToInt，flatMapToLong和flatMapToDouble。
     * flatMap传入的Lambda表达式必须是Function实例，参数可以为任意类型，而其返回值类型必须是一个Stream。 flatMap 把 Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，
     * 
     * <li>distinct() 已删除了重复的流元素
     * 
     * <li>sorted() sorted方法将对原Stream进行排序，返回一个有序列的新Stream。 sorterd有两种变体sorted()，sorted(Comparator)，
     * 前者将默认使用Object.equals(Object)进行排序，而后者接受一个自定义排序规则函数(Comparator)，可按照意愿排序。
     * <li>Sorted(Comparator<T>) 按提供的比较符排序的流元素
     * 
     * <li>limit(long) 截断至所提供长度的流元素
     * 
     * <li>skip(long) skip方法将过滤掉原Stream中的前N个元素，返回剩下的元素所组成的新Stream。
     * 如果原Stream的元素个数大于N，将返回原Stream的后（原Stream长度-N）个元素所组成的新Stream； 如果原Stream的元素个数小于或等于N，将返回一个空Stream。
     * 
     * <li>takeWhile(Predicate<T>) （仅限 Java 9）在第一个提供的预期不是 true 的元素处阶段的流元素
     * <li>dropWhile(Predicate<T>) （仅限 Java 9）丢弃了所提供的预期为 true 的初始元素分段的流元素
     */
    static void intermediate() {
        Comparator<String> byLength = Comparator.comparing(String::length);
        Stream.of(1, 2, 3).flatMap(integer -> Stream.of(integer * 10)).forEach(System.out::println);
        Stream.of(1, 2, 3).map(item -> item * 10).forEach(System.out::println);
    }

    /**
     * 排序：
     * <li>sorted() 默认使用自然序排序， 其中的元素必须实现Comparable 接口
     * <li>sorted(Comparator<? super T> comparator) ：我们可以使用lambada 来创建一个Comparator 实例。可以按照升序或着降序来排序元素。
     */
    static void testSorted() {
        Collection<String> strings = Stream.generate(new Supplier<Integer>() {
            public Integer get() {
                return random.nextInt(5000);
            }
        }).limit(10).map(String::valueOf).collect(Collectors.toList());
        // 自然序排序一个list
        strings.stream().sorted().forEach(System.out::println);
        System.out.println("--------------------");
        // 自然序逆序元素，使用Comparator 提供的reverseOrder() 方法
        strings.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    /**
     * <h2>终止操作</h2>
     * <p>
     * 数据集的处理在执行终止操作时开始，比如缩减（sum() 或 max()）、应用 (forEach()) 或搜索 (findFirst())
     * 操作。终止操作会生成一个结果或副作用。执行终止操作时，会终止流管道，如果您想再次遍历同一个数据集，可以设置一个新的流管道。
     * 
     * <li>forEach(Consumer<T> action) 用于遍历Stream中的所元素，避免了使用for循环，让代码更简洁，逻辑更清晰。
     * <li>forEachOrdered方法与forEach类似，都是遍历Stream中的所有元素，不同的是，如果该Stream预先设定了顺序，会按照预先设定的顺序执行（Stream是无序的），默认为元素插入的顺序。
     * <li>toArray() 使用流的元素创建一个数组。
     * <li>reduce(...) 将流的元素聚合为一个汇总值。
     * <li>collect(...) 将流的元素聚合到一个汇总结果容器中。
     * <li>min(Comparator<T>) 通过比较符返回流的最小元素。
     * <li>max(Comparator<T>) 通过比较符返回流的最大元素。
     * <li>count() 返回Stream中元素的个数。
     * 
     * <h3>Short-circuiting</h3>
     * <li>allMatch操作用于判断Stream中的元素是否全部满足指定条件。如果全部满足条件返回true，否则返回false。
     * <li>anyMatch操作用于判断Stream中的是否有满足指定条件的元素。如果最少有一个满足条件返回true，否则返回false。
     * <li>noneMatch方法将判断Stream中的所有元素是否满足指定的条件，如果所有元素都不满足条件，返回true；否则，返回false.
     * <li>{any,all,none}Match(Predicate<T>) 返回流的任何/所有元素是否与提供的预期相匹配。
     * <li>findFirst() 返回流的第一个元素（如果有）。
     * <li>findAny() 返回流的任何元素（如果有）。
     */
    @Test
    public void terminal() {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).forEach(System.out::println);
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0).forEachOrdered(System.out::println);
    }

    public void shortCircuiting() {

    }

    /**
     * 把输入的元素们累积到一个可变的容器中，比如Collection或者StringBuilder；可变汇聚对应的只有一个方法：collect，正如其名字显示的，它可以把Stream中的要有元素收集到一个结果容器中。
     * 缩减的构建块是一个身份值和一种将两个值组合成新值的途径；可变缩减的类似方法包括：
     * <ol>
     * <li>一种生成空结果容器的途径
     * <li>一种将新元素合并到结果容器中的途径
     * <li>一种合并两个结果容器的途径
     * </ol>
     * 收集器: 传递给 collect() 的 3 个函数（创建、填充和合并结果容器）之间的关系非常重要，所以有必要提供它自己的抽象 Collector 和 collect() 的相应简化版本。 Collectors
     * 类包含许多常见聚合操作的因素，比如累加到集合中、字符串串联、缩减和其他汇总计算，以及创建汇总表（通过 groupingBy()）。
     * <h5>内置收集器：</h5>
     * <li>toList() 将元素收集到一个 List 中。
     * <li>toSet() 将元素收集到一个 Set 中。
     * <li>toCollection(Supplier<Collection>) 将元素收集到一个特定类型的 Collection 中。
     * <li>toMap(Function<T, K>, Function<T, V>) 将元素收集到一个 Map 中，依据提供的映射函数将元素转换为键值。
     * <li>summingInt(ToIntFunction<T>) 计算将提供的 int 值映射函数应用于每个元素（以及 long 和 double 版本）的结果的总和。
     * <li>summarizingInt(ToIntFunction<T>) 计算将提供的 int 值映射函数应用于每个元素（以及 long 和 double 版本）的结果的 sum、min、max、count 和
     * average。
     * <li>reducing() 向元素应用缩减（通常用作下游收集器，比如用于 groupingBy）（各种版本）。
     * <li>partitioningBy(Predicate<T>) 将元素分为两组：为其保留了提供的预期的组和未保留预期的组。
     * <li>partitioningBy(Predicate<T>, Collector) 将元素分区，使用指定的下游收集器处理每个分区。
     * <li>groupingBy(Function<T,U>) 将元素分组到一个 Map 中，其中的键是所提供的应用于流元素的函数，值是共享该键的元素列表。
     * <li>groupingBy(Function<T,U>, Collector) 将元素分组，使用指定的下游收集器来处理与每个组有关联的值。
     * <li>minBy(BinaryOperator<T>) 计算元素的最小值（与 maxBy() 相同）。
     * <li>mapping(Function<T,U>, Collector) 将提供的映射函数应用于每个元素，并使用指定的下游收集器（通常用作下游收集器本身，比如用于 groupingBy）进行处理。
     * <li>joining() 假设元素为 String 类型，将这些元素联结到一个字符串中（或许使用分隔符、前缀和后缀）。
     * <li>counting() 计算元素数量。（通常用作下游收集器。）
     */
    static void collectDemo(Collection<String> strings) {
        // 此方法使用 StringBuilder 作为结果容器。传递给 collect() 的 3 个函数使用默认构造函数创建了一个空容器，append(String)
        // 方法将一个元素添加到容器中，append(StringBuilder) 方法将一个容器合并到另一个容器中。
        StringBuilder concat = strings.stream().collect(() -> new StringBuilder(), (sb, s) -> sb.append(s),
                (sb, sb2) -> sb.append(sb2));
        // 使用方法引用可能可以比拉姆达表达式更好地表达此代码：
        StringBuilder concat2 = strings.stream().collect(StringBuilder::new, StringBuilder::append,
                StringBuilder::append);
        // 类似地，要将一个流收集到一个 HashSet 中
        Set<String> uniqueStrings = strings.stream().collect(HashSet::new, HashSet::add, HashSet::addAll);
        String concat3 = strings.stream().collect(Collectors.joining());
        Set<String> uniqueStrings2 = strings.stream().collect(Collectors.toSet());

    }

    /**
     * List 转MAP
     */
    static void testCollectors2Map() {
        String[] array = { "a", "b", "c", null };
        Map<String, String> map = Arrays.stream(array).collect(Collectors.toMap(e -> e, e -> e + " hehe!"));
        System.out.println(map);
    }

    /**
     * 收集器，一种通用的、从流生成复杂值的结构。
     * 
     * <pre>
     * <R> collect(Supplier<R> resultSupplier,
            BiConsumer<R, T> accumulator, 
            BiConsumer<R, R> combiner)
     * </pre>
     * 
     * <li>Supplier<T>接口是一个函数接口，该接口声明了一个get方法，主要用来创建返回一个指定数据类型的对象。一种生成空结果容器的途径
     * <li>BiConsumer<T, U>接口是一个函数接口，该接口声明了accept方法，并无返回值，该函数接口主要用来声明一些预期操作。一种将新元素合并到结果容器中的途径
     * <li>一种合并两个结果容器的途径
     * 
     * 
     */
    @Test
    public void collect() {
        Collection<String> strings = Stream.generate(new Supplier<Integer>() {
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

        List<String> emptyList = items.stream().filter(E -> "TAN".equals(E)).collect(Collectors.toList());
        System.out.println(emptyList == null ? "null":emptyList.size());

        Map<String, Long> result = items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(result);

        Collection<Person> people = new ArrayList<>();
        Map<City, Set<String>> lastNamesByCity = people.stream().collect(
                Collectors.groupingBy(Person::getCity, Collectors.mapping(Person::getLastName, Collectors.toSet())));

    }

    /**
     * 其他汇聚操作：
     * <p>
     * reduce方法：reduce方法非常的通用，后面介绍的count，sum等都可以使用其实现。reduce 这个方法的主要作用是把 Stream 元素组合起来。
     * 它提供一个起始值（种子），然后依照运算规则（BinaryOperator），和前面 Stream 的第一个、第二个、第 n 个元素组合。
     * 
     * 从这个意义上说，字符串拼接、数值的 sum、min、max、average 都是特殊的 reduce。
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
}
