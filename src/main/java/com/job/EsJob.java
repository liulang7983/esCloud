package com.job;

import com.es.Config;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author ming.li
 * @Date 2024/4/19 10:36
 * @Version 1.0
 */
@Slf4j
@Component
public class EsJob {
    public static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd-HH-mm");
    //设置索引模版，后续自动创建前缀为esb-和erub-的索引，就可以直接使用其参数定义
    @Autowired
    private RestHighLevelClient client;
  /*  PUT _template/esb
    {
        "index_patterns": ["esb-*","erub-*"],
        "settings": {
        "number_of_shards": 1,
                "number_of_replicas":0
    },
        "mappings": {
        "properties": {
            "host_name": {
                "type": "keyword"
            },
            "created_date": {
                "type": "date",
                        "format": "yyyy-MM-dd HH:mm:ss"
            },
            "file_name":{
                "type": "keyword"
            },
            "log":{
                "type": "text",
                        "analyzer": "ik_max_word"
            }
        }
    }
    }*/

    @Scheduled(cron = "0 */5 * * * ? ")
    public void createESB() {
        try {
            Date date = new Date();
            String format = sdf.format(date);
            String index = "esb-" + format;
            log.info("索引名称:"+index);
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
