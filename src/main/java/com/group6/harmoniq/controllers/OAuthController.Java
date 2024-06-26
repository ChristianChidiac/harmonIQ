import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class OAuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/callback")
    public String callback(Map<String, String> oauthData) {
        // Extract necessary data from OAuth response
        String spotifyId = oauthData.get("spotify_id");
        String displayName = oauthData.get("display_name");
        String email = oauthData.get("email");
        String accessToken = oauthData.get("access_token");
        String refreshToken = oauthData.get("refresh_token");
        int expiresIn = Integer.parseInt(oauthData.get("expires_in"));

        // Check if user already exists
        User user = userRepository.findBySpotifyId(spotifyId);
        if (user == null) {
            user = new User();
            user.setSpotifyId(spotifyId);
            user.setDisplayName(displayName);
            user.setEmail(email);
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setExpiresIn(expiresIn);
            userRepository.save(user);
        } else {
            // Update existing user tokens and expiration
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setExpiresIn(expiresIn);
            userRepository.save(user);
        }

        // Redirect to the desired page
        return "redirect:/";
    }
}
