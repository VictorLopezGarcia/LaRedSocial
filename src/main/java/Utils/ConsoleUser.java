package Utils;

import module.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ConsoleUser {

    private static final int MAX_POSTS = 10;
    private static Usuario currentUser;
    public static final TreeSet<Usuario> users = new TreeSet<>();

    public static void setCurrentUser(Usuario currentUser) {
        ConsoleUser.currentUser = currentUser;
    }

    public static void showMenu() {
        if (currentUser == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }
        System.out.println("----------------------");
        System.out.println("    LA RED SOCIAL");
        System.out.println("----------------------");
        System.out.println("Bienvenido " + currentUser.getNombre() + "!");
        int option;
        do {
            showOptions();
            option = Utils.integer();
            select(option);
        } while (option != 10);
    }

    private static void showOptions() {
        System.out.println("----------------------");
        System.out.println("    MENU DE USUARIO");
        System.out.println("1. Mostrar mis posts");
        System.out.println("2. Mostrar mis comentarios");
        System.out.println("3. Mostrar muro");
        System.out.println("4. Agregar post");
        System.out.println("5. Eliminar post");
        System.out.println("6. Mostrar seguidores");
        System.out.println("7. Agregar seguidor");
        System.out.println("8. Eliminar seguidor");
        System.out.println("9. Sugerir amigos por amigos comunes");
        System.out.println("10. Salir");
        System.out.println("Seleccione una opción: ");
        System.out.println("----------------------");
    }

    private static void select(int option) {
        switch (option) {
            case 1:
                showPosts(currentUser.getPosts(), true);
                break;
            case 2:
                showComments();
                break;
            case 3:
                showWall();
                break;
            case 4:
                showAddPost();
                break;
            case 5:
                showDelete();
                break;
            case 6:
                showUsers(currentUser.getSeguidores(), true);
                break;
            case 7:
                showAndAddFollower();
                break;
            case 8:
                showUsers(currentUser.getSeguidores(), false);
                System.out.println("----------------------");
                removeFollower();
                break;
            case 9:
                sugerirAmigosPorAmigosComunes();
                break;
            case 10:
                System.out.println("Hasta luego " + currentUser.getNombre() + "!");
                setCurrentUser(null);
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    private static void showAndAddFollower() {
        if (users.isEmpty() || currentUser.getSeguidores().size() >= users.size() - 1) {
            System.out.println("No hay usuarios disponibles para seguir.");
            return;
        }
        System.out.println("Usuarios disponibles para seguir:");
        TreeSet<Usuario> users = new TreeSet<>(ConsoleUser.users);
        users.remove(currentUser);
        users.removeAll(currentUser.getSeguidores());
        showUsers(users, false);
        System.out.println("----------------------");
        String entrada = Utils.string("Ingrese el nombre de usuario a agregar como seguidor: ");
        addFollower(entrada);
        System.out.println("----------------------");
    }

    private static void showUsers(TreeSet<Usuario> users, boolean addComment) {
        int index = 1;
        for (Usuario u : users) {
            System.out.println(index + ". " + u.getNombre());
            index++;
        }
        System.out.println("-----------------------");
        System.out.println("Quieres ver el perfil de alguno de tus seguidores? (S/N)");
        String respuesta = Utils.string().toUpperCase();
        if (respuesta.equals("S")) {
            showUserProfile(users, addComment);
        } else {
            System.out.println("No se mostrará ningún perfil.");
        }
    }

    private static void removeFollower() {
        String seguidor = Utils.string("Ingrese el nombre del seguidor a eliminar: ");
        if (currentUser.removeSeguidor(seguidor)) System.out.println("Seguidor eliminado: " + seguidor);
        else System.out.println("No se ha podido eliminar el seguidor.");
    }

    private static void showPosts(TreeSet<Post> posts, boolean addComment) {
        int index = 1;
        if (posts.isEmpty()) {
            System.out.println("No hay posts disponibles.");
            return;
        }
        System.out.println("---------------------");
        for (Post post : posts) {
            System.out.println("----------" + index + "----------");
            System.out.println(post);
            index++;
        }
        System.out.println("Total de posts: " + (index - 1));
        if (addComment) {
            System.out.println("¿Desea agregar un comentario a alguno de sus posts? (S/N)");
            String respuesta = Utils.string().toUpperCase();
            if (respuesta.equals("S")) {
                showAddComment(posts);
            } else {
                System.out.println("No se agregará ningún comentario.");
            }
        }
        System.out.println("---------------------");
    }

    private static void showComments() {
        System.out.println("---------------------");
        System.out.println("Comentarios de " + currentUser.getNombre() + ":");
        List<Comentario> comentariosUsuario = new ArrayList<>();
        List<Post> postsDeComentario = new ArrayList<>();
        int index = 1;
        for (Post post : getAllPosts()) {
            for (Comentario comentario : post.getComments()) {
                if (comentario.getOwner().equals(currentUser)) {
                    System.out.println("----------" + post.getTitle() + "----------");
                    System.out.println(index + ". " + comentario);
                    comentariosUsuario.add(comentario);
                    postsDeComentario.add(post);
                    index++;
                }
            }
        }
        System.out.println("---------------------");
        if (comentariosUsuario.isEmpty()) {
            System.out.println("No tienes comentarios.");
        } else {
            String respuesta = Utils.string("¿Deseas borrar algún comentario? (S/N): ").toUpperCase();
            if (respuesta.equals("S")) {
                deleteComment(comentariosUsuario, postsDeComentario);
            } else {
                System.out.println("No se borrará ningún comentario.");
            }
        }
    }

    private static void deleteComment(List<Comentario> comentariosUsuario, List<Post> postsDeComentario) {
        int seleccion = Utils.integer("Introduce el número del comentario a borrar (0 para cancelar): ");
        if (seleccion > 0 && seleccion <= comentariosUsuario.size()) {
            Comentario comentarioABorrar = comentariosUsuario.get(seleccion - 1);
            Post postAsociado = postsDeComentario.get(seleccion - 1);
            postAsociado.removeComentario(comentarioABorrar);
            System.out.println("Comentario borrado correctamente.");
        } else if (seleccion == -1) {
            System.out.println("Operación cancelada.");
        } else {
            System.out.println("Selección inválida.");
        }
    }

    private static void showWall() {
        int count = 1;
        System.out.println("Muro de " + currentUser.getNombre() + ":");
        for (Post post : getFollowersPosts()) {
            if (count <= MAX_POSTS) {
                System.out.println("----------" + count + "----------");
                System.out.println(post);
                count++;
                System.out.println("----------------------");
            } else {
                break;
            }
        }
        if (count == 1) {
            System.out.println("No hay posts en el muro.");
        } else {
            System.out.println("Quieres agregar un comentario a alguno de los posts? (S/N)");
            String respuesta = Utils.string().toUpperCase();
            if (respuesta.equals("S"))
                showAddComment(getFollowersPosts());
            else
                System.out.println("No se agregará ningún comentario.");
        }
    }

    private static void showAddComment(TreeSet<Post> posts) {
        int index = Utils.integer("Ingrese el número del post al que desea comentar: "),
                cont = 1;
        for (Post post : posts) {
            if (index == cont) {
                String commentText = Utils.string("Ingrese el texto del comentario: ");
                Comentario comentario = new Comentario(commentText, currentUser);
                post.addComentario(comentario);
                System.out.println("Comentario agregado al post: " + post.getTitle());
                return;
            }
            cont++;
        }
    }

    private static void showDelete() {
        showPosts(currentUser.getPosts(), false);
        int index = Utils.integer("Ingrese el número del post a eliminar: "),
                cont = 1;
        for (Post post : currentUser.getPosts()) {
            if (index == cont) {
                System.out.println("Post seleccionado para eliminar:");
                System.out.println(cont + ". " + post.getTitle() + " (" + post.getNumComentarios() + " comentarios)");
                removePost(post);
                System.out.println("----------------------");
                return;
            }
            cont++;
        }
        System.out.println("Ingresa un número válido.");
    }

    private static void showAddPost() {
        System.out.println("Agregar post:");
        System.out.println("1. Texto");
        System.out.println("2. Video");
        System.out.println("3. Imagen");
        int option = Utils.integer("Ingrese una opción (1-3): ");
        addPost(option);
    }

    private static void showUserProfile(TreeSet<Usuario> users, boolean addComment) {
        int index = Utils.integer("Ingrese el número del seguidor para ver su perfil: (0 para cancelar) ");
        if (index == 0) return;
        int cont = 1;
        for (Usuario seguidor : users) {
            if (index == cont) {
                System.out.println("Perfil de " + seguidor.getNombre() + ":");
                showPosts(seguidor.getPosts(), addComment);
                return;
            }
            cont++;
        }
        System.out.println("Índice fuera de rango. Intente nuevamente.");
        showUserProfile(users, addComment);
    }

    private static void addPost(int postType) {
        switch (postType) {
            case 1:
                addTextPost();
                break;
            case 2:
                addVideoPost();
                break;
            case 3:
                addImagePost();
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    private static void addImagePost() {
        String title = Utils.string("Ingrese el título del post: ");
        String imageUrl = Utils.string("Ingrese la URL de la imagen: ");
        ImagePost post = new ImagePost(title, imageUrl, currentUser);
        currentUser.addPost(post);
    }

    private static void addVideoPost() {
        String title = Utils.string("Ingrese el título del post: ");
        String quality = Utils.string("Ingrese la calidad del video: ");
        int duration = Utils.integer("Ingrese la duración del video (en segundos): ");
        VideoPost post = new VideoPost(title, quality, duration, currentUser);
        currentUser.addPost(post);
    }

    private static void addTextPost() {
        String title = Utils.string("Ingrese el título del post: ");
        String text = Utils.string("Ingrese el texto del post: ");
        TextPost post = new TextPost(title, text, currentUser);
        currentUser.addPost(post);
    }

    private static void addFollower(String entrada) {
        for (Usuario u : users) {
            if (u.getNombre().equalsIgnoreCase(entrada)) {
                currentUser.addSeguidor(u);
                System.out.println("Seguidor agregado: " + u.getNombre());
                return;
            }
        }
        System.out.println("Usuario no encontrado.");
    }

    private static void removePost(Post post) {
        if (currentUser.removePost(post)) System.out.println("Post eliminado: " + post.getTitle());
        else System.out.println("No se ha podido eliminar el post.");
    }

    public static void addPost(Post post) {
        currentUser.addPost(post);
    }

    public static void addComment(Post post, Comentario comentario) {
        post.addComentario(comentario);
    }

    private static TreeSet<Post> getAllPosts() {
        return users.stream().flatMap(u -> u.getPosts().stream())
                .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    }

    private static TreeSet<Post> getFollowersPosts() {
        TreeSet<Post> posts = new TreeSet<>();
        for (Usuario seguidor : currentUser.getSeguidores()) {
            posts.addAll(seguidor.getPosts());
        }
        return posts;
    }

    public static void sugerirAmigosPorAmigosComunes() {
        if (currentUser == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }
        System.out.println("Sugerencias de amistad para " + currentUser.getNombre() + ":");
        List<Usuario> sugerencias = new ArrayList<>();
        List<Integer> comunes = new ArrayList<>();
        for (Usuario posible : users) {
            if (posible.equals(currentUser)) continue;
            if (currentUser.getSeguidores().contains(posible)) continue;
            int count = 0;
            for (Usuario seguidor : currentUser.getSeguidores()) {
                if (posible.getSeguidores().contains(seguidor)) {
                    count++;
                }
            }
            if (count > 0) {
                sugerencias.add(posible);
                comunes.add(count);
            }
        }
        if (sugerencias.isEmpty()) {
            System.out.println("No hay sugerencias de amistad por amigos en común.");
        } else {
            for (int i = 0; i < sugerencias.size(); i++) {
                System.out.println((i + 1) + ". " + sugerencias.get(i).getNombre() + " (" + comunes.get(i) + " amigos en común)");
            }
            String respuesta = Utils.string("¿Desea ver el perfil de alguno de estos usuarios? (S/N)").toUpperCase();
            if (respuesta.equals("S")) {
                showUserProfile(new TreeSet<>(sugerencias), false);
            }
            respuesta = Utils.string("¿Desea agregar alguno de estos usuarios como seguidor? (S/N)").toUpperCase();
            if (respuesta.equals("S")) {
                String entrada = Utils.string("Ingrese el nombre de usuario a agregar: ");
                addFollower(entrada);
            }
        }
    }
}
