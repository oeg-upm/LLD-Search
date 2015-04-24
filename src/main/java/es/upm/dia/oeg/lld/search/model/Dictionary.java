package es.upm.dia.oeg.lld.search.model;

public class Dictionary {

    private String langSource;
    private String langTarget;

    public Dictionary() {
    }

    public Dictionary(String source, String target) {
        this.langSource = source;
        this.langTarget = target;
    }

    /**
     * @return the langSource
     */
    public final String getLangSource() {
        return this.langSource;
    }

    /**
     * @param langSource
     *            the langSource to set
     */
    public final void setLangSource(String langSource) {
        this.langSource = langSource;
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
    public final void setLangTarget(String langTarget) {
        this.langTarget = langTarget;
    }
}
