package br.facape.sistemas.distribuidos.chat.utils.beans;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Message implements Serializable {

	private static final long serialVersionUID = -8026304746111593842L;

	private User user;
	private String text;
	private Date date;
	
	public Message() {
	}

	public Message(User user, String text, Date date) {
		super();
		this.user = user;
		this.text = text;
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE) ;
	}

}
