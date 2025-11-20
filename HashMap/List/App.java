import javax.swing.JOptionPane;

public class App {

    // Comprobar si el usuario ya existe en la lista
    // Basado en email, que es el identificador correcto
    private static boolean existUser(String[] dataUser, ListLinked<User> list) {

        String name = dataUser[0];
        String surname = dataUser[1];
        String date = dataUser[2];
        String email = dataUser[3];
        String password = dataUser[4];

        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);

            // Comprobamos si coinciden todos los datos o al menos el email
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // Registrar usuario
    public static void RegisterUser(ListLinked<User> list) {

        String name = JOptionPane.showInputDialog("Ingrese su Nombre ha registrar: ");
        String surname = JOptionPane.showInputDialog("Ingrese su Apellido ha registrar:");
        String date = JOptionPane.showInputDialog("Ingrese su fecha de nacimiento ha registrar (DD/MM/AAAA):");
        String email = JOptionPane.showInputDialog("Ingrese su email ha registrar: ");
        String password = JOptionPane.showInputDialog("INgrese su contraseña ha registrar: ");

        int sexOption = JOptionPane.showConfirmDialog(
                null,
                "Eliga su sexo hombre o mujer (Sí=Hombre / No=Mujer)",
                "Confirmación de Sexo",
                JOptionPane.YES_NO_OPTION);

        String sex = (sexOption == JOptionPane.YES_OPTION) ? "Hombre" : "Mujer";

        if (!name.isEmpty() && !surname.isEmpty() && !date.isEmpty()
                && !email.isEmpty() && !password.isEmpty()) {

            String[] data = {name, surname, date, email, password};

            // CORREGIDO: faltaba el paréntesis final
            if (!existUser(data, list)) {

                User newUser = new User(name, surname, date, email, password, sex);

                list.add(newUser);

                JOptionPane.showMessageDialog(null,
                        "Usuario registrado Correctamente: " + newUser.getName());

            } else {
                JOptionPane.showMessageDialog(null,
                        "El usuario ya existe. Use otro EMAIL.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos...");
        }
    }

    // Eliminar usuario
    public void removeUser(ListLinked<User> list) {

        String nameToRemove = JOptionPane.showInputDialog("Ingrese el Nombre de usuario ha eliminar: ");

        if (nameToRemove == null || nameToRemove.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre.");
            return;
        }

        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (user.getName().equalsIgnoreCase(nameToRemove)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            list.remove(index);
            JOptionPane.showMessageDialog(null,
                    "Usuario '" + nameToRemove + "' eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Usuario no encontrado, pruebe con otro...");
        }
    }

    // Obtener datos de usuario
    public void getUserData(ListLinked<User> list) {

        String nameToSearch = JOptionPane.showInputDialog(
                "Ingrese el Nombre de usuario Para búscar sus datos: ");

        if (nameToSearch == null || nameToSearch.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe de rellenar el campo...");
            return;
        }

        int index = -1;
        User foundUser = null;

        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            if (user.getName().equalsIgnoreCase(nameToSearch)) {
                index = i;
                foundUser = user;
                break;
            }
        }

        if (index != -1) {

            String dataMessage =
                    "-----------------------\n" +
                            "DATOS DEL USUARIO\n" +
                            "-----------------------\n" +
                            "Nombre: " + foundUser.getName() + "\n" +
                            "Apellido: " + foundUser.getSurname() + "\n" +
                            "Email: " + foundUser.getEmail() + "\n" +
                            "Fecha de Nacimiento: " + foundUser.getDate() + "\n" +
                            "Sexo: " + foundUser.getSex() + "\n" +
                            "-----------------------\n";

            JOptionPane.showMessageDialog(null, dataMessage);

        } else {
            JOptionPane.showMessageDialog(null,
                    "Usuario no registrado, debe registrarlo para visualizar sus datos...");
        }
    }

    // MAIN
    public static void main(String[] args) {

        ListLinked<User> list = new ListLinked<>();
        App appInstance = new App();

        int option = 0;

        while (option != 4) {
            try {
                String input = JOptionPane.showInputDialog(
                        "Seleccione una Opción del menú\n" +
                                "1) Registrar Usuario\n" +
                                "2) Eliminar Usuario\n" +
                                "3) Obtener datos de Usuario\n" +
                                "4) Salir\n" +
                                "Seleccione alguna Opción para continuar:"
                );

                if (input == null) break;

                option = Integer.parseInt(input);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Por favor, ingrese un número válido del menú.");
                continue;
            }

            switch (option) {
                case 1:
                    RegisterUser(list);
                    break;
                case 2:
                    appInstance.removeUser(list);
                    break;
                case 3:
                    appInstance.getUserData(list);
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null,
                            "¡¡¡Gracias Por preferirnos, lo esperamos de vuelta !!!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null,
                            "La opción ingresada no es válida.");
            }
        }
    }
}

