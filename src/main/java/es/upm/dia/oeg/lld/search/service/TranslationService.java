package es.upm.dia.oeg.lld.search.service;

import java.util.List;

import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;

public interface TranslationService {

    public List<Translation> getTranslations(SearchQuery searchQuery, Language lang);
    
}
