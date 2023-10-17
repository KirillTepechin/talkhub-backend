package talkhub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import talkhub.dto.ComplaintResponseDto;
import talkhub.dto.ComplaintRequestDto;
import talkhub.model.User;
import talkhub.service.ComplaintService;

import java.util.List;

@RestController
@RequestMapping("/complaint")
@CrossOrigin(origins = "*")
public class ComplaintController {
    @Autowired
    private ComplaintService complaintService;

    @Secured("MODERATOR")
    @GetMapping
    public List<ComplaintResponseDto> findAllComplaints(){
        return complaintService.getAllComplaints();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Secured("USER")
    @PostMapping
    public void createComplaint(@RequestBody ComplaintRequestDto dto, @AuthenticationPrincipal User user){
        complaintService.createComplaint(dto, user);
    }
    @Secured("MODERATOR")
    @DeleteMapping("/approve")
    public void approveComplaint(@RequestBody ComplaintRequestDto complaintDto){
        complaintService.approveComplaint(complaintDto);
    }
    @Secured("MODERATOR")
    @DeleteMapping("/dismiss")
    public void dismissComplaint(@RequestBody ComplaintRequestDto complaintDto){
        complaintService.dismissComplaint(complaintDto);
    }
}
