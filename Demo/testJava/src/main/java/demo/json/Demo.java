package demo.json;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Demo {

    public static void main(String[] args) {
        List <RewardVO> list = Arrays.asList(new RewardVO(1, 2, 3));
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
