package module;

import Utils.Utils;

import java.time.Instant;
import java.util.*;

public abstract class Post implements Comparable<Post> {

    private final String title;
    private final Date date;
    private final TreeSet<Comentario> comments = new TreeSet<>();
    private final Usuario owner;


    public Post (String title, Usuario owner) {
        this.owner = owner;
        this.title = title;
        this.date = Date.from(Instant.now());
    }

    public Post(String title, Date date, Usuario owner) {
        this.owner = owner;
        this.title = title;
        this.date = date;
    }

    public Post(String title, Date date, TreeSet<Comentario> comentarios,  Usuario owner) {
        this.owner = owner;
        this.title = title;
        this.date = date;
        this.comments.addAll(comentarios);
    }
    public TreeSet<Comentario> getComments() {
        return comments;
    }
    public void addComentario(Comentario comentario) {
        comments.add(comentario);
    }

    public void removeComentario(Comentario comentario) {
        comments.remove(comentario);
    }
    public String getTitle() {
        return title;
    }
    public int getNumComentarios() {
        return comments.size();
    }

    public abstract String getParam();

    private String getCommentsString() {
        StringBuilder sb = new StringBuilder();
        for (Comentario comentario : comments) {
            sb.append(comentario.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Post o) {
        int cmp = o.date.compareTo(this.date);
        if (cmp == 0) {
            cmp = this.title.compareTo(o.title);
        }
        return cmp;
    }

    @Override
    public String toString() {
        return  title.toUpperCase() + "\n" +
                owner + ", " + Utils.formatFechaEspecial(date) + ":\n" +
                getParam()+ "\n" +
                "comentarios(" + getNumComentarios()+ ") :\n"+
                getCommentsString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(title, post.title) && Objects.equals(date, post.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date);
    }
}
