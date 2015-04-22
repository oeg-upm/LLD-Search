package es.upm.dia.oeg.lld.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.dia.oeg.lld.search.dao.TranslationDAO;
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
    public List<Translation> getAllTranslations(String label,
            String langSource, String langTarget) {
        final List<Translation> translations = this.translationDAO.searchAllTranslations(
                label, langSource, langTarget);

        return translations;
    }

}
