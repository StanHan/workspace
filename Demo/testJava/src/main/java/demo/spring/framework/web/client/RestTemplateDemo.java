package demo.spring.framework.web.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import demo.vo.PnsMsg;
import demo.vo.common.ApiResult;

/**
 * REST（RepresentationalState Transfer）是Roy Fielding 提出的一个描述互联系统架构风格的名词。REST定义了一组体系架构原则，您可以根据这些原则设计以系统资源为中心的Web
 * 服务，包括使用不同语言编写的客户端如何通过 HTTP处理和传输资源状态。
 * <p>
 * 2、REST成熟度的四个层次:
 * <li>第一个层次（Level0）的Web 服务只是使用 HTTP 作为传输方式，实际上只是远程方法调用（RPC）的一种具体形 式。SOAP和 XML-RPC都属于此类。
 * <li>第二个层次（Level1）的Web 服务引入了资源的概念。每个资源有对应的标识符和表达。
 * <li>第三个层次（Level2）的Web 服务使用不同的 HTTP 方法来进行不同的操作，并且使用HTTP 状态码来表示不同的结果。如 HTTPGET 方法来获取资源，HTTPDELETE 方法来删除资源。
 * <li>第四个层次（Level3）的Web 服务使用 HATEOAS。在资源的表达中包含了链接信息。客户端可以根据链接来发现可以执行的动作。
 * <p>
 * 
 * 其中第三个层次建立了创建、读取、更新和删除（create,read, update, and delete，CRUD）操作与 HTTP方法之间的一对一映射。根据此映射：
 * 
 * <li>（1）若要在服务器上创建资源，应该使用POST 方法。
 * <li>（2）若要检索某个资源，应该使用GET 方法。
 * <li>（3）若要更改资源状态或对其进行更新，应该使用PUT 方法。
 * <li>（4）若要删除某个资源，应该使用DELETE 方法。
 * <p>
 * 3、HTTP请求的方法
 * <li>（1）GET：通过请求URI得到资源
 * <li>（2）POST：用于添加新的内容
 * <li>（3）PUT：用于修改某个内容，若不存在则添加
 * <li>（4）DELETE：删除某个内容
 * <li>（5）OPTIONS ：询问可以执行哪些方法
 * <li>（6）HEAD ：类似于GET, 但是不返回body信息，用于检查对象是否存在，以及得到对象的元数据
 * <li>（7）CONNECT ：用于代理进行传输，如使用SSL
 * <li>（8）TRACE：用于远程诊断服务器
 * <p>
 * 4、HTTP请求的状态码
 * <li>（1）成功Successful 2xx：此类状态码标识客户端的请求被成功接收、理解并接受。常见如200（OK）、204（NoContent）。
 * <li>（2）重定向Redirection 3xx：这个类别的状态码标识用户代理要做出进一步的动作来完成请求。常见如301（MovedPermanently）、302（MovedTemprarily）。
 * <li>（3）客户端错误Client Error
 * 4xx：4xx类别的状态码是当客户端象是出错的时使用的。常见如400（BadRequest）、401（Unauthorized）、403（Forbidden）、404（NotFound)。
 * <li>（4）服务器错误Server Error 5xx：响应状态码以5开头表示服务器知道自己出错或者没有能力执行请求。常见如500（InternalServer
 * Error）、502（BadGateway）、504（GatewayTimeout）。
 */
