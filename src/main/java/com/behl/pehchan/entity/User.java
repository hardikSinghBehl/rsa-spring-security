package com.behl.pehchan.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = -4126054527443459976L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private UUID id;

	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;

	@Column(name = "full_name", nullable = false)
	private String name;

	@Column(name = "public_key", nullable = false)
	private byte[] publicKey;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	void onCreate() {
		this.id = UUID.randomUUID();
		this.createdAt = LocalDateTime.now(ZoneId.of("+00:00"));
		this.updatedAt = LocalDateTime.now(ZoneId.of("+00:00"));
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = LocalDateTime.now(ZoneId.of("+00:00"));
	}

}