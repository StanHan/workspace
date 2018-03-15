package demo.dianping.cat;

import java.net.URL;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

/**
 * <H1>CAT 详细介绍</H1>
 * <li>GIT地址：https://gitee.com/mirrors/CAT
 * <P>
 * CAT 由大众点评开发的，基于 Java 的实时应用监控平台，包括实时应用监控，业务监控。Cat使用消息树（MessageTree）组织日志，树的每个节点都是一个消息（Message），消息共有5种具体类型，
 * 分别是Transaction, Trace, Event，Heartbeat和Metric。每个消息树都有一个唯一的messageId，且消息树之间有单父级关系。
 * <LI>Transaction可以理解为是一个事务，它分为三种：独立事务，分支事务以及标记事务。事务之间可以互相嵌套，事务还可以嵌套任意其他消息类型，也只有事务才可以嵌套消息。
 * 分支事务是从当前事务里派生出的异步事务，当前事务不需要等待其完成。分支事务创建新的消息树，和主事务之间是平行的关系。主事务通过新建event记录与分支事务之间的关系（一种软连接关系），
 * 分支事务通过设置消息树的parentMessageId维护与主事务消息之间的关系。
 * 标记事务和分支事务之间非常类似，但标记事务是当前事务的子事务而不是另一个平行事务。其他线程可以通过tag（标记）找到标记事务并进行绑定，标记事务创建新event记录绑定信息，每一次绑定对应一个event，
 * event里记录绑定线程的事务信息；同时绑定线程会也通过parentMessageId维护与主事务消息之间的关系。
 * Transaction适合记录跨越系统边界的程序访问行为,比如远程调用，数据库调用，也适合执行时间较长的业务逻辑监控，Transaction用来记录一段代码的执行时间和次数。
 * <LI>Event代表系统是在某个时间点发生的一次事件，例如新用户注册、登陆，系统异常等，理论上可以记录任何事情。Event用来记录一件事发生的次数，比如记录系统异常，它和transaction相比缺少了时间的统计，开销比transaction要小。
 * <LI>Heartbeat 表示程序内定期产生的统计信息, 如CPU%, MEM%, 连接池状态, 系统负载等。Cat默认收集client端节点的心跳信息。对报文结构感兴趣的可以查看status.xsd。
 * <LI>Metric 用于记录业务指标、指标可能包含对一个指标记录次数、记录平均值、记录总和，业务指标最低统计粒度为1分钟，cat提供logMetricForSum,
 * logMetricForCount以及logMetricForDuration对指标做简单的度量统计。
 * <LI>Trace 用于记录基本的trace信息，类似于log4j的info信息，这些信息仅用于查看一些相关信息
 * <H1>关键配置的静态结构</H1>
 * <P>
 * Cat有三个重要配置，分别是ClientConfig, RouterConfig和ServerConfig，分别代表客户端信息，服务路由信息以及服务端配置信息。
 * 
 * ClientConfig.每个客户端对应一组对服务器以及一个domain信息，客户端默认从这组服务中的一个节点上拉取路由配置信息。
 * 客户端的查询参数里带有domain信息，服务端的路由配置里如果有相应的domain则返回相应domain下的一组server信息，如果没有则返回default servers。
 * 
 * 返回的路由信息包含一组日志服务节点以及采样比例（sample），日志服务节点包含权重，socket端口号以及id（ip）。采样比例是指客户端的cat日志多少次里抽样发送1次，例如0.2则代表记录5次日志会忘服务端发送1次。
 * 客户端拉取到router信息后，和router的日志server列表中第一个可用server之间建立netty channel，并启动一个线程对channel进行维护。对channel的维护主要包括： 1.
 * 比较服务端路由信息和客户端上次抓取的是否一致，不一致则更新客户端router信息，并重新建立新channel 2.
 * 判断当前channel状态，如果状态不正常，则从router的server列表里重新找出一个能用的server建立channel
 * 
 * 客户端拉取不到router信息时，默认使用客户端下的server列表作为远程日志服务器组。
 * <p>
 * ServerConfig主要用于服务端节点的职能描述，主要有以下几类功能：
 * <li>1. 定义服务节点职能，可以运行哪类任务，以及是否可以发送告警信息和是否是hdfs存储节点。
 * <li>2. 通过consoleConfig定义相应报表数据的获取节点，一般用于告警节点远程拉取所有节点的报表数据进行筛选。
 * <li>3. ConsumerConfig定义各种类型的事务时间阈值，从名字可以看出来分别定义url，sql以及cache类型的事务时间的阈值，超过这个时间会被认为是一个problem。
 */
public class CAT {

    Logger log = LoggerFactory.getLogger(CAT.class);

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * Cat.newTransaction 与Cat.getProducer().newTransaction 区别在于 一个是重新生成一个transation 和获取当前线程绑定的transaction
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cycletransation", method = RequestMethod.GET)
    public String newtransation() throws Exception {
        Transaction t = Cat.getProducer().newTransaction("TransactionTest", "Cat.getProducer()");
        Cat.getProducer().logEvent("eventType1", "1", Message.SUCCESS, "");
        Cat.getProducer().logEvent("eventType1", "2", Message.SUCCESS, "");
        Transaction t2 = Cat.getProducer().newTransaction("TransactionTest-1", "child transaction 1");
        Cat.getProducer().logEvent("eventType2-1", "2-1", Message.SUCCESS, "");
        Cat.getProducer().logEvent("eventType2-2", "2-2", Message.SUCCESS, "");
        t2.addData("tChild transaction-1");
        t2.setStatus(Message.SUCCESS);
        t2.complete();
        Transaction t3 = Cat.getProducer().newTransaction("TransactionTest-2", "child transaction 2");
        Cat.getProducer().logEvent("eventType3-1", "3-1", Message.SUCCESS, "");
        Cat.getProducer().logEvent("eventType3-2", "3-2", Message.SUCCESS, "");
        t3.addData("Child transaction-2");
        t3.setStatus(Message.SUCCESS);
        // 休眠3s 验证时间
        Thread.sleep(4000);
        t3.complete();
        t.addData(" Parent transaction");
        t.setStatus(Message.SUCCESS);
        t.complete();
        return "";
    }

    private Object aroundTransaction(String type, String name, ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Transaction transaction = null;
        // 不让cat异常导致业务异常
        try {
            transaction = Cat.getProducer().newTransaction(type, name);
        } catch (Exception e) {
            log.error("Cat.getProducer().newTransaction Error", e);
        }
        try {
            log.info("大众点评cat拦截：type=" + type + ";name=" + name);
            result = joinPoint.proceed();
            if (transaction != null)
                transaction.setStatus(Transaction.SUCCESS);
        } catch (Throwable throwable) {
            if (transaction != null)
                transaction.setStatus(throwable);
            log.error("aroundTransaction exception", throwable);
            throw throwable;
        } finally {
            if (transaction != null)
                transaction.complete();
        }
        return result;
    }

}
