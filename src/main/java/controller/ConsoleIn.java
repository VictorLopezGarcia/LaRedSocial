package controller;

import Utils.Utils;
import model.*;

import java.util.List;


public class ConsoleIn {

    private static final DataController dataController = DataController.getInstance();
    private static final Usuario currentUser = dataController.getCurrentUser();

    public static void showMenu() {
        Usuario currentUser = dataController.getCurrentUser();
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
            ConsoleOut.showOptions();
            option = Utils.integer();
            ConsoleOut.select(option);
        } while (option != 10);
    }

    public static void deleteComment(List<Comentario> comentariosUsuario) {
        System.out.println("Quiere borrar un comentario?");
        String respuesta = Utils.string("S/N: ").toUpperCase();
        if (respuesta.equals("N")) {
            System.out.println("Operación cancelada.");
            return;
        } else if (!respuesta.equals("S")) {
            System.out.println("Respuesta inválida.");
            deleteComment(comentariosUsuario);
            return;
        }
        int seleccion = Utils.integer("Introduce el número del comentario a borrar (0 para cancelar): ");
        if (seleccion > 0 && seleccion <= comentariosUsuario.size()) {
            dataController.deleteComment(comentariosUsuario.get(seleccion - 1));
            System.out.println("Comentario borrado correctamente.");
        } else if (seleccion == 0) {
            System.out.println("Operación cancelada.");
        } else {
            System.out.println("Selección inválida.");
        }
    }

    private static void addPost(int postType) {
        PostType type = PostType.fromInt(postType);
        String message = PostType.getTypeMessage(type);
        String title = Utils.string("Ingrese el título del post: ");
        String text = Utils.string(message), text2, contenido = text;
        if (type == PostType.VIDEO) {
            text2 = Utils.string("Ingrese la calidad del video: ");
            contenido = text + "\n" + text2;
        }
        Post post = new Post(currentUser.getId(), dataController.getNextPostId() ,title, type, contenido);
        dataController.addPost(post);
    }


    public static void addFollower() {
        if (currentUser == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }
        System.out.println("Quieres agregar un seguidor?");
        String respuesta = Utils.string("S/N: ").toUpperCase();
        if (respuesta.equals("N")) {
            System.out.println("Operación cancelada.");
            return;
        } else if (!respuesta.equals("S")) {
            System.out.println("Respuesta inválida.");
            addFollower();
            return;
        }
        String entrada = Utils.string("Ingrese el nombre del usuario a agregar como seguidor: ");
        if (dataController.addFollower(entrada)) {
            System.out.println("Seguidor agregado: " + entrada.toUpperCase());
        }
        else {
            System.out.println("No se pudo agregar el seguidor. Verifique que el usuario exista y no lo siga ya.");
        }
    }


    public static void addComment(List<Post> posts) {
        System.out.println("Quieres comentar un post?");
        String respuesta = Utils.string("S/N: ").toUpperCase();
        if (respuesta.equals("N")) {
            System.out.println("Operación cancelada.");
            return;
        } else if (!respuesta.equalsIgnoreCase("S")) {
            System.out.println("Respuesta inválida.");
            addComment(posts);
        }
        int seleccion = Utils.integer("Seleccione el número del post al que desea comentar (0 para cancelar): ");
        if (seleccion <= 0 || seleccion > posts.size()) {
            if (seleccion == 0) {
                System.out.println("Operación cancelada.");
                return;
            }
            System.out.println("Selección inválida. Por favor, intente de nuevo.");
            addComment(posts);
        }
        Post postSeleccionado = posts.get(seleccion - 1);
        String comentarioTexto = Utils.string("Ingrese su comentario: ");
        Comentario comentario = new Comentario(currentUser.getId(), postSeleccionado.getId(), comentarioTexto);
        dataController.addComment(comentario);
        System.out.println("Comentario agregado correctamente.");
    }

    public static void askShowUserProfile(List<Usuario> users, boolean addComment) {
        System.out.println("¿Quieres ver el perfil de alguno? (S/N)");
        String respuesta = Utils.string().toUpperCase();
        if (respuesta.equals("S")) {
            int seleccion = Utils.integer("Seleccione el número del usuario a ver:");
            if (seleccion > 0 && seleccion <= users.size()) {
                Usuario usuarioSeleccionado = users.get(seleccion - 1);
                ConsoleOut.showUserProfile(usuarioSeleccionado, addComment);
            } else {
                System.out.println("Selección inválida.");
            }
        } else {
            System.out.println("No se mostrará ningún perfil.");
        }
    }

    public static void askAddPost() {
        int postType = Utils.integer("Seleccione el tipo de post:\n1. Texto\n2. Video\n3. Imagen\n0. Cancelar\n");
        if (postType == 0) {
            System.out.println("Operación cancelada.");
            return;
        } else if (postType < 1 || postType > 3) {
            System.out.println("Selección inválida. Por favor, intente de nuevo.");
            askAddPost();
        }
        addPost(postType);
    }

    public static void deletePost() {
        List<Post> posts = dataController.getPostsByUserId(currentUser.getId());
        if (posts.isEmpty()) {
            System.out.println("No tienes posts para eliminar.");
            return;
        }
        System.out.println("Tus posts:");
        ConsoleOut.showPosts(posts, false);
        int seleccion = Utils.integer("Seleccione el número del post a eliminar (0 para cancelar): ");
        if (seleccion == 0) {
            System.out.println("Operación cancelada.");
        } else if (seleccion < 1 || seleccion > posts.size()) {
            System.out.println("Selección inválida. Por favor, intente de nuevo.");
            deletePost();
        } else {
            Post postSeleccionado = posts.get(seleccion - 1);
            dataController.deletePost(postSeleccionado);
            System.out.println("Post eliminado correctamente: " + postSeleccionado.getTitle());
        }
    }

    public static void removeFollower() {
        if (currentUser == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }
        List<Usuario> seguidores = dataController.getFollowers(currentUser.getId());
        int seleccion = Utils.integer("Seleccione el número del seguidor a eliminar (0 para cancelar): ");
        if (seleccion == 0) {
            System.out.println("Operación cancelada.");
        } else if (seleccion < 1 || seleccion > seguidores.size()) {
            System.out.println("Selección inválida. Por favor, intente de nuevo.");
            removeFollower();
        } else {
            Usuario seguidorSeleccionado = seguidores.get(seleccion - 1);
            if(dataController.removeFollower(seguidorSeleccionado.getId())) {
                System.out.println("Seguidor eliminado correctamente: " + seguidorSeleccionado.getNombre());
            } else {
                System.out.println("No se pudo eliminar el seguidor. Verifique que lo siga.");
            }
        }
    }
}
