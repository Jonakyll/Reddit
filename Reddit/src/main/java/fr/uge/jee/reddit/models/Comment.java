package fr.uge.jee.reddit.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "parentId")
    private long parentId;

    @Column(name = "date")
    private long date;

    @Column(name = "content")
    private String content;

    @Column(name = "value")
    private int value;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private Set<User> upvotes;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private Set<User> downvotes;

    @ManyToOne
    @JoinColumn(name="author_id", nullable = false)
    private User author;

    @OneToMany(targetEntity = Comment.class, fetch = FetchType.LAZY)
    private List<Comment> answers;

    public Comment(String content, User author, long parentId) {
        this.content = content;
        this.author = author;
        this.parentId = parentId;
        upvotes = new HashSet<>();
        downvotes = new HashSet<>();
        date = System.currentTimeMillis();
        answers = new ArrayList<>();
    }

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", value=" + value +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getDate() {
        return date;
    }

    public String getDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Date resultDate = new Date(date);
        return sdf.format(resultDate);
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public static int numberComments(Comment comment) {
        return numberCommentsRec(0, comment);
    }

    public static int numberAllComments(List<Comment> comments) {
        int number = 0;
        for (Comment c: comments) {
            number += numberComments(c);
        }
        return comments.size() + number;
    }

    private static int numberCommentsRec(int number, Comment comment) {
        if (!comment.answers.isEmpty()) {
            for (Comment c : comment.answers) {
                number = numberCommentsRec(number + 1, c);
            }
        }
        return number;
    }
}
