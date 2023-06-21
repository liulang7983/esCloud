package com.es;


import com.alibaba.fastjson2.JSONObject;
import com.bean.EsDb;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.ShardsAcknowledgedResponse;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CloseIndexResponse;
import org.elasticsearch.client.indices.FreezeIndexRequest;
import org.elasticsearch.client.indices.UnfreezeIndexRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;

import java.io.IOException;


/**
 * @author ming.li
 * @date 2022/11/15 10:12
 */
public class esdbTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    public static String ES_DB="es_db";


    @Test
    public void  getById() throws IOException {
        GetRequest getRequest = new GetRequest(ES_DB,"1");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        System.out.println(sourceAsString);
        EsDb esDb = JSONObject.parseObject(sourceAsString, EsDb.class);
        esDb.setId(response.getId());
        System.out.println(esDb);

    }

    @Test
    public void  getSearch() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(100);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i <hits.length ; i++) {
            EsDb esDb = JSONObject.parseObject(hits[i].getSourceAsString(), EsDb.class);
            esDb.setId(hits[i].getId());
            System.out.println(esDb);
            System.out.println("-----------------------");
        }
    }

    @Test
    public void searchByKeywords() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //查询name或者address带有张三的
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("张三", "name", "address");
        searchSourceBuilder.query(builder);
        request.source(searchSourceBuilder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i <hits.length ; i++) {
            EsDb esDb = JSONObject.parseObject(hits[i].getSourceAsString(), EsDb.class);
            esDb.setId(hits[i].getId());
            System.out.println(esDb);
            System.out.println("-----------------------");
        }
    }
    @Test
    public  void searchByPage()throws  IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询name或者address带有张三的，且按照年龄从大到小排序取前两个
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("张三", "name", "address");
        sourceBuilder.query(builder);
        sourceBuilder.size(2);
        sourceBuilder.from(0);
        sourceBuilder.sort("age", SortOrder.DESC);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);

        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");

        }
    }

    @Test
    public void SearchRequest()throws  IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //精准查询,name是张三的
        TermQueryBuilder builder = QueryBuilders.termQuery("name", "张三");
        sourceBuilder.query(builder);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");

        }
    }

    @Test
    public void SearchRequest1()throws  IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("张三", "name","address");
        sourceBuilder.query(builder);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public void SearchRequest2()throws  IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //name以张开头
        PrefixQueryBuilder builder = QueryBuilders.prefixQuery("name", "张");
        //MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("张三", "name","address");
        sourceBuilder.query(builder);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public  void operatorSearch() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //模糊查询remark带有java的doc
        MatchQueryBuilder builder = QueryBuilders.matchQuery("remark", "java");

        //builder.queryName("java");
        sourceBuilder.query(builder);
        sourceBuilder.size(100);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }

    }

    @Test
    public  void operatorMust() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        BoolQueryBuilder builder1 = QueryBuilders.boolQuery();
        BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
        BoolQueryBuilder builder3 = QueryBuilders.boolQuery();
        builder1.must(QueryBuilders.termQuery("remark","java"));
        builder2.must(QueryBuilders.termQuery("remark","developer"));
        builder.must(builder1);
        builder.must(builder2);
        builder3.must(builder);
        sourceBuilder.query(builder3);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public  void operatorMust2() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        BoolQueryBuilder builder1 = QueryBuilders.boolQuery();
        BoolQueryBuilder builder2 = QueryBuilders.boolQuery();
        BoolQueryBuilder builder3 = QueryBuilders.boolQuery();
        builder1.must(QueryBuilders.termQuery("remark","java"));
        builder2.must(QueryBuilders.termQuery("remark","developer"));
        builder3.must(QueryBuilders.termQuery("remark","the"));
        builder.must(builder1);
        builder.must(builder2);
        builder.must(builder3);
        sourceBuilder.query(builder);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public  void operatorMust3() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder builder = QueryBuilders.matchQuery("remark", "java");
        MatchQueryBuilder builder2 = QueryBuilders.matchQuery("remark", "the");
        BoolQueryBuilder builder1 = QueryBuilders.boolQuery();
        builder1.must(builder);
        builder1.must(builder2);
        sourceBuilder.query(builder1);
        request.source(sourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public  void exists() throws IOException {
        GetRequest getRequest = new GetRequest(ES_DB,"1");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    @Test
    public void filter()throws IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("remark","张三丰"));
        builder.query(boolQuery);
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public void filter2()throws IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        RangeQueryBuilder age = QueryBuilders.rangeQuery("age");
        age.gte("20");
        age.lte("25");
        builder.query(age);
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public void queryString()throws IOException{
        SearchRequest request = new SearchRequest(ES_DB);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        QueryStringQueryBuilder query = QueryBuilders.queryStringQuery("张三");
        builder.query(query);
        request.source(builder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            EsDb esDb = JSONObject.parseObject(hit.getSourceAsString(), EsDb.class);
            esDb.setId(hit.getId());
            System.out.println(esDb);
            System.out.println("----------------------");
        }
    }

    @Test
    public void close() throws IOException {
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(ES_DB);
        CloseIndexResponse close = client.indices().close(closeIndexRequest, RequestOptions.DEFAULT);
        System.out.println(close.isAcknowledged());
    }

    @Test
    public void open() throws IOException {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest(ES_DB);
        OpenIndexResponse open = client.indices().open(openIndexRequest, RequestOptions.DEFAULT);
        System.out.println(open.isAcknowledged());
    }

    @Test
    public void freeze() throws IOException {
        FreezeIndexRequest request = new FreezeIndexRequest(ES_DB);
        ShardsAcknowledgedResponse freeze = client.indices().freeze(request, RequestOptions.DEFAULT);
        System.out.println(freeze.isShardsAcknowledged());
    }

    @Test
    public void unfreeze() throws IOException {
        UnfreezeIndexRequest request = new UnfreezeIndexRequest(ES_DB);
        ShardsAcknowledgedResponse freeze = client.indices().unfreeze(request, RequestOptions.DEFAULT);
        System.out.println(freeze.isShardsAcknowledged());
    }

    @Test
    public void freezeSelect() throws IOException {
        SearchRequest request = new SearchRequest(ES_DB);
        //设置冻结也可查，否则返回为空
        request.indicesOptions(IndicesOptions.fromOptions(false,true,true,false,true,true,false,false));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery("张三", "name", "address");
        searchSourceBuilder.query(builder);
        request.source(searchSourceBuilder);
        SearchResponse search = client.search(request,RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        for (int i = 0; i <hits.length ; i++) {
            EsDb esDb = JSONObject.parseObject(hits[i].getSourceAsString(), EsDb.class);
            esDb.setId(hits[i].getId());
            System.out.println(esDb);
            System.out.println("-----------------------");
        }
    }

}
