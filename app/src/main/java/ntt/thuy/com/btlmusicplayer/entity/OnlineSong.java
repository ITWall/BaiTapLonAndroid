package ntt.thuy.com.btlmusicplayer.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class OnlineSong extends Song {
    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("commentable")
    @Expose
    public Boolean commentable;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("original_content_size")
    @Expose
    public Integer originalContentSize;
    @SerializedName("last_modified")
    @Expose
    public String lastModified;
    @SerializedName("sharing")
    @Expose
    public String sharing;
    @SerializedName("tag_list")
    @Expose
    public String tagList;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("streamable")
    @Expose
    public Boolean streamable;
    @SerializedName("embeddable_by")
    @Expose
    public String embeddableBy;
    @SerializedName("downloadable")
    @Expose
    public Boolean downloadable;
    @SerializedName("purchase_url")
    @Expose
    public Object purchaseUrl;
    @SerializedName("label_id")
    @Expose
    public Object labelId;
    @SerializedName("purchase_title")
    @Expose
    public Object purchaseTitle;
    @SerializedName("genre")
    @Expose
    public String genre;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("label_name")
    @Expose
    public Object labelName;
    @SerializedName("release")
    @Expose
    public Object release;
    @SerializedName("track_type")
    @Expose
    public Object trackType;
    @SerializedName("key_signature")
    @Expose
    public Object keySignature;
    @SerializedName("isrc")
    @Expose
    public Object isrc;
    @SerializedName("video_url")
    @Expose
    public Object videoUrl;
    @SerializedName("bpm")
    @Expose
    public Object bpm;
    @SerializedName("release_year")
    @Expose
    public Object releaseYear;
    @SerializedName("release_month")
    @Expose
    public Object releaseMonth;
    @SerializedName("release_day")
    @Expose
    public Object releaseDay;
    @SerializedName("original_format")
    @Expose
    public String originalFormat;
    @SerializedName("license")
    @Expose
    public String license;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("attachments_uri")
    @Expose
    public String attachmentsUri;
    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;
    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;
    @SerializedName("waveform_url")
    @Expose
    public String waveformUrl;
    @SerializedName("stream_url")
    @Expose
    public String streamUrl;
    @SerializedName("playback_count")
    @Expose
    public Integer playbackCount;
    @SerializedName("download_count")
    @Expose
    public Integer downloadCount;
    @SerializedName("favoritings_count")
    @Expose
    public Integer favoritingsCount;
    @SerializedName("comment_count")
    @Expose
    public Integer commentCount;
    @SerializedName("likes_count")
    @Expose
    public Integer likesCount;
    @SerializedName("reposts_count")
    @Expose
    public Integer repostsCount;
    @SerializedName("policy")
    @Expose
    public String policy;
    @SerializedName("monetization_model")
    @Expose
    public String monetizationModel;

    public class User {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("kind")
        @Expose
        public String kind;
        @SerializedName("permalink")
        @Expose
        public String permalink;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("last_modified")
        @Expose
        public String lastModified;
        @SerializedName("uri")
        @Expose
        public String uri;
        @SerializedName("permalink_url")
        @Expose
        public String permalinkUrl;
        @SerializedName("avatar_url")
        @Expose
        public String avatarUrl;

    }
}
