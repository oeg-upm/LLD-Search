package es.upm.dia.oeg.lld.search.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.builder.ToStringBuilder;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Translation {

    private URI trans;
    private String langSource;
    private String langTarget;
    private URI lexiconSource;
    private URI lexiconTarget;
    private URI senseSource;
    private URI senseTarget;
    private String writtenRepSource;
    private String writtenRepTarget;
    private URI partOfSpeech;

    private String babelnetSynset;

    // For indirect translations, direct=false and pivotLanguage and score have
    // values
    // private boolean direct;
    // private String pivotLanguage;
    // private float score;

    public Translation() {
    }

    // public Translation(String id, Map<String, Object> source) {
    // this.trans = id;
    // this.lexiconSource = source.get("lexicon_source").toString();
    // this.lexiconTarget = source.get("lexicon_target").toString();
    // this.senseSource = source.get("sense_source").toString();
    // this.senseTarget = source.get("sense_target").toString();
    // this.writtenRepSource = source.get("written_rep_source").toString();
    // this.writtenRepTarget = source.get("written_rep_target").toString();
    // this.partOfSpeech = source.get("POS").toString();
    // }

    // /**
    // * @return the direct
    // */
    // public final boolean isDirect() {
    // return direct;
    // }
    //
    // /**
    // * @param direct
    // * the direct to set
    // */
    // public final void setDirect(boolean direct) {
    // this.direct = direct;
    // }
    //
    // /**
    // * @return the pivotLanguage
    // */
    // public final String getPivotLanguage() {
    // return pivotLanguage;
    // }
    //
    // /**
    // * @param pivotLanguage
    // * the pivotLanguage to set
    // */
    // public final void setPivotLanguage(String pivotLanguage) {
    // this.pivotLanguage = pivotLanguage;
    // }
    //
    // /**
    // * @return the score
    // */
    // public final float getScore() {
    // return score;
    // }
    //
    // /**
    // * @param score
    // * the score to set
    // */
    // public final void setScore(float score) {
    // this.score = score;
    // }

    /**
     * @return the trans
     */
    public final URI getTrans() {
        return this.trans;
    }

    /**
     * @param trans
     *            the trans to set
     * @throws URISyntaxException
     */
    public final void setTrans(String trans) throws URISyntaxException {
        this.trans = new URI(trans);
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
     * @return the senseSource
     */
    public final URI getSenseSource() {
        return this.senseSource;
    }

    /**
     * @param senseSource
     *            the senseSource to set
     * @throws URISyntaxException
     */
    public final void setSenseSource(String senseSource)
            throws URISyntaxException {
        this.senseSource = new URI(senseSource);
    }

    /**
     * @return the senseTarget
     */
    public final URI getSenseTarget() {
        return this.senseTarget;
    }

    /**
     * @param senseTarget
     *            the senseTarget to set
     * @throws URISyntaxException
     */
    public final void setSenseTarget(String senseTarget)
            throws URISyntaxException {
        this.senseTarget = new URI(senseTarget);
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
    public final URI getPartOfSpeech() {
        return this.partOfSpeech;
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

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
