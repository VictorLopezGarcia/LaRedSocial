package module;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Comentario implements Comparable<Comentario>{

    private Usuario owner;
    private String text;
    private Date date;

    public Comentario(String text, Usuario owner) {
        this.owner = owner;
        this.text = text;
        this.date = Date.from(Instant.now());
    }

    public Comentario(String text, Date date, Usuario owner) {
        this.owner = owner;
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Usuario getOwner() {
        return owner;
    }

    public void setOwner(Usuario owner) {
        this.owner = owner;
    }

    @Override
    public int compareTo(Comentario o) {
        int cmp = this.date.compareTo(o.date);
        if (cmp == 0) {
            cmp = this.getText().compareTo(o.getText());
        }
        return cmp;
    }

    @Override
    public String toString() {
        return "("+ date +") " + owner + ": " + text;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comentario that = (Comentario) o;
        return Objects.equals(owner, that.owner) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, text);
    }
}
