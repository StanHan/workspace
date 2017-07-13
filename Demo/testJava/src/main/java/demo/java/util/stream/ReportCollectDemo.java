package demo.java.util.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import demo.vo.Report;

public class ReportCollectDemo {

    public static void main(String[] args) {
        String[] aa = { "18-c", "18-c", "18-c", "5-c", "18-c", "12-d", "5-c", "5-c", "6-d", "6-c", "6-d", "6-d", "18-c",
                "6-d", "18-c", "18-w_1", "18-w_1", "3-d", "18-w_1-r_1", "6-d", "18-c", "18-c", "18-w_1", "18-w_1-r_3",
                "18-w_1", "18-w_1", "3-d", "18-w_1-r_1", "3-c", "3-w_1", "3-d", "3-w_1-r_1", "18-w_1", "3-d",
                "18-w_1-r_1", "18-w_3", "18-w_3-r_3", "18-w_1", "3-d", "18-w_1-r_1", "18-w_5", "18-w_1", "18-w_1-r_3",
                "18-w_1", "3-d", "18-w_1-r_1", "18-w_1", "18-w_1-r_3", "18-w_1", "18-w_1-r_3", "18-w_1", "18-w_1-r_3",
                "18-w_1", "3-d", "18-w_1-r_1", "18-c", "18-w_1", "3-d", "18-w_1-r_1", "18-w_1", "18-w_1-r_3", "18-w_1",
                "18-w_1-r_3", "18-w_1" };
        List<String> list = Arrays.asList(aa);
        Map<String, Long> map = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(map);
        map = list.stream().collect(Collectors.groupingBy((e -> e), Collectors.counting()));
        String json = JSON.toJSONString(map);
        System.out.println(json);
        
        Map<String, List<Bean>> map2 = map.entrySet().stream().map(new Function<Map.Entry<String, Long>, Bean>() {
            @Override
            public Bean apply(Entry<String, Long> t) {
                int idx = t.getKey().indexOf("-");
                String type = t.getKey().substring(0, idx);
                String value = t.getKey().substring(idx);
                return new Bean(type,value,t.getValue());
            }
        }).collect(Collectors.groupingBy(Bean::getType));
        
        System.out.println(map2);
        
        Map<String, Optional<Report>> map3 = map.entrySet().stream().map(new Function<Map.Entry<String, Long>, Report>() {
            @Override
            public Report apply(Entry<String, Long> t) {
                return buildReport(t);
            }
        }).collect(Collectors.groupingBy(Report::getType,Collectors.reducing(biFunction)));
        
        System.out.println(map3);

    }
    
    private static Report buildReport(Map.Entry<String, Long> t){
        int idx = t.getKey().indexOf("-");
        String type = t.getKey().substring(0, idx);
        String value = t.getKey().substring(idx);
        long count = t.getValue();
        
        Report vo = new Report();
        vo.setType(type);
        switch (value) {
        case "-d":
            vo.setShowTime(count);
            break;
        case "-c":
            vo.setPointTime(count);
            break;
        case "-w_1-r_1":
            vo.setQqTime(count);
            break;
        case "-w_2-r_1":
            vo.setQzoneTime(count);
            break;
        case "-w_3-r_1":
            vo.setWxTime(count);
            break;
        case "-w_4-r_1":
            vo.setWxzoneTime(count);
            break;
        case "-w_5-r_1":
            vo.setCopyTime(count);
            break;
        case "-w_1":
        case "-w_2":
        case "-w_3":
        case "-w_4":
        case "-w_5":
            vo.setShareTime(count);
            break;
        default:
            break;
        }
        return vo;
    }
    
    private static class Bean {
        String type;
        String vulue;
        long count;

        @Override
        public String toString() {
            return "Bean [type=" + type + ", vulue=" + vulue + ", count=" + count + "]";
        }

        public Bean() {
            super();
        }

        public Bean(String type, String vulue, long count) {
            super();
            this.type = type;
            this.vulue = vulue;
            this.count = count;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVulue() {
            return vulue;
        }

        public void setVulue(String vulue) {
            this.vulue = vulue;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
    
    private static BinaryOperator<Report> biFunction = new BinaryOperator<Report>() {
        @Override
        public Report apply(Report t, Report u) {
            Report r = new Report();
            r.setCopyTime(u.getCopyTime() + t.getCopyTime());
            r.setPointTime(t.getPointTime() + u.getPointTime());
            r.setQqTime(t.getQqTime() + u.getQqTime());
            r.setQzoneTime(t.getQzoneTime() + u.getQzoneTime());
            r.setShowTime(t.getShowTime() + u.getShowTime());
            r.setShareTime(t.getShareTime() + u.getShareTime());
            r.setType(u.getType());
            r.setWxTime(t.getWxTime() + u.getWxTime());
            r.setWxzoneTime(t.getWxzoneTime() + u.getWxzoneTime());
            return r;
        }
    };

}
