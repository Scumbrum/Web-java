package connection.entities;

import java.util.Objects;

public class Statement {

    private Long id;
    private User user;
    private Faculty faculty;
    private Short average;
    private Short status = 1;

    public Statement(User user, Faculty faculty) {
        this.user = user;
        this.faculty = faculty;
    }

    public Statement(Long id, User user, Faculty faculty) {
        this.id = id;
        this.user = user;
        this.faculty = faculty;
    }

    public Statement() {

    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getAverage() {
        return average;
    }

    public void setAverage(Short average) {
        this.average = average;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statement statement = (Statement) o;
        return id.equals(statement.id) && user.equals(statement.user) && faculty.equals(statement.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, faculty);
    }

    @Override
    public String toString() {
        return "Statement{" +
                "user=" + user +
                ", faculty=" + faculty +
                '}';
    }
}
