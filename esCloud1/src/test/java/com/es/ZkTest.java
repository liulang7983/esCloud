package com.es;


import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author ming.li
 * @date 2022/11/17 14:21
 */
public class ZkTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    public static String ZK="zk";
    @Test
    public void create()throws IOException {
        IndicesClient indices = client.indices();
        CreateIndexRequest zk = new CreateIndexRequest("zk");
        CreateIndexResponse createIndexResponse = indices.create(zk, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.isAcknowledged());
    }

    @Test
    public void delete()throws IOException{
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(ZK);
        AcknowledgedResponse delete1 = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete1.isAcknowledged());
    }


}
