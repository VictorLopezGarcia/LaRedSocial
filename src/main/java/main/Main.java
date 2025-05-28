package main;


import controller.ConsoleIn;
import Utils.Utils;
import controller.DataController;
import model.*;


public class Main {


    public static void main(String[] args) {
        DataController.getInstance().fetchata();
        String nombreUsuario;
        do {
            System.out.println("Bienvenido a La Red Social!");
            nombreUsuario = Utils.string("Ingrese su nombre de usuario (R -> Registrarse | S -> salir): ");
            if (nombreUsuario.equalsIgnoreCase("S")) {
                System.out.println("Saliendo de la aplicación...");
                break;
            }else if (nombreUsuario.equalsIgnoreCase("R")) {
                System.out.println("Registrando nuevo usuario...");
                String nombre = Utils.string("Ingrese su nombre: ");
                Usuario nuevoUsuario = new Usuario(DataController.getInstance().getNextUserId(), nombre);
                DataController.getInstance().addUser(nuevoUsuario);
                DataController.getInstance().setCurrentUser(nuevoUsuario);
                System.out.println("Usuario registrado exitosamente!");
            }else{
                Usuario usuario = DataController.getInstance().getUserByName(nombreUsuario);
                if (usuario == null) {
                    System.out.println("Usuario no encontrado. Por favor, intente de nuevo.");
                    continue;
                }
                DataController.getInstance().setCurrentUser(usuario);
            }

            ConsoleIn.showMenu();
        }while (!nombreUsuario.equalsIgnoreCase("E"));
    }

}
