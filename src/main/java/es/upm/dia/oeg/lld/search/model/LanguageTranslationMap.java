package es.upm.dia.oeg.lld.search.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import es.upm.dia.oeg.lld.search.dao.LanguageTranslationMapDAOImpl;


@Component
public class LanguageTranslationMap {
	
	
	Language [] LanguageArray;
	Map <String, String> LangCodesMap;
	LanguageTranslationMapDAOImpl MapDAOimpl;
	
	public LanguageTranslationMap(){
		
		MapDAOimpl= new LanguageTranslationMapDAOImpl();
		LanguageArray=MapDAOimpl.createLanguageArray();
		LangCodesMap= MapDAOimpl.createLangCodeMap();
		
	}
	
	
	public Language[] getLanguageArray(){
		
		if(LanguageArray==null){
			LanguageArray=MapDAOimpl.createLanguageArray();
		}
		
		return LanguageArray;
	}
	
	
	public String getLangCode(String lang){
			
			if(LangCodesMap==null){
				LangCodesMap=MapDAOimpl.createLangCodeMap();
			}
			
			if (lang == null) {return "All";}
			if (lang.equals("null")) {return "All";}
			
			return LangCodesMap.get(lang);
	}
	
	public Map<String,String> getLangCodesMap(){
		
		if(LangCodesMap==null){
			LangCodesMap=MapDAOimpl.createLangCodeMap();
		}
		return LangCodesMap;
	}


	public Language getSourceLanguage(String langSource) {
					
			for (Language lang: this.LanguageArray){
				if(lang.getLanguageLabel().equals(langSource)){
					return lang;
				}
			}
			// if there is an error (null value lang source), the result is an empty Language
			return new Language();
	}


	public List<String> getAllLanguagesLabels() {
		
		List <String> ListLabels= new ArrayList<String>();
		for (Language Lang: this.LanguageArray){
			ListLabels.add(Lang.getLanguageLabel());
		}
		
		return ListLabels;
	}
	

	

	
	

}
