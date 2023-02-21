package com.service.Impl;


import com.alibaba.fastjson2.JSONObject;
import com.bean.JobDetail;
import com.service.JobFullTextService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ming.li
 * @date 2022/11/2 11:18
 */
public class JobFullTextServiceImpl implements JobFullTextService {

    private RestHighLevelClient client;

    private static String JOB_INDEX="job_index";

    public JobFullTextServiceImpl() {
        client= new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    }

    @Override
    public void add(JobDetail jobDetail) throws IOException {
        IndexRequest indexRequest = new IndexRequest(JOB_INDEX);
        indexRequest.id(jobDetail.getId()+"");
        String json = JSONObject.toJSONString(jobDetail);
        indexRequest.source(json, XContentType.JSON);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public JobDetail findById(long id) throws IOException {
        GetRequest getRequest = new GetRequest(JOB_INDEX, id + "");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
        String string = response.getSourceAsString();
        JobDetail jobDetail = JSONObject.parseObject(string, JobDetail.class);
        jobDetail.setId(id);
        return jobDetail;
    }

    @Override
    public void update(JobDetail jobDetail) throws IOException {
        GetRequest getRequest = new GetRequest(JOB_INDEX, jobDetail.getId() + "");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        if (exists){
            UpdateRequest updateRequest = new UpdateRequest(JOB_INDEX,jobDetail.getId()+"");
            updateRequest.doc(JSONObject.toJSONString(jobDetail),XContentType.JSON);
            client.update(updateRequest,RequestOptions.DEFAULT);
            //updateRequest.
        }

    }

    @Override
    public void deleteById(long id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(JOB_INDEX, id + "");
        client.delete(deleteRequest,RequestOptions.DEFAULT);

    }

    @Override
    public List<JobDetail> searchByKeywords(String keywords) throws IOException {
        SearchRequest searchRequest = new SearchRequest(JOB_INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, "title", "jd");
        sourceBuilder.query(multiMatchQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        ArrayList<JobDetail> jobDetailArrayList=new ArrayList<>();

        for(SearchHit searchHit:hits){
            String sourceAsString = searchHit.getSourceAsString();
            JobDetail jobDetail = JSONObject.parseObject(sourceAsString, JobDetail.class);
            jobDetailArrayList.add(jobDetail);
        }


        return jobDetailArrayList;
    }

    @Override
    public Map<String, Object> searchByPage(String keywords, int pageNum, int pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest(JOB_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, "title", "jd");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.from((pageNum-1)*pageSize);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        ArrayList<JobDetail> jobDetailArrayList=new ArrayList<>();
        for(SearchHit searchHit:hits){
            String sourceAsString = searchHit.getSourceAsString();
            JobDetail jobDetail = JSONObject.parseObject(sourceAsString, JobDetail.class);
            jobDetailArrayList.add(jobDetail);
        }
        Map map=new HashMap();
        map.put("list",jobDetailArrayList);
        map.put("total",search.getHits().getTotalHits().value);
        return map;
    }

    @Override
    public Map<String, Object> searchByScrollPage(String keywords, String scrollId, int pageSize) throws IOException {
        SearchResponse searchResponse = null;
        if (scrollId==null){
            SearchRequest searchRequest = new SearchRequest(JOB_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, "title", "jd");
            searchSourceBuilder.query(multiMatchQueryBuilder);
            searchSourceBuilder.size(pageSize);
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(TimeValue.timeValueMinutes(5));
             searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        }else {
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            scrollRequest.scroll(TimeValue.timeValueMinutes(5));
             searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
        }
        SearchHit[] hits = searchResponse.getHits().getHits();
        ArrayList<JobDetail> jobDetailArrayList=new ArrayList<>();
        for(SearchHit searchHit:hits){
            String sourceAsString = searchHit.getSourceAsString();
            JobDetail jobDetail = JSONObject.parseObject(sourceAsString, JobDetail.class);
            jobDetail.setId(Integer.parseInt(searchHit.getId()));
            jobDetailArrayList.add(jobDetail);
        }
        Map map=new HashMap();
        map.put("list",jobDetailArrayList);
        map.put("total",searchResponse.getHits().getTotalHits().value);
        map.put("scrollId",searchResponse.getScrollId()) ;
        return map;

    }

    @Override
    public void close() throws IOException {

    }
}
