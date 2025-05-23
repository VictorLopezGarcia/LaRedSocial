package module;


import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Usuario implements Comparable<Usuario> {

    private String nombre;
    private Set<Usuario> seguidores = new TreeSet<>();

    public Usuario(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set<Usuario> getSeguidores() {
        return seguidores;
    }
    public void setSeguidores(Set<Usuario> seguidores) {
        this.seguidores = seguidores;
    }
    public boolean addSeguidor(Usuario seguidor) {
        return seguidores.add(seguidor);
    }
    public boolean removeSeguidor(String seguidor) {
        for (Usuario u : seguidores) {
            if (u.getNombre().equals(seguidor)) {
                seguidores.remove(u);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nombre, usuario.nombre);
    }


    @Override
    public int compareTo(Usuario o) {
        return this.nombre.compareTo(o.getNombre());
    }
}
