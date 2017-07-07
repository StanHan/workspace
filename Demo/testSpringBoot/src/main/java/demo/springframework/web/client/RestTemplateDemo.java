package demo.springframework.web.client;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.daikuan.as.vo.ShareInformationVO;

import demo.vo.ApiResult;

public class RestTemplateDemo {

    public static void main(String[] args) {
        // demo1();
        getShareInfo();
    }

    public static void demo1() {
        String token = "1D35C50C6EDFF2EB9428E5CB0422C95344B9B12D3C58C483491B5A96D9923DB5";
        String mobilephone = "18217006685";
        String newCode = "123465";
        String idCard = "123456";
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8088/cs-iam/secure/phone/change";

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Token", token);
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>(3);
        map.add("mobilephone", mobilephone);
        map.add("newCode", newCode);
        map.add("idCard", idCard);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<LinkedMultiValueMap<String, String>>(
                map, headers);

        ResponseEntity<ApiResult> response = restTemplate.postForEntity(url, httpEntity, ApiResult.class);
        System.out.println(response);
        ApiResult<Boolean> body = response.getBody();
        Error error = body.getError();
        if (error != null) {
            System.err.println(error);
        }
        Boolean result = body.getResult();
        System.out.println(result);
    }

    /**
     * 查询分享信息
     * 
     * @return
     */
    public static List<ShareInformationVO> getShareInfo() {
        String url = "http://127.0.0.1:8088/b2b2p-ws/api/v4/interest-free/shareInfo";

        String token = "1D35C50C6EDFF2EB9428E5CB0422C95344B9B12D3C58C483491B5A96D9923DB5";
        int type = 1;
        int feature = 1;
        String version = "4.4";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Token", token);
        headers.add("version", version);
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        LinkedMultiValueMap<String, Integer> map = new LinkedMultiValueMap<String, Integer>(2);
        map.add("device_type", type);
        map.add("feature", feature);

        HttpEntity<LinkedMultiValueMap<String, Integer>> httpEntity = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiResult> response = restTemplate.postForEntity(url, httpEntity, ApiResult.class);
        System.out.println(response);
        ApiResult<List<ShareInformationVO>> body = response.getBody();
        Error error = body.getError();
        System.out.println(error);
        List<ShareInformationVO> result = body.getResult();
        System.out.println(result);
        return result;
    }

}
