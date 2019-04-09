package test;

import com.yqhp.Application;
import com.yqhp.mbg.po.Device;
import com.yqhp.model.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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
        Map map = new HashMap();
    }
}
