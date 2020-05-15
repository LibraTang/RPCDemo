package rpcTest;

import com.example.rpcdemo.core.RPC;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/java/rpcTest/ServerContext.xml"})
public class ServerApp {

    @Test
    public void start() {
        // 启动服务端
        RPC.start();
    }
}
