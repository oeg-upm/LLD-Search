package es.upm.dia.oeg.lld.search.model;

import java.net.URISyntaxException;

public class TranslatablePair {
	
	String sourceLabel;
	String sourceLexicalEntryURI;
	String targetLexicalEntryURI;
	String targetLabel;
	String posURI;
	double score;
        
        String TargetLang;
        String Babelnet;
        String SourceLang;
	
	public TranslatablePair(String sourceLabel, String sourceLang,String sourceLexicalEntry2,  String targetTranslation, String targetLabel, String pos,
                String target_lang, String babelnet,
                double score){
		
                this.TargetLang = target_lang;
                this.Babelnet = babelnet;
		this.sourceLabel = sourceLabel;
		this.sourceLexicalEntryURI = sourceLexicalEntry2;
		this.targetLexicalEntryURI = targetTranslation;
		this.targetLabel = targetLabel;
		this.posURI = pos;
		this.score = score;	
		this.SourceLang = sourceLang;
	}
	
	public String getSourceLabel(){ return this.sourceLabel;}
	public String getTargetLabel(){ return this.targetLabel;}
	public String getSourceLexicalEntry(){ return this.sourceLexicalEntryURI;}
	public String getTargetLexicalEntry(){ return this.targetLexicalEntryURI;}
	public String getPos(){ return this.posURI;};
	public void setScore(Double score){ this.score = score;}
	public double getScore(){ return this.score;}
	
	
	
	public Translation toTranslation(){
		
		Translation t= new Translation();
		t.setBabelnetSynset(this.Babelnet);
		t.setIndirect(true);
		try {
			t.setPartOfSpeech(this.posURI);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t.setLangTarget(this.TargetLang);
		t.setPivotLanguage(this.SourceLang);
		t.setScore((float)this.score);
		t.setWrittenRepTarget(this.targetLabel);
		
		return t;
		
		
	}
	

}
