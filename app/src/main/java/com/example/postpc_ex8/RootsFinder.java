package com.example.postpc_ex8;

import java.util.UUID;

public class RootsFinder {

    UUID id;
    Long number;
    String preffix;
    String suffix;
    Long progress; // todo: it needs to hold the iterNumber/NumberToCalculate as the progress of the worker??
    // todo: hold a worker too??

    public RootsFinder()
    {
        // todo: is it necessary? because of serelaizblity???
    }

    public RootsFinder(Long num,String pre, String suff, Long progressState)
    {
        number = num;
        preffix = pre;
        suffix = suff;
        progress = progressState; //todo: maybe update this from worker?
    }
    public RootsFinder(Long num,String pre, String suff, Long progressState,UUID finderId)
    {
        number = num;
        preffix = pre;
        suffix = suff;
        progress = progressState; //todo: maybe update this from worker?
        id = finderId;
    }

    public String getPreffix() {return this.preffix;}
    public String getSuffix() {return this.suffix;}
    public Long getProgress() {return this.progress;}
    public Long getNumber() {return this.number;}
    public UUID getId(){return this.id;}
    public void setId(UUID finderId) {this.id = finderId;}
    public void setProgress(Long progress){this.progress = progress;}
    public void setPreffix(String newPreffix){this.preffix = newPreffix;}
    public void setSuffix(String newSuffix){this.suffix = newSuffix;}

    // todo: enable sereliabluty using parsers (if necessary)

}
