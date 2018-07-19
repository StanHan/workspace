package demo.spring.cloud.ribbon.service;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.spring.cloud.ribbon.vo.User;

/**
 * <h1>Async/Sync fallback.</h1>A fallback can be async or sync, at certain cases it depends on command execution type,
 * below listed all possible uses :
 * 
 * Supported
 * <li>case 1: sync command, sync fallback
 * <li>case 2: async command, sync fallback
 * <li>case 3: async command, async fallback
 */
@Service
public class ServiceC {

    /**
     * Javanica provides an ability to get execution exception (exception thrown that caused the failure of a command)
     * within a fallback is being executed. A fallback method signature can be extended with an additional parameter in
     * order to get an exception thrown by a command. Javanica exposes execution exception through additional parameter
     * of fallback method. Execution exception is derived by calling method getExecutionException() as in vanilla
     * hystrix.
     * 
     * @return
     */
    @HystrixCommand(fallbackMethod = "fallback1")
    public User getUserById(String id) {
        throw new RuntimeException("getUserById command failed");
    }

    @HystrixCommand(fallbackMethod = "fallback2")
    public User fallback1(String id, Throwable e) {
        assert "getUserById command failed".equals(e.getMessage());
        throw new RuntimeException("fallback1 failed");
    }

    @HystrixCommand(fallbackMethod = "fallback3")
    public User fallback2(String id) {
        throw new RuntimeException("fallback2 failed");
    }

    @HystrixCommand(fallbackMethod = "staticFallback")
    public User fallback3(String id, Throwable e) {
        assert "fallback2 failed".equals(e.getMessage());
        throw new RuntimeException("fallback3 failed");
    }

    public User staticFallback(String id, Throwable e) {
        assert "fallback3 failed".equals(e.getMessage());
        return new User("def", "def");
    }
}
