package Winspire.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

   private String fullName;
  private   String userId;
   private String email;
   private String password;
  private   String profileImage;
   private boolean verified;
  private   List<String> rareCollectionImages;
}
