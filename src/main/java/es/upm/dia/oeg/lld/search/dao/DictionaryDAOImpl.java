package es.upm.dia.oeg.lld.search.dao;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;
import org.elasticsearch.search.SearchHit;
import es.upm.dia.oeg.lld.search.model.Dictionary;

@Component
public class DictionaryDAOImpl implements DictionaryDAO {

    @Override
    public List<Dictionary> getDictionaries() {
    	
    	Client client= ElasticsSearchAccess.getInstance();//.startClient();
    	
    	 String guery="{" +
    		        "  \"query\": { \"match_all\": {} }" +
    		        "}";
    		        
    	 SearchRequestBuilder sRequestBuilder = client.prepareSearch(guery);//
    	 sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
    	 sRequestBuilder.setTypes("dictionary");
    	 sRequestBuilder.setSize(500);

    	SearchResponse response = sRequestBuilder.execute().actionGet();

    	final List<Dictionary> dictionaries = new ArrayList<Dictionary>();
    		        
    	for (SearchHit se : response.getHits().getHits()){
    		 
    		            final Dictionary dict = new Dictionary();
    		            dict.setLangSource(se.getSource().get("source").toString());
    		            dict.setLangTarget(se.getSource().get("target").toString());
    		            dictionaries.add(dict);
    		            
    	}
    	
    		        
        //ElasticsSearchAccess.closeClient();
    	
        return dictionaries;
    }
    
    
    
    /*
    final String queryString = AppConstants.GET_ALL_DICTIONARIES_QUERY;

    final Query query = QueryFactory.create(queryString);

    // Execute the query and obtain results
    final QueryExecution qe = QueryExecutionFactory.sparqlService(
            AppConstants.SPARQL_ENDPOINT, query);

    final ResultSet results = qe.execSelect();

    final List<Dictionary> dictionaries = new ArrayList<Dictionary>();

    while (results.hasNext()) {
        final QuerySolution result = results.next();
        final Dictionary dict = new Dictionary();
        dict.setLangSource(result.get("lang_source").asLiteral().getString());
        dict.setLangTarget(result.get("lang_target").asLiteral().getString());
        dictionaries.add(dict);
    }

    qe.close();
	*/

}
