package cleancode.eLearningPlatform.auth.controller;

import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.service.UserService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUserWithToken")
    public ResponseEntity<User> getUserWithToken(@RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.getUserWithToken(authHeader));
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<User>> getUserBySearchEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserBySearchEmail(email));
    }

    @GetMapping("/{userId}/completedStuff")
    public ResponseEntity<CompletedItemsResponse> getCompletedStuffByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getCompletedItems(userId));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.findAllByOrderByRankPoints());
    }

    @GetMapping("/leaderboard/allTime")
    public ResponseEntity<List<User>> getUsersWeekly(){
        return ResponseEntity.ok(userService.findAllByOrderByWeeklyRankPoints());
    }

    @GetMapping("/auth/forgotPassword/{email}")
    public ResponseEntity<AuthenticationResponse> forgotPasswordRequest(@PathVariable String email ){
        return ResponseEntity.ok(userService.generateForgotPasswordToken(email));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable long userId, @RequestBody Map<String, Object> updateFields){
        return ResponseEntity.ok( userService.updateUser(userId, updateFields));
    }

    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }


    @PatchMapping("/reset_CleanCode_password")
    public ResponseEntity<AuthenticationResponse> resetUserPassword(@RequestBody Map<String, String> requestBody, @RequestHeader("Authorization") String authHeader ){
        return ResponseEntity.ok(userService.resetUserPassword( requestBody.get("password"), authHeader));
    }

    @PatchMapping()
    public ResponseEntity<Response> addOrRemoveLessonFromUser(@RequestParam (name = "userId") Long userId, @RequestParam (name = "lessonId") Integer lessonId , @RequestParam (name = "weekId") Integer weekId ,  @RequestBody Status status){
        return ResponseEntity.ok(userService.addOrRemoveLessonFromUser(userId, lessonId,weekId, status));
    }

    @PostMapping("/addimage")
    public ResponseEntity<String> addNewImage(@RequestParam(name="userId") Long userId,
                                              @RequestParam(name="profileImageUrl") String profileImageUrl) {
//        System.out.println(userId);
//        System.out.println(profileImageUrl);
        String result = userService.addImageToUser(userId, profileImageUrl);
            return ResponseEntity.ok(result);
    }
}
