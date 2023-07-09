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

    public Request(Integer id, Integer version, @NotNull String title, @NotNull String description, User requester, User assignedTo, String resolution, LocalDateTime createdDate, LocalDateTime updateDate) {
        this.id = id;
        this.version = version;
        this.title = title;
        this.description = description;
        this.setRequester(requester);
        this.setAssignedTo(assignedTo);
        this.resolution = resolution;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Version
    private Integer version;

    @NotBlank(message = "Title must not be blank.")
    @NotNull(message = "Title must have a value.")
    @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters.")
    @Column(length = 50)
    private String title;

    @NotBlank(message = "Description must not be blank.")
    @NotNull(message = "Description must have a value.")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters.")
    @Column(length = 500)
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "requester_user_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedTo;

    @Column(length = 500)
    private String resolution;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    public void setRequester(User requester) {
        this.requester = requester;
        requester.getRequests().add(this);
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;

        if (assignedTo != null)
            assignedTo.getAssignedRequests().add(this);
    }
}
