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
            nombreUsuario = Utils.string("Ingrese su nombre de usuario (E para salir): ");
            if (nombreUsuario.equalsIgnoreCase("E")) {
                System.out.println("Saliendo de la aplicación...");
                break;
            }
            Usuario usuario = DataController.getInstance().getUserByName(nombreUsuario);
            if (usuario == null) {
                System.out.println("Usuario no encontrado. Por favor, intente de nuevo.");
                continue;
            }
            DataController.getInstance().setCurrentUser(usuario);
            ConsoleIn.showMenu();
        }while (!nombreUsuario.equalsIgnoreCase("E"));
    }

}
