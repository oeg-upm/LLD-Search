package es.upm.dia.oeg.lld.search.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import es.upm.dia.oeg.lld.search.model.Language;

@Component
public class LanguageTranslationMap {
	
	
	Language [] LanguageArray;
	Map <String, String> LangCodesMap;
	
	public LanguageTranslationMap(){
		
		LanguageArray=createLanguageArray();
		LangCodesMap= createLangCodeMap();
	}
	
	
	public Language[] getLanguageArray(){
		
		if(LanguageArray==null){
			LanguageArray=this.createLanguageArray();
		}
		
		return LanguageArray;
		

	}
	
	
	public String getLangCode(String lang){
			
			if(LangCodesMap==null){
				LangCodesMap=this.createLangCodeMap();
			}
			
			if (lang == null) {return "All";}
			if (lang.equals("null")) {return "All";}
			
			return LangCodesMap.get(lang);
			
	
	}
	
	public Map<String,String> getLangCodesMap(){
		
		if(LangCodesMap==null){
			LangCodesMap=this.createLangCodeMap();
		}
		
		return LangCodesMap;
	}

	
	///// this should not be here
	
	public Map<String,String> createLangCodeMap(){
        //Get document
		
		
		
		Client client= ElasticsSearchAccess.getInstance();
    	
	   	String guery="{" +
	   		"  \"query\": { \"match_all\": {} }" +
	   		"}";
	   		        
	   	 SearchRequestBuilder sRequestBuilder = client.prepareSearch(guery);//
	   	 sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
	   	 sRequestBuilder.setTypes("language");
	   	 sRequestBuilder.setSize(500);
	
	   	SearchResponse response = sRequestBuilder.execute().actionGet();
	
	   	Map <String,String> langCodeMap = new HashMap<String,String>();
	   	langCodeMap.put("All", "All");
	   		        
	   	for (SearchHit se : response.getHits().getHits()){
	   		 
	   		 String langExtended= se.getSource().get("langExtended").toString();
	   		 String lang= se.getSource().get("lang").toString();
	   		 langCodeMap.put(langExtended, lang);
	   	}
   	
	   	return langCodeMap;

        

            
    }
	
	
	public  Language[] createLanguageArray(){
        //Get document
		
		
		
		Client client= ElasticsSearchAccess.getInstance();
    	
	   	 String guery="{" +
	   		        "  \"query\": { \"match_all\": {} }" +
	   		        "}";
	   		        
	   	 SearchRequestBuilder sRequestBuilder = client.prepareSearch(guery);//
	   	 sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
	   	 sRequestBuilder.setTypes("languageMap");
	   	 sRequestBuilder.setSize(500);
	
	   	SearchResponse response = sRequestBuilder.execute().actionGet();
	
	   	final List<Language> langsList = new ArrayList<Language>();
	   		        
	   	for (SearchHit se : response.getHits().getHits()){
	   		 
	   		 String langExtended= se.getSource().get("langExtended").toString();
	   		 String directExtended= se.getSource().get("directTranslationLanguagesExtended").toString();         
	   		 String indirectExtended= se.getSource().get("indirectTranslationLanguagesExtended").toString();
	   		 String pivotExtended= se.getSource().get("pivotLanguagesExtended").toString();  
	   		 langsList.add(new Language(langExtended,directExtended.split(","),indirectExtended.split(","),pivotExtended.split(",")));
	   	}
   	
	   	return langsList.toArray(new Language[ langsList.size()] );

        

            
    }


	/*
	public boolean checkIsIndirect(String langSource, String langTarget) {
		
		if(langTarget.equals("All")) {return false;}
		
		for (Language lang: this.LanguageArray){
			if(lang.label.equals(langSource)){
				for(String Indirect: lang.indirectLang){
					if (langTarget.equals(Indirect)){
						return true;
					}
				}
				return false;
			}
			
		}
		return false;
		
		
	}
	*/
	
	public Language getSourceLanguage(String langSource) {
					
			for (Language lang: this.LanguageArray){
				if(lang.label.equals(langSource)){
					return lang;
				}
			}
			return new Language();
		}
	
	

}
