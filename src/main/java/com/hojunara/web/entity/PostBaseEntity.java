package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class PostBaseEntity {
    private static final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp timestamp = Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant());
        this.createdAt = timestamp;
        this.updatedAt = timestamp;
    }

    @PreUpdate
    protected void onUpdate() {
        Timestamp timestamp = Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant());
        this.updatedAt = timestamp;
    }
}