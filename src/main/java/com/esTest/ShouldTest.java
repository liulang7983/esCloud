package com.esTest;

import com.alibaba.fastjson2.JSONObject;
import com.bean.EsDb;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ming.li
 * @date 2023/11/15 15:50
 */
public class ShouldTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9201)));
    private static String ES_DB="es_db";
    //bool查询name的值是张三的，address的值含有公园的,age是99的(满足一个即可)
    @Test
    public void test1() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        //张三用matchQuery可以查到，用termQuery查不到
        query.should(QueryBuilders.matchQuery("name","张三"));
        query.should(QueryBuilders.matchQuery("address","白云"));
        query.should(QueryBuilders.termQuery("age",99));
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
