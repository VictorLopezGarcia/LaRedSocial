package Utils;

import module.*;


import java.util.Scanner;
import java.util.TreeSet;

import static module.Main.users;

public class ConsoleUser {

    private static final int MAX_POSTS = 10;
    private static Scanner scanner = new Scanner(System.in);
    private static Usuario currentUser;
    private static final TreeSet<Post> posts = new TreeSet<>();

    public static void setCurrentUser(Usuario currentUser) {
        ConsoleUser.currentUser = currentUser;
    }

    public static void showMenu() {
        if (currentUser == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }
        int option = 0;
        do {
            showOptions();
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
            select(option);
        } while (option!= 9);
    }

    private static void showOptions(){
        System.out.println("Bienvenido " + currentUser.getNombre() + "!");
        System.out.println("1. Mostrar mis posts");
        System.out.println("2. Mostrar mis comentarios");
        System.out.println("3. Mostrar muro");
        System.out.println("4. Agregar post");
        System.out.println("5. Eliminar post");
        System.out.println("6. Mostrar seguidores");
        System.out.println("7. Agregar seguidor");
        System.out.println("8. Eliminar seguidor");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void select(int option){
        switch (option) {
            case 1:
                showPosts();
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
                showFollowers();
                break;
            case 7:
                showUsers();
                break;
            case 8:
                removeFollower();
                break;
            case 9:
                System.out.println("Saliendo...");
                break;
            case 10:
                for (Post post : posts) {
                    System.out.println(post);
                }
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    private static void showUsers() {
        System.out.println("Usuarios disponibles:");
        users.forEach(follower -> {;
            System.out.println(follower);
        });
        System.out.print("Ingrese el nombre del usuario a agregar: ");
        String nombreUsuario = scanner.nextLine();
        for (Usuario u : users) {
            if (u.getNombre().equals(nombreUsuario)) {
                currentUser.addSeguidor(u);
                System.out.println("Seguidor agregado: " + u.getNombre());
            }
        }
        System.out.println("Usuario no encontrado.");
    }

    private static void removeFollower() {
        System.out.print("Ingrese el nombre del seguidor a eliminar: ");
        String seguidor = scanner.nextLine();
        if(currentUser.removeSeguidor(seguidor)) System.out.println("Seguidor eliminado: " + seguidor);
        else System.out.println("No se ha podido eliminar el seguidor.");
    }


    private static void showPosts() {
        System.out.println("Posts de " + currentUser.getNombre() + ":");
        for (Post post : posts) {
            if(post.getOwner().equals(currentUser)) {
                System.out.println(post);
            }
        }
        System.out.println("---------------------");

    }

    private static void showComments() {
        System.out.println("Comentarios de " + currentUser.getNombre() + ":");
        for (Post post : posts) {
            System.out.println("Post: " + post.getTitle());
            for (Comentario comentario : post.getComments()) {
                if (comentario.getOwner().equals(currentUser)) {
                    System.out.println(comentario);
                }
            }
        }
        System.out.println("---------------------");
    }

    private static void showWall() {
        int count = 0;
        TreeSet<Post> tempPosts = new TreeSet<>();
        currentUser.getSeguidores().forEach(s-> tempPosts.addAll(getPosts(s)));
        System.out.println("Muro de " + currentUser.getNombre() + ":");
        for (Post post : tempPosts) {
            if (count < MAX_POSTS) {
                System.out.println(post);
                count++;
            } else {
                break;
            }
        }
        System.out.println("---------------------");
    }

    private static void showDelete() {
        showPosts();
        System.out.print("Ingrese el título del post a eliminar: ");
        String titleToRemove = scanner.nextLine();
        for (Post p : posts) {
            if (p.getTitle().equals(titleToRemove)) {
                removePost(p);
            }
        }
        System.out.println("----------------------");
    }

    private static void showAddPost() {
        System.out.println("Agregar post:");
        System.out.println("1. Texto");
        System.out.println("2. Video");
        System.out.println("3. Imagen");
        System.out.print("Seleccione el tipo de post: ");
        int option = 0;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida. Intente nuevamente.");
        }
        addPost(option);
    }

    private static void showFollowers() {
        System.out.println("Seguidores de " + currentUser.getNombre() + ":");
        for (Usuario seguidor : currentUser.getSeguidores()) {
            System.out.println(seguidor.getNombre());
        }
        System.out.println("---------------------");
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
        System.out.print("Ingrese el título del post: ");
        String title = scanner.nextLine();
        System.out.print("Ingrese la URL de la imagen: ");
        String imageUrl =  scanner.nextLine();
        ImagePost post = new ImagePost(title, imageUrl, currentUser);
        posts.add(post);
    }

    private static void addVideoPost() {
        System.out.print("Ingrese el título del post: ");
        String title =  scanner.nextLine();
        System.out.print("Ingrese la calidad del video: ");
        String quality =  scanner.nextLine();
        System.out.print("Ingrese la duración del video (en segundos): ");
        int duration = Integer.parseInt( scanner.nextLine());
        VideoPost post = new VideoPost(title, quality, duration, currentUser);
        posts.add(post);
    }

    private static void addTextPost() {
        System.out.print("Ingrese el título del post: ");
        String title =  scanner.nextLine();
        System.out.print("Ingrese el texto del post: ");
        String text =  scanner.nextLine();
        TextPost post = new TextPost(title, text, currentUser);
        posts.add(post);
    }

    private static void removePost(Post post) {
        if (posts.contains(post) && post.getOwner().equals(currentUser)) posts.remove(post);
        else System.out.println("No se ha podido eliminar el post.");
    }

    public static TreeSet<Post> getPosts(Usuario usr) {
        TreeSet<Post> tempPosts = new TreeSet<>();
        for (Post post : posts) {
            if (post.getOwner().equals(usr)) {
                tempPosts.add(post);
            }
        }
        return tempPosts;
    }

    public static void addPost(Post post) {
        posts.add(post);
    }

    public static void addComment(Post post,Comentario comentario) {
        post.addComentario(comentario);
    }


}
