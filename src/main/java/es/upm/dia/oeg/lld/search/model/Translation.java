package es.upm.dia.oeg.lld.search.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Translation {

    private String trans;
    private String lexiconSource;
    private String lexiconTarget;
    private String senseSource;
    private String senseTarget;
    private String writtenRepSource;
    private String writtenRepTarget;
    private String partOfSpeech;

    // private String babelnetSynset;

    // For indirect translations, direct=false and pivotLanguage and score have
    // values
    // private boolean direct;
    // private String pivotLanguage;
    // private float score;

    public Translation() {
    }

    public Translation(String id, Map<String, Object> source) {
        this.trans = id;
        this.lexiconSource = source.get("lexicon_source").toString();
        this.lexiconTarget = source.get("lexicon_target").toString();
        this.senseSource = source.get("sense_source").toString();
        this.senseTarget = source.get("sense_target").toString();
        this.writtenRepSource = source.get("written_rep_source").toString();
        this.writtenRepTarget = source.get("written_rep_target").toString();
        this.partOfSpeech = source.get("POS").toString();
    }

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
    public final String getTrans() {
        return this.trans;
    }

    /**
     * @param trans
     *            the trans to set
     */
    public final void setTrans(String trans) {
        this.trans = trans;
    }

    /**
     * @return the lexiconSource
     */
    public final String getLexiconSource() {
        return this.lexiconSource;
    }

    /**
     * @param lexiconSource
     *            the lexiconSource to set
     */
    public final void setLexiconSource(String lexiconSource) {
        this.lexiconSource = lexiconSource;
    }

    /**
     * @return the lexiconTarget
     */
    public final String getLexiconTarget() {
        return this.lexiconTarget;
    }

    /**
     * @param lexiconTarget
     *            the lexiconTarget to set
     */
    public final void setLexiconTarget(String lexiconTarget) {
        this.lexiconTarget = lexiconTarget;
    }

    /**
     * @return the senseSource
     */
    public final String getSenseSource() {
        return this.senseSource;
    }

    /**
     * @param senseSource
     *            the senseSource to set
     */
    public final void setSenseSource(String senseSource) {
        this.senseSource = senseSource;
    }

    /**
     * @return the senseTarget
     */
    public final String getSenseTarget() {
        return this.senseTarget;
    }

    /**
     * @param senseTarget
     *            the senseTarget to set
     */
    public final void setSenseTarget(String senseTarget) {
        this.senseTarget = senseTarget;
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
     * @return the partOfSpeech
     */
    public final String getPartOfSpeech() {
        return this.partOfSpeech;
    }

    /**
     * @param partOfSpeech
     *            the partOfSpeech to set
     */
    public final void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    // /**
    // * @return the babelnetSynset
    // */
    // public final String getBabelnetSynset() {
    // return babelnetSynset;
    // }
    //
    // /**
    // * @param babelnetSynset
    // * the babelnetSynset to set
    // */
    // public final void setBabelnetSynset(String babelnetSynset) {
    // this.babelnetSynset = babelnetSynset;
    // }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
