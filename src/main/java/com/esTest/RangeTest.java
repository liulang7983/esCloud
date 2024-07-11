package com.esTest;

import com.alibaba.fastjson2.JSONObject;
import com.bean.EsDb;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ming.li
 * @date 2023/11/15 14:55
 */
public class RangeTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    private static String ES_DB="es_db";

    //range查询age在19-26之间的值(包含)
    @Test
    public void test1() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        RangeQueryBuilder query = QueryBuilders.rangeQuery("age").from(19).to(26);
        builder.query(query);
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            System.out.println(esDb);
        }
    }
    //range查询age在19-26之间的值(包含)
    @Test
    public void test2() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        RangeQueryBuilder query = QueryBuilders.rangeQuery("age").gte(19).lte(26);

        builder.query(query);
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            System.out.println(esDb);
        }
    }

    //range查询age在19-26之间的值(不包含)
    @Test
    public void test3() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        RangeQueryBuilder query = QueryBuilders.rangeQuery("age").gt(19).lt(26);

        builder.query(query);
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            System.out.println(esDb);
        }
    }
}
