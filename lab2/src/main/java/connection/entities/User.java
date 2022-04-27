package connection.entities;

import java.util.Objects;

public class User {

    private Long id;
    private String name;
    private String surname;
    private String patronimic;
    private String mail;
    private String region;
    private String city;
    private String education;
    private String password;

    public User(Long id, String name, String surname, String patronimic, String password, String mail, String region, String city, String education) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronimic = patronimic;
        this.password = password;
        this.mail = mail;
        this.region = region;
        this.city = city;
        this.education = education;
    }

    public User(String name, String surname, String patronimic, String mail, String region, String city, String education, String password) {
        this.name = name;
        this.surname = surname;
        this.patronimic = patronimic;
        this.mail = mail;
        this.region = region;
        this.city = city;
        this.education = education;
        this.password = password;
    }

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && name.equals(user.name) && surname.equals(user.surname) && patronimic.equals(user.patronimic) && mail.equals(user.mail) && region.equals(user.region) && city.equals(user.city) && education.equals(user.education);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronimic, mail, region, city, education);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronimic='" + patronimic + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronimic() {
        return patronimic;
    }

    public void setPatronimic(String patronimic) {
        this.patronimic = patronimic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}
