package com.es;

import com.alibaba.fastjson2.JSON;
import com.bean.Good;
import com.bean.Student;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public  class EsApplicationTests {

    @Autowired
    RestHighLevelClient client;

    /**
     * 创建索引
     */
    @Test
    void createIndex() throws Exception {
        CreateIndexRequest request = new CreateIndexRequest("myindex");
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    /**
     * 判断索引存在
     */
    @Test
    void existsIndex() throws Exception {
        GetIndexRequest request = new GetIndexRequest("myindex");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 删除索引
     */
    @Test
    void deleteIndex() throws Exception {
        DeleteIndexRequest request = new DeleteIndexRequest("myindex");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        boolean acknowledged = response.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 创建文档
     */
    @Test
    void createDoc() throws Exception {
        Student student = new Student("111", "张三", 33, "111222333");	// 文档数据

        IndexRequest request = new IndexRequest("myindex");
        request.id(student.getId());
        request.source(JSON.toJSONString(student), XContentType.JSON);	// 文档数据添加到索引

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);	// 执行
        System.out.println(response.toString());
        System.out.println(response.status());
    }

    /**
     * 判断文档存在
     */
    @Test
    void existsDoc() throws Exception {
        GetRequest request = new GetRequest("myindex", "111");
        request.fetchSourceContext(new FetchSourceContext(false));
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 获取文档,貌似无法获取内容
     */
    @Test
    void getDoc() throws Exception {
        GetRequest request = new GetRequest("myindex", "111");
        request.fetchSourceContext(new FetchSourceContext(false));
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String source = response.getSourceAsString();
        System.out.println(source);
        System.out.println(response);
    }

    /**
     * 删除文档
     */
    @Test
    void deleteDoc() throws Exception {
        DeleteRequest request = new DeleteRequest("myindex", "111");
        request.timeout("2s");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 更新文档
     */
    @Test
    void updateDoc() throws Exception {
        UpdateRequest request = new UpdateRequest("myindex", "111");
        request.timeout("2s");

        Student student = new Student("111", "张四", 44, "444555666");
        request.doc(JSON.toJSONString(student), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 批量操作文档
     */
    @Test
    void bulkOperateDoc() throws Exception {
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(new Student("1", "一桶", 11, "aaa"));
        studentList.add(new Student("2", "二条", 22, "sss"));
        studentList.add(new Student("3", "三万", 33, "ddd"));
        studentList.add(new Student("9", "发财", 44, "fff"));
        studentList.add(new Student("4", "白板", 55, "ggg"));
        studentList.add(new Student("5", "东风", 66, "hhh"));
        studentList.add(new Student("6", "南风", 77, "jjj"));
        studentList.add(new Student("7", "西风", 88, "kkk"));
        studentList.add(new Student("8", "北风", 99, "lll"));

        BulkRequest request = new BulkRequest();
        request.timeout("30s");

        // 批量创建文档
        for (Student student : studentList) {
            request.add(new IndexRequest("myindex").id(student.getId()).source(JSON.toJSONString(student), XContentType.JSON));
        }

        // 批量更新文档
//		for (Student student : studentList) {
//			student.setAge(student.getAge()+100);	// 年龄+100
//			request.add(new UpdateRequest("myindex", student.getId()).doc(JSON.toJSONString(student), XContentType.JSON));
//		}

        // 批量删除文档
//		for (Student student : studentList) {
//			request.add(new DeleteRequest("myindex", student.getId()));
//		}

        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 文档查询
     */
    @Test
    void searchDoc() throws Exception {
        SearchRequest request = new SearchRequest("myindex");

        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 超时时间
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 设置分页
        builder.from(0);
        builder.size(5);
        // 精确匹配
//		builder.query(QueryBuilders.matchPhraseQuery("name", "东风"));	// 查到
        builder.query(QueryBuilders.matchPhraseQuery("name", "风"));		// 查到
//		builder.query(QueryBuilders.matchPhraseQuery("age", "111"));	// 查到
//		builder.query(QueryBuilders.matchPhraseQuery("age", "1"));		// 查不到
//		builder.query(QueryBuilders.rangeQuery("age").gt(130).lt(180));	// 查到
        // 高亮显示
        HighlightBuilder hBuild = new HighlightBuilder();
        hBuild.field("name");
        hBuild.requireFieldMatch(false);	// 单条结果只高亮一个
        hBuild.preTags("<span style='color:red'>");
        hBuild.postTags("</span>");
        builder.highlighter(hBuild);

        request.source(builder);

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println("=====结果====");
        // 结果数据
        for (SearchHit hit : response.getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println(map);
        }
        System.out.println("=====高亮====");
        // 高亮数据
        for (SearchHit hit : response.getHits()) {
            Map<String, HighlightField> map = hit.getHighlightFields();
            System.out.println("获取名字:"+map.get("name"));
            System.out.println(map);
        }
        System.out.println("=====高亮解析====");
        // 高亮数据解析到结果数据中
        for (SearchHit hit : response.getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();	// {phone=hhh, name=东风, id=5, age=166}
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();	// {name=[name], fragments[[东<span style='color:red'>风</span>]]}
            HighlightField field = highlightFields.get("name");
            if (field!=null) {
                Text[] fragments = field.fragments();
                StringBuilder sb = new StringBuilder();
                for (Text fragment : fragments) {
                    sb.append(fragment);
                }
                map.put("name", sb.toString());	// {phone=hhh, name=东<span style='color:red'>风</span>, id=5, age=166}
            }
            System.out.println(map);
        }
    }

    /**
     * jsoup获取网页数据并存入ES
     */
    @Test
    void jsoupCreateDoc() throws Exception {
        ArrayList<Good> goodList = new ArrayList<>();
        // jsoup获取数据并存入集合
        Document document = Jsoup.connect("https://search.jd.com/Search?keyword=oracle").get();
        Elements elements = document.getElementById("J_goodsList").getElementsByTag("li");
        for (Element e : elements) {
            String name = e.getElementsByClass("p-name").eq(0).text();
            String price = e.getElementsByClass("p-price").eq(0).text();
            String img = e.getElementsByTag("img").eq(0).attr("data-lazy-img");
            goodList.add(new Good(name, price, img));
        }
        // 集合数据存入ES的good_index索引下
        BulkRequest request = new BulkRequest();
        request.timeout("1m");
        for (Good good : goodList) {
            request.add(new IndexRequest("good_index").source(JSON.toJSONString(good), XContentType.JSON));
        }
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(!response.hasFailures());
    }

}
