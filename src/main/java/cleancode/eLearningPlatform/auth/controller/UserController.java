package cleancode.eLearningPlatform.auth.controller;


import cleancode.eLearningPlatform.auth.model.AuthenticationRequest;
import cleancode.eLearningPlatform.auth.model.AuthenticationResponse;
import cleancode.eLearningPlatform.auth.model.RegisterRequest;
import cleancode.eLearningPlatform.auth.model.User;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @GetMapping("/getUserWithToken")
    public ResponseEntity<User> getUserWithToken(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.getUserWithToken(authHeader));
    }

    @PatchMapping("/{userId}/addOrRemoveLesson/{lessonId}")
    public ResponseEntity<String> addOrRemoveLessonFromUser(@PathVariable (name = "userId") Long userId, @PathVariable (name = "lessonId") Integer lessonId, @RequestBody Status status){
        return ResponseEntity.ok(userService.addOrRemoveLessonFromUser(userId, lessonId, status));
    }

}
