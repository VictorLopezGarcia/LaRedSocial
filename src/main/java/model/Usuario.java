package model;

public class Usuario implements Comparable<Usuario> {

    private final int id;
    private final String nombre;

    public Usuario(int id, String nombre) {
        this.nombre = nombre;
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
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

    public int getId() {
        return id;
    }
}