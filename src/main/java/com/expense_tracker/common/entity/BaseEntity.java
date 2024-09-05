package com.expense_tracker.common.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

	@Column(updatable = false)
	@CreationTimestamp
	@JsonIgnore
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@JsonIgnore
	private LocalDateTime updatedAt;

}