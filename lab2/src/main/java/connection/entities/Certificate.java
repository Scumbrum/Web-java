package connection.entities;

import java.util.Objects;

public class Certificate {

    private Long id;
    private User user;
    private Discipline discipline;
    private Short mark;

    public Certificate(User user, Discipline discipline, Short mark) {
        this.user = user;
        this.discipline = discipline;
        this.mark = mark;
    }

    public Certificate() {

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

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Short getMark() {
        return mark;
    }

    public void setMark(Short mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return id.equals(that.id) && user.equals(that.user) && discipline.equals(that.discipline) && mark.equals(that.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, discipline, mark);
    }

    @Override
    public String toString() {
        return "Ceertificate{" +
                "user=" + user +
                ", discipline=" + discipline +
                ", mark=" + mark +
                '}';
    }
}
