package es.upm.dia.oeg.lld.search.service;

import java.util.List;

import es.upm.dia.oeg.lld.search.model.Translation;

public interface TranslationService {

    public List<String> getLanguages();

    public List<Translation> getAllTranslations(String label,
            String langSpurce, String langTarget);
}
