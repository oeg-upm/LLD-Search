package es.upm.dia.oeg.lld.search.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;

public class ElasticSearchQueries {
	
	public static String getTranslationSetFromDictionary(Client client, String indexName, String SourceLang, String TargetLang) {

        TermFilterBuilder fil= FilterBuilders.termFilter("source",SourceLang);
        FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("target", TargetLang),fil);
         
        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(indexName);
        sRequestBuilder.setTypes("dictionary");
        sRequestBuilder.setSize(500);
        
        SearchResponse response = sRequestBuilder.execute().actionGet();
        String res="";
        for (SearchHit se : response.getHits().getHits()){
            res= se.getSource().get("transet").toString();
        }
        return res;
    }
    
    
    public static String getLexiconfromLanguage(Client client, String indexName, String Lang) {

        TermQueryBuilder builder = QueryBuilders.termQuery("lang", Lang);
        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);
        
        sRequestBuilder.setIndices(indexName);
        sRequestBuilder.setTypes("language");
        sRequestBuilder.setSize(500);

        SearchResponse response = sRequestBuilder.execute().actionGet();
        String res = "";
        for (SearchHit se : response.getHits().getHits()) {
            res = se.getSource().get("lexicon_apertium").toString();
        }
        return res;
    }

    
    public static List<String> getLexicalEntriesFromLemma(Client client, String indexName, String SourceLexicon, String Sourcelemma, String sourceLanguage) {

        TermFilterBuilder fil = FilterBuilders.termFilter("lexicon_source", SourceLexicon);
        TermFilterBuilder fil2 = FilterBuilders.termFilter("source_lang", sourceLanguage);

        FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("source_word", Sourcelemma),
                FilterBuilders.andFilter(fil).add(fil2));

        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(indexName);
        sRequestBuilder.setTypes("translation");
        sRequestBuilder.setSize(500);

        Set<String> LexicalEntryList = new HashSet<String>();

        SearchResponse response = sRequestBuilder.execute().actionGet();
        float score=0;

        for (SearchHit se : response.getHits().getHits()) {
        	if (se.getScore()>= score){
        		score= se.getScore();
        		LexicalEntryList.add(se.getSource().get("lexicalentrySource").toString());
        	}
        }
        return new ArrayList<String>(LexicalEntryList);

    }
    
    
    public static List<String> obtainDirectTranslationsFromLexicalEntry(Client client, String indexName, String translationSetURI, String sourceLexicalEntryURI) {

        TermFilterBuilder fil = FilterBuilders.termFilter("translationSet", translationSetURI);
        FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("lexicalentrySource", sourceLexicalEntryURI), fil);

        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(indexName);
        sRequestBuilder.setTypes("translation");
        sRequestBuilder.setSize(500);

        Set<String> LexicalEntryList = new HashSet<>();

        SearchResponse response = sRequestBuilder.execute().actionGet();

        for (SearchHit se : response.getHits().getHits()) {

            LexicalEntryList.add(se.getSource().get("lexicalentryTarget").toString());
        }
        return new ArrayList<String>(LexicalEntryList);

    }

    public static String[] getLemaAndPosofTargetTranslation(Client client, String indexName, String targetTranslation) {

        TermQueryBuilder builder = QueryBuilders.termQuery("lexicalentryTarget", targetTranslation);
        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(indexName);
        sRequestBuilder.setTypes("translation");
        sRequestBuilder.setSize(500);

        SearchResponse response = sRequestBuilder.execute().actionGet();
        String[] res = null;
        for (SearchHit se : response.getHits().getHits()) {

            String babelnet= "";
            try{
                babelnet=se.getSource().get("babelnet").toString();
            }catch(Exception e){
            
            }
            
            String[] lema = {se.getSource().get("target_word").toString(), 
                            se.getSource().get("POS").toString(),
                            se.getSource().get("target_lang").toString(),babelnet};
            res = lema;
            break; // just one
        }
        return res;
    }
	
	
	
	
	

}
