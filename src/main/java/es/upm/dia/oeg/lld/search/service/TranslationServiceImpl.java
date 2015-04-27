package es.upm.dia.oeg.lld.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.dia.oeg.lld.search.dao.DictionaryDAO;
import es.upm.dia.oeg.lld.search.dao.TranslationDAO;
import es.upm.dia.oeg.lld.search.model.Dictionary;
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
        // languages.add("All");

        languages.addAll(this.translationDAO.getLanguages());

        return languages;
    }

    private List<Dictionary> getDictionariesHardcoded() {
        final List<Dictionary> dictionaries = new ArrayList<Dictionary>();
        dictionaries.add(new Dictionary("ca", "it"));
        dictionaries.add(new Dictionary("en", "es"));
        dictionaries.add(new Dictionary("en", "ca"));
        dictionaries.add(new Dictionary("en", "gl"));
        dictionaries.add(new Dictionary("eo", "en"));
        dictionaries.add(new Dictionary("eo", "es"));
        dictionaries.add(new Dictionary("eo", "ca"));
        dictionaries.add(new Dictionary("eo", "fr"));
        dictionaries.add(new Dictionary("es", "ca"));
        dictionaries.add(new Dictionary("es", "gl"));
        dictionaries.add(new Dictionary("es", "pt"));
        dictionaries.add(new Dictionary("es", "ro"));
        dictionaries.add(new Dictionary("es", "ast"));
        dictionaries.add(new Dictionary("es", "an"));
        dictionaries.add(new Dictionary("eu", "en"));
        dictionaries.add(new Dictionary("eu", "es"));
        dictionaries.add(new Dictionary("fr", "es"));
        dictionaries.add(new Dictionary("fr", "ca"));
        dictionaries.add(new Dictionary("oc", "es"));
        dictionaries.add(new Dictionary("oc", "ca"));
        dictionaries.add(new Dictionary("pt", "ca"));
        dictionaries.add(new Dictionary("pt", "gl"));

        return dictionaries;
    }

    @Override
    public List<Dictionary> getDictionaries() {
        // return this.dictionaryDAO.getDictionaries();
        return getDictionariesHardcoded();
    }

    @Override
    public List<Translation> getTranslations(SearchQuery searchQuery) {

        String langTarget = searchQuery.getLangTarget();
        String langPivot = searchQuery.getPivotLanguage();

        if (langTarget.equalsIgnoreCase("All")) {
            langTarget = null;
        }

        if (langPivot.equalsIgnoreCase("All")) {
            langPivot = null;
        }

        if (searchQuery.isIndirect()) {
            return this.translationDAO.searchIndirectTranslations(
                    searchQuery.getTerm(), searchQuery.getLangSource(),
                    langTarget, langPivot, searchQuery.getBabelnet());
        }

        return this.translationDAO.searchDirectTranslations(
                searchQuery.getTerm(), searchQuery.getLangSource(), langTarget,
                searchQuery.getBabelnet());
    }

}
