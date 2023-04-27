package com.es;

import com.alibaba.fastjson2.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author ming.li
 * @date 2023/4/2 0:10
 */
public class getIndexTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    public static String ES_DB="es_db";

    @Test
    public void  test1() throws IOException {
        GetAliasesResponse alias = client.indices().getAlias(new GetAliasesRequest(), RequestOptions.DEFAULT);
        Map<String, Set<AliasMetaData>> aliases = alias.getAliases();
        Iterator<String> iterator = aliases.keySet().iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }

    }

    @Test
    public void  test2() throws IOException {
        //client.getLowLevelClient().performRequest();

    }

    @Test
    public void catIndeces() {
        Request request = new Request("GET" ,"/_cat/indices");
        System.out.println("进入方法");
        Map<String, Long> map = new HashMap<>(4);
        try {
            Response response = client.getLowLevelClient().performRequest(request);
            HttpEntity entity = request.getEntity();
            String string = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println(string);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String content = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = JSONArray.parseArray(content);
                //遍历索引
                for (Object obj : jsonArray) {
                    Map<String, String> indexInfoMap = (Map) obj;
                    System.out.println(indexInfoMap);
                    long docsCount = Long.valueOf(indexInfoMap.get("docs.count"));
                    System.out.println(docsCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
