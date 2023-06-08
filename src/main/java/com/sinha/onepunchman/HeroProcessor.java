package com.sinha.onepunchman;

import org.springframework.batch.item.ItemProcessor;

public class HeroProcessor implements ItemProcessor<Hero, Hero> {

    @Override
    public Hero process(Hero item) throws Exception {

        item.setClassName(item.getClassName().split("-")[0]);

        item.setPowers(item.getPowers()!=null && item.getPowers().length() > 0 ? item.getPowers() : "NA");

        return item;
    }
}
