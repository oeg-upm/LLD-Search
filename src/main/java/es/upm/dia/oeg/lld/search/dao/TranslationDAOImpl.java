package es.upm.dia.oeg.lld.search.dao;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;




import java.util.Set;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.stereotype.Component;
import org.elasticsearch.search.SearchHit;

import es.upm.dia.oeg.lld.search.model.Translation;

@Component
public class TranslationDAOImpl implements TranslationDAO {

	/**
	 *  Get all dictionaries 
	 */
    @Override
    public List<String> getLanguages() {

    	
    	Client client= ElasticsSearchAccess.getInstance();//.startClient();
    	
   	 String guery="{" +
   		        "  \"query\": { \"match_all\": {} }" +
   		        "}";
   		        
   	 SearchRequestBuilder sRequestBuilder = client.prepareSearch(guery);//
   	 sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
   	 sRequestBuilder.setTypes("language");
   	 sRequestBuilder.setSize(500);

   	 SearchResponse response = sRequestBuilder.execute().actionGet();

   	 final List<String> languages = new ArrayList<String>();
   		        
   	 for (SearchHit se : response.getHits().getHits()){
   		 
   		  languages.add(se.getSource().get("langExtended").toString());
   		  
   		            
   	 }
   	
   		        
       //ElasticsSearchAccess.closeClient();
   	
       return languages;
       
       
    }

    /*
      final String queryString = AppConstants.GET_ALL_LANGUAGES_QUERY;

        final Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        final QueryExecution qe = QueryExecutionFactory.sparqlService(
                AppConstants.SPARQL_ENDPOINT, query);

        final ResultSet results = qe.execSelect();

        final List<String> languages = new ArrayList<String>();

        while (results.hasNext()) {
            final QuerySolution result = results.next();
            languages.add(result.get("lang").asLiteral().getString());
        }

        qe.close();

        return languages;
     * */
    
    
    /**
     * Create translation from searchhit from elasticsearch
     * @param se
     * @param indirect
     * @param babelnet
     * @return
     */
    private final Translation createTranslationFromSearchHit(SearchHit se, boolean indirect, boolean babelnet) {
        final Translation trans = new Translation();

        // for the demo only
        final Random rand = new Random();
        final float min = 0.5f;
        final float max = 1f;
        
        try {
         
            trans.setLexiconSource(se.getSource().get("lexicon_source").toString());
            trans.setLexiconTarget(se.getSource().get("lexicon_target").toString());
            
            trans.setWrittenRepSource(se.getSource().get("source_word").toString());
            trans.setWrittenRepTarget(se.getSource().get("target_word").toString());
            
            trans.setLangSource(se.getSource().get("source_lang").toString());
            trans.setLangTarget(se.getSource().get("target_lang").toString());
            
            trans.setPartOfSpeech(se.getSource().get("POS").toString());
            trans.setIndirect(indirect);

            if (babelnet && (se.getSource().get("babelnet").toString()) != null) {
                trans.setBabelnetSynset(se.getSource().get("babelnet").toString());
            }

            if (indirect) {
            	/********** MOCKED VALUE *********/
                //trans.setScore(rand.nextFloat() * (max - min) + min);
                trans.setPivotLanguage(se.getSource().get("pivot_lang").toString());
                
            }

        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
        return trans;
    }

    /** Execute the elasticsearch query and return the results in a list of translations
     * @param sRequestBuilder
     * @param indirect
     * @param babelnet
     * @return
     */
    private final List<Translation> extractTranslationsFromES(
    		
    	SearchRequestBuilder sRequestBuilder, boolean indirect, boolean babelnet) {
    	
    	SearchResponse response = sRequestBuilder.execute().actionGet();
    	final List<Translation> translations = new ArrayList<Translation>();
    	float score=0;

        for (SearchHit se : response.getHits().getHits()){
        	if(se.getScore()>=score){
        		score= se.getScore();
        		Translation trans = createTranslationFromSearchHit(se, indirect, babelnet);
        		translations.add(trans);
        	}
        }
    	
        //ElasticsSearchAccess.closeClient();
        
        if(!babelnet){
        	
        	return cleanDuplicates(translations);
        }
        return translations;
    }
    
    

    

    /** 
     * Elasticsearch access for direct translation queries
     * 
     */
    @Override
    public List<Translation> searchDirectTranslations(String label,String langSource, String langTarget, boolean babelnet) {

    	
    	/*
    	 *
    	 
        TermFilterBuilder fil= FilterBuilders.termFilter("source_lang",Sourcelang);

         //FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("source_word", word),fil);
         FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.queryStringQuery(word),fil);
        
        
 
        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(indexName);
        sRequestBuilder.setTypes("translation");
        sRequestBuilder.setSize(500);
        

        SearchResponse response = sRequestBuilder.execute().actionGet();
        //System.out.println(response);
        int a=0;
        for (SearchHit se : response.getHits().getHits()){
            a++;
            System.out.print(se.getScore()+" ");
            System.out.print(" - ");
            System.out.print(se.getSource().get("source_word").toString());
            System.out.print(" - ");
            System.out.print(se.getSource().get("target_word").toString());
            System.out.print(" - ");
            System.out.println(se.getSource().get("target_lang").toString());
            
        }
        System.out.println(a);
    	 */
    	
    	SearchRequestBuilder sRequestBuilder;

        // TO ALL POSSIBLE LANGUAGES
        if (langTarget == null) {
            
        	TermFilterBuilder fil= FilterBuilders.termFilter("source_lang",langSource);
        	

            //FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("source_word", label),fil);
            FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.queryStringQuery(label),fil);
     
            sRequestBuilder = ElasticsSearchAccess.getInstance().prepareSearch().setQuery(builder);//.startClient()
            sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
            sRequestBuilder.setTypes("translation");
            sRequestBuilder.setSize(1000);
        
        // TO A ONLY ONE LANGUAGE  
        } else {
            
        	TermFilterBuilder fil= FilterBuilders.termFilter("source_lang",langSource);
            TermFilterBuilder fil2= FilterBuilders.termFilter("target_lang",langTarget);

            //FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("source_word", label),FilterBuilders.andFilter(fil).add(fil2));
            FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.queryStringQuery(label),FilterBuilders.andFilter(fil).add(fil2));
     
            sRequestBuilder = ElasticsSearchAccess.getInstance().prepareSearch().setQuery(builder);//.startClient()
            sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
            sRequestBuilder.setTypes("translation");
            sRequestBuilder.setSize(1000);
            
        	
        }
        
