package es.upm.dia.oeg.lld.search.model;

import org.apache.commons.lang3.ArrayUtils;

public class Language {
	
	
	
	public String label;
	//public String labelExtended;
	public String [] translationLang;
	public String [] indirectLang;
	
	public Language(String Label, String [] trans, String [] indtrans){
		label= Label;
		translationLang= ArrayUtils.addAll(new String[]{"All"}, trans);
		indirectLang=indtrans;
	}
	
	public String getLabel(){
		return label;
		
	}
	
	public String [] getTranslationLang(){
		return translationLang;
		
	}

	public String[] getIndirectLang() {
		return indirectLang;
	}
	
	
	
	

}
