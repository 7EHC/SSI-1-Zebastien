package sit.project.projectv1.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import sit.project.projectv1.advice.ErrorResponse;
import sit.project.projectv1.dtos.*;
import sit.project.projectv1.entities.Announcement;
import sit.project.projectv1.entities.User;
import sit.project.projectv1.enums.Mode;
import sit.project.projectv1.repositories.UserRepository;
import sit.project.projectv1.services.AnnouncementService;
import sit.project.projectv1.services.CategoryService;
import sit.project.projectv1.utils.ListMapper;

import java.util.List;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@CrossOrigin
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private UserRepository userRepository;

//    @GetMapping
//    public List<AnnouncementDTO> getAnnouncementList(@RequestParam(defaultValue = "admin") Mode mode,
//                                                     @RequestParam(defaultValue = "0") int category) {
//        List<Announcement> announcementList = announcementService.getAnnouncementList(mode, category);
//        List<AnnouncementDTO> announcementDTOList = announcementList.stream()
//                .map(announcement -> modelMapper.map(announcement, AnnouncementDTO.class))
//                .collect(Collectors.toList());
//        return announcementDTOList;
//    }

    @GetMapping
    public List<AnnouncementDTO> getAnnouncementList(@RequestParam(defaultValue = "admin") Mode mode, @RequestParam(defaultValue = "0") int category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.announcementService.getUserFromToken(authentication);
        List<Announcement> announcementList = this.announcementService.getAnnouncementList(mode, category, user);
        List<AnnouncementDTO> announcementDTOList = (List)announcementList.stream().map((announcement) -> {
            return (AnnouncementDTO)this.modelMapper.map(announcement, AnnouncementDTO.class);
        }).collect(Collectors.toList());
        return announcementDTOList;
    }

    @GetMapping("/pages")
    public PageDTO<AnnouncementDTO> getAnnouncementPageDTO(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size,
                                                           @RequestParam(defaultValue = "admin") Mode mode,
                                                           @RequestParam(defaultValue = "0") int category) {
        Page<Announcement> announcementPage = announcementService.getAnnouncementPage(page, size, mode, category);
        return listMapper.toPageDTO(announcementPage, AnnouncementDTO.class, modelMapper);
    }

    @GetMapping("/{announcementId}")
    public AnnouncementDetailDTO getAnnouncementById(@PathVariable Integer announcementId) {
        return modelMapper.map(announcementService.getAnnouncementById(announcementId), AnnouncementDetailDTO.class);
    }

    @DeleteMapping("/{announcementId}")
    public void deleteAnnouncementById(@PathVariable Integer announcementId) {
        announcementService.deleteAnnouncementById(announcementId);
    }

    @PostMapping
    public OutputAnnouncementDTO createAnnouncement(@RequestBody AddAnnouncementDTO announcementDTO,
                                                    @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        // Assuming you have a method to extract the username from the JWT token
        String username = extractUsernameFromToken(authorizationHeader);

        Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
//        announcement.setAnnouncementOwner(userRepository.findByUsername(announcementDTO.getUsername()));
        announcement.setId(null);
        announcement.setAnnouncementCategory(categoryService.getCategory(announcementDTO.getCategoryId()));

        // Set AnnouncementOwner using the extracted username
        announcement.setAnnouncementOwner(userRepository.findByUsername(username));

        announcementService.createAnnouncement(announcement);
        return modelMapper.map(announcement, OutputAnnouncementDTO.class);
    }

    private String extractUsernameFromToken(String authorizationHeader) {
        // Assuming the token is in the format "Bearer <token>"
        String token = authorizationHeader.substring("Bearer ".length()).trim();

        // Replace with the actual secret key used to sign the JWT
        String secretKey = "ssi1-bronze-shell-bast";

        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @PutMapping("/{announcementID}")
    public OutputAnnouncementDTO update(@PathVariable Integer announcementID, @RequestBody AddAnnouncementDTO updateAnnouncement) {
        Announcement existingAnnouncement = announcementService.getAnnouncementById(announcementID);
        User existingOwner = existingAnnouncement.getAnnouncementOwner();
        Announcement announcement = modelMapper.map(updateAnnouncement, Announcement.class);
        announcement.setId(announcementID);
        announcement.setAnnouncementCategory(categoryService.getCategory(updateAnnouncement.getCategoryId()));
        announcement.setAnnouncementOwner(existingOwner);
        announcementService.updateAnnouncement(announcementID, announcement);
        return modelMapper.map(announcement, OutputAnnouncementDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponse er = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Entity attributes validation failed!", request.getDescription(false));
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            er.addValidationError(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
