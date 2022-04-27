package connection.entities;

import java.util.Objects;

public class Score {

    private Long id;
    private Statement statement;
    private Discipline discipline;
    private Short mark;

    public Score(Discipline discipline, Statement statement, Short mark) {
        this.statement = statement;
        this.discipline = discipline;
        this.mark = mark;
    }

    public Score() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
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
        Score score = (Score) o;
        return statement.equals(score.statement) && discipline.equals(score.discipline) && mark.equals(score.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statement, discipline, mark);
    }

    @Override
    public String toString() {
        return "Score{" +
                "statement=" + statement +
                ", discipline=" + discipline +
                ", mark=" + mark +
                '}';
    }
}
