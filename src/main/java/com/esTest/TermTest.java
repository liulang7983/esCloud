package com.esTest;

import com.alibaba.fastjson2.JSONObject;
import com.bean.EsDb;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ming.li
 * @date 2023/11/15 15:35
 */
public class TermTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    private static String ES_DB="es_db";

    //term查询name的值是张三的
    @Test
    public void test1() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermQueryBuilder query = QueryBuilders.termQuery("name", "张三");
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
    //match查询name的值包含张三的
    @Test
    public void test2() throws IOException {
        SearchRequest searchRequest = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MatchQueryBuilder query = QueryBuilders.matchQuery("name", "张三");
        builder.query(query);
        searchRequest.source(builder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (SearchHit hit:hits){
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            System.out.println(esDb);
        }
    }
    //term查询age的值是22的
    @Test
    public void test3() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermQueryBuilder query = QueryBuilders.termQuery("age", "22");
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

    //terms查询age的值是22或99的
    @Test
    public void test4() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermsQueryBuilder query = QueryBuilders.termsQuery("age", "22", "99");
        //TermQueryBuilder query = QueryBuilders.termsQuery("name", "张三");
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
