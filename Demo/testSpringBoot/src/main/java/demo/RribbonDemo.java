package demo;

import java.net.URI;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.client.http.RestClient;

public class RribbonDemo {
    /*public static void main(String[] args) throws Exception {
        ConfigurationManager.loadPropertiesFromResources("sample-client.properties");
        System.out.println(ConfigurationManager.getConfigInstance().getProperty("sample-client.ribbon.listOfServers"));

        RestClient client = (RestClient) ClientFactory.getNamedClient("sample-client");
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("/")).build();

        for (int i = 0; i < 20; i++) {
            HttpResponse response = client.executeWithLoadBalancer(request);
            System.out.println("Status for URI:" + response.getRequestedURI() + " is :" + response.getStatus());
        }

        ZoneAwareLoadBalancer lb = (ZoneAwareLoadBalancer) client.getLoadBalancer();
        System.out.println(lb.getLoadBalancerStats());

        ConfigurationManager.getConfigInstance().setProperty("sample-client.ribbon.listOfServers",
                "www.baidu.com:80,www.linkedin.com:80");

        System.out.println("changing servers ...");
        Thread.sleep(3000);

        for (int i = 0; i < 20; i++) {
            HttpResponse response = client.executeWithLoadBalancer(request);
            System.out.println("Status for URI:" + response.getRequestedURI() + " is :" + response.getStatus());
        }
        System.out.println(lb.getLoadBalancerStats());
    }*/

//    public static void demo(String[] args) throws Exception {
//        ConfigurationManager.loadPropertiesFromResources("sample-client.properties"); // 1
//        System.out.println(ConfigurationManager.getConfigInstance().getProperty("sample-client.ribbon.listOfServers"));
//        RestClient client = (RestClient) ClientFactory.getNamedClient("sample-client"); // 2
//        HttpClientRequest request = HttpClientRequest.newBuilder().setUri(new URI("/")).build(); // 3
//        for (int i = 0; i < 20; i++) {
//            HttpClientResponse response = client.executeWithLoadBalancer(request); // 4
//            System.out.println("Status code for " + response.getRequestedURI() + "  :" + response.getStatus());
//        }
//        ZoneAwareLoadBalancer lb = (ZoneAwareLoadBalancer) client.getLoadBalancer();
//        System.out.println(lb.getLoadBalancerStats());
//        ConfigurationManager.getConfigInstance().setProperty("sample-client.ribbon.listOfServers",
//                "www.linkedin.com:80,www.google.com:80"); // 5
//        System.out.println("changing servers ...");
//        Thread.sleep(3000); // 6
//        for (int i = 0; i < 20; i++) {
//            HttpClientResponse response = client.executeWithLoadBalancer(request);
//            System.out.println("Status code for " + response.getRequestedURI() + "  : " + response.getStatus());
//            response.releaseResources();
//        }
//        System.out.println(lb.getLoadBalancerStats()); // 7
//    }
}
