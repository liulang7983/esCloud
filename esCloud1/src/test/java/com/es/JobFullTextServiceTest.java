package com.es;


import com.bean.JobDetail;
import com.service.Impl.JobFullTextServiceImpl;
import com.service.JobFullTextService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ming.li
 * @date 2022/11/2 13:53
 */
//@SpringBootTest
public class JobFullTextServiceTest {

    private JobFullTextService jobFullTextService;

    @BeforeTest
    public void beforeTest(){
        jobFullTextService=new JobFullTextServiceImpl();
    }

    @Test
    public void add() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        JobDetail jobDetail = new JobDetail();
        jobDetail.setId(3);
        jobDetail.setArea("南昌");
        jobDetail.setCmp("南昌大学");
        jobDetail.setEdu("硕士及以上");
        jobDetail.setExp("九年工作经验");
        jobDetail.setTitle("java架构师");
        jobDetail.setJob_type("全职");
        jobDetail.setPv("3700次浏览");
        jobDetail.setJd("Java架构");
        jobDetail.setSalary("20K/月");
        jobDetail.setAge(30);
        jobFullTextService.add(jobDetail);
    }

    @Test
    public  void getTest() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        System.out.println(jobFullTextService.findById(4));
    }

    @Test
    public  void update() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        JobDetail jobDetail = new JobDetail();
        jobDetail.setId(4);
        jobDetail.setArea("清华");
        jobFullTextService.update(jobDetail);
    }
    @Test
    public  void delete() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        jobFullTextService.deleteById(4);
    }

    @Test
    public  void searchByKeywords() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        List<JobDetail> details = jobFullTextService.searchByKeywords("java");
        for (JobDetail jobDetail:details) {
            System.out.println(jobDetail);
        }
    }
    @Test
    public  void searchByPage() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        Map<String, Object> map = jobFullTextService.searchByPage("java", 3, 1);
        List<JobDetail> details =(List<JobDetail>)map.get("list");
        System.out.println(map.get("total"));
        for (JobDetail jobDetail:details) {
            System.out.println(jobDetail);
        }
    }
    @Test
    public  void searchByScrollPage1() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        Map<String, Object> map = jobFullTextService.searchByScrollPage("java", null, 1);
        List<JobDetail> details =(List<JobDetail>)map.get("list");
        System.out.println(map.get("total"));
        System.out.println(map.get("scrollId"));
        for (JobDetail jobDetail:details) {
            System.out.println(jobDetail);
        }
    }

    @Test
    public  void searchByScrollPage2() throws IOException {
        jobFullTextService=new JobFullTextServiceImpl();
        Map<String, Object> map = jobFullTextService.searchByScrollPage("java", "DXF1ZXJ5QW5kRmV0Y2gBAAAAAAAAABcWZ0VXT0wwbXJUM0s4MmRKWGVkcENMUQ==", 1);
        List<JobDetail> details =(List<JobDetail>)map.get("list");
        System.out.println(map.get("total"));
        System.out.println(map.get("scrollId"));
        for (JobDetail jobDetail:details) {
            System.out.println(jobDetail);
        }
    }

}
