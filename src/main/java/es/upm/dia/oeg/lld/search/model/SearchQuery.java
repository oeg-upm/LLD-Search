package es.upm.dia.oeg.lld.search.model;

import javax.validation.constraints.NotNull;

public class SearchQuery {

    @NotNull
    private String term;

    @NotNull
    private String langSource;

    private String langTarget;

    private boolean babelnet;

    private boolean indirect;

    /**
     * @return the query
     */
    public final String getTerm() {
        return this.term;
    }

    /**
     * @param query
     *            the query to set
     */
    public final void setTerm(String query) {
        this.term = query;
    }

    /**
     * @return the lang
     */
    public final String getLangSource() {
        return this.langSource;
    }

    /**
     * @param lang
     *            the lang to set
     */
    public final void setLangSource(String lang) {
        this.langSource = lang;
    }

    /**
     * @return the langTarget
     */
    public final String getLangTarget() {
        return this.langTarget;
    }

    /**
     * @param langTarget
     *            the langTarget to set
     */
    public final void setLangTarget(String lang) {
        this.langTarget = lang;
    }

    /**
     * @return the babelnet
     */
    public final boolean getBabelnet() {
        return this.babelnet;
    }

    /**
     * @param babelnet
     *            the babelnet to set
     */
    public final void setBabelnet(boolean babelnet) {
        this.babelnet = babelnet;
    }

    /**
     * @return the indirect
     */
    public final boolean isIndirect() {
        return this.indirect;
    }

    /**
     * @param indirect
     *            the indirect to set
     */
    public final void setIndirect(boolean indirect) {
        this.indirect = indirect;
    }
}
