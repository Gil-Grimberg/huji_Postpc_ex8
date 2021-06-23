package com.example.postpc_ex8;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class RootsFinder implements Serializable {

    UUID id;
    Long number;
    String preffix;
    String suffix;
    Long progress;


    public RootsFinder() {
    }

    public RootsFinder(Long num, String pre, String suff, Long progressState) {
        number = num;
        preffix = pre;
        suffix = suff;
        progress = progressState;
    }

    public RootsFinder(Long num, String pre, String suff, Long progressState, UUID finderId) {
        number = num;
        preffix = pre;
        suffix = suff;
        progress = progressState;
        id = finderId;
    }

    public String getPreffix() {
        return this.preffix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Long getProgress() {
        return this.progress;
    }

    public Long getNumber() {
        return this.number;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID finderId) {
        this.id = finderId;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public void setPreffix(String newPreffix) {
        this.preffix = newPreffix;
    }

    public void setSuffix(String newSuffix) {
        this.suffix = newSuffix;
    }

    public String serializable() {

        return this.id + "#" + this.number + "#" + this.preffix + "#" + this.suffix + "#" + this.progress;
    }

    public static RootsFinder string_to_Item(String rootFinderString) {
        if (rootFinderString == null) {
            return null;
        }
        try {
            String[] split = rootFinderString.split("#");
            UUID id = UUID.fromString(split[0]);
            Long number = Long.valueOf(split[1]);
            String preffix = split[2];
            String suffix = split[3];
            Long progress = Long.valueOf(split[4]);

            return new RootsFinder(number,preffix,suffix,progress,id);
        } catch (Exception e) {
            System.out.println("exception: input " + rootFinderString + "output: " + e.getMessage());
            return null;
        }
    }
}
