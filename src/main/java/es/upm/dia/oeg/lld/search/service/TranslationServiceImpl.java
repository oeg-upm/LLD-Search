package es.upm.dia.oeg.lld.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.dia.oeg.lld.search.dao.DictionaryDAO;
import es.upm.dia.oeg.lld.search.dao.TranslationDAO;
import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;

@Service
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    TranslationDAO translationDAO;

    @Autowired
    DictionaryDAO dictionaryDAO;

    @Override
    public List<String> getLanguages() {
        final List<String> languages = new ArrayList<String>();
        // // First option is all languages (no restriction)
        languages.addAll(this.translationDAO.getLanguages());
        return languages;
    }

    @Override
    public String getLanguageCode(String language) {
        final String languageCode = this.translationDAO.getLanguageCode(language);
        return languageCode;
    }

    /*
    @Override
    public List<Dictionary> getDictionaries() {
        return this.dictionaryDAO.getDictionaries();
        //return getDictionariesHardcoded();
    }
     */
    @Override
    public List<Translation> getTranslations(SearchQuery searchQuery, Language Lang) {

        String langTarget = searchQuery.getLangTargetCode();
        String langPivot = searchQuery.getPivotLanguageCode();

        if (langTarget.equalsIgnoreCase("All")) {
            langTarget = null;
        }

        if (langPivot.equalsIgnoreCase("All")) {
            langPivot = null;
        }

        if (searchQuery.isIndirect()) {
            return this.translationDAO.searchIndirectTranslations(Lang,
                    searchQuery.getTerm(), searchQuery.getLangSourceCode(),
                    langTarget, langPivot, searchQuery.getBabelnet(),searchQuery.getThreshold());
        }

        return this.translationDAO.searchDirectTranslations( Lang,
                searchQuery.getTerm(), searchQuery.getLangSourceCode(), langTarget,
                searchQuery.getBabelnet());
    }

}
