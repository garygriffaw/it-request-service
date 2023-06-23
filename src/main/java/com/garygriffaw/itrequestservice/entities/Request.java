package com.garygriffaw.itrequestservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
public class Request {

    public Request(Integer id, Integer version, @NotNull String title, @NotNull String description, User requester, String resolution, LocalDateTime createdDate, LocalDateTime updateDate) {
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
    @GeneratedValue
    private Integer id;

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
