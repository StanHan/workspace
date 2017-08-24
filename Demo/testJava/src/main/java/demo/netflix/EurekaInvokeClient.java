package demo.netflix;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.guice.EurekaModule;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.resources.ApplicationResource;

public class EurekaInvokeClient {
    private static final Logger logger = LoggerFactory.getLogger(EurekaInvokeClient.class);

    private static EurekaClient client;

    private static RestTemplate restTemplate;

    private static ReentrantLock lock = new ReentrantLock();

    private static ReentrantLock singletonLock = new ReentrantLock();

    private static EurekaInvokeClient eurekaInvokeClient;

    private EurekaInvokeClient() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }

        if (client == null) {
            lock.lock();
            if (client == null) {
                init();
            }
            lock.unlock();
        }
    }

    /**
     * 单例模式创建对象，当然可以通过注解的形式进行创建
     * 
     * @return
     */
    public static EurekaInvokeClient getSingletonEurekaInvokeClient() {
        if (eurekaInvokeClient == null) {
            singletonLock.lock();
            if (eurekaInvokeClient == null) {
                eurekaInvokeClient = new EurekaInvokeClient();
            }
            singletonLock.unlock();
        }
        return eurekaInvokeClient;
    }

    private void init() {
        DiscoveryManager.getInstance().initComponent(new MyDataCenterInstanceConfig(), new DefaultEurekaClientConfig());
        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceStatus.UP);
        client = DiscoveryManager.getInstance().getEurekaClient();
    }

    public static void demo() {
        EurekaModule eurekaModule = new EurekaModule();
        ApplicationInfoManager applicationInfoManager = null;
        EurekaClientConfig config = null;
        EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, config);
        Applications applications = eurekaClient.getApplications();
//        ApplicationResource
    }

    /**
     * 随机轮询策略
     */
    private InstanceInfo getRandomInstanceInfo(List<InstanceInfo> instances) {
        Random random = new Random();
        int randomPos = random.nextInt(instances.size());
        return instances.get(randomPos);
    }

    /**
     * 根据服务名随机获取一个实例
     * 
     * @param serviceName
     * @return
     * @author hanjy
     */
    private InstanceInfo randomInstance(String serviceName) {
        InstanceInfo instance = null;
        Applications apps = client.getApplications();
        Application app = apps.getRegisteredApplications(serviceName);
        if (app == null) {
            logger.error(serviceName + " is not register.");
            throw new RestClientException(serviceName + " is not register.");
        }

        List<InstanceInfo> instances = app.getInstances();
        if (instances.size() > 0) {
            instance = getRandomInstanceInfo(instances);
        } else {
            logger.error(serviceName + "'s instance not exist.");
            throw new RestClientException(serviceName + "'s instance not exist.");
        }
        return instance;
    }

    /**
     * 解析微服务名称，获取实例，生成URL
     * 
     * @param url
     * @return
     * @throws MalformedURLException
     */
    private String buildURL(String url) {
        try {
            URL vo = new URL(url);
            String host = vo.getHost();
            InstanceInfo instance = randomInstance(host);
            String protocal = vo.getProtocol();
            String file = vo.getFile();
            URL u = new URL(protocal, instance.getIPAddr(), instance.getPort(), file);
            return u.toString();
        } catch (MalformedURLException e) {
            throw new RestClientException("URL is Malformed :" + url);
        }
    }

    /**
     * GET请求
     * 
     * @param url
     * @param responseType
     * @param urlVariables
     * @return
     * @throws RestClientException
     * @throws MalformedURLException
     * @author hanjy
     */
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... urlVariables)
            throws RestClientException {
        String reqUrl = buildURL(url);
        return restTemplate.getForEntity(reqUrl, responseType, urlVariables);
    }

    /**
     * POST 请求
     * 
     * @param url
     * @param request
     * @param responseType
     * @return
     * @throws RestClientException
     * @throws MalformedURLException
     * @author hanjy
     */
    public <T> ResponseEntity<T> postForEntity(final String url, Object request, Class<T> responseType,
            Object... urlVariables) throws RestClientException {
        String reqUrl = buildURL(url);
        return restTemplate.postForEntity(reqUrl, request, responseType, urlVariables);
    }

    /**
     * DELETE 请求
     * 
     * @param url
     * @param urlVariables
     * @throws RestClientException
     * @throws MalformedURLException
     * @author hanjy
     */
    public void delete(String url, Object... urlVariables) throws RestClientException {
        String reqUrl = buildURL(url);
        restTemplate.delete(reqUrl, urlVariables);
    }

    /**
     * PUT请求
     * 
     * @param url
     * @param request
     * @param urlVariables
     * @throws RestClientException
     * @throws MalformedURLException
     */
    public void put(String url, Object request, Object... urlVariables) throws RestClientException {
        String reqUrl = buildURL(url);
        restTemplate.put(reqUrl, request, urlVariables);
    }
}