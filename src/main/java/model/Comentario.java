package model;

import Utils.Utils;
import controller.DataController;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Comentario implements Comparable<Comentario>{

    private final int ownerId;
    private final int postId;
    private final String text;
    private final Date date;

    public Comentario(int ownerId, int postId, String text) {
        this.ownerId = ownerId;
        this.postId = postId;
        this.text = text;
        this.date = Date.from(Instant.now());
    }

    public Comentario(int ownerId, int postId, String text, Date date) {
        this.ownerId = ownerId;
        this.postId = postId;
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return DataController.getInstance().getUserNameById(ownerId) + ", "+  Utils.formatFechaEspecial(date) + ": " + text;
    }
    @Override
    public int compareTo(Comentario o) {
        int cmp = o.date.compareTo(this.date);
        if (cmp != 0) return cmp;
        cmp = this.text.compareTo(o.text);
        if (cmp != 0) return cmp;
        return Integer.compare(this.ownerId, o.ownerId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Comentario)) return false;
        Comentario other = (Comentario) obj;
        return ownerId == other.ownerId &&
                postId == other.postId &&
                Objects.equals(text, other.text) &&
                Objects.equals(date, other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, postId, text, date);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getPostId() {
        return postId;
    }

    public Object getDate() {
        return date;
    }
}
