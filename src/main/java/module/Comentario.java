package module;

import Utils.Utils;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Comentario implements Comparable<Comentario>{

    private final Usuario owner;
    private final String text;
    private final Date date;

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

    public Usuario getOwner() {
        return owner;
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
        return owner + ", "+  Utils.formatFechaEspecial(date) + ": " + text;
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
