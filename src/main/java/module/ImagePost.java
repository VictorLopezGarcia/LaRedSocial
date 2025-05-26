package module;

import java.util.Date;
import java.util.TreeSet;

public class ImagePost extends Post{
    private final String imageUrl;

    public ImagePost(String title, String imageUrl, Usuario user) {
        super(title, user);
        this.imageUrl = imageUrl;
    }

    public ImagePost(String title, String imageUrl, Date fecha, Usuario user) {
        super(title, fecha, user);
        this.imageUrl = imageUrl;
    }

    public ImagePost(String title, String imageUrl, Date fecha, TreeSet<Comentario> comentarios, Usuario user) {
        super(title, fecha, comentarios, user);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    @Override
    public String getParam() {
        return "Imagen: \n" + this.getImageUrl();
    }
}
