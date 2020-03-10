package com.atguigu.gmall.mange;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallMangeWebApplicationTests {
    @Test
    public void contextLoads() throws IOException, MyException {
        String tracker = GmallMangeWebApplicationTests.class.getResource("/tracker.conf").getPath();
        ClientGlobal.init(tracker);
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] uploadInfos = storageClient.upload_file("d:/a.jpg", "jpg", null);
        String url = "http://192.168.3.117";
        for (String uploadInfo : uploadInfos) {
            url += "/" + uploadInfo;
        }
        System.out.println(url);
    }

}
