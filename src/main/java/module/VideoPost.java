package module;

import java.util.Date;
import java.util.TreeSet;

public class VideoPost extends Post{

    private String quality;

    private int duration;

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

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String getParam() {
        return "Video: \nCalidad:"+ this.getQuality() + ", Duracion: " + this.getDuration()+ " s";
    }
}