public class RestTemplateDemo {
    static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        PnsMsg vo = new PnsMsg(1L, 1L, (byte) 1, "title", "content", (byte) 1, "www.baidu.com", "www.baidu.com");
         String s = JSON.toJSONString(vo);
         System.out.println(s);
         HttpHeaders requestHeaders = new HttpHeaders();
         requestHeaders.add("wsKey", "platform_push");
         requestHeaders.add("Content-Type", "application/json");
         HttpEntity<PnsMsg> httpEntity = new HttpEntity<>(vo, requestHeaders);
        try {
            String r = restTemplate.postForObject("http://t3-wsdaikuan.2345.com/b2b2p-ws/api/v6_1/sendPlatForm", httpEntity, String.class);
            System.out.println(r);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            ResponseEntity<String> r2 = restTemplate.postForEntity("http://t3-wsdaikuan.2345.com/b2b2p-ws/api/v6_1/sendPlatForm", httpEntity, String.class);
            System.out.println(r2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            ParameterizedTypeReference<String> pt = new ParameterizedTypeReference<String>() {
            };
            ResponseEntity<String> response = restTemplate.exchange("http://t3-wsdaikuan.2345.com/b2b2p-ws/api/v6_1/sendPlatForm", HttpMethod.POST, httpEntity, pt);
            System.out.println(response);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * ParameterizedTypeReference<ArrayList<String>>并不根据实参而是使用getGenericSuperclass()方法获取其父类的类型，
     * 父类的类型通过java.lang.reflect.Type描述，然后通过Type的getActualTypeArguments()获得了父类的实参类型，注意得到的Type类，并不是class类。
     */
    static void testParameterizedTypeReference() {
        List<String> list = new ArrayList<String>();
        System.out.println(list.getClass());
        System.out.println(list.getClass().getGenericSuperclass());
        ApiResult<List<String>> result = new ApiResult<>();
        System.out.println(result.getClass());
        System.out.println(result.getClass().getGenericSuperclass());
        /** （注意这里的new有花括号，是ParameterizedTypeReference的子类） */
        ParameterizedTypeReference<ApiResult<ArrayList<String>>> pt = new ParameterizedTypeReference<ApiResult<ArrayList<String>>>() {
        };
        System.out.println(pt.getType());
    }

    /**
     * String类型的URI支持占位符,最终访问的URI为：http://example.com/hotels/42/bookings/21
     * 但是String有一个小缺陷：String形式的URI会被URL编码两次，这就要求服务器在获取URI中的参数时主动进行一次解码，但如果服务的提供者不这么做呢？这时就需要使用不会使用任何编码的java.net.URI
     */
    static void getForObject() {
        String urlStr = "http://example.com/hotels/{hotel}/bookings/{booking}";
        restTemplate.getForObject(urlStr, ApiResult.class, "42", "21");
    }

    /**
     * Exchange:
     * <li>允许调用者指定HTTP请求的方法（GET,POST,PUT等）
     * <li>可以在请求中增加body以及头信息，其内容通过参数‘HttpEntity<?>requestEntity’描述
     * <li>exchange支持‘含参数的类型’（即泛型类）作为返回类型，该特性通过‘ParameterizedTypeReference<T>responseType’描述。
     */
    static void exchange() {
        List<String> list = new ArrayList<String>();
        ParameterizedTypeReference<ApiResult<ArrayList<String>>> pt = new ParameterizedTypeReference<ApiResult<ArrayList<String>>>() {
        };
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(list);
        String urlStr = "http://example.com/hotels/{hotel}/bookings/{booking}";
        restTemplate.exchange(urlStr, HttpMethod.POST, requestEntity, pt, "42", "21");
    }

    /**
     * excute : 所有的get、post、delete、put、options、head、exchange方法最终调用的都是excute方法。
     * 
     * RequestCallback : Callback interface for code that operates on a ClientHttpRequest. Allows to manipulate the
     * request headers, and write to the request body. 简单说：用于操作请求头和body，在请求发出前执行。 DELETE、HEAD、OPTIONS没有使用这个接口。
     * 
     * 该接口有两个实现类：
     * <li>AcceptHeaderRequestCallback 只处理请求头，用于getXXX()方法。
     * <li>HttpEntityRequestCallback 继承于AcceptHeaderRequestCallback可以处理请求头和body，用于putXXX()、postXXX()和exchange()方法。
     * 
     * ResponseExtractor :Generic callback interface used by RestTemplate's retrieval methods Implementations of this
     * interface perform the actual work of extracting data from a ClientHttpResponse, but don't need to worry about
     * exception handling or closing resources. 简单说：解析HTTP响应的数据，而且不需要担心异常和资源的关闭。
     * 
     * 该接口有三个实现类：
     * <li>HeadersExtractor 用于提取请求头。
     * <li>HttpMessageConverterExtractor 用于提取响应body。
     * <li>ResponseEntityResponseExtractor
     * 使用HttpMessageConverterExtractor提取body（委托模式），然后将body和响应头、状态封装成ResponseEntity对象。
     * 
     * 提取响应body: 提取分三步： （1）提取器HttpMessageConverterExtractor寻找可用的转化器。在默认的RestTemplate的构造函数中初始化了转化器集合，包括：
     * 
     * <li>ByteArrayHttpMessageConverter byte[]
     * <li>StringHttpMessageConverter String
     * <li>ResourceHttpMessageConverter Resource
     * <li>SourceHttpMessageConverter javax.xml.transform.*
     * <li>AllEncompassingFormHttpMessageConverter MultiValueMap
     * <li>Jaxb2RootElementHttpMessageConverter XmlRootElement,XmlType（注解）
     * <li>MappingJackson2HttpMessageConverter Json
     * 
     * 除了前五个，其他的转化器会由classloader尝试加载某个类来判断工程是否包含某个包，而后决定是否加入转化器集合。
     * 提取器遍历转化器集合以查找可用的转化器，其中MappingJackson2HttpMessageConverter总是在最后一个，因为该类实现了GenericHttpMessageConverter，算是一个通用转化器，只有在找不到合适的转化器时才轮到它。
     * Spring提供了一个该类的实现，以保证总是能得到该类。
     * 
     * （2）转化器寻找可用的反序列化器 转化器持有一个反序列化器缓存集合，首先从缓存中寻找 如果已有可用的反序列化器，则直接返回。否则创建一个新的反序列化器。
     * 
     * 反序列化器保存着待反序列化类的域、方法、构造器等信息，反序列化时就是使用构造器创建了一个新的实例。
     * 以jackson为例，创建反序列化器的过程在jackson-databind-xxx.jar中，有兴趣的可以看一下。调用栈如下(由下往上找)：
     * BeanDeserializerFactory.addBeanProps/addObjectIdReader/addReferenceProperties/addInjectables
     * BeanDeserializerFactory.buildBeanDeserializer BeanDeserializerFactory.createBeanDeserializer
     * 
     * （3）反序列化器执行反序列化
     * <li>TOKEN | Json的一个或一组字符
     * <li>START_OBJECT | {
     * <li>END_OBJECT | }
     * <li>START_ARRAY | [
     * <li>END_ARRAY | ]
     * <li>VALUE_TRUE | true
     * <li>VALUE_FALSE | false
     * 
     */
    static void doExecute() {
        RequestCallback requestCallBack = new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest request) throws IOException {
            }
        };
        ResponseExtractor<ApiResult<ArrayList<String>>> responseExtractor = new ResponseExtractor<ApiResult<ArrayList<String>>>() {
            @Override
            public ApiResult<ArrayList<String>> extractData(ClientHttpResponse response) throws IOException {
                return null;
            }
        };
        String urlStr = "http://example.com/hotels/{hotel}/bookings/{booking}";
        restTemplate.execute(urlStr, HttpMethod.POST, requestCallBack, responseExtractor, "42", "21");
    }

