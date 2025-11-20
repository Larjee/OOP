package ru.nsu.munkuev;

public class Vertex {
    private String label;
    private int id;

    public Vertex(String label, int id) {
        this.label = label;
        this.id = id;
    }

    public Vertex(String label) {
        this.label = label;
    }

    public Vertex(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
