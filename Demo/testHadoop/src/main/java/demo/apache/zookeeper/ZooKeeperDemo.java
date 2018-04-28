package demo.apache.zookeeper;

/**
 * ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed
 * synchronization, and providing group services.
 * 
 * <h2>简介</h2> ZooKeeper 是一个开源的分布式协调服务，由雅虎创建，是 Google Chubby 的开源实现。分布式应用程序可以基于 ZooKeeper
 * 实现诸如数据发布/订阅、负载均衡、命名服务、分布式协调/通知、集群管理、Master选举、分布式锁和分布式队列等功能。
 * 
 * <h2>Zookeeper能干嘛？！</h2>
 * <li>配置管理。分布式系统都有好多机器，比如我在搭建hadoop的HDFS的时候，需要在一个主机器上（Master节点）配置好HDFS需要的各种配置文件，然后通过scp命令把这些配置文件拷贝到其他节点上，这样各个机器拿到的配置信息是一致的，才能成功运行起来HDFS服务。
 * Zookeeper提供了这样的一种服务：一种集中管理配置的方法，我们在这个集中的地方修改了配置，所有对这个配置感兴趣的都可以获得变更。这样就省去手动拷贝配置了，还保证了可靠和一致性。
 * 
 * <li>名字服务。分布式环境下，经常需要对应用/服务进行统一命名，便于识别不同服务；类似于域名与ip之间对应关系，域名容易记住；通过名称来获取资源或服务的地址，提供者等信息
 * 
 * <li>分布式锁。
 * 单机程序的各个进程需要对互斥资源进行访问时需要加锁，那分布式程序分布在各个主机上的进程对互斥资源进行访问时也需要加锁。很多分布式系统有多个可服务的窗口，但是在某个时刻只让一个服务去干活，当这台服务出问题的时候锁释放，立即fail
 * over到另外的服务。这在很多分布式系统中都是这么做，这种设计有一个更好听的名字叫Leader Election(leader选举)。
 * 举个通俗点的例子，比如银行取钱，有多个窗口，但是呢对你来说，只能有一个窗口对你服务，如果正在对你服务的窗口的柜员突然有急事走了，那咋办？找大堂经理（zookeeper）!大堂经理指定另外的一个窗口继续为你服务！
 * 
 * <li>4. 集群管理。
 * 在分布式的集群中，经常会由于各种原因，比如硬件故障，软件故障，网络问题，有些节点会进进出出。有新的节点加入进来，也有老的节点退出集群。这个时候，集群中有些机器（比如Master节点）需要感知到这种变化，然后根据这种变化做出对应的决策。
 * 我已经知道HDFS中namenode是通过datanode的心跳机制来实现上述感知的，那么我们可以先假设Zookeeper其实也是实现了类似心跳机制的功能吧！
 * 
 * <h2>Zookeeper的特点</h2>
 * <li>1 最终一致性：为客户端展示同一视图，这是zookeeper最重要的功能。
 * <li>2 可靠性：如果消息被到一台服务器接受，那么它将被所有的服务器接受。
 * <li>3 实时性：Zookeeper不能保证两个客户端能同时得到刚更新的数据，如果需要最新数据，应该在读数据之前调用sync()接口。
 * <li>4 等待无关（wait-free）：慢的或者失效的client不干预快速的client的请求。
 * <li>5 原子性：更新只能成功或者失败，没有中间状态。
 * <li>6 顺序性：所有Server，同一消息发布顺序一致。
 * 
 * <h2>用到Zookeeper的系统</h2>
 * <li>HDFS中的HA方案
 * <li>YARN的HA方案
 * <li>HBase：必须依赖Zookeeper，保存了Regionserver的心跳信息，和其他的一些关键信息。
 * <li>Flume：负载均衡，单点故障
 * 
 * <h2>集群角色</h2> 在 ZooKeeper 中，有三种角色： Leader、Follower、Observer。 一个 ZooKeeper集群同一时刻只会有一个 Leader，其他都是 Follower或 Observer。
 * ZooKeeper 默认只有 Leader 和 Follower 两种角色，没有 Observer 角色。为了使用 Observer 模式，在任何想变成Observer的节点的配置文件中加入:peerType=observer
 * 并在所有 server 的配置文件中，配置成 observer 模式的 server 的那行配置追加 :observer，例如：server.1:localhost:2888:3888:observer ZooKeeper
 * 集群的所有机器通过一个 Leader 选举过程来选定一台被称为『Leader』的机器，Leader服务器为客户端提供读和写服务。 Follower 和 Observer
 * 都能提供读服务，不能提供写服务。两者唯一的区别在于，Observer 机器不参与 Leader 选举过程，也不参与写操作的『过半写成功』策略，因此 Observer 可以在不影响写性能的情况下提升集群的读性能。
 * 
 * <h2>会话（Session）</h2> Session 是指客户端会话，在讲解客户端会话之前，我们先来了解下客户端连接。在 ZooKeeper 中，一个客户端连接是指客户端和 ZooKeeper 服务器之间的TCP长连接。
 * ZooKeeper 对外的服务端口默认是2181，客户端启动时，首先会与服务器建立一个TCP连接，从第一次连接建立开始，客户端会话的生命周期也开始了，通过这个连接，客户端能够通过心跳检测和服务器保持有效的会话，也能够向
 * ZooKeeper 服务器发送请求并接受响应，同时还能通过该连接接收来自服务器的 Watch 事件通知。 Session 的 SessionTimeout
 * 值用来设置一个客户端会话的超时时间。当由于服务器压力太大、网络故障或是客户端主动断开连接等各种原因导致客户端连接断开时，只要在 SessionTimeout 规定的时间内能够重新连接上集群中任意一台服务器，那么之前创建的会话仍然有效。
 * 
 * <h2>数据节点（ZNode）</h2> 在谈到分布式的时候，一般『节点』指的是组成集群的每一台机器。而ZooKeeper 中的数据节点是指数据模型中的数据单元，称为 ZNode。ZooKeeper
 * 将所有数据存储在内存中，数据模型是一棵树（ZNode Tree），由斜杠（/）进行分割的路径，就是一个ZNode，如 /hbase/master，其中 hbase 和 master 都是 ZNode。每个 ZNode
 * 上都会保存自己的数据内容，同时会保存一系列属性信息。 注：这里的 ZNode 可以理解成既是Unix里的文件，又是Unix里的目录。因为每个 ZNode
 * 不仅本身可以写数据（相当于Unix里的文件），还可以有下一级文件或目录（相当于Unix里的目录）。 在 ZooKeeper 中，ZNode 可以分为持久节点和临时节点两类。
 * <li>所谓持久节点是指一旦这个 ZNode 被创建了，除非主动进行 ZNode 的移除操作，否则这个 ZNode 将一直保存在 ZooKeeper 上。
 * <li>临时节点的生命周期跟客户端会话绑定，一旦客户端会话失效，那么这个客户端创建的所有临时节点都会被移除。
 * <p>
 * 另外，ZooKeeper 还允许用户为每个节点添加一个特殊的属性：SEQUENTIAL。一旦节点被标记上这个属性，那么在这个节点被创建的时候，ZooKeeper
 * 就会自动在其节点后面追加上一个整型数字，这个整型数字是一个由父节点维护的自增数字。
 * 
 * <h2>事务操作</h2>
 * 
 * 在ZooKeeper中，能改变ZooKeeper服务器状态的操作称为事务操作。一般包括数据节点创建与删除、数据内容更新和客户端会话创建与失效等操作。对应每一个事务请求，ZooKeeper 都会为其分配一个全局唯一的事务ID，用
 * ZXID 表示，通常是一个64位的数字。每一个 ZXID 对应一次更新操作，从这些 ZXID 中可以间接地识别出 ZooKeeper 处理这些事务操作请求的全局顺序。
 * 
 * <h2>Watcher</h2>
 * 
 * Watcher（事件监听器），是 ZooKeeper 中一个很重要的特性。ZooKeeper允许用户在指定节点上注册一些 Watcher，并且在一些特定事件触发的时候，ZooKeeper
 * 服务端会将事件通知到感兴趣的客户端上去。该机制是 ZooKeeper 实现分布式协调服务的重要特性。
 * 
 * <h2>ACL</h2>
 * 
 * ZooKeeper 采用 ACL（Access Control Lists）策略来进行权限控制。ZooKeeper 定义了如下5种权限。
 * 
 * CREATE: 创建子节点的权限。
 * 
 * READ: 获取节点数据和子节点列表的权限。
 * 
 * WRITE：更新节点数据的权限。
 * 
 * DELETE: 删除子节点的权限。
 * 
 * ADMIN: 设置节点ACL的权限。
 * 
 * <p>
 * 分布式的线性强一致性有两种实现方式：2PC两段提交和Paxos算法是常见两种。
 * 
 * 通过2PC写入新数据需要经过两次来回，第一次请求commit，第二次才正式确认commit，在这两者之间过程中，所有服务器都会堵塞等待发起者发出整个事务成功还是失败的结果(只有发起者知道所有服务器的情况)，
 * 如果失败，所有服务器返回之前状态，相当于写入数据失败，写入数据没有发生过一样。
 * 
 * 而Paxos算法能够回避2PC的堵塞死锁等问题更好地实现服务器之间数据强一致复制，具体内容见：Paxos算法。也可参考比Paxos算法改进的Raft算法。
 */
