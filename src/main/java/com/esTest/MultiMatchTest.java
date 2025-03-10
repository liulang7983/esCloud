package com.esTest;

import com.alibaba.fastjson2.JSONObject;
import com.bean.EsDb;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ming.li
 * @date 2023/11/15 14:39
 */
public class MultiMatchTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9201)));
    private static String ES_DB="es_db";
    //multiMatch默认或查询索引中address或name字段含有张三的值
    @Test
    public void test1() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery("张三", "address", "name");
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
    //multiMatch与查询索引中address或name字段含有广州张三的值(此时查不到，应该是需要address或者name同时含有广州张三，而不是一个在address，一个在name中)
    //长沙张三可以，有一个数据address是'广长沙岳麓山张三'
    @Test
    public void test2() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery("长沙张三", "address", "name").operator(Operator.AND);
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
