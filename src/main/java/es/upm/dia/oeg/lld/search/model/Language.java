package es.upm.dia.oeg.lld.search.model;

import org.apache.commons.lang3.ArrayUtils;

public class Language {
	
	
	
	public String languageLabel;
	// view
	public String [] translationLang;
	public String [] indirectLang;
	public String [] pivotLang;
	
	// controller
	public String [] translationLangCodes;
	public String [] indirectLangCodes;
	public String [] pivotLangCodes;
	
	public Language(String LangLabel, String trans, String transCodes, String indirecttrans, String indirecttransCodes,String pivtrans, String pivtransCodes){
		languageLabel= LangLabel;
		translationLang= ArrayUtils.addAll(new String[]{"All"}, trans.split(","));
		translationLangCodes=  transCodes.split(",");
		
		indirectLang=indirecttrans.split(",");
		indirectLangCodes= indirecttransCodes.split(",");
		
		pivotLang= ArrayUtils.addAll(new String[]{"All"}, pivtrans.split(","));
		pivotLangCodes=pivtransCodes.split(",");
		
	}
	
	public Language(){
		languageLabel= new String();
		translationLang= new  String[0];
		indirectLang=new  String[0];
		pivotLang= new  String[0];
	}
	
	
	public String getLanguageLabel(){
		return languageLabel;
		
	}
	
	public String [] getTranslationLang(){
		return translationLang;
		
	}

	public String[] getIndirectLang() {
		return indirectLang;
	}
	
	public String[] getPivotLang() {
		return pivotLang;
	}
	

	public String[] getTranslationLangCodes() {
		return translationLangCodes;
	}

	public String[] getIndirectLangCodes() {
		return indirectLangCodes;
	}

	public String[] getPivotLangCodes() {
		return pivotLangCodes;
	}

	public boolean checkIsIndirect(String langTarget) {
			
		if(langTarget.equals("All")) {return false;}
			
			for(String Indirect: this.indirectLang){
				if (langTarget.equals(Indirect)){
					return true;
				}
			}
					
		return false;
	}
	
	
	

}