public class ZooKeeperDemo {

    /**
     * Paxos算法是莱斯利·兰伯特1990年提出的一种基于消息传递的一致性算法。这个算法被认为是类似算法中最有效的。 Paxos 算法解决的问题是一个分布式系统如何就某个值（决议）达成一致。
     * 一个典型的场景是，在一个分布式数据库系统中，如果各节点的初始状态一致，每个节点执行相同的操作序列，那么他们最后能得到一个一致的状态。
     * 为保证每个节点执行相同的命令序列，需要在每一条指令上执行一个“一致性算法”以保证每个节点看到的指令一致。
     * 一个通用的一致性算法可以应用在许多场景中，是分布式计算中的重要问题。因此从20世纪80年代起对于一致性算法的研究就没有停止过。 节点通信存在两种模型：共享内存（Shared memory）和消息传递（Messages
     * passing）。
     * 
     * Paxos 算法就是一种基于消息传递模型的一致性算法。 不仅仅是分布式系统中，凡是多个过程需要达成某种一致的场合都可以使用Paxos 算法。 一致性算法可以通过共享内存（需要锁）或者消息传递实现，Paxos 算法采用的是后者。
     * Paxos 算法适用的几种情况：一台机器中多个进程/线程达成数据一致；分布式文件系统或者分布式数据库中多客户端并发读写数据；分布式存储中多个副本响应读写请求的一致性。
     * 
     */
    public void Paxos算法() {

    }

}
