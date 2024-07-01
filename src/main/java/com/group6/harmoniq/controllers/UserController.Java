import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    // callback mapping is placeholder
    @GetMapping("/callback")
    public String callback(Map<String, String> oauthData) {
        // Extract necessary data from OAuth response
        String spotifyId = oauthData.get("spotify_id");
        String displayName = oauthData.get("display_name");
        String email = oauthData.get("email");
        int followers = oauthData.get("followers");
        String imageUrl = oauthData.get("imageUrl");

        // Check if user already exists
        User user = userRepository.findBySpotifyId(spotifyId);
        if (user == null) {
            user = new User();
            user.setSpotifyId(spotifyId);
            user.setDisplayName(displayName);
            user.setEmail(email);
            user.setFollowers(followers);
            user.setImageUrl(imageUrl);
            userRepository.save(user);
        } else {
            // Update existing user tokens and expiration
            userRepository.save(user);
        }

        // Redirect to the desired page
        return "redirect:/";
    }
}
