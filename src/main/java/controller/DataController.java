package controller;

import model.Comentario;
import model.Post;
import model.PostType;
import model.Usuario;

import java.io.BufferedReader;
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
    private final HashMap<Integer, Usuario> usuarios = new HashMap<>();
    private final HashMap<Integer, TreeSet<Post>> postsPorUsuario = new HashMap<>();
    private final HashMap<Integer, HashMap<Integer,TreeSet<Comentario>>> comentariosPorPost = new HashMap<>();
    private final HashMap<Integer, Integer[]> seguidoresPorUsuario = new HashMap<>();
    private Usuario currentUser;
    private final int MAX_WALL_POSTS = 10;

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    private DataController() {
    }

    public static DataController getInstance() {
        return INSTANCE;
    }

    public void fetchata() {



        try(BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\victl\\OneDrive\\Documents\\LaRedSocial\\src\\main\\resources\\data\\users.csv"), StandardCharsets.UTF_8)){
            loadUsers(br);
        } catch (IOException e) {
            System.err.println("Error al cargar Usuarios: " + e.getMessage());
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\victl\\OneDrive\\Documents\\LaRedSocial\\src\\main\\resources\\data\\posts.csv"), StandardCharsets.UTF_8)) {
            loadPosts(br);
        } catch (IOException e) {
            System.err.println("Error al cargar posts: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Error al parsear fecha de post: " + e.getMessage());
        }
        try(BufferedReader br = Files.newBufferedReader(Paths.get("C:\\Users\\victl\\OneDrive\\Documents\\LaRedSocial\\src\\main\\resources\\data\\comments.csv"), StandardCharsets.UTF_8)) {
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
            String nombre = partes[0].trim();
            int id = Integer.parseInt(partes[1].trim());
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
            String contenido = partes[5].trim();
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

    public void addPost(Post post) {
        int userId = post.getOwnerId();
        if (!postsPorUsuario.containsKey(userId)) {
            postsPorUsuario.put(userId, new TreeSet<>());
        }
        postsPorUsuario.get(userId).add(post);
    }

    public String gerUserNameById(int id) {
        Usuario usuario = usuarios.get(id);
        if (usuario != null) {
            return usuario.getNombre();
        } else {
            return "Usuario no encontrado";
        }
    }

    public Usuario getUserById(int id) {
        return usuarios.get(id);
    }

    public Usuario getUserByName(String name) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
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
                posts.remove(postToRemove);
            }
        }
    }

    public void deletePost(Post post) {
        int userId = post.getOwnerId();
        int postId = post.getId();
        deletePost(userId, postId);
    }

    public void addComment(int userId, int postId, Comentario comentario) {
        if (postsPorUsuario.containsKey(userId)) {
            TreeSet<Post> posts = postsPorUsuario.get(userId);
            for (Post post : posts) {
                if (post.getId() == postId) {
                    comentariosPorPost.computeIfAbsent(userId, k -> new HashMap<>())
                            .computeIfAbsent(postId, k -> new TreeSet<>()).add(comentario);
                }
            }
        }
    }

    public void addComment(Comentario comentario) {
        int userId = comentario.getOwnerId();
        int postId = comentario.getPostId();
        addComment(userId, postId, comentario);
    }

    public boolean deleteComment(Comentario comentario) {
        if (comentariosPorPost.containsKey(comentario.getOwnerId())) {
            HashMap<Integer, TreeSet<Comentario>> postComments = comentariosPorPost.get(comentario.getOwnerId());
            if (postComments.containsKey(comentario.getPostId())) {
                TreeSet<Comentario> comments = postComments.get(comentario.getPostId());
                return comments.remove(comentario);
            }
        }
        return false;
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

    public List<Comentario> getCommentsByPost(int userId, int postId) {
        if (comentariosPorPost.containsKey(userId)) {
            HashMap<Integer, TreeSet<Comentario>> postComments = comentariosPorPost.get(userId);
            return new ArrayList<>(postComments.get(postId));
        }
        return new ArrayList<>();
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

    public void removeComentario(Comentario comentario) {

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
        if (comentariosPorPost.containsKey(id)) {
            HashMap<Integer, TreeSet<Comentario>> postComments = comentariosPorPost.get(id);
            return postComments.values().stream()
                    .mapToInt(TreeSet::size)
                    .sum();
        } else {
            return 0;
        }
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

    public int getUsersCount() {
        return usuarios.size();
    }

    public List<Usuario> getAllUsers() {
        return new ArrayList<>(usuarios.values());
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

    public boolean addFollower(int id, int id1) {
        if (usuarios.containsKey(id) && usuarios.containsKey(id1)) {
            Integer[] seguidores = seguidoresPorUsuario.get(id);
            if (Arrays.stream(seguidores).noneMatch(s -> s == id1)) {
                seguidoresPorUsuario.put(id, Arrays.copyOf(seguidores, seguidores.length + 1));
                seguidoresPorUsuario.get(id)[seguidores.length] = id1;
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
        return addFollower(usuarioFollow.getId(), usuarioFollow.getId());
    }

    public boolean removeFollower(int seguidorSeleccionado) {
        if (currentUser == null) {
            return false;
        }
        Integer[] seguidores = seguidoresPorUsuario.get(currentUser.getId());
        if(getFollowers(currentUser.getId()).contains(usuarios.get(seguidorSeleccionado))) {
            seguidoresPorUsuario.put(currentUser.getId(), Arrays.stream(seguidores)
                    .filter(id -> id != seguidorSeleccionado)
                    .toArray(Integer[]::new));
            return true;
        } else {
            return false;
        }

    }
}
