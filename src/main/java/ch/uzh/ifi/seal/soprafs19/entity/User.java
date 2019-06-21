package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id // primary key annotation
	// @NotBlank Not null or not empty
	// Possibly use javax.validation for automatic rejection of invalid requests, makes it easier
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private UserStatus status;

	private Date birthDate;

	@Column(nullable = false)
	private Date creationDate;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() { return password; }

	public void setPassword(String password) { this.password = password;}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public void setBirthDate(Date birthDate) { this.birthDate = birthDate;}

	public Date getBirthDate() { return birthDate;}

	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

	public Date getCreationDate() {return creationDate; }


	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