    static void testRestTemplateWaste() {
        long begin = System.currentTimeMillis();
        int i = 1_000;
        while (i-- > 0) {
            new RestTemplate();
        }
        long end = System.currentTimeMillis();
        System.out.println("创建" + 1_000 + "个RestTemplate 对象耗时(ms)：" + (end - begin));
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

        // ResponseEntity<ApiResult> response = restTemplate.postForEntity(url, httpEntity, ApiResult.class);
        // System.out.println(response);
        // ApiResult<Boolean> body = response.getBody();
        // Error error = body.getError();
        // if (error != null) {
        // System.err.println(error);
        // }
        // Boolean result = body.getResult();
        // System.out.println(result);
    }

    /**
     * 查询分享信息
     * 
     * @return
     */
    // public static List<ShareInformationVO> getShareInfo() {
    // String url = "http://127.0.0.1:8088/b2b2p-ws/api/v4/interest-free/shareInfo";
    //
    // String token = "1D35C50C6EDFF2EB9428E5CB0422C95344B9B12D3C58C483491B5A96D9923DB5";
    // int type = 1;
    // int feature = 1;
    // String version = "4.4";
    // HttpHeaders headers = new HttpHeaders();
    // headers.add("X-Token", token);
    // headers.add("version", version);
    // headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
    // headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    // headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    //
    // LinkedMultiValueMap<String, Integer> map = new LinkedMultiValueMap<String, Integer>(2);
    // map.add("device_type", type);
    // map.add("feature", feature);
    //
    // HttpEntity<LinkedMultiValueMap<String, Integer>> httpEntity = new HttpEntity<>(map, headers);
    //
    // RestTemplate restTemplate = new RestTemplate();
    // ResponseEntity<ApiResult> response = restTemplate.postForEntity(url, httpEntity, ApiResult.class);
    // System.out.println(response);
    // ApiResult<List<ShareInformationVO>> body = response.getBody();
    // Error error = body.getError();
    // System.out.println(error);
    // List<ShareInformationVO> result = body.getResult();
    // System.out.println(result);
    // return result;
    // }

}
