package com.maximmold.batch.jobs;

import org.springframework.batch.item.ItemProcessor;

/**
 * Created by maxmoldenhauer on 5/17/17.
 */
public class GeneItemProcessor implements ItemProcessor<Gene, Gene>{

    @Override
    public Gene process(Gene item) throws Exception {
        return new Gene(item.getGeneCode().toUpperCase(), item.getGeneName().toUpperCase());
    }
}
