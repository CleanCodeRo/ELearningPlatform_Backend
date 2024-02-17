package cleancode.eLearningPlatform.auth.service;



import cleancode.eLearningPlatform.auth.model.*;
import cleancode.eLearningPlatform.auth.repository.UserRepository;
import cleancode.eLearningPlatform.config.JWTService;
import cleancode.eLearningPlatform.modulesAndLessons.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        boolean usernameExists = userRepository.existsByUsername(registerRequest.getUsername());

        if(usernameExists){
            return AuthenticationResponse.builder().response("0").build();
        }

        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .password( passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().response(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ));

        var user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().response(jwtToken).build();
    }

    public User getUserWithToken(String authHeader) {
        String token = authHeader.substring(7);

        User user = userRepository.findByUsername(jwtService.extractUsername(token)).orElse(null);

        return user;
    }

    public Response addOrRemoveLessonFromUser(Long userId, Integer lessonId, Status status) {
        Optional<User> optionalUser = userRepository.findById(userId);
        System.out.println(status + " " + userId + " " + lessonId);

        if(optionalUser.isPresent()){
            if(status.equals(Status.DONE)){
                optionalUser.get().getCompletedLessons().add(lessonId);
                System.out.println( optionalUser.get().toString());
            }else{
                optionalUser.get().getCompletedLessons().remove(Integer.valueOf(lessonId));
                System.out.println( optionalUser.get().toString());
            }

        }

        userRepository.save(optionalUser.get());
        return Response.builder().response("ok").build();
    }
}
