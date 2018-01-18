package demo.vo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * describe: 语音详情信息实体类 Date: 2017年11月17日 下午14:15:20 <br/>
 * 
 * @author tga130
 * @since JDK 1.7
 */
@Document(collection = "mx_carrier_calls")
public class MxCarrierCalls {
    @Id
    private ObjectId id;
    @Indexed
    private String task_id;// 任务id
    @Indexed
    @Field(value = "user_id")
    private String userId;// 用户id（一个用户可能多次任务）
    // 每个任务里面会有多条语音详情信息记录
    // 语音详情月份
    private String bill_month;
    // 该月详情记录总数
    private int total_size;
    // 通话详情明细
    private List<MxCarrierCallsItems> items;
    private String create_time;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBill_month() {
        return bill_month;
    }

    public void setBill_month(String bill_month) {
        this.bill_month = bill_month;
    }

    public int getTotal_size() {
        return total_size;
    }

    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }

    public List<MxCarrierCallsItems> getItems() {
        return items;
    }

    public void setItems(List<MxCarrierCallsItems> items) {
        this.items = items;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

}
