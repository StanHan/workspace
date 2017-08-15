package demo.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.daikuan.ic.request.TransManageHistorySaveRequest;

import demo.vo.Course;
import demo.vo.Student;
import demo.vo.Teacher;

/**
 * 1，对于JSON对象与JSON格式字符串的转换可以直接用 toJSONString()这个方法。
 * 
 * 2，javaBean与JSON格式字符串之间的转换要用到：JSON.toJSONString(obj);
 * 
 * 3，javaBean与json对象间的转换使用：JSON.toJSON(obj)，然后使用强制类型转换，JSONObject或者JSONArray。
 * 
 * @author hanjy
 *
 */
public class FastjsonDemo {

    public static void main(String[] args) {
        testJSONStrToJavaBeanObj();
        // testComplexJSONStrToJSONObject();
        // testJSONStrToJSONArray();
        // testJSONStrToJSONObject();

    }

    /**
     * 复杂json格式字符串与JavaBean_obj之间的转换
     */
    public static void testComplexJSONStrToJavaBean() {

        Teacher teacher = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {
        });
        // Teacher teacher1 = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>()
        // {});//因为JSONObject继承了JSON，所以这样也是可以的
        String teacherName = teacher.getTeacherName();
        Integer teacherAge = teacher.getTeacherAge();
        Course course = teacher.getCourse();
        List<Student> students = teacher.getStudents();
    }

    /**
     * json字符串-数组类型与JavaBean_List之间的转换
     */
    public static void testJSONStrToJavaBeanList() {

        ArrayList<Student> students = JSON.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>() {
        });
        // ArrayList<Student> students1 = JSONArray.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Student>>()
        // {});//因为JSONArray继承了JSON，所以这样也是可以的

        for (Student student : students) {
            System.out.println(student.getStudentName() + ":" + student.getStudentAge());
        }
    }

    /**
     * json字符串-简单对象与JavaBean_obj之间的转换
     */
    static void testJSONStrToJavaBeanObj() {

        Student student = JSON.parseObject(JSON_OBJ_STR, new TypeReference<Student>() {
        });
        // Student student1 = JSONObject.parseObject(JSON_OBJ_STR, new TypeReference<Student>()
        // {});//因为JSONObject继承了JSON，所以这样也是可以的

        System.out.println(student.getStudentName() + ":" + student.getStudentAge());

    }

    /**
     * 复杂json格式字符串与JSONObject之间的转换
     */
    static void testComplexJSONStrToJSONObject() {

        JSONObject jsonObject = JSON.parseObject(COMPLEX_JSON_STR);
        // JSONObject jsonObject1 = JSONObject.parseObject(COMPLEX_JSON_STR);//因为JSONObject继承了JSON，所以这样也是可以的

        String teacherName = jsonObject.getString("teacherName");
        System.out.println(teacherName);
        Integer teacherAge = jsonObject.getInteger("teacherAge");
        System.out.println(teacherAge);
        JSONObject course = jsonObject.getJSONObject("course");
        System.out.println(course);
        JSONArray students = jsonObject.getJSONArray("students");
        System.out.println(students);
    }

    /**
     * json字符串-数组类型与JSONArray之间的转换
     */
    static void testJSONStrToJSONArray() {

        JSONArray jsonArray = JSON.parseArray(JSON_ARRAY_STR);
        // JSONArray jsonArray1 = JSONArray.parseArray(JSON_ARRAY_STR);//因为JSONArray继承了JSON，所以这样也是可以的

        // 遍历方式1
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println(jsonObject.getString("studentName") + ":" + jsonObject.getInteger("studentAge"));
        }

        // 遍历方式2
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject.getString("studentName") + ":" + jsonObject.getInteger("studentAge"));
        }
    }

    /**
     * json字符串-简单对象型与JSONObject之间的转换
     */
    static void testJSONStrToJSONObject() {

        JSONObject jsonObject = JSON.parseObject(JSON_OBJ_STR);
        JSONObject jsonObject1 = JSONObject.parseObject(JSON_OBJ_STR); // 因为JSONObject继承了JSON，所以这样也是可以的

        System.out.println(jsonObject.getString("studentName") + ":" + jsonObject.getInteger("studentAge"));
        System.out.println(jsonObject1.getString("studentName") + ":" + jsonObject1.getInteger("studentAge"));
    }

    static void demo1() {
        TransManageHistorySaveRequest vo = new TransManageHistorySaveRequest();
        vo.setRequestXml("123");
        vo.setResponseXml("456");
        vo.setUserId(23545);
        vo.setTransCode("22");
        vo.setCreateAt(new Date());
        System.out.println(JSON.toJSONString(vo));

        List<RewardVO> list = Arrays.asList(new RewardVO(0x1, 0x2, 0x3a));
        String json = JSON.toJSONString(list);
        System.out.println(json);
    }

    // json字符串-简单对象型
    private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";
    // json字符串-数组类型
    private static final String JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";
    // 复杂格式json字符串
    private static final String COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";

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
