package demo.vo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**  
 * describe: 通话明细类（语音详情实体类的辅助类，存放语音详情中的语音详情明细）
 * Date:     2017年11月17日 下午14:15:20 <br/>  
 * @author   tga130  
 * @since    JDK 1.7  
 */
public class MxCarrierCallsItems {
	//每个语音详情记录中有多个通话明细
	// 详情唯一标识
    private String details_id;
    //	通话时间，格式：yyyy-MM-dd HH:mm:ss
    private String time	;
    //	对方通话号码
    private String peer_number;
	// 通话地(自己的)
    private String location;
    //	通话地类型. e.g.省内漫游
    private String location_type;
    //	通话时长(单位:秒)
    private int duration;
	// 呼叫类型. DIAL-主叫; DIALED-被叫
    private String dial_type;
    //	通话费(单位:分)
    private int fee	;
	public String getDetails_id() {
		return details_id;
	}
	public void setDetails_id(String details_id) {
		this.details_id = details_id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPeer_number() {
		return peer_number;
	}
	public void setPeer_number(String peer_number) {
		this.peer_number = peer_number;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation_type() {
		return location_type;
	}
	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDial_type() {
		return dial_type;
	}
	public void setDial_type(String dial_type) {
		this.dial_type = dial_type;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}

}
  
