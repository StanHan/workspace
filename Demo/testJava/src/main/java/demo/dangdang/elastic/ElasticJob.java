package demo.dangdang.elastic;

import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.lite.internal.schedule.JobScheduleController;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

import demo.quartz.JobDemo;
import demo.vo.pojo.User;

/**
 * 
 * elastic-job是当当开源的一款非常好用的作业框架，在这之前，我们开发定时任务一般都是使用quartz或者spring-task（ScheduledExecutorService），无论是使用quartz还是spring-task，我们都会至少遇到两个痛点：
 * <li>1.不敢轻易跟着应用服务多节点部署，可能会重复多次执行而引发系统逻辑的错误。
 * <li>2.quartz的集群仅仅只是用来HA，节点数量的增加并不能给我们的每次执行效率带来提升，即不能实现水平扩展。
 * <p>
 * elastic底层的任务调度还是使用的quartz，通过zookeeper来动态给job节点分片。
 * <h3>很大体量的用户需要在特定的时间段内计息完成</h3>
 * 我们肯定是希望我们的任务可以通过集群达到水平扩展，集群里的每个节点都处理部分用户，不管用户数量有多庞大，我们只要增加机器就可以了。比如单台机器特定时间能处理n个用户，2台机器处理2n个用户，3台3n，4台4n...，再多的用户也不怕了。
 * 使用elastic-job开发的作业都是zookeeper的客户端，比如我希望3台机器跑job，我们将任务分成3片，框架通过zk的协调，最终会让3台机器分别分配到0,1,2的任务片。
 * 比如server0-->0，server1-->1，server2-->2，当server0执行时，可以只查询id%3==0的用户，server1执行时，只查询id%3==1的用户，server2执行时，只查询id%3==2的用户。
 * <h3>任务部署多节点引发重复执行</h3><br>
 * 在上面的基础上，我们再增加server3，此时，server3分不到任务分片，因为只有3片，已经分完了。没有分到任务分片的作业程序将不执行。
 * 如果此时server2挂了，那么server2的分片项会分配给server3，server3有了分片，就会替代server2执行。
 * 如果此时server3也挂了，只剩下server0和server1了，框架也会自动把server3的分片随机分配给server0或者server1，可能会这样，server0-->0，server1-->1,2。
 * 这种特性称之为弹性扩容，即elastic-job名称的由来。
 * 
 * <p>
 * elastic-job是不支持单机多实例的，通过zk的协调分片是以ip为单元的。
 */
public class ElasticJob {

    public static void main(String[] args) {

    }

    private void demo1() {
        // 定义Zookeeper注册中心配置对象
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration("localhost:2181", "elastic-job-example");
        zkConfig.setMaxRetries(3);
        zkConfig.setBaseSleepTimeMilliseconds(1000);
        zkConfig.setConnectionTimeoutMilliseconds(3000);
        // 定义Zookeeper注册中心
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);

        JobCoreConfiguration.Builder builder = JobCoreConfiguration.newBuilder("oneOffElasticDemoJob", "0/5 * * * * ?",
                1);
        builder.jobProperties("", "");
        JobCoreConfiguration jobConfig1 = builder.build();
        // 定义作业1配置对象

        // 连接注册中心
        regCenter.init();

        // JobScheduleController controller = new JobScheduleController(scheduler, jobDetail, schedulerFacade,
        // triggerIdentity);
        // controller.scheduleJob("");

    }
}

class MySimpleJob implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println(String.format("------Thread ID: %s, 任务总片数: %s, 当前分片项: %s", Thread.currentThread().getId(),
                shardingContext.getShardingTotalCount(), shardingContext.getShardingItem()));
        /**
         * 实际开发中，有了任务总片数和当前分片项，就可以对任务进行分片执行了 比如 SELECT * FROM user WHERE status = 0 AND MOD(id, shardingTotalCount) =
         * shardingItem
         */
        String jobParameter = shardingContext.getJobParameter();

    }
}

/**
 * 
 * Dataflow类型用于处理数据流，需实现DataflowJob接口。该接口提供2个方法可供覆盖，分别用于抓取(fetchData)和处理(processData)数据。
 * 可通过DataflowJobConfiguration配置是否流式处理。 流式处理数据只有fetchData方法的返回值为null或集合长度为空时，作业才停止抓取，否则作业将一直运行下去；
 * 非流式处理数据则只会在每次作业执行过程中执行一次fetchData方法和processData方法，随即完成本次作业。
 *
 */
class MyDataFlowJob implements DataflowJob<User> {

    /*
     * status 0：待处理 1：已处理
     */

    @Override
    public List<User> fetchData(ShardingContext shardingContext) {
        List<User> users = null;
        /**
         * users = SELECT * FROM user WHERE status = 0 AND MOD(id, shardingTotalCount) = shardingItem Limit 0, 30
         */
        return users;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<User> data) {
        for (User user : data) {
            System.out.println(String.format("用户 %s 开始计息", user.getId()));
            user.setStatus((byte) 1);
            /**
             * update user
             */
        }
    }
}
