package com.es;

import com.sun.org.apache.bcel.internal.generic.NEW;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.client.enrich.StatsRequest;
import org.elasticsearch.client.enrich.StatsResponse;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CloseIndexResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ming.li
 * @date 2022/11/24 9:50
 */
public class ClusterTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));
    /**
     * 打印集群中索引的各项信息
     */
    @Test
    public void status ()throws IOException {{
        Response response = client.getLowLevelClient().performRequest(new Request("GET", "/_cat/indices"));
        // 3、数据处理
        HttpEntity entity = response.getEntity();
        System.out.println("打印:entity:"+entity);
        String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        System.out.println(responseStr);
        // 4、数据分解
        String[] indexInfoArr = responseStr.split("\n");
        for (String indexInfo : indexInfoArr) {
            // 4.1、索引信息输出
            String[] infoArr = indexInfo.split("\\s+");
            String status = infoArr[0];
            String open = infoArr[1];
            String name = infoArr[2];
            String id = infoArr[3];
            String mainShardNum = infoArr[4];
            String viceShardNum = infoArr[5];
            String docNum = infoArr[6];
            String deletedDocNum = infoArr[7];
            String allShardSize = infoArr[8];
            String mainShardSize = infoArr[9];
            System.out.println("名称：" + name+"   id：" + id+" 状态：" + status+"   是否开放：" + open+"   主分片数量：" + mainShardNum+"   副本分片数量：" + viceShardNum+"   Lucene文档数量：" + docNum
            +"   被删除文档数量：" + deletedDocNum+"   所有分片大小：" + allShardSize+"   主分片大小：" + mainShardSize);
        }
        // 6、关闭ES客户端对象
        }
    }

    @Test
    public void mapping ()throws IOException {{
        Response response = client.getLowLevelClient().performRequest(new Request("GET", "/_cat/indices"));
        // 3、数据处理
        HttpEntity entity = response.getEntity();
        System.out.println("打印:entity:"+entity);
        String responseStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        System.out.println(responseStr);
        // 4、数据分解
        String[] indexInfoArr = responseStr.split("\n");
        for (String indexInfo : indexInfoArr) {
            // 4.1、索引信息输出
            String[] infoArr = indexInfo.split("\\s+");
            String name = infoArr[2];
            System.out.println("index名字:"+name);
            //构建请求
            GetMappingsRequest request = new GetMappingsRequest().indices(name);
            //使用RestHighLevelClient发起请求
            GetMappingsResponse mapping = client.indices().getMapping(request, RequestOptions.DEFAULT);
            Map<String, MappingMetaData> mappings = mapping.mappings();
            Iterator<String> iterator = mappings.keySet().iterator();
            System.out.println(iterator.hasNext());
            while (iterator.hasNext()){
                String next = iterator.next();
                System.out.println(next+":"+mappings.get(next));
                MappingMetaData mappingMetaData = mappings.get(next);
                Map<String, Object> sourceAsMap = mappingMetaData.getSourceAsMap();
                Object properties = sourceAsMap.get("properties");
                System.out.println(properties);
            }

            System.out.println("==================");
        }
        // 6、关闭ES客户端对象
    }
    }

    //获取所有的索引信息
    @Test
    public  void index() {
        try {
            GetAliasesRequest request = new GetAliasesRequest();
            GetAliasesResponse getAliasesResponse =  client.indices().getAlias(request,RequestOptions.DEFAULT);
            Map<String, Set<AliasMetaData>> map = getAliasesResponse.getAliases();
            Set<String> indices = map.keySet();
            for (String key : indices) {
                System.out.println(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取索引的分片数
    @Test
    public void setting ()throws IOException {
        GetSettingsRequest getSettings=new GetSettingsRequest().indices("job_idx_shard_temp");
        GetSettingsResponse getSettingsResponse=client.indices().getSettings(getSettings, RequestOptions.DEFAULT);

        String numberOfShardsString = getSettingsResponse.getSetting("job_idx_shard_temp", "index.number_of_shards");
        System.out.println(numberOfShardsString);

    }
    //关闭索引
    @Test
    public void close ()throws IOException {
        CloseIndexRequest closeIndexRequest = new CloseIndexRequest(("job_idx_shard_temp"));
        CloseIndexResponse close = client.indices().close(closeIndexRequest, RequestOptions.DEFAULT);
        System.out.println(close.isAcknowledged());

    }

    //打开索引
    @Test
    public void open ()throws IOException {
        OpenIndexRequest openIndexRequest = new OpenIndexRequest("job_idx_shard_temp");
        OpenIndexResponse open = client.indices().open(openIndexRequest, RequestOptions.DEFAULT);
        System.out.println(open.isAcknowledged());

    }
}
