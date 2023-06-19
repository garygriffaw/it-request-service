package com.garygriffaw.itrequestservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
public class Request {

    public Request(UUID id, Integer version, @NotNull String title, @NotNull String description, User requester, String resolution, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.description = description;
        this.setRequester(requester);
        this.resolution = resolution;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @Version
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String title;

    @NotBlank
    @NotNull
    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requester_user_id", nullable = false)
    private User requester;

    private String resolution;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    public void setRequester(User requester) {
        this.requester = requester;
        requester.getRequests().add(this);
    }
}
