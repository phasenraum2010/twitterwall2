package org.woehlke.twitterwall.oodm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by tw on 10.06.17.
 */
@Entity
public class MyMediaEntity extends MyTwitterObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true,nullable=false)
    private long idTwitter;

    @Column
    private String mediaHttp;

    @Column
    private String mediaHttps;

    @Column
    private String url;

    @Column
    private String display;

    @Column
    private String expanded;

    @Column
    private String type;

    @Column
    private int[] indices;

    public MyMediaEntity(long idTwitter, String mediaHttp, String mediaHttps, String url, String display, String expanded, String type, int[] indices) {
        this.idTwitter = idTwitter;
        this.mediaHttp = mediaHttp;
        this.mediaHttps = mediaHttps;
        this.url = url;
        this.display = display;
        this.expanded = expanded;
        this.type = type;
        this.indices = indices;
    }

    private MyMediaEntity() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdTwitter() {
        return idTwitter;
    }

    public void setIdTwitter(long idTwitter) {
        this.idTwitter = idTwitter;
    }

    public String getMediaHttp() {
        return mediaHttp;
    }

    public void setMediaHttp(String mediaHttp) {
        this.mediaHttp = mediaHttp;
    }

    public String getMediaHttps() {
        return mediaHttps;
    }

    public void setMediaHttps(String mediaHttps) {
        this.mediaHttps = mediaHttps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getExpanded() {
        return expanded;
    }

    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyMediaEntity)) return false;

        MyMediaEntity that = (MyMediaEntity) o;

        if (idTwitter != that.idTwitter) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (mediaHttp != null ? !mediaHttp.equals(that.mediaHttp) : that.mediaHttp != null) return false;
        if (mediaHttps != null ? !mediaHttps.equals(that.mediaHttps) : that.mediaHttps != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (display != null ? !display.equals(that.display) : that.display != null) return false;
        if (expanded != null ? !expanded.equals(that.expanded) : that.expanded != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return Arrays.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (idTwitter ^ (idTwitter >>> 32));
        result = 31 * result + (mediaHttp != null ? mediaHttp.hashCode() : 0);
        result = 31 * result + (mediaHttps != null ? mediaHttps.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (display != null ? display.hashCode() : 0);
        result = 31 * result + (expanded != null ? expanded.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(indices);
        return result;
    }
}