package com.sebastian.unobackend.player;

import com.sebastian.unobackend.gameplayer.GamePlayer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
public class Player implements UserDetails {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(nullable = false)
   private String firstname;

   @Column(nullable = false)
   private String lastname;

   @Column(unique = true, nullable = false)
   private String username;

   @Column(nullable = false)
   private String password;

   private boolean isAccountNonExpired = true;

   private boolean isAccountNonLocked = true;

   private boolean isCredentialsNonExpired = true;

   private boolean isEnabled = true;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
        name = "user_role",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id")}
   )
   private Set<Role> authorities;

   @OneToMany(
        mappedBy = "player",
        cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE},
        orphanRemoval = true)
   private Set<GamePlayer> games = new HashSet<>();

   // Constructors
   public Player() {
   }

   public Player(String firstname, String lastname, String username, String password, Set<Role> authorities) {
      this.firstname = firstname;
      this.lastname = lastname;
      this.username = username;
      this.password = password;
      this.authorities = authorities;
   }

   //Todo: Fix equals and hashCode
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Player player = (Player) o;
      return Objects.equals(id, player.id) && Objects.equals(firstname, player.firstname);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, firstname);
   }


}
