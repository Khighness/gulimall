package top.parak.gulimall.search;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.parak.gulimall.search.config.GulimallElasticSearchConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class GulimallSearchApplicationTest {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void t() {
        System.out.println(client);
    }

    @Data
    static class User {
        private String username;
        private String gender;
        private Integer age;
    }

    @Data
    @ToString
    static class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    /**
     * 添加操作，向users索引添加数据
     */
    @Test
    public void index() throws IOException {
        User user = new User();
        user.setUsername("Khighness");
        user.setAge(20);
        user.setGender("男");
        String s = JSON.toJSONString(user);

        // 构造请求
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        indexRequest.source(s, XContentType.JSON);

        // 获取结果
        IndexResponse indexResponse = client.index(
                indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS
        );

        System.out.println(indexResponse);
    }

    /**
     * 检索操作，搜索address中包含mill的所有的人的年龄分布以及平均年龄
     * <pre>
     * GET /bank/_search
     * {
     *   "query": {
     *     "match": {
     *       "address": "mill"
     *     }
     *   },
     *   "aggs": {
     *     "ageAgg": {
     *       "terms": {
     *         "field": "age",
     *         "size": 10
     *       }
     *     },
     *     "ageAvg": {
     *       "avg": {
     *         "field": "age"
     *       }
     *     },
     *     "balanceAvg": {
     *       "avg": {
     *         "field": "balance"
     *       }
     *     }
     *   },
     *   "size": 0
     * }
     * </pre>
     */
    @Test
    public void search() throws IOException {
        // 1. 构造请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 检索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 1) 检索田间
        sourceBuilder.query(
                QueryBuilders.matchQuery("address", "mill")
        );

        // 2) 按照年龄进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders
                .terms("ageAgg").field("age").size(10);

        // 3) 计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders
                .avg("balanceAvg").field("balance");

        sourceBuilder.aggregation(ageAgg).aggregation(balanceAvg);
        searchRequest.source(sourceBuilder);
        System.out.println("查询条件：" + sourceBuilder.toString());

        // 2. 执行检索
        SearchResponse searchResponse = client.search(
                searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS
        );
        System.out.println("返回结果：" + searchResponse);

        // 1) 获取数据
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            Account account = JSON.parseObject(searchHit.getSourceAsString(), Account.class);
            System.out.println(account);
        }

        // 2) 获取聚合
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageAgg1 = aggregations.get("ageAgg");
        List<? extends Terms.Bucket> buckets = ageAgg1.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            System.out.printf("年龄：%s, 人数：%d\n", keyAsString, bucket.getDocCount());
        }
        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资：" + balanceAvg1.getValue());
    }

    @Test
    void split() {
        String[] s1 = "_3".split("_"); // ["", "3"]
        String[] s2 = "3_".split("_"); // ["3"]
        System.out.println(Arrays.toString(s1));
        System.out.println(Arrays.toString(s2));
    }

}
