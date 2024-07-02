package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "members")
@Data
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "furigana", nullable = false)
	private String furigana;
	
	@Column(name = "postal_code", nullable = false)
	private String postalCode;
	
	@Column(name = "address", nullable = false)
	private String address;
	
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;
	
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
	
	@OneToMany(mappedBy = "member")
	private Set<Reservation> reservations;
	
	@OneToMany(mappedBy = "member")
	private Set<Review> reviews;
	
	@OneToMany(mappedBy = "member")
	private Set<Favorite> favorites;
	
	public enum Status {
		FREE,
		PAID
	}
}
