package module;

import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

public class TextPost extends Post{

    private String text;

    public TextPost(String title, String text, Usuario user) {
        super(title, user);
        this.text = text;
    }

    public TextPost(String title, String text, Date fecha, Usuario user) {
        super(title, fecha, user);
        this.text = text;
    }

    public TextPost(String title, String text, Date fecha, TreeSet<Comentario> comentarios, Usuario user) {
        super(title, fecha, comentarios, user);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getParam() {
        return "Texto: \n" + this.getText()+ "\n";
    }
}