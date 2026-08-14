package com.example.booksocialnetwork.Role;

import com.example.booksocialnetwork.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {


    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer id;
    @Column(unique = true)
    private String name;




}
