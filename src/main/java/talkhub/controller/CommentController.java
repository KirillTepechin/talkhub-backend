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
@PreAuthorize("isAuthenticated()")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public CommentDto createComment(@RequestBody CommentDto dto, @AuthenticationPrincipal User user){
        return commentService.createComment(dto, user);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
    }

    @PutMapping("/{id}")
    public CommentDto editComment(@PathVariable Long id, @RequestBody CommentDto dto){
        return commentService.editComment(id, dto);
    }
}
