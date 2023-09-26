package com.wynsumart.wynsum.models;

public class MeditationTargetContainer {
    public String name;
    public String icon;
    public String description;
    public String short_description;
    public String guide;
    public Integer id;

    public MeditationTargetContainer(int id, String name, String icon, String description,
                                     String short_description, String guide) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.short_description = short_description;
        this.guide = guide;
    }
}
