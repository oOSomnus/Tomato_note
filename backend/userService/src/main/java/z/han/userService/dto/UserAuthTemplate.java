package z.han.userService.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthTemplate {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 6, max = 16, message = "Length of username must keep between 3 to 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 30, message = "Length of password must stay between 8 to 30 characters")
    private String password;
}
