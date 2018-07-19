package demo.spring.cloud.ribbon.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import rx.Observable;
import rx.Subscriber;

@Service
public class ServiceA {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Synchronous Execution
     * <li>@HystrixCommand注解 该注解对该方法创建了熔断器的功能，并指定了fallbackMethod熔断方法。
     * <li>Its important to remember that Hystrix command and fallback should be placed in the same class and have same
     * method signature (optional parameter for failed execution exception).
     * 
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://Module/hi?name=" + name, String.class);
    }

    /**
     * Asynchronous Execution
     * 
     */
    @HystrixCommand
    public Future<String> getUserByIdAsync(final String name) {
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return restTemplate.getForObject("http://Module/hi?name=" + name, String.class);
            }
        };
    }

    /**
     * Reactive Execution
     */
    @HystrixCommand
    public Observable<String> getUserById(final String name) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(restTemplate.getForObject("http://Module/hi?name=" + name, String.class));
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    public String hiError(String name) {
        return "hi," + name + ",sorry,error!";
    }
}
