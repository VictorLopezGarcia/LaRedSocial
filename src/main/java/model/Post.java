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

    public String getParam(){
        return "";
    }

    private String getCommentsString() {
        StringBuilder sb = new StringBuilder();
        for (Comentario comentario : DataController.getInstance().getCommentsfromPost(id)) {
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
                DataController.getInstance().gerUserNameById(ownerId) + ", " + Utils.formatFechaEspecial(date) + ":\n" +
                getParam()+ "\n" +
                "comentarios(" + DataController.getInstance().getCommentsNumFromPost(id)+ ") :\n"+
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
}
