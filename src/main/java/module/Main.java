package module;


import Utils.ConsoleUser;
import Utils.Utils;

import java.util.Calendar;



import static Utils.ConsoleUser.users;

public class Main {


    public static void main(String[] args) {
        init();
        String nombreUsuario;
        do {
            System.out.println("Bienvenido a La Red Social!");
            nombreUsuario = Utils.string("Ingrese su nombre de usuario (E para salir): ");
            if (nombreUsuario.equalsIgnoreCase("E")) {
                System.out.println("Saliendo de la aplicación...");
                break;
            }
            Usuario usuario = null;
            for (Usuario u : users) {
                if (u.getNombre().equalsIgnoreCase(nombreUsuario)) {
                    usuario = u;
                    break;
                }
            }
            if (usuario == null) {
                System.out.println("Usuario no encontrado. Por favor, intente de nuevo.");
                continue;
            }
            ConsoleUser.setCurrentUser(usuario);
            ConsoleUser.showMenu();
        }while (!nombreUsuario.equalsIgnoreCase("E"));
    }

    public static void init() {
        // Crear usuarios
        Calendar calendar = Calendar.getInstance();


        Usuario ana = new Usuario("Ana");
        Usuario juan = new Usuario("Juan");
        Usuario maria = new Usuario("Maria");
        Usuario pedro = new Usuario("Pedro");

        // Agregar usuarios al conjunto global
        users.add(ana);
        users.add(juan);
        users.add(maria);
        users.add(pedro);

        Post post1 = new TextPost("Hola mundo", "Este es mi primer post!", ana);
        Post post2 = new ImagePost("Vacaciones", "https://img.com/vacaciones.jpg", ana);
        calendar.set(2025, Calendar.MAY, 25);
        Post post3 = new VideoPost("Mi video", "HD", 120, calendar.getTime() , juan);
        calendar.set(2025, Calendar.MAY, 22);
        Post post4 = new TextPost("Reflexión", "Hoy fue un gran día.", calendar.getTime(), maria);
        Post post5 = new ImagePost("Mascota", "https://img.com/perro.jpg", pedro);
        Post post6 = new VideoPost("Chiste", "SD", 30, pedro);
        Post post7 = new TextPost("Hola a todos", "Espero que estén bien.", pedro);

        // Crear posts
        ConsoleUser.setCurrentUser(ana);
        ConsoleUser.addPost(post1);
        ConsoleUser.addPost(post2);
        ConsoleUser.setCurrentUser(juan);
        ConsoleUser.addPost(post3);
        ConsoleUser.setCurrentUser(maria);
        ConsoleUser.addPost(post4);
        ConsoleUser.setCurrentUser(pedro);
        ConsoleUser.addPost(post5);
        ConsoleUser.addPost(post6);
        ConsoleUser.addPost(post7);

        // Agregar comentarios
        ConsoleUser.setCurrentUser(ana);
        calendar.set(2025, Calendar.MAY, 25);
        ConsoleUser.addComment(post1,new Comentario("Me gusta!", calendar.getTime() ,ana));
        ConsoleUser.addComment(post2,new Comentario("Increíble!", ana));
        ConsoleUser.setCurrentUser(juan);
        calendar.set(2025, Calendar.MAY, 20);
        ConsoleUser.addComment(post3,new Comentario("Genial!",calendar.getTime(), juan));
        ConsoleUser.addComment(post4,new Comentario("Interesante!", juan));
        ConsoleUser.setCurrentUser(maria);
        ConsoleUser.addComment(post5,new Comentario("Hermoso!",calendar.getTime(), maria));
        ConsoleUser.addComment(post6,new Comentario("Divertido!", maria));
        ConsoleUser.setCurrentUser(pedro);
        ConsoleUser.addComment(post7,new Comentario("Hola Ana!",calendar.getTime(), pedro));
        calendar.set(2025, Calendar.MAY, 25);
        ConsoleUser.addComment(post7,new Comentario("Hola Juan!",calendar.getTime(), pedro));
        ConsoleUser.addComment(post7,new Comentario("Hola Maria!", pedro));
        // Relaciones de seguidores
        ana.addSeguidor(juan);
        ana.addSeguidor(maria);
        juan.addSeguidor(ana);
        juan.addSeguidor(pedro);
        maria.addSeguidor(ana);
        pedro.addSeguidor(maria);



    }
}
