package demo.netflix.discovery.EurekaClient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import com.netflix.discovery.DiscoveryClient;

//@EnableDiscoveryClient
//@SpringBootApplication
public class ReservationClientApplication {
//    @Bean
//    CommandLineRunner runner(DiscoveryClient dc) {
//        return args -> {
//            dc.getInstancesById("reservation-service").forEach(si -> System.out.println(
//                    String.format("Found %s %s:%s", si.getSecureHealthCheckUrl(), si.getHostName(), si.getPort())));
//        };
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(ReservationClientApplication.class, args);
//    }
}
