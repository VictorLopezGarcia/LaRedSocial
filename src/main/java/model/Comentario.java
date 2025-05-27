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
    public int compareTo(Comentario o) {
        int cmp = o.date.compareTo(this.date);
        if (cmp == 0) {
            cmp = this.getText().compareTo(o.getText());
        }
        return cmp;
    }

    @Override
    public String toString() {
        return DataController.getInstance().gerUserNameById(ownerId) + ", "+  Utils.formatFechaEspecial(date) + ": " + text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comentario that = (Comentario) o;
        return Objects.equals(ownerId, that.ownerId) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, text);
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getPostId() {
        return postId;
    }
}
