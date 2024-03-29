package cleancode.eLearningPlatform.auth.controller;


import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserWithToken")
    public ResponseEntity<User> getUserWithToken(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.getUserWithToken(authHeader));
    }

    @GetMapping("/{userId}/completedStuff")
    public ResponseEntity<CompletedItemsResponse> getCompletedStuffByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getCompletedItems(userId));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @PatchMapping()
    public ResponseEntity<Response> addOrRemoveLessonFromUser(@RequestParam (name = "userId") Long userId, @RequestParam (name = "lessonId") Integer lessonId , @RequestParam (name = "weekId") Integer weekId ,  @RequestBody Status status){
        return ResponseEntity.ok(userService.addOrRemoveLessonFromUser(userId, lessonId,weekId, status));
    }

    @PatchMapping("/addCompleteKata")
    public ResponseEntity<Response> addOrRemoveKataFromUser(@RequestParam (name = "userId") Long userId, @RequestParam (name = "kataId") Integer kataId ){
        return ResponseEntity.ok(userService.addOrRemoveKataFromUser(userId,kataId,Status.DONE));
    }

}
