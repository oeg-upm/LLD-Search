package es.upm.dia.oeg.lld.search.model;

import javax.validation.constraints.NotNull;

public class SearchElement {

    @NotNull
    private String query;

    /**
     * @return the query
     */
    public final String getQuery() {
        return query;
    }

    /**
     * @param query
     *            the query to set
     */
    public final void setQuery(String query) {
        this.query = query;
    }
}
