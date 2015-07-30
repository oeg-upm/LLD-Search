package es.upm.dia.oeg.lld.search.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Translation implements Comparable<Translation> {

    private String langSource;
    private String langTarget;
    private URI lexiconSource;
    private URI lexiconTarget;
    private String writtenRepSource;
    private String writtenRepTarget;
    private URI partOfSpeech;

    private String babelnetSynset;

    // For indirect translations, direct=false and pivotLanguage and score have
    // values
    private boolean indirect;
    private String pivotLanguage;

    private float score;

    public Translation() {
    }

    /**
     * @return the lexiconSource
     */
    public final URI getLexiconSource() {
        return this.lexiconSource;
    }

    /**
     * @param lexiconSource
     *            the lexiconSource to set
     * @throws URISyntaxException
     */
    public final void setLexiconSource(String lexiconSource)
            throws URISyntaxException {
        this.lexiconSource = new URI(lexiconSource);
    }

    /**
     * @return the lexiconTarget
     */
    public final URI getLexiconTarget() {
        return this.lexiconTarget;
    }

    /**
     * @param lexiconTarget
     *            the lexiconTarget to set
     * @throws URISyntaxException
     */
    public final void setLexiconTarget(String lexiconTarget)
            throws URISyntaxException {
        this.lexiconTarget = new URI(lexiconTarget);
    }

    /**
     * @return the writtenRepSource
     */
    public final String getWrittenRepSource() {
        return this.writtenRepSource;
    }

    /**
     * @param writtenRepSource
     *            the writtenRepSource to set
     */
    public final void setWrittenRepSource(String writtenRepSource) {
        this.writtenRepSource = writtenRepSource;
    }

    /**
     * @return the writtenRepTarget
     */
    public final String getWrittenRepTarget() {
        return this.writtenRepTarget;
    }

    /**
     * @param writtenRepTarget
     *            the writtenRepTarget to set
     */
    public final void setWrittenRepTarget(String writtenRepTarget) {
        this.writtenRepTarget = writtenRepTarget;
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

    /**
     * @return the partOfSpeech
     */
    public final String getPartOfSpeech() {
        return this.partOfSpeech.getFragment();
    }

    /**
     * @param partOfSpeech
     *            the partOfSpeech to set
     * @throws URISyntaxException
     */
    public final void setPartOfSpeech(String partOfSpeech)
            throws URISyntaxException {
        this.partOfSpeech = new URI(partOfSpeech);
    }

    /**
     * @return the babelnetSynset
     */
    public final String getBabelnetSynset() {
        return this.babelnetSynset;
    }

    /**
     * @param babelnetSynset
     *            the babelnetSynset to set
     */
    public final void setBabelnetSynset(String babelnetSynset) {
        this.babelnetSynset = babelnetSynset;
    }

    /**
     * @return the direct
     */
    public final boolean isIndirect() {
        return this.indirect;
    }

    /**
     * @param direct
     *            the direct to set
     */
    public final void setIndirect(boolean indirect) {
        this.indirect = indirect;
    }

    /**
     * @return the pivotLanguage
     */
    public final String getPivotLanguage() {
        return this.pivotLanguage;
    }

    /**
     * @param pivotLanguage
     *            the pivotLanguage to set
     */
    public final void setPivotLanguage(String pivotLanguage) {
        this.pivotLanguage = pivotLanguage;
    }

    /**
     * @return the score
     */
    public final float getScore() {
        return this.score;
    }

    /**
     * @param score
     *            the score to set
     */
    public final void setScore(float score) {
        this.score = score;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	@Override
	public int compareTo(Translation o) {

            if (score < o.score) {
                return 1;
            }
            if (score > o.score) {
                return -1;
            }
            return 0;
       
	}
}
