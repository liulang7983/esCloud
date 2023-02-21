package com.es;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xpack.sql.jdbc.EsDriver;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author ming.li
 * @date 2022/11/25 9:37
 */
public class sqlTest {
    public static RestHighLevelClient client=new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1",9200)));

    @Test
    public void test() throws Exception {
        // 1. 加载ES驱动
        Class.forName(EsDriver.class.getName());

        // 2. 建立连接
        Connection connection = DriverManager.getConnection("jdbc:es://http://127.0.0.1:9200");

        // 3. 准备SQL语句
        String sql = "select *from es_db order by age";

        // 4. 使用PreparedStatement执行SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 5. 遍历结果
        while(resultSet.next()) {
            System.out.println("名字:"+resultSet.getString("name"));
        }

        // 6. 关闭连接
        resultSet.close();
        connection.close();
    }

    @Test
    public void test1() throws IOException {

        String method = "GET";
        String endPoint = "/_sql";
        //String sql="{\"query\":\"select * from es_db LIMIT 5\"}";
        String sql1="{\"query\":\"select * from \\\"es-db\\\" limit 5\"}";
        Request request = new Request(method, endPoint);
        request.addParameter("format", "json");
        request.setJsonEntity(sql1);
        System.out.println(sql1);
        Response response =client.getLowLevelClient().performRequest(request);
        String s = EntityUtils.toString(response.getEntity());
        System.out.println(s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONArray columns = jsonObject.getJSONArray("columns");
        for (int i = 0; i < columns.size(); i++) {
            Object o = columns.get(i);
            JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
            System.out.println(jsonObject1.getString("name"));
        }
        System.out.println();
        JSONArray rows = jsonObject.getJSONArray("rows");
        for (int i = 0; i < rows.size(); i++) {
            JSONArray jsonArray = rows.getJSONArray(i);
            for (int j = 0; j < jsonArray.size(); j++) {
                System.out.println(jsonArray.get(j));
            }
            System.out.println();
        }
        System.out.println(sql1);
        //System.out.println(sql1);
    }

}
