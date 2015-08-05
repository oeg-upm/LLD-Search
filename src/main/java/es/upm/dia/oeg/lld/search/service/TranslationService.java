package es.upm.dia.oeg.lld.search.service;

import java.util.List;

import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;

public interface TranslationService {

    public List<String> getLanguages();
    
    public String getLanguageCode(String language);

    //public List<Dictionary> getDictionaries();

    public List<Translation> getTranslations(SearchQuery searchQuery, Language lang);
}
