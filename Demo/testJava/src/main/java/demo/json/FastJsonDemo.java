package demo.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import demo.vo.Course;
import demo.vo.Student;
import demo.vo.Teacher;
import demo.vo.common.ApiResult;
import demo.vo.common.BusinessError;
import demo.vo.common.ErrorCode;

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
public class FastJsonDemo {

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id", "123456,123456789");
        map.put("task_id", "123456,123456789");
        map.put("totalCreditLine", new BigDecimal("5.33"));
        System.err.println(JSONObject.toJSONString(map));
        
        String a = "{\"blacklistRule\":-9999999,\"customerId\":\"10000002113-2\",\"freezeListRule\":104,\"inalBlacklistRule\":105,\"serialNo\":\"10000002114-2\",\"userId\":\"10000002112-2\"}";
        System.out.println(a);
        JSONObject json = JSONObject.parseObject(a);
        System.out.println(json.toJSONString());
    }

    static void demoMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("mobile", "15030101798");
        map.put("carrier_name", "赵**");
        map.put("carrier_idcard", "132302************");
        map.put("available_balance", "810");

        System.out.println(JSON.toJSONString(map));
    }

    static void testToJSONString() {
        // System.out.println("ok.");
        String json = JSON.toJSONString(new Student(), SerializerFeature.WriteNonStringKeyAsString);
        System.err.println(json);
        json = JSON.toJSONString(new Student());
        System.err.println(json);
        json = JSON.toJSONString(new ApiResult(null, BusinessError.NOT_LOGIN), SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        System.err.println(json);
        ApiResult<String> apiResult = JSON.parseObject(json, ApiResult.class);
        System.out.println(apiResult.getResult());
        System.out.println(apiResult.getErrorMsg().getMessage());
        json = JSON.toJSONString(new ApiResult(null, new ErrorCode("not_login", "用户未登录")),
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
        System.err.println(json);
        json = JSON.toJSONString(new ApiResult(null, BusinessError.NOT_LOGIN));
        System.err.println(json);
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

        Student student2 = JSON.parseObject(JSON_OBJ_STR, Student.class);
        // Student student1 = JSONObject.parseObject(JSON_OBJ_STR, new TypeReference<Student>()
        // {});//因为JSONObject继承了JSON，所以这样也是可以的

        System.out.println(student.getStudentName() + ":" + student.getStudentAge());
        System.out.println(student2.getStudentName() + ":" + student2.getStudentAge());

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

    // json字符串-简单对象型
    private static final String JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";
    // json字符串-数组类型
    private static final String JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";
    // 复杂格式json字符串
    private static final String COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";

}
