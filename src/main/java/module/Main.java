package module;


import Utils.ConsoleUser;

import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    public static final TreeSet<Usuario> users = new TreeSet<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        init();
        System.out.println("Bienvenido a la aplicación de gestión de posts");
        System.out.println("Ingrese su nombre de usuario:");
        String nombreUsuario = sc.nextLine();
        for (Usuario u : users) {
            if (u.getNombre().equalsIgnoreCase(nombreUsuario)) {
                ConsoleUser.setCurrentUser(u);
                ConsoleUser.showMenu();
                break;
            }
        }
    }


    // src/main/java/module/Main.java

    public static void init() {
        System.out.println("Inicializando la aplicación...");

        // Crear usuarios
        Usuario ana = new Usuario("Ana");
        Usuario juan = new Usuario("Juan");
        Usuario maria = new Usuario("Maria");
        Usuario pedro = new Usuario("Pedro");

        Post post1 = new TextPost("Hola mundo", "Este es mi primer post!", ana);
        Post post2 = new ImagePost("Vacaciones", "https://img.com/vacaciones.jpg", ana);
        Post post3 = new VideoPost("Mi video", "HD", 120, juan);
        Post post4 = new TextPost("Reflexión", "Hoy fue un gran día.", maria);
        Post post5 = new ImagePost("Mascota", "https://img.com/perro.jpg", pedro);
        Post post6 = new VideoPost("Chiste", "SD", 30, pedro);
        Post post7 = new TextPost("Hola a todos", "Espero que estén bien.", pedro);

        // Crear posts
        ConsoleUser.addPost(post1);
        ConsoleUser.addPost(post2);
        ConsoleUser.addPost(post3);
        ConsoleUser.addPost(post4);
        ConsoleUser.addPost(post5);
        ConsoleUser.addPost(post6);
        ConsoleUser.addPost(post7);

        // Agregar comentarios
        ConsoleUser.setCurrentUser(ana);
        ConsoleUser.addComment(post1,new Comentario("Me gusta!", ana));
        ConsoleUser.addComment(post2,new Comentario("Increíble!", ana));
        ConsoleUser.setCurrentUser(juan);
        ConsoleUser.addComment(post3,new Comentario("Genial!", juan));
        ConsoleUser.addComment(post4,new Comentario("Interesante!", juan));
        ConsoleUser.setCurrentUser(maria);
        ConsoleUser.addComment(post5,new Comentario("Hermoso!", maria));
        ConsoleUser.addComment(post6,new Comentario("Divertido!", maria));
        ConsoleUser.setCurrentUser(pedro);
        ConsoleUser.addComment(post7,new Comentario("Hola Ana!", pedro));
        ConsoleUser.addComment(post7,new Comentario("Hola Juan!", pedro));
        ConsoleUser.addComment(post7,new Comentario("Hola Maria!", pedro));
        // Relaciones de seguidores
        ana.addSeguidor(juan);
        ana.addSeguidor(maria);
        juan.addSeguidor(ana);
        juan.addSeguidor(pedro);
        maria.addSeguidor(ana);
        pedro.addSeguidor(maria);

        // Agregar usuarios al conjunto global
        users.add(ana);
        users.add(juan);
        users.add(maria);
        users.add(pedro);

        System.out.println("Usuarios y relaciones creados aleatoriamente.");
    }
}
