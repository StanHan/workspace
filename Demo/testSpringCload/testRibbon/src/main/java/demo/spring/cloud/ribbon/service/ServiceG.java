package demo.spring.cloud.ribbon.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.spring.cloud.ribbon.vo.User;
import rx.Observable;

/**
 * A method annotated with @HystrixCollapser annotation can return any value with compatible type, it does not affect
 * the result of collapser execution, collapser method can even return null or another stub. There are several rules
 * applied for methods signatures.
 * 
 * <li>Collapser method must have one argument of any type, desired a wrapper of a primitive type like Integer, Long,
 * String and etc.
 * <li>A batch method must have one argument with type java.util.List parameterized with corresponding type, that's if a
 * type of collapser argument is Integer then type of batch method argument must be List<Integer>.
 * <li>Return type of batch method must be java.util.List parameterized with corresponding type, that's if a return type
 * of collapser method is User then a return type of batch command must be List<User>.
 *
 */
@Service
public class ServiceG {

    /** Asynchronous Execution */
    @HystrixCollapser(batchMethod = "getUserByIds")
    public Future<User> getUserByIdAsync(String id) {
        return null;
    }

    /** Reactive Execution */
    @HystrixCollapser(batchMethod = "getUserByIds")
    public Observable<User> getUserByIdReact(String id) {
        return null;
    }

    @HystrixCommand
    public List<User> getUserByIds(List<String> ids) {
        List<User> users = new ArrayList<User>();
        for (String id : ids) {
            users.add(new User(id, "name: " + id));
        }
        return users;
    }

    public void demo() {
        // Async
        Future<User> f1 = getUserByIdAsync("1");
        Future<User> f2 = getUserByIdAsync("2");
        Future<User> f3 = getUserByIdAsync("3");
        Future<User> f4 = getUserByIdAsync("4");
        Future<User> f5 = getUserByIdAsync("5");

        // Reactive
        Observable<User> u1 = getUserByIdReact("1");
        Observable<User> u2 = getUserByIdReact("2");
        Observable<User> u3 = getUserByIdReact("3");
        Observable<User> u4 = getUserByIdReact("4");
        Observable<User> u5 = getUserByIdReact("5");

        // Materialize reactive commands
        Iterable<User> users = Observable.merge(u1, u2, u3, u4, u5).toBlocking().toIterable();
    }

}
