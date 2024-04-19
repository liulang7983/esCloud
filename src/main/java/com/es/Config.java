package com.es;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author ming.li
 * @date 2022/11/15 10:13
 */

public class Config {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
}
