package org.woehlke.twitterwall.oodm.entities;

import org.hibernate.validator.constraints.SafeHtml;
import org.woehlke.twitterwall.oodm.entities.parts.AbstractTwitterObject;
import org.woehlke.twitterwall.oodm.entities.common.DomainObject;
import org.woehlke.twitterwall.oodm.entities.common.DomainObjectWithTask;
import org.woehlke.twitterwall.oodm.entities.listener.HashTagListener;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tw on 10.06.17.
 */
@Entity
@Table(
    name = "hashtag",
    uniqueConstraints = {
        @UniqueConstraint(name="unique_hashtag",columnNames = {"text"})
    }
)
@EntityListeners(HashTagListener.class)
public class HashTag extends AbstractTwitterObject<HashTag> implements DomainObject<HashTag>,DomainObjectWithTask<HashTag> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @SafeHtml
    @Column(name="text", nullable = false,length=4096)
    private String text = "";

    public HashTag(Task createdBy, Task updatedBy, String text) {
        super(createdBy,updatedBy);
        this.text = text;
    }

    private HashTag() {
        super();
    }

    public final static String HASHTAG_TEXT_PATTERN = "[öÖäÄüÜßa-zA-Z0-9_]{1,139}";

    @Transient
    public boolean hasValidText(){
        Pattern p = Pattern.compile(HASHTAG_TEXT_PATTERN);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public static boolean isValidText(String hashtagText){
        Pattern p = Pattern.compile(HASHTAG_TEXT_PATTERN);
        Matcher m = p.matcher(hashtagText);
        return m.matches();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUniqueId() {
        return text;
    }


    public String getText() {
        return this.text;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTag)) return false;
        if (!super.equals(o)) return false;

        HashTag hashTag = (HashTag) o;

        return text != null ? text.equals(hashTag.text) : hashTag.text == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HashTag{" +
                " id=" + id +
                ", text='" + text + '\'' +
                super.toString() +
                " }\n";
    }

    @Override
    public boolean isValid() {
        if((text == null)||(text.isEmpty())){
            return false;
        }
        return true;
    }
}
