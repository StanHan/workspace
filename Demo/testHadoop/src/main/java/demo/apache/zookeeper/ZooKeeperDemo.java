package demo.apache.zookeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ZooKeeperDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperDemo.class);

    public static void main(String[] args) {
        delete("/Znode1");
    }

    /**
     * ZooKeeper类提供了 delete 方法来删除指定的znode。
     * 
     * @param path
     */
    static void delete(String path) {
        try {
            ZooKeeper zooKeeper = connect("localhost:2181");
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                zooKeeper.delete(path, stat.getVersion());
            } else {
                System.err.println("Node does not exists");
            }
        } catch (IOException | InterruptedException | KeeperException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * ZooKeeper类提供 getChildren 方法来获取特定znode的所有子节点。
     * 
     */
    static void getChildren(String path) {
        try {
            ZooKeeper zooKeeper = connect("localhost:2181");
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                List<String> children = zooKeeper.getChildren(path, false);
                for (String string : children) {
                    LOGGER.info(string);
                    System.err.println(string);
                }

            } else {
                System.err.println("Node does not exists");
            }
        } catch (IOException | InterruptedException | KeeperException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * ZooKeeper类提供 setData 方法来修改指定znode中附加的数据。
     * 
     * @param path
     *            Znode路径
     * @param data
     *            要存储在指定znode路径中的数据。
     * @param version
     *            znode的当前版本。每当数据更改时，ZooKeeper会更新znode的版本号。
     */
    static void setData(String path, String data) {
        try {
            ZooKeeper zooKeeper = connect("localhost:2181");
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                Stat stat2 = zooKeeper.setData(path, data.getBytes(StandardCharsets.UTF_8), stat.getVersion());
                LOGGER.info(JSON.toJSONString(stat2));
            } else {
                System.err.println("Node does not exists");
            }
            zooKeeper.close();
        } catch (IOException | InterruptedException | KeeperException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * getData 方法来获取附加在指定znode中的数据及其状态
     * <li>getData(String path, Watcher watcher, Stat stat)
     * 
     * @param path
     *            - Znode路径。
     * @param watcher
     *            - 监视器类型的回调函数。当指定的znode的数据改变时，ZooKeeper集合将通过监视器回调进行通知。这是一次性通知。
     * @param stat
     *            - 返回znode的元数据。
     */
    static void getData(String path) {
        CountDownLatch connectedSignal = new CountDownLatch(1);
        try {
            ZooKeeper zooKeeper = connect("localhost:2181");
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                byte[] data = zooKeeper.getData(path, new Watcher() {

                    public void process(WatchedEvent watchedEvent) {
                        if (watchedEvent.getType() == Event.EventType.None) {
                            switch (watchedEvent.getState()) {
                            case Expired:
                                connectedSignal.countDown();
                                break;
                            default:
                                break;
                            }
                        } else {
                            try {
                                byte[] bn = zooKeeper.getData(path, false, null);
                                String data = new String(bn, "UTF-8");
                                System.err.println(data);
                                connectedSignal.countDown();
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }, stat);
                String str = new String(data, "UTF-8");
                System.out.println(str);
                connectedSignal.await();
            } else {
                System.out.println("Node does not exists");
            }
        } catch (IOException | InterruptedException | KeeperException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Exists - 检查Znode的存在
     * 
     * ZooKeeper类提供了 exists 方法来检查znode的存在。如果指定的znode存在，则返回一个znode的元数据。
     */
    static void exists(String path) {
        try {
            ZooKeeper zooKeeper = connect("localhost:2181");
            /**
             * watcher - 布尔值，用于指定是否监视指定的znode
             */
            Stat stat = zooKeeper.exists(path, true);
            if (stat != null) {
                LOGGER.info("STAT : {}", JSONObject.toJSON(stat));
                System.err.println("Node exists and the node version is " + stat.getVersion());
            } else {
                System.err.println("Node does not exists");
            }
            zooKeeper.close();
        } catch (IOException | InterruptedException | KeeperException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 创建Znode
     */
    static void create(String path, String data) {
        try {
            ZooKeeper zooKeeper = connect("localhost:2181");
            zooKeeper.create(path, data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
            zooKeeper.close();
        } catch (IOException | InterruptedException | KeeperException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    static ZooKeeper connect(String host) throws IOException, InterruptedException {
        CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new Watcher() {

            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });

        connectedSignal.await();
        return zooKeeper;
    }
}
