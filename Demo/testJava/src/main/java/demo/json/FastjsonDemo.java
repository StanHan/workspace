package demo.json;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.daikuan.ic.request.TransManageHistorySaveRequest;


public class FastjsonDemo {

    public static void main(String[] args) {
        TransManageHistorySaveRequest vo =new TransManageHistorySaveRequest();
        vo.setRequestXml("123");
        vo.setResponseXml("456");
        vo.setUserId(23545);
        vo.setTransCode("22");
        vo.setCreateAt(new Date());
        System.out.println(JSON.toJSONString(vo));
        
        
        List <RewardVO> list = Arrays.asList(new RewardVO(0x1, 0x2, 0x3a));
        String json = JSON.toJSONString(list);
        System.out.println(json);

    }

}


class RewardVO {
    private long userId;
    private int invitedUserCount;
    private long reward;

    public RewardVO(long userId, int invitedUserCount, long reward) {
        super();
        this.userId = userId;
        this.invitedUserCount = invitedUserCount;
        this.reward = reward;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getInvitedUserCount() {
        return invitedUserCount;
    }

    public void setInvitedUserCount(int invitedUserCount) {
        this.invitedUserCount = invitedUserCount;
    }

    public long getReward() {
        return reward;
    }

    public void setReward(long reward) {
        this.reward = reward;
    }
}