        List<Translation> ListRes = extractTranslationsFromES(sRequestBuilder, false, babelnet);
        return ListRes;
    }
    
    
   
    /** 
     * Elasticsearch access for Indirect translation queries
     * 
     */
    @Override
    public List<Translation> searchIndirectTranslations(String label, String langSource, String langTarget, String langPivot, boolean babelnet,double threshold) {

        
    	List<Translation> listResults= new ArrayList<Translation>();
    	
        if (langPivot != null) {
        	listResults=IndirectTranslation.searchIndirectTranslationWithPivotLang(label,langSource,langPivot, langTarget,threshold);
        	
        	
             
        } else {
        	
        	String [] pivotLangs = IndirectTranslation.getPivotLang2(langSource);
        	for(String pivL: pivotLangs){
        		listResults.addAll(IndirectTranslation.searchIndirectTranslationWithPivotLang(label,langSource,pivL, langTarget,threshold));
        		
        	}        
        }
        
        Collections.sort(listResults);
        

        return listResults;
    
    }
    

    public List<Translation> cleanDuplicates(List <Translation> lista ){
    	
    	List <Translation> newLista= new ArrayList<Translation>();
    	Set <String> set= new HashSet<String>();
    	
    	for (Translation t: lista){
    		
    		String text= t.getWrittenRepTarget()+"@"+t.getLangTarget();
    		
    		if(!set.contains(text)){
    			set.add(text);
    			newLista.add(t);
    			
    		}
    		
    	}
    	
    	return newLista;
    }

	@Override
	public String getLanguageCode(String language) {
		
		if (language.equals("All")){
			
			return "All";
		}
		
		Client client= ElasticsSearchAccess.getInstance();
    	
	   	 String guery="{" +
	   		        "  \"query\": { \"match_all\": {} }" +
	   		        "}";
	   		        
	   	 SearchRequestBuilder sRequestBuilder = client.prepareSearch(guery);//
	   	 sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
	   	 sRequestBuilder.setTypes("language");
	   	 sRequestBuilder.setSize(500);

	   	 SearchResponse response = sRequestBuilder.execute().actionGet();

	   
	   	 String res="";
	   	 for (SearchHit se : response.getHits().getHits()){
	   		 
	   		  if(se.getSource().get("langExtended").toString().equals(language)){
	   			  res=se.getSource().get("lang").toString();
	   			  return res; 
	   		  }
	   		  
	   		            
	   	 }
	   	
	   		        
	       //ElasticsSearchAccess.closeClient();
	   	
	       return res;
        
 
        
	}
    
   
    
   
   
    
}
