package es.upm.dia.oeg.lld.search.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;

import es.upm.dia.oeg.lld.search.model.Dictionary;
import es.upm.dia.oeg.lld.search.model.Language;

public class LanguageTranslationMap {
	
	
	Map<String,List <String>> translationMap;
	
	public LanguageTranslationMap(){
		
		
	}
	

	
	public List<String> get(String s){
		System.out.println("this is>"+s);
		List <String> list= translationMap.get(s);
		System.out.println("this is>"+list.size());
		return list;
	}
	
	public String[] get2(String s){
		System.out.println("this is >"+s);
		List <String> list= translationMap.get(s);
		System.out.println("this is>"+list.size());
		
		return list.toArray(new String[list.size()]);
	}
	
	public Language[] getLangArray(){
		
		
		List <String> listaTraduciones= getLanguages();
		
		
		System.out.println("this is the access >");
		
		List <Language> listaLang= new ArrayList<Language>();
		
		for (String lan: listaTraduciones){
			listaLang.add(new Language(lan,getDirectTranslationLang(lan),getIndirectTranslationLang(lan)));
			
		}
		
		
		return listaLang.toArray(new Language[listaLang.size()]);
	}
	
	
	///// this should not be here
	
	public  List<String> getLanguages(){
        //Get document
		
		Client client= ElasticsSearchAccess.getInstance();//.startClient();
    	
	   	 String guery="{" +
	   		        "  \"query\": { \"match_all\": {} }" +
	   		        "}";
	   		        
	   	 SearchRequestBuilder sRequestBuilder = client.prepareSearch(guery);//
	   	 sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
	   	 sRequestBuilder.setTypes("language");
	   	 sRequestBuilder.setSize(500);
	
	   	SearchResponse response = sRequestBuilder.execute().actionGet();
	
	   	final List<String> langsList = new ArrayList<String>();
	   		        
	   	for (SearchHit se : response.getHits().getHits()){
	   		 
	   		 langsList.add(se.getSource().get("langExtended").toString());
	   		           
	   		            
	   	}
   	
	   	return langsList;

        

            
    }
	
	public  String[] getDirectTranslationLang(String s){
        //Get document
		
		Client client= ElasticsSearchAccess.getInstance();//.startClient();
    	
		System.out.println("word "+s);
		TermQueryBuilder builder = QueryBuilders.termQuery("langExtended", s.toLowerCase());
        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
        sRequestBuilder.setTypes("languageMap");
        sRequestBuilder.setSize(1000);
        

        SearchResponse response = sRequestBuilder.execute().actionGet();
       String direct= "";

        for (SearchHit se : response.getHits().getHits()){
           
            //System.out.print(se.getSource().get("directTranslationLanguagesExtended").toString());
            direct=se.getSource().get("directTranslationLanguagesExtended").toString();
            break;
        }
        
        
        System.out.println("Direct >> "+direct);
	   	
   	
	   	return direct.split(",");

        

            
    }
	
	public  String[] getIndirectTranslationLang(String LangExtended){
        //Get document
		
		Client client= ElasticsSearchAccess.getInstance();//.startClient();
    	
		System.out.println("word "+LangExtended);
		TermQueryBuilder builder = QueryBuilders.termQuery("langExtended", LangExtended.toLowerCase());
        SearchRequestBuilder sRequestBuilder = client.prepareSearch().setQuery(builder);//
        sRequestBuilder.setIndices(ElasticsSearchAccess.Index);
        sRequestBuilder.setTypes("languageMap");
        sRequestBuilder.setSize(1000);
        

        SearchResponse response = sRequestBuilder.execute().actionGet();
       String direct= "";

        for (SearchHit se : response.getHits().getHits()){
           
            //System.out.print(se.getSource().get("directTranslationLanguagesExtended").toString());
            direct=se.getSource().get("indirectTranslationLanguagesExtended").toString();
            break;
        }
        
        
        System.out.println(" Indirect >>  "+direct);
	   	
   	
	   	return direct.split(",");

        

            
    }

}
