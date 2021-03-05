package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DummyModel {
    private String name;
    private String mood;

    public DummyModel(String name, String mood){
        setName(name);
        setMood(mood);
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
