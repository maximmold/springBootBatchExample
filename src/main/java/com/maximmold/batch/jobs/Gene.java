package com.maximmold.batch.jobs;

/**
 * Created by maxmoldenhauer on 5/17/17.
 */
public class Gene {
    private String geneCode;
    private String geneName;

    public Gene() {
    }

    public Gene(String geneCode, String geneName) {
        this.geneCode = geneCode;
        this.geneName = geneName;
    }

    public String getGeneCode() {
        return geneCode;
    }

    public void setGeneCode(String geneCode) {
        this.geneCode = geneCode;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }
}
