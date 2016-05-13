package br.facape.sistemas.distribuidos.chat.utils.beans;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class User implements Serializable {
	
	private static final long serialVersionUID = -2500271043369747676L;

	private String nickname;
	private String host;
	
	public User() {
	}

	public User(String nickname, String host) {
		super();
		this.nickname = nickname;
		this.host = host;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
