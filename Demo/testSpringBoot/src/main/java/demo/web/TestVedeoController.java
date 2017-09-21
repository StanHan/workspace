package demo.web;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestVedeoController {

    private Logger logger = LoggerFactory.getLogger(TestVedeoController.class);

    @RequestMapping("/test/video")
    public void imageShow(HttpServletRequest req, HttpServletResponse resp) {
        byte[] buffer = new byte[102400];
        try (FileInputStream fileIS = new FileInputStream("/test.mp4");) {
            while (fileIS.read(buffer) != -1) {
                resp.getOutputStream().write(buffer);
                Thread.sleep(500);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
