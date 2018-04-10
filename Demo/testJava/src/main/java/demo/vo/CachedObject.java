package demo.vo;

/**
 * 缓存的对象
 * 
 * @author hanjy
 *
 */
public class CachedObject {

    private long timestamp;
    private Object data;

    public long getTimestamp() {
        return timestamp;
    }

    public Object getData() {
        return data;
    }

    public CachedObject(Object data) {
        super();
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
