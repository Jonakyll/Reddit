package fr.uge.jee.reddit.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "Topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "date")
    private long date;

    @Column(name = "content")
    @Type(type = "text")
    private String content;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private Set<User> upvotes;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private Set<User> downvotes;

    @Column(name = "value")
    private int value;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(targetEntity = Comment.class, fetch = FetchType.LAZY)
    private List<Comment> answers;

    public Topic(String content, String title, User author) {
        this.content = content;
        this.title = title;
        this.author = author;
        upvotes = new HashSet<>();
        downvotes = new HashSet<>();
        value = 0;
        date = System.currentTimeMillis();
        answers = new ArrayList<Comment>();
    }

    public Topic() {
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", upvotes=" + upvotes.size() +
                ", downvotes=" + downvotes.size() +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", answers=" + answers +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date resultDate = new Date(date);
        return sdf.format(resultDate);
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Comment> answers) {
        this.answers = answers;
    }

    public Set<User> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Set<User> upvotes) {
        this.upvotes = upvotes;
    }

    public Set<User> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Set<User> downvotes) {
        this.downvotes = downvotes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
