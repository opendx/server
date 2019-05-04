package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yqhp.Application;
import com.yqhp.mbg.po.Device;
import com.yqhp.model.Response;
import com.yqhp.model.vo.AgentVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Created by jiangyitao.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test1 {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test() {


        String filePath = "C:\\Users\\jyt\\Desktop\\微信图片_20190412224937.png";

        FileSystemResource resource = new FileSystemResource(new File(filePath));
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file", resource);


        Response response = restTemplate.postForObject("http://localhost:8887/upload/file", params, Response.class);

        Map<String,String> map = JSON.parseObject(JSON.toJSONString(response.getData()), Map.class);

        System.out.println(map.get("downloadURL"));
    }
}
