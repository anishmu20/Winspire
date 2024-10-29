package Winspire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
  private    String userId;
    @Column(name = "user_name")
  private   String fullName;
    @Column(name = "user_email")
   private String email;
    @Column(name="user_password")
  private   String password;
    String profileImage;
  private   boolean verified;

  @ElementCollection
  @CollectionTable(name = "user_rare_collection", joinColumns = @JoinColumn (name = "user_id"))
  @Column(name = "user_rare_collection")
  private List<String> rareCollectionImages;



}
