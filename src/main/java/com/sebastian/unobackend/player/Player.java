package com.sebastian.unobackend.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sebastian.unobackend.association.GamePlayer;
import com.sebastian.unobackend.card.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

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
   @OneToMany(
        mappedBy = "player",
        cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
        orphanRemoval = true)
   private Set<GamePlayer> games = new HashSet<>();

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Player player = (Player) o;
      return Objects.equals(id, player.id) && Objects.equals(name, player.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name);
   }
}
