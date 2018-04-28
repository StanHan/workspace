package demo.design.patterns.create;

/**
 * <h1>设计模式之Factory</h1> 工厂模式定义:提供创建对象的接口. 工厂模式中有: 工厂方法(Factory Method) 抽象工厂(Abstract Factory).
 * 
 * <h2>为何使用?</h2> 工厂模式在Java程序系统可以说是随处可见。 为什么工厂模式是如此常用？因为工厂模式就相当于创建实例对象的new，我们经常要根据类Class生成实例对象，如A a=new A()
 * 工厂模式也是用来创建实例对象的，所以以后new时就要多个心眼，是否可以考虑实用工厂模式，虽然这样做，可能多做一些工作，但会给你系统带来更大的可扩展性和尽量少的修改量。 以类Sample为例，
 * 如果我们要创建Sample的实例对象:Sample sample=new Sample(); 可是，实际情况是，通常我们都要在创建sample实例时做点初始化的工作,比如赋值 查询数据库等。
 * 首先，我们想到的是，可以使用Sample的构造函数，这样生成实例就写成: Sample sample=new Sample(参数);
 * 但是，如果创建sample实例时所做的初始化工作不是象赋值这样简单的事，可能是很长一段代码，如果也写入构造函数中，那你的代码很难看了。
 * 为什么说代码很难看，初学者可能没有这种感觉，我们分析如下，初始化工作如果是很长一段代码，说明要做的工作很多，将很多工作装入一个方法中，相当于将很多鸡蛋放在一个篮子里，是很危险的，这也是有背于Java面向对象的原则，面向对象的封装(Encapsulation)和分派(Delegation)告诉我们，尽量将长的代码分派"切割"成每段，将每段再"封装"起来(减少段和段之间偶合联系性)，这样，就会将风险分散，以后如果需要修改，只要更改每段，不会再发生牵一动百的事情。
 * 
 * 在本例中，首先，我们需要将创建实例的工作与使用实例的工作分开, 也就是说，让创建实例所需要的大量初始化工作从Sample的构造函数中分离出去。
 * 
 * 这时我们就需要Factory工厂模式来生成对象了，不能再用上面简单new Sample(参数)。还有,如果Sample有个继承如MySample,
 * 按照面向接口编程,我们需要将Sample抽象成一个接口.现在Sample是接口,有两个子类MySample 和HisSample .我们要实例化他们时,如下:
 * 
 * Sample mysample=new MySample(); Sample hissample=new HisSample();
 * 
 * 随着项目的深入,Sample可能还会"生出很多儿子出来", 那么我们要对这些儿子一个个实例化,更糟糕的是,可能还要对以前的代码进行修改:加入后来生出儿子的实例.这在传统程序中是无法避免的.
 * 
 * 但如果你一开始就有意识使用了工厂模式,这些麻烦就没有了.
 * 
 * <h2>工厂方法</h2>
 *
 */
public class FactoryDemo {

    /**
     * 这样,在整个就不涉及到Sample的具体子类,达到封装效果,也就减少错误修改的机会,这个原理可以用很通俗的话来比喻:就是具体事情做得越多,越容易范错误.这每个做过具体工作的人都深有体会,
     * 相应·,官做得越高,说出的话越抽象越笼统,范错误可能性就越少.
     */
    static void demo1() {
        Object sampleA = Factory.creator(1);
    }
}

/**
 * <h2>工厂方法</h2>, 你会建立一个专门生产Sample实例的工厂。
 * 使用工厂方法要注意几个角色，首先你要定义产品接口，如上面的Sample,产品接口下有Sample接口的实现类,如SampleA,其次要有一个factory类，用来生成产品Sample，
 *
 */
class Factory {

    public static Object creator(int which) {

        // getClass 产生Sample 一般可使用动态类装载装入类。
        if (which == 1) {
            return new Object();
        } else if (which == 2) {
            return new Object();
        }
        return null;

    }

}

/**
 * <h2>抽象工厂</h2> 工厂模式中有: 工厂方法(Factory Method) 抽象工厂(Abstract Factory).
 * 
 * 这两个模式区别在于需要创建对象的复杂程度上。如果我们创建对象的方法变得复杂了,如上面工厂方法中是创建一个对象Sample,如果我们还有新的产品接口Sample2.
 * 
 * 这里假设：Sample有两个concrete类SampleA和SamleB，而Sample2也有两个concrete类Sample2A和SampleB2
 * 
 * 那么，我们就将上例中Factory变成抽象类,将共同部分封装在抽象类中,不同部分使用子类实现，
 * 
 * 从上面看到两个工厂各自生产出一套Sample和Sample2,也许你会疑问，为什么我不可以使用两个工厂方法来分别生产Sample和Sample2?
 * 
 * 抽象工厂还有另外一个关键要点，是因为SimpleFactory内，生产Sample和生产Sample2的方法之间有一定联系，所以才要将这两个方法捆绑在一个类中，这个工厂类有其本身特征，也许制造过程是统一的，比如：制造工艺比较简单，所以名称叫SimpleFactory。
 * 
 * 在实际应用中，工厂方法用得比较多一些，而且是和动态类装入器组合在一起应用。
 */
abstract class AbsFactory {

    public abstract Object creator();

    public abstract Object creator(String name);

}

class SimpleFactory extends AbsFactory {

    public Object creator() {
        // .........
        return new Object();
    }

    public Object creator(String name) {
        // .........
        return new Object();
    }

}

class BombFactory extends AbsFactory {

    public Object creator() {
        // ......
        return new Object();
    }

    public Object creator(String name) {
        // ......
        return new Object();
    }

}
