package com.hojunara.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {
    private static final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime nowSydney = LocalDateTime.now(SYDNEY_ZONE);
        Timestamp timestampSydney = Timestamp.valueOf(nowSydney);
        this.createdAt = timestampSydney;
        this.updatedAt = timestampSydney;
    }

    @PreUpdate
    protected void onUpdate() {
        LocalDateTime nowSydney = LocalDateTime.now(SYDNEY_ZONE);
        this.updatedAt = Timestamp.valueOf(nowSydney);
    }
}
