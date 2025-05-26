package module;

import java.util.TreeSet;

public class Usuario implements Comparable<Usuario> {

    private final String nombre;
    private final TreeSet<Usuario> seguidores = new TreeSet<>();
    private final TreeSet<Post> posts = new TreeSet<>();

    public Usuario(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public TreeSet<Usuario> getSeguidores() {
        return seguidores;
    }
    public void addSeguidor(Usuario seguidor) {
        seguidores.add(seguidor);
    }
    public boolean removeSeguidor(String seguidor) {
        for (Usuario u : seguidores) {
            if (u.getNombre().equalsIgnoreCase(seguidor)) {
                seguidores.remove(u);
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return nombre ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return nombre.equalsIgnoreCase(usuario.nombre);
    }

    @Override
    public int compareTo(Usuario o) {
        return this.nombre.compareTo(o.getNombre());
    }

    public TreeSet<Post> getPosts() {
        return posts;
    }
    public void addPost(Post post) {
        posts.add(post);
    }
    public boolean removePost(Post post) {
        return posts.remove(post);
    }
}