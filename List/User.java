/**
 * Clase User: Agrupa los datos del usuario en un solo objeto
 * y permite la comparación (necesaria para ListLinked<T extends Comparable<T>>).
 */
class User implements Comparable<User> {
    private String name;
    private String surname;
    private String date;
    private String email;
    private String password;
    private String sex;

    public User(String name, String surname, String date, String email, String password, String sex) {
        this.name = name;
        this.surname = surname;
        this.date = date;
        this.email = email;
        this.password = password;
        this.sex = sex;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getDate() { return date; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getSex() { return sex; }

    @Override
    public int compareTo(User other) {
        // Criterio de ordenamiento: Por Nombre, luego por Apellido, luego por Email.
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) return nameComparison;
        
        int surnameComparison = this.surname.compareTo(other.surname);
        if (surnameComparison != 0) return surnameComparison;
        
        return this.email.compareTo(other.email);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Surname: " + surname + ", Email: " + email;
    }
}
