package es.upm.dia.oeg.lld.search.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.upm.dia.oeg.lld.search.dao.TranslationDAO;
import es.upm.dia.oeg.lld.search.model.Language;
import es.upm.dia.oeg.lld.search.model.SearchQuery;
import es.upm.dia.oeg.lld.search.model.Translation;

@Service
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    TranslationDAO translationDAO;

    
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
