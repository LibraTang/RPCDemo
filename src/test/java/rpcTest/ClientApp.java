package rpcTest;

import com.example.rpcdemo.core.RPC;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 客户端启动类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/java/rpcTest/ClientContext.xml"})
@Slf4j
public class ClientApp {

    @Test
    public void start() {
        // 服务代理类
        Service serviceProxy = (Service) RPC.call(Service.class);
        log.info("调用add方法返回结果: {}", serviceProxy.add(1, 11.1));
    }
}
