package model;

import Utils.Utils;
import controller.DataController;

import java.time.Instant;
import java.util.*;

public class Post implements Comparable<Post> {

    private final int ownerId;
    private final int id;
    private final String title;
    private final Date date;
    private final PostType type;
    private final String content;


    public Post (int ownerId, int id, String title, PostType type, String content) {
        this.ownerId = ownerId;
        this.id = id;
        this.title = title;
        this.type = type;
        this.content = content;
        this.date = Date.from(Instant.now());
    }

    public Post(int ownerId, int id, String title, Date date, PostType type, String content) {
        this.ownerId = ownerId;
        this.id = id;
        this.title = title;
        this.date = date;
        this.type = type;
        this.content = content;
    }
    public String getTitle() {
        return title;
    }


    public int getOwnerId() {
        return ownerId;
    }

    public int getId() {
        return id;
    }

    public PostType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    private String getCommentsString() {
        StringBuilder sb = new StringBuilder();
        for (Comentario comentario : DataController.getInstance().getCommentsfromPost(id)) {
            sb.append(comentario.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return  title.toUpperCase() + "\n" +
                DataController.getInstance().getUserNameById(ownerId) + ", " + Utils.formatFechaEspecial(date) + ":\n" +
                content+ "\n" +
                "comentarios(" + DataController.getInstance().getCommentsNumFromPost(id)+ ") :\n"+
                getCommentsString();
    }

    // En Post.java
    @Override
    public int compareTo(Post o) {
        int cmp = o.date.compareTo(this.date);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.id, o.id);
        if (cmp != 0) return cmp;
        return this.title.compareTo(o.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
