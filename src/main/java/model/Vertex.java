package model;

public class Vertex {
    // and the model used in actual computing is Vertex and Graph
    private int id;
    private int priority;
    private int owner;
    private String label;
    private Integer region = null;

    // this is necessary
    public Vertex(){

    }

    public Vertex(int id, int priority, int owner) {
        this.id = id;
        this.priority = priority;
        this.owner = owner;
    }

    public Vertex(int id, int priority, int owner, String label) {
        this(id, priority, owner);
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public String toString(){
        return "" + this.id;
    }
}
