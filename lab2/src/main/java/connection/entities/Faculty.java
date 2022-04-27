package connection.entities;

import java.util.Objects;

public class Faculty {

    private Long id;
    private String name;
    private Integer budgetPlace;
    private Integer allPlace;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Faculty(String name, Integer budgetPlace, Integer allPlace, String description) {
        this.allPlace = allPlace;
        this.budgetPlace = budgetPlace;
        this.name = name;
        this.description = description;
    }

    public Faculty() {

    }

    public Faculty(Long id, String name, Integer budgetPlace, Integer allPlace, String description) {
        this.id = id;
        this.name = name;
        this.budgetPlace = budgetPlace;
        this.allPlace = allPlace;
        this.description = description;
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

    public Integer getBudgetPlace() {
        return budgetPlace;
    }

    public void setBudgetPlace(Integer budgetPlace) {
        this.budgetPlace = budgetPlace;
    }

    public Integer getAllPlace() {
        return allPlace;
    }

    public void setAllPlace(Integer allPlace) {
        this.allPlace = allPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return id.equals(faculty.id) && name.equals(faculty.name) && budgetPlace.equals(faculty.budgetPlace) && allPlace.equals(faculty.allPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, budgetPlace, allPlace);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                '}';
    }
}
