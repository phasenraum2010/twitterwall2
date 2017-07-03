package org.woehlke.twitterwall.oodm.entities.entities;

import org.woehlke.twitterwall.oodm.entities.User;
import org.woehlke.twitterwall.oodm.entities.common.AbstractTwitterObject;
import org.woehlke.twitterwall.oodm.entities.common.DomainObjectWithIdTwitter;
import org.woehlke.twitterwall.oodm.entities.common.DomainObjectWithScreenName;
import org.woehlke.twitterwall.oodm.listener.entities.MentionListener;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tw on 10.06.17.
 */
@Entity
@Table(name = "mention", uniqueConstraints = {
        @UniqueConstraint(name="unique_mention", columnNames = {"screen_name","id_twitter"})
}, indexes = {
        @Index(name="idx_mention_name", columnList="name")
})
@NamedQueries({
        @NamedQuery(
                name = "Mention.findByIdTwitter",
                query = "select t from Mention as t where t.idTwitter=:idTwitter"
        ),
        @NamedQuery(
                name =  "Mention.findByScreenName",
                query = "select t from Mention as t where t.screenName=:screenName"
        ),
        @NamedQuery(
                name =  "Mention.findByIdTwitterAndScreenName",
                query = "select t from Mention as t where t.idTwitter=:idTwitter and t.screenName=:screenName"
        )


})
@EntityListeners(MentionListener.class)
public class Mention extends AbstractTwitterObject<Mention>implements DomainObjectWithIdTwitter<Mention>,DomainObjectWithScreenName<Mention> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "id_twitter")
    private long idTwitter;

    public static boolean isValidScreenName(String screenName){
        Pattern p = Pattern.compile("^"+User.SCREEN_NAME_PATTERN+"$");
        Matcher m = p.matcher(screenName);
        return m.matches();
    }

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "name")
    private String name;


    public Mention(long idTwitter, String screenName, String name, int[] indices) {
        this.idTwitter = idTwitter;
        this.screenName = screenName;
        this.name = name;
        this.indices = indices;
    }

    private Mention() {
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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mention)) return false;
        if (!super.equals(o)) return false;

        Mention mention = (Mention) o;

        if (screenName != null ? !screenName.equals(mention.screenName) : mention.screenName != null) return false;
        return name != null ? name.equals(mention.name) : mention.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (screenName != null ? screenName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Mention other) {
        return screenName.compareTo(other.getScreenName());
    }

    @Override
    public String toString() {
        return "Mention{" +
                "id=" + id +
                ", idTwitter=" + idTwitter +
                ", screenName='" + screenName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
