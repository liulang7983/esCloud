package com;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ming.li
 * @date 2022/11/1 9:21
 */
@EnableScheduling
@SpringBootApplication
public class ESCloud1Main {
    public static void main(String[] args) {
        SpringApplication.run(ESCloud1Main.class,args);
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")
                        //new HttpHost()
                )
        );
        return client;
    }


}
