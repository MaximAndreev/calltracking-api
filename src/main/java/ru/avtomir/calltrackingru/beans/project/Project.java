package ru.avtomir.calltrackingru.beans.project;

import java.util.List;
import java.util.Objects;

public class Project {
    protected int id;
    protected String name;
    protected List<Call> calls;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id &&
                Objects.equals(name, project.name) &&
                Objects.equals(calls, project.calls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calls);
    }

    @Override
    public String toString() {
        return "Id: " + id +
                " Name: " + name;
    }
}
