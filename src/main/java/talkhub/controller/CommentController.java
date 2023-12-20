package talkhub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import talkhub.dto.CommentDto;
import talkhub.model.User;
import talkhub.service.CommentService;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public CommentDto getComment(@PathVariable Long id){
        return commentService.getComment(id);
    }
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public CommentDto createComment(@RequestBody CommentDto dto, @AuthenticationPrincipal User user){
        return commentService.createComment(dto, user);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommentDto editComment(@PathVariable Long id, @RequestBody CommentDto dto){
        return commentService.editComment(id, dto);
    }
}
