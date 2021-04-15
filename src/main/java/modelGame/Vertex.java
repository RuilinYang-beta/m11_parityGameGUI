package modelGame;

public class Vertex {
    private int id;
    private int priority;
    private int owner;
    private String label;  // sometimes in the .pg file there's init label for a node

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

    public String toString(){
        return "" + this.id;
    }
}
