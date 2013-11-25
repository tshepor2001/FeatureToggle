package com.test.feature.toggle;

public class Feature {

    public String id;
    private String name;
    private boolean active;

    public Feature(String id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Feature{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }
}
