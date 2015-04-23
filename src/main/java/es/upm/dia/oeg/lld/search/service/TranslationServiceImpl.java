package es.upm.dia.oeg.lld.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.dia.oeg.lld.search.dao.TranslationDAO;
import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;

@Service
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    TranslationDAO translationDAO;

    @Override
    public List<String> getLanguages() {
        final List<String> languages = new ArrayList<String>();
        // First option is all languages (no restriction)
        languages.add("All");

        languages.addAll(this.translationDAO.getLanguages());

        return languages;
    }

    @Override
    public List<Translation> getTranslations(SearchQuery searchQuery) {

        String langTarget = searchQuery.getLangTarget();

        if (langTarget.equalsIgnoreCase("All")) {
            langTarget = null;
        }

        if (searchQuery.isIndirect()) {
            return this.translationDAO.searchIndirectTranslations(
                    searchQuery.getTerm(), searchQuery.getLangSource(),
                    langTarget, searchQuery.getBabelnet());
        }

        return this.translationDAO.searchDirectTranslations(
                searchQuery.getTerm(), searchQuery.getLangSource(), langTarget,
                searchQuery.getBabelnet());
    }

}
