package controller;

import model.Comentario;
import model.Post;
import model.PostType;
import model.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataController {


    private static final DataController INSTANCE = new DataController();
    private final String commentsPath = "C:\\Users\\victl\\OneDrive\\Documents\\LaRedSocial\\src\\main\\resources\\data\\comments.csv";
    private final String postsPath = "C:\\Users\\victl\\OneDrive\\Documents\\LaRedSocial\\src\\main\\resources\\data\\posts.csv";
    private final String usersPath = "C:\\Users\\victl\\OneDrive\\Documents\\LaRedSocial\\src\\main\\resources\\data\\users.csv";
    private final HashMap<Integer, Usuario> usuarios = new HashMap<>();
    private final HashMap<Integer, TreeSet<Post>> postsPorUsuario = new HashMap<>();
    private final HashMap<Integer, HashMap<Integer,TreeSet<Comentario>>> comentariosPorPost = new HashMap<>();
    private final HashMap<Integer, Integer[]> seguidoresPorUsuario = new HashMap<>();
    private Usuario currentUser;
    private final int MAX_WALL_POSTS = 10;


    private DataController() {
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public static DataController getInstance() {
        return INSTANCE;
    }

    public void fetchata() {
        try(BufferedReader br = Files.newBufferedReader(Paths.get(usersPath), StandardCharsets.UTF_8)){
            loadUsers(br);
        } catch (IOException e) {
            System.err.println("Error al cargar Usuarios: " + e.getMessage());
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get(postsPath), StandardCharsets.UTF_8)) {
            loadPosts(br);
        } catch (IOException e) {
            System.err.println("Error al cargar posts: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error al parsear fecha de post: " + e.getMessage());
        }
        try(BufferedReader br = Files.newBufferedReader(Paths.get(commentsPath), StandardCharsets.UTF_8)) {
            loadComments(br);
        } catch (IOException e) {
            System.err.println("Error al cargar comentarios: " + e.getMessage());
        }catch (ParseException e) {
            System.err.println("Error al parsear fecha de comentario: " + e.getMessage());
        }
    }

    private void loadUsers(BufferedReader br) throws IOException {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length < 2) {
                System.err.println("Error: línea con formato incorrecto: " + linea);
                continue;
            }
            int id = Integer.parseInt(partes[0].trim());
            String nombre = partes[1].trim();
            int[] seguidores = partes.length > 2 ? Arrays.stream(partes[2].trim().split(","))
                    .mapToInt(Integer::parseInt).toArray() : new int[0];
            Usuario usuario = new Usuario(id, nombre);
            usuarios.put(id, usuario);
            if (!postsPorUsuario.containsKey(id)) {
                postsPorUsuario.put(id, new TreeSet<>());
            }
            seguidoresPorUsuario.put(id, Arrays.stream(seguidores).boxed().toArray(Integer[]::new));
        }
    }

    private void loadPosts(BufferedReader br) throws IOException, ParseException {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length < 2) {
                System.err.println("Error: línea con formato incorrecto: " + linea);
                continue;
            }
            int userId = Integer.parseInt(partes[0].trim());
            int postId = Integer.parseInt(partes[1].trim());
            String titulo = partes[2].trim();
            String fechaStr = partes[3].trim();
            PostType tipo = PostType.fromString(partes[4].trim());
            String contenido = partes[5].trim().replace(",", "\n");
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            Post post = new Post(userId, postId, titulo, fecha, tipo, contenido);
            if (usuarios.containsKey(userId)) {
                addPost(post);
            } else {
                System.err.println("Error: usuario con ID " + userId + " no encontrado para el post: " + postId);
            }
        }
    }

    private void loadComments(BufferedReader br) throws IOException, ParseException {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length < 3) {
                System.err.println("Error: línea con formato incorrecto: " + linea);
                continue;
            }
            int userId = Integer.parseInt(partes[0].trim());
            int postId = Integer.parseInt(partes[1].trim());
            String contenido = partes[2].trim();
            String fechaStr = partes.length > 3 ? partes[3].trim() : new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Date fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaStr);
            Comentario comentario = new Comentario(userId, postId, contenido, fecha);

            comentariosPorPost.computeIfAbsent(userId, k -> new HashMap<>())
                    .computeIfAbsent(postId, k -> new TreeSet<>()).add(comentario);
        }
    }

    public void addUser(Usuario usuario) {
        if (!usuarios.containsKey(usuario.getId())) {
            usuarios.put(usuario.getId(), usuario);
            postsPorUsuario.put(usuario.getId(), new TreeSet<>());
            seguidoresPorUsuario.put(usuario.getId(), new Integer[0]);
            try {
                saveUsersToCSV(usersPath);
            } catch (IOException e) {
                System.err.println("Error al guardar usuarios: " + e.getMessage());
            }
        }
    }

    public void addPost(Post post) {
        int userId = post.getOwnerId();
        if (!postsPorUsuario.containsKey(userId)) {
            postsPorUsuario.put(userId, new TreeSet<>());
        }
        if(postsPorUsuario.get(userId).add(post)){
            try {
                savePostsToCSV(postsPath);
            } catch (IOException e) {
                System.err.println("Error al guardar posts: " + e.getMessage());
            }
        }
    }

    public void addComment(Comentario comentario) {
        int postId = comentario.getPostId();
        int userId = comentario.getOwnerId();
        if (!comentariosPorPost.containsKey(userId)) {
            comentariosPorPost.put(userId, new HashMap<>());
        }
        HashMap<Integer, TreeSet<Comentario>> postComments = comentariosPorPost.get(userId);
        if (!postComments.containsKey(postId)) {
            postComments.put(postId, new TreeSet<>());
        }
        TreeSet<Comentario> comments = postComments.get(postId);
        if (comments.add(comentario)) {
            try {
                saveCommentsToCSV(commentsPath);
            } catch (IOException e) {
                System.err.println("Error al guardar comentarios: " + e.getMessage());
            }
        }
    }

    public boolean addFollower(int userId, int followerId) {
        if (usuarios.containsKey(userId) && usuarios.containsKey(followerId)) {
            Integer[] seguidores = seguidoresPorUsuario.get(userId);
            if (Arrays.stream(seguidores).noneMatch(s -> s == followerId)) {
                seguidoresPorUsuario.put(userId, Arrays.copyOf(seguidores, seguidores.length + 1));
                seguidoresPorUsuario.get(userId)[seguidores.length] = followerId;
                try {
                    saveUsersToCSV(usersPath);
                } catch (IOException e) {
                    System.err.println("Error al guardar usuarios: " + e.getMessage());
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean addFollower(String entrada) {
        if (currentUser == null) {
            return false;
        }
        Usuario usuarioFollow = getUsersIDontFollow().stream()
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase(entrada) && !usuario.equals(currentUser))
                .findFirst()
                .orElse(null);
        if (usuarioFollow == null) {
            return false;
        }
        return addFollower(currentUser.getId(), usuarioFollow.getId());
    }


    public boolean deleteComment(Comentario comentario) {
        if (comentariosPorPost.containsKey(comentario.getOwnerId())) {
            HashMap<Integer, TreeSet<Comentario>> postComments = comentariosPorPost.get(comentario.getOwnerId());
            if (postComments.containsKey(comentario.getPostId())) {
                TreeSet<Comentario> comments = postComments.get(comentario.getPostId());
                if (comments.remove(comentario)){
                    try {
                        saveCommentsToCSV(commentsPath);
                    } catch (IOException e) {
                        System.err.println("Error al guardar comentarios: " + e.getMessage());
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public void deletePost(int userId, int postId) {
        if (postsPorUsuario.containsKey(userId)) {
            TreeSet<Post> posts = postsPorUsuario.get(userId);
            Post postToRemove = null;
            for (Post post : posts) {
                if (post.getId() == postId) {
                    postToRemove = post;
                    break;
                }
            }
            if (postToRemove != null) {
                if(posts.remove(postToRemove)){
                    try {
                        savePostsToCSV(postsPath);
                    } catch (IOException e) {
                        System.err.println("Error al guardar posts: " + e.getMessage());
                    }
                }
            }
        }
    }

    public void deletePost(Post post) {
        int userId = post.getOwnerId();
        int postId = post.getId();
        deletePost(userId, postId);
    }

    public boolean deleteFollower(int seguidorSeleccionado) {
        if (currentUser == null) {
            return false;
        }
        Integer[] seguidores = seguidoresPorUsuario.get(currentUser.getId());
        if(getFollowers(currentUser.getId()).contains(usuarios.get(seguidorSeleccionado))) {
            seguidoresPorUsuario.put(currentUser.getId(), Arrays.stream(seguidores)
                    .filter(id -> id != seguidorSeleccionado)
                    .toArray(Integer[]::new));
            try {
                saveUsersToCSV(usersPath);
            } catch (IOException e) {
                System.err.println("Error al guardar usuarios: " + e.getMessage());
            }
            return true;
        } else {
            return false;
        }
    }

    public String getUserNameById(int id) {
        Usuario usuario = usuarios.get(id);
        if (usuario != null) {
            return usuario.getNombre();
        } else {
            return "Usuario no encontrado";
        }
    }

    public Usuario getUserByName(String name) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Post getPostById(int postId) {
        for (TreeSet<Post> posts : postsPorUsuario.values()) {
            for (Post post : posts) {
                if (post.getId() == postId) {
                    return post;
                }
            }
        }
        return null;
    }

    public List<Post> getPostsByUserId(int userId) {
        return new ArrayList<>(postsPorUsuario.get(userId));
    }

    public List<Comentario> getCommentsByUserId(int userId) {
        List<Comentario> comentarios = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<Integer, TreeSet<Comentario>>> entry : comentariosPorPost.entrySet()) {
            HashMap<Integer, TreeSet<Comentario>> postComments = entry.getValue();
            for (TreeSet<Comentario> commentSet : postComments.values()) {
                for (Comentario comentario : commentSet) {
                    if (comentario.getOwnerId() == userId) {
                        comentarios.add(comentario);
                    }
                }
            }
        }
        return comentarios.stream().sorted().collect(Collectors.toList());
    }

    public List<Post> getWallPosts() {
        List<Post> wallPosts = new ArrayList<>();
        if (currentUser != null) {
            for (Usuario seguidor : getFollowers(currentUser.getId())) {
                wallPosts.addAll(getPostsByUserId(seguidor.getId()));
            }
        }
        return wallPosts.stream().sorted().limit(MAX_WALL_POSTS).collect(Collectors.toList());
    }


    public List<Usuario> getFollowers(int userId) {
        if (seguidoresPorUsuario.containsKey(userId)) {
            Integer[] seguidores = seguidoresPorUsuario.get(userId);
            return Arrays.stream(seguidores)
                    .map(usuarios::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } else {
            return Collections.emptyList();
        }
    }

    public int getCommentsNumFromPost(int id) {
        int count = 0;
        for (Map.Entry<Integer, HashMap<Integer, TreeSet<Comentario>>> entry : comentariosPorPost.entrySet()) {
            HashMap<Integer, TreeSet<Comentario>> postComments = entry.getValue();
            if (postComments.containsKey(id)) {
                count += postComments.get(id).size();
            }
        }
        return count;
    }

    public List<Comentario> getCommentsfromPost(int postId) {
        List<Comentario> comentarios = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<Integer, TreeSet<Comentario>>> entry : comentariosPorPost.entrySet()) {
            HashMap<Integer, TreeSet<Comentario>> postComments = entry.getValue();
            if (postComments.containsKey(postId)) {
                comentarios.addAll(postComments.get(postId));
            }
        }
        return comentarios.stream().sorted().collect(Collectors.toList());
    }

    public List<Usuario> getUsersIDontFollow() {
        if (currentUser == null) {
            return Collections.emptyList();
        }
        List<Usuario> usersIDontFollow = new ArrayList<>();
        for (Usuario usuario : usuarios.values()) {
            if (!usuario.equals(currentUser) && !Arrays.asList(seguidoresPorUsuario.get(currentUser.getId())).contains(usuario.getId())) {
                usersIDontFollow.add(usuario);
            }
        }
        return usersIDontFollow;
    }

    public int getNextPostId() {
        return postsPorUsuario.values().stream()
                .flatMap(Set::stream)
                .mapToInt(Post::getId)
                .max()
                .orElse(0) + 1;
    }

    public int getNextUserId() {
        return usuarios.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }


    public void savePostsToCSV(String path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            for (TreeSet<Post> posts : postsPorUsuario.values()) {
                for (Post post : posts) {
                    String linea = String.format("%d;%d;%s;%s;%s;%s",
                            post.getOwnerId(),
                            post.getId(),
                            post.getTitle(),
                            new SimpleDateFormat("dd/MM/yyyy").format(post.getDate()),
                            post.getType().toString(),
                            post.getContent().replace("\n", " "));
                    bw.write(linea);
                    bw.newLine();
                }
            }
        }
    }

    public void saveCommentsToCSV(String path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            for (HashMap<Integer, TreeSet<Comentario>> postComments : comentariosPorPost.values()) {
                for (Map.Entry<Integer, TreeSet<Comentario>> entry : postComments.entrySet()) {
                    for (Comentario comentario : entry.getValue()) {
                        String linea = String.format("%d;%d;%s;%s",
                                comentario.getOwnerId(),
                                comentario.getPostId(),
                                comentario.getText().replace("\n", " "),
                                new SimpleDateFormat("dd/MM/yyyy").format(comentario.getDate()));
                        bw.write(linea);
                        bw.newLine();
                    }
                }
            }
        }
    }

    public void saveUsersToCSV(String path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            for (Usuario usuario : usuarios.values()) {
                String linea = String.format("%d;%s;%s", usuario.getId(), usuario.getNombre(),
                        Arrays.stream(seguidoresPorUsuario.get(usuario.getId()))
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")));
                bw.write(linea);
                bw.newLine();
            }
        }
    }
}
