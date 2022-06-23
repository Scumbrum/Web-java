package connection.entities;

import java.util.Objects;

public class Discipline {
    private Long id;
    private String name;

    public Discipline(String name) {
        this.name = name;
    }

    public Discipline(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Discipline() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Discipline{" +
                "name='" + name + '\'' +
                '}';
    }
}
