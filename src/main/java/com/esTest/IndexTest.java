package com.esTest;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author ming.li
 * @Date 2024/4/19 15:50
 * @Version 1.0
 */
public class IndexTest {
    public static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));

    //查询所有的索引
    @Test
    public void test1() {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest("*");
            GetIndexResponse indexResponse = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            Map<String, List<AliasMetaData>> aliases = indexResponse.getAliases();
            aliases.get("s");
            String[] indices = indexResponse.getIndices();
            for (String s : indices) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询前缀为esb的索引
    @Test
    public void test2() {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest("esb*");
            GetIndexResponse indexResponse = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            Map<String, List<AliasMetaData>> aliases = indexResponse.getAliases();
            aliases.get("s");
            String[] indices = indexResponse.getIndices();
            for (String s : indices) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询前缀为esb的索引
    @Test
    public void test3() {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest("*");
            GetIndexResponse indexResponse = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            Map<String, List<AliasMetaData>> aliases = indexResponse.getAliases();
            aliases.get("s");
            String[] indices = indexResponse.getIndices();
            for (String s : indices) {
                if (s.startsWith("esb")) {
                    System.out.println(s);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
