package ntt.thuy.com.btlmusicplayer.entity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("id")
    @Expose
    protected long id;
    @SerializedName("duration")
    @Expose
    protected long duration;
    @SerializedName("title")
    @Expose
    protected String title;
    @SerializedName("uri")
    @Expose
    protected String uri;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
