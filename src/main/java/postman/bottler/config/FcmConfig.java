package postman.bottler.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FcmConfig {
    @Value("${fcm.private-key}")
    private String privateKey;

    @PostConstruct
    public void init() {
        try {
            // JSON 문자열을 InputStream으로 변환
            System.out.println("privateKey: " + privateKey);
            byte[] decodedBytes = Base64.getDecoder().decode(privateKey);
            System.out.println("decodedBytes = " + Arrays.toString(decodedBytes));
            InputStream serviceAccount = new ByteArrayInputStream(decodedBytes);
            System.out.println("serviceAccount = " + serviceAccount.getClass());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
