package es.upm.dia.oeg.lld.search.dao;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.springframework.stereotype.Component;
import org.elasticsearch.search.SearchHit;

import es.upm.dia.oeg.lld.search.model.IndirectTranslationAlgorithm;
import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.Translation;
import es.upm.dia.oeg.lld.search.service.ElasticsSearchAccess;


@Component
public class TranslationDAOImpl implements TranslationDAO {

    /**
     * Create translation from searchhit from elasticsearch
     * @param se
     * @param indirect
     * @param babelnet
     * @return
     */
	
    private final Translation createTranslationFromSearchHit(SearchHit se, boolean indirect, boolean babelnet) {
        final Translation trans = new Translation();
        
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
    	float score=0; // avoid : "banco" .... "banco Santander"

        for (SearchHit se : response.getHits().getHits()){
        	if(se.getScore()>=score){
        		score= se.getScore();
        		Translation trans = createTranslationFromSearchHit(se, indirect, babelnet);
        		translations.add(trans);
        	}
        }
        
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
    public List<Translation> searchDirectTranslations(Language lang,String label,String langSource, String langTarget, boolean babelnet) {
    	
    	
    	List<Translation> ListRes = new ArrayList<Translation>();
    	SearchRequestBuilder sRequestBuilder;

        // TO ALL POSSIBLE LANGUAGES
        if (langTarget == null) {
            
        	
        		
        		TermFilterBuilder fil= FilterBuilders.termFilter("source_lang",langSource);
                FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("source_word", label),fil);
        		//FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.queryStringQuery(label),FilterBuilders.andFilter(fil).add(fil2));
        		
                sRequestBuilder = ElasticsSearchAccess.getInstance().prepareSearch().setQuery(builder);
                sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
                sRequestBuilder.setTypes("translation");
                sRequestBuilder.setSize(1000);
                
                
        // TO A ONLY ONE LANGUAGE  
        } else {
            
        	TermFilterBuilder fil= FilterBuilders.termFilter("source_lang",langSource);
            TermFilterBuilder fil2= FilterBuilders.termFilter("target_lang",langTarget);

            FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.termQuery("source_word", label),FilterBuilders.andFilter(fil).add(fil2));
            //FilteredQueryBuilder builder = QueryBuilders.filteredQuery(QueryBuilders.queryStringQuery(label),FilterBuilders.andFilter(fil).add(fil2));
     
            sRequestBuilder = ElasticsSearchAccess.getInstance().prepareSearch().setQuery(builder);
            sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
            sRequestBuilder.setTypes("translation");
            sRequestBuilder.setSize(1000);

        }
        ListRes = extractTranslationsFromES(sRequestBuilder, false, babelnet);
        
        return ListRes;
    }
    
    
   
    /** 
     * Elasticsearch access for Indirect translation queries
     * 
     */
    @Override
    public List<Translation> searchIndirectTranslations(Language Lang,String label, String langSource, String langTarget, String langPivot, boolean babelnet,double threshold) {
        
    	List<Translation> listResults= new ArrayList<Translation>();
    	
        if (langPivot != null) {
        	listResults=IndirectTranslationAlgorithm.searchIndirectTranslationWithPivotLang(label,langSource,langPivot, langTarget,threshold);

        } else {
        	
        	for(String pivL: Lang.getPivotLangCodes()){
        		listResults.addAll(IndirectTranslationAlgorithm.searchIndirectTranslationWithPivotLang(label,langSource,pivL, langTarget,threshold));
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
    
    
}
