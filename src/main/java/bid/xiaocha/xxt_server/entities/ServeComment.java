package bid.xiaocha.xxt_server.entities;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mysql.fabric.Server;

@Entity
@Table(name="serve_comment")
public class ServeComment {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int serveCommentId;
	@Column(length=20,nullable=false)
	private String serveId;
	@Column(nullable=false)
    private double mark;
	@Column(length=140)
	private String commentContent;
	public int getServeCommentId() {
		return serveCommentId;
	}
	public void setServeCommentId(int serveCommentId) {
		this.serveCommentId = serveCommentId;
	}
	public String getServeId() {
		return serveId;
	}
	public void setServeId(String serveId) {
		this.serveId = serveId;
	}
	public double getMark() {
		return mark;
	}
	public void setMark(double mark) {
		this.mark = mark;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	
}