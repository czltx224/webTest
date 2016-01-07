package com.czl.es;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * 
 *
 *  @version ： 1.0
 *  
 *  @author  ： 苏若年              <a href="mailto:DennisIT@163.com">发送邮件</a>
 *    
 *  @since   ： 1.0        创建时间:    2013-4-8    上午11:34:04
 *     
 *  @function： TODO        
 *
 */
public class ElasticSearchHandler {

    private Client client;

    public ElasticSearchHandler(){    
        //使用本机做为节点
        this("172.16.10.43");
    }
    
    public ElasticSearchHandler(String ipAddress){
        //集群连接超时设置
        /*  
              Settings settings = ImmutableSettings.settingsBuilder().put("client.transport.ping_timeout", "10s").build();
            client = new TransportClient(settings);
         */
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(ipAddress, 9300));
    }
    
    
    /**
     * 建立索引,索引建立好之后,会在elasticsearch-0.20.6\data\elasticsearch\nodes\0创建所以你看
     * @param indexName  为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @param indexType  Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @param jsondata     json格式的数据集合
     * 
     * @return
     */
    public void createIndexResponse(String indexname, String type, List<String> jsondata){
        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
        //IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type).setRefresh(true);
    	IndexRequestBuilder requestBuilder = client.prepareIndex(indexname, type);
        for(int i=0; i<jsondata.size(); i++){
            try{
            	requestBuilder.setSource(jsondata.get(i)).execute().actionGet();
            }catch(Exception e){
            	e.printStackTrace();
            }
        }     
         
    }
    
    /**
     * 创建索引
     * @param client
     * @param jsondata
     * @return
     */
    public IndexResponse createIndexResponse(String indexname, String type,String jsondata){
        IndexResponse response = client.prepareIndex(indexname, type)
            .setSource(jsondata)
            .execute()
            .actionGet();
        return response;
    }
    
    /**
     * 执行搜索
     * @param queryBuilder
     * @param indexname
     * @param type
     * @return
     */
    public List<Medicine>  searcher(QueryBuilder queryBuilder, String indexname, String type){
        List<Medicine> list = new ArrayList<Medicine>();
        SearchRequestBuilder searchRequestBuilder =client.prepareSearch(indexname).setTypes(type)
                .setQuery(queryBuilder).setSize(20);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询到记录数=" + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        System.out.println("length=" + searchHists.length);
        if(searchHists.length>0){
            for(SearchHit hit:searchHists){
                Integer id = (Integer)hit.getSource().get("id");
                String name =  (String) hit.getSource().get("name");
                String function =  (String) hit.getSource().get("funciton");
                list.add(new Medicine(id, name, function));
            }
        }
        return list;
    }
    
    public List<Medicine>  searcher(String keyword, String indexname, String type){
        List<Medicine> list = new ArrayList<Medicine>();
        SearchRequestBuilder searchRequestBuilder =client.prepareSearch(indexname).setTypes(type)
                .setQuery(getQuery(keyword)).setSize(20);
        if(StringUtils.isNotBlank(keyword)){
			searchRequestBuilder.addHighlightedField("name")
			                    .addHighlightedField("funciton")
			                    .setHighlighterPreTags("<font color='red'>")
			                    .setHighlighterPostTags("</font>");
		}
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询到记录数=" + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        System.out.println("length=" + searchHists.length);
        if(searchHists.length>0){
            for(SearchHit hit:searchHists){
                Integer id = (Integer)hit.getSource().get("id");
                String name =  (String) hit.getSource().get("name");
                String function =  (String) hit.getSource().get("funciton");
                list.add(new Medicine(id, name, function));
            }
        }
        return list;
    }
    
    private QueryBuilder getQuery(String keyword) {
		if(StringUtils.isBlank(keyword)){
			return QueryBuilders.matchAllQuery();
		}
		return QueryBuilders.multiMatchQuery(keyword, "name","funciton").operator(MatchQueryBuilder.Operator.AND);
	}
    
    public static void main(String[] args) {
        ElasticSearchHandler esHandler = new ElasticSearchHandler();
        List<String> jsondata = DataFactory.getInitJsonData();
        String indexname = "indexdemo";
        String type = "typedemo";
        //esHandler.createIndexResponse(indexname, type, jsondata);

        //查询条件
        //QueryBuilder queryBuilder = QueryBuilders.fieldQuery("name", "感冒");
        QueryBuilder queryBuilder = QueryBuilders.termQuery("keyword", "银");
        /*QueryBuilder queryBuilder = QueryBuilders.boolQuery()
          .must(QueryBuilders.termQuery("id", 1));*/
        List<Medicine> result = esHandler.searcher("解表清热", indexname, type);
        for(int i=0; i<result.size(); i++){
            Medicine medicine = result.get(i);
            System.out.println("(" + medicine.getId() + ")药品名称:" +medicine.getName() + "\t\t" + medicine.getFunction());
        }
    }
}