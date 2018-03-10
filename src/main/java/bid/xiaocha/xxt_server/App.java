package bid.xiaocha.xxt_server;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StringMultipartFileEditor;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import bid.xiaocha.xxt_server.entities.ActiveOrderEntity;
import bid.xiaocha.xxt_server.entities.NeedServeEntity;
import bid.xiaocha.xxt_server.rest_controller.UserController;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
public class App {
    public static void main( String[] args ){
        System.out.println( "Hello World!" );
//        JSONObject jsonObject = new JSONObject();
//        NeedServeEntity needServeEntity = new NeedServeEntity();
//        needServeEntity.setPublishdate(new Date());
//        jsonObject.put("aaa", needServeEntity);
       SpringApplication.run(App.class, args);
        //20152645645645654654565005028
    }
}
