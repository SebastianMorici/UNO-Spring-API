package com.sebastian.unobackend.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.unotable.UnoTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 30, message = "Name's length must be between 2 and 30")
    @Column(unique = true)
    private String name;

    // Associations
    @JsonIgnore
    @OneToMany(mappedBy = "playerOne")
    private Set<UnoTable> playerOneUnoTables = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "playerTwo")
    private Set<UnoTable> playerTwoUnoTables = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "playerThree")
    private Set<UnoTable> playerThreeUnoTables = new HashSet<>();
}
