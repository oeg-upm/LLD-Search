package es.upm.dia.oeg.lld.search.dao;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.elasticsearch.client.Client;

import es.upm.dia.oeg.lld.search.model.Translation;

public class IndirectTranslation {
	
	
	public static List<Translation> searchIndirectTranslationWithPivotLang(String word,String SourceLang, String PivotLang,String TargetLang,double threshold) {

        Client client = ElasticsSearchAccess.getInstance();//.startClient();

        ArrayList<TranslatablePair> translatablePairs = obtainTranslationScoresFromLemma(client, ElasticsSearchAccess.Index, word, SourceLang, PivotLang, TargetLang);
        //ElasticsSearchAccess.closeClient();
        
        List<Translation> listResults= new ArrayList<Translation>();
        for (TranslatablePair trans: translatablePairs){
        	
        	if(trans.score>threshold){
        	listResults.add(trans.toTranslation());
        	}
        }
        return listResults;
        
        

    }
	
	

    /**
     * Given a lemma in a source language, it returns their possible
     * translations into a target language through a given pivot language, along
     * with their score, in an array of TranslatablePair objects. The scores are
     * obtained based on the "One Time Single Consultation" algorithm
     *
     * @param sourceLemma
     * @param sourceLanguage
     * @param pivotLanguage
     * @param targetLanguage
     * @return translatable pairs
     */
    public static ArrayList<TranslatablePair> obtainTranslationScoresFromLemma(Client client, String indexname, String sourceLemma, String sourceLanguage, String pivotLanguage, String targetLanguage) {

        ArrayList<TranslatablePair> translatablePairs = new ArrayList<>();

        // estraigo del diccionario el transet
        String translationSetURI1 = ElasticSearchQueries.getTranslationSetFromDictionary(client, indexname, sourceLanguage, pivotLanguage);
        String translationSetURI2 = ElasticSearchQueries.getTranslationSetFromDictionary(client, indexname, pivotLanguage, targetLanguage);
        if(translationSetURI1.equals("")){
            translationSetURI1 = ElasticSearchQueries.getTranslationSetFromDictionary(client, indexname, pivotLanguage,sourceLanguage);
        }
        if(translationSetURI2.equals("")){
            translationSetURI2 = ElasticSearchQueries.getTranslationSetFromDictionary(client, indexname, targetLanguage,pivotLanguage);
        }
		

        
        String sourceLexicon = ElasticSearchQueries.getLexiconfromLanguage(client, indexname, sourceLanguage);

        // Get Lexical entries associated to source label
        List<String> sourceLexicalEntries = ElasticSearchQueries.getLexicalEntriesFromLemma(client, indexname, sourceLexicon, sourceLemma, sourceLanguage);

        for (String sourceLexicalEntry : sourceLexicalEntries) {

           
            translatablePairs.addAll(obtainTranslationScoresFromLexicalEntry(client, indexname, sourceLemma, sourceLexicalEntry, pivotLanguage, translationSetURI1, translationSetURI2));

        }

        return translatablePairs;
    }

    
	///////////////////////////////////////////////////////////////////////////////

    public static ArrayList<TranslatablePair> obtainTranslationScoresFromLexicalEntry(Client client, String indexName, String sourceLemma, String sourceLexicalEntry,String pivotLang, String translationSetURI1, String translationSetURI2) {

        ArrayList<TranslatablePair> translatablePairs = new ArrayList<>(); // to collect the output
        Set<String> P1 = new HashSet<>();  // to store translations in the pivot language (P1)
        Set<String> T = new HashSet<>();  //  to store translations in the target language (T)

		// 1. For the source lexical entry, look up all translations in the pivot language (P1).
        //get translations for the source lexical entry
        List<String> pivotTranslations = ElasticSearchQueries.obtainDirectTranslationsFromLexicalEntry(client, indexName, translationSetURI1, sourceLexicalEntry);
        

        //populate set of pivot translations (P1)
        P1.addAll(pivotTranslations);

        // 2. For every pivot translation of every source lexical entry, look up its target translations (T).
        for (String pivotTranslation : pivotTranslations) {

            List<String> newTargetTranslations = ElasticSearchQueries.obtainDirectTranslationsFromLexicalEntry(client, indexName, translationSetURI2, pivotTranslation);
            
            newTargetTranslations.removeAll(T); //retains only those translations that are really new ones
            
            //Create the array of translatable pairs leaving the scores empty for the moment
            for (String targetTranslation : newTargetTranslations) {

                String[] Target = ElasticSearchQueries.getLemaAndPosofTargetTranslation(client, indexName, targetTranslation);
                translatablePairs.add(new TranslatablePair(sourceLemma, pivotLang,sourceLexicalEntry, targetTranslation, Target[0], Target[1],Target[2],Target[3], -1.0));
                T.addAll(newTargetTranslations);
             
            }
        }

        // 3. For every target translation, look up its pivot translations (P2)
        for (TranslatablePair ts : translatablePairs) {

            Set<String> P2 = new HashSet<>();

            P2.addAll(ElasticSearchQueries.obtainDirectTranslationsFromLexicalEntry(client, indexName, translationSetURI2, ts.getTargetLexicalEntry()));

			// 4. Measure how translations in P2 match those in P1. For each t in T, the more matches 
            // between P1 and P2, the better t is as a candidate translation of the original
            // Formula: 	score(t) = 2 ×(P1 ∩ P2)/P1+P2      
            Set<String> intersection = new HashSet<>();
            intersection.addAll(P1);
            intersection.retainAll(P2);
            double score = 2.0 * intersection.size() / (P1.size() + P2.size());

            // add all the information in the translation score object				
            ts.setScore(score);
            P2.clear();
        }

        return translatablePairs;
    }
    
    
    /* Just an function that must be in an xml or something. 
     * */
    public static String [] getPivotLang2( String SourceLang){
    	
    	if(SourceLang.equals("ro")){
    		return "es".split(",");
    		
    	}
    	if(SourceLang.equals("ast")){
    		return "es".split(",");	
    	}
    	if(SourceLang.equals("an")){
    		return "es".split(",");	
    	}
    	if(SourceLang.equals("gl")){
    		return "pt,es,en".split(",");	
    	}
    	if(SourceLang.equals("es")){
    		return "gl,pt,eu,en,ca,eo,oc".split(",");	
    	}
    	if(SourceLang.equals("eu")){
    		return "es,en".split(",");	
    	}
    	if(SourceLang.equals("pt")){
    		return "es,ca,gl".split(",");	
    	}
    	if(SourceLang.equals("en")){
    		return "ca,es,eu,eo,gl".split(",");	
    	}
    	if(SourceLang.equals("oc")){
    		return "es,ca".split(",");	
    	}
    	if(SourceLang.equals("ca")){
    		return "es,fr,eo,en".split(",");	
    	}
    	if(SourceLang.equals("eo")){
    		return "es,ca,en".split(",");	
    	}
    	if(SourceLang.equals("it")){
    		return "ca".split(",");	
    	}
    	if(SourceLang.equals("fr")){
    		return "ca,es".split(",");	
    	}
    	else{
    		return "es,en".split(",");
    		
    	}
    	
    }

}
