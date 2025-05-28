package controller;

import model.Comentario;
import model.Post;
import model.Usuario;

import java.util.*;

public class ConsoleOut {


    public static void showOptions() {
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

    public static void select(int option) {
        Usuario currentUser = DataController.getInstance().getCurrentUser();
        switch (option) {
            case 1:
                showPosts(DataController.getInstance().getPostsByUserId(currentUser.getId()), true);
                break;
            case 2:
                showMyComments();
                break;
            case 3:
                showWall();
                break;
            case 4:
                ConsoleIn.askAddPost();
                break;
            case 5:
                ConsoleIn.deletePost();
                break;
            case 6:
                showUsers(DataController.getInstance().getFollowers(currentUser.getId()), true);
                break;
            case 7:
                showAndAddFollower();
                break;
            case 8:
                showUsers(DataController.getInstance().getFollowers(currentUser.getId()), false);
                System.out.println("----------------------");
                ConsoleIn.removeFollower();
                break;
            case 9:
                sugerirAmigosPorAmigosComunes();
                break;
            case 10:
                System.out.println("Hasta luego " + currentUser.getNombre() + "!");
                DataController.getInstance().setCurrentUser(null);
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    public static void showPosts(List<Post> posts, boolean addComment) {
        if (posts.isEmpty()) {
            System.out.println("No hay posts disponibles.");
            return;
        }
        System.out.println("---------------------");
        for (int i = 0; i < posts.size(); i++) {
            System.out.println("----------" + (i + 1) + "----------");
            System.out.println(posts.get(i));
        }
        System.out.println("Total de posts: " + posts.size());
        if (addComment) {
            ConsoleIn.addComment(posts);
        }
        System.out.println("---------------------");
    }

    private static void showMyComments() {
        Usuario currentUser = DataController.getInstance().getCurrentUser();
        System.out.println("---------------------");
        System.out.println("Comentarios de " + currentUser.getNombre() + ":");
        ArrayList<Comentario> comentariosUsuario = new ArrayList<>(DataController.getInstance().getCommentsByUserId(currentUser.getId()));
        if (comentariosUsuario.isEmpty()) {
            System.out.println("No tienes comentarios.");
            return;
        }
        for (int i = 0; i < comentariosUsuario.size(); i++) {
            System.out.println(i + 1 + ". " + comentariosUsuario.get(i) + " en el post: " +
                    DataController.getInstance().getPostById(comentariosUsuario.get(i).getPostId()).getTitle());
        }
        System.out.println("---------------------");
        ConsoleIn.deleteComment(comentariosUsuario);
    }

    private static void showWall() {
        List<Post> wallPosts = DataController.getInstance().getWallPosts();
        if (wallPosts.isEmpty()) {
            System.out.println("No hay posts en el muro.");
        } else {
            System.out.println("---------------------");
            System.out.println("Muro de " + DataController.getInstance().getCurrentUser().getNombre() + ":");
            showPosts(wallPosts, true);
            System.out.println("---------------------");
        }
    }

    private static void showAndAddFollower() {
        if (DataController.getInstance().getUsersIDontFollow().isEmpty()) {
            System.out.println("No hay usuarios disponibles para seguir.");
            return;
        }
        System.out.println("Usuarios disponibles para seguir:");
        List<Usuario> users = new ArrayList<>(DataController.getInstance().getUsersIDontFollow());
        showUsers(users, false);
        System.out.println("----------------------");
        ConsoleIn.addFollower();
    }

    private static void showUsers(List<Usuario> users, boolean addComment) {
        if (users.isEmpty()) {
            System.out.println("No hay usuarios disponibles.");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i).getNombre());
        }
        System.out.println("-----------------------");
        ConsoleIn.askShowUserProfile(users, addComment);
    }

    public static void showUserProfile(Usuario user, boolean addComment) {
        System.out.println("Perfil de " + user.getNombre() + ":");
        showPosts(DataController.getInstance().getPostsByUserId(user.getId()), addComment);
    }

    public static void sugerirAmigosPorAmigosComunes() {
        System.out.println("Sugerencias de amistad para " + DataController.getInstance().getCurrentUser().getNombre() + ":");
        List<Usuario> sugerencias = new ArrayList<>();
        List<Integer> comunes = new ArrayList<>();
        List<Usuario> seguidores = DataController.getInstance().getFollowers(DataController.getInstance().getCurrentUser().getId());
        List<Usuario> usersIDontFollow = DataController.getInstance().getUsersIDontFollow();
        for (Usuario posible : usersIDontFollow) {
            if (posible.equals(DataController.getInstance().getCurrentUser())) continue;
            if (seguidores.contains(posible)) continue;
            int count = 0;
            for (Usuario seguidor : seguidores) {
                if (DataController.getInstance().getFollowers(posible.getId()).contains(seguidor)) {
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
            ConsoleIn.askShowUserProfile(sugerencias, false);
            ConsoleIn.addFollower();
        }
    }
}