package bid.xiaocha.xxt_server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="comment")
public class CommentEntity {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int commentId;
	
	private short type;//对于被评论人是充当怎样身份的type
	@Column(length=20,nullable=false)
    private String commentorId;//评论人
	@Column(length=140)
    private String commentContent;
	@Column(nullable=false)
	private double marks;
    @Column(length=20,nullable=false)
    private String commentedId;//被评论人
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public String getCommentorId() {
		return commentorId;
	}
	public void setCommentorId(String commentorId) {
		this.commentorId = commentorId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	public String getCommentedId() {
		return commentedId;
	}
	public void setCommentedId(String commentedId) {
		this.commentedId = commentedId;
	}
    
}
