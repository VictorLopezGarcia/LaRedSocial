package module;

import java.util.Date;
import java.util.TreeSet;

public class VideoPost extends Post{

    private final String quality;

    private final int duration;

    public VideoPost(String title, String quality, int duration, Usuario user) {
        super(title, user);
        this.quality = quality;
        this.duration = duration;
    }

    public VideoPost(String title, String quality, int duration, Date fecha, Usuario user) {
        super(title, fecha, user);
        this.quality = quality;
        this.duration = duration;
    }

    public VideoPost(String title, String quality, int duration, Date fecha, TreeSet<Comentario> comentarios, Usuario user) {
        super(title, fecha, comentarios, user);
        this.quality = quality;
        this.duration = duration;
    }

    public String getQuality() {
        return quality;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String getParam() {
        return "Video: \nCalidad:"+ this.getQuality() + ", Duracion: " + this.getDuration()+ " s";
    }
}