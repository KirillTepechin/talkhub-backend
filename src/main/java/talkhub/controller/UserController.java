package talkhub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import talkhub.dto.UserCredentialsDto;
import talkhub.dto.UserDto;
import talkhub.model.User;
import talkhub.service.UserService;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/exist")
    public Boolean isUserExist(@RequestParam String login){
        return userService.findByLogin(login) != null;
    }
    @PostMapping("/register")
    public void register(@RequestBody UserCredentialsDto dto) {
        userService.register(dto);
    }

    @PostMapping("/auth")
    public String auth(@RequestBody UserCredentialsDto dto) {
        return userService.auth(dto);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/edit-photo", consumes =  {MULTIPART_FORM_DATA_VALUE})
    public void editPhoto(@RequestPart MultipartFile file, @AuthenticationPrincipal User user){
        userService.editPhoto(file, user);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/edit-credentials")
    public void editCredentials(@RequestBody UserCredentialsDto dto, @AuthenticationPrincipal User user){
        userService.editCredentials(dto, user);
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal User user) {
        return userService.me(user);
    }
}
