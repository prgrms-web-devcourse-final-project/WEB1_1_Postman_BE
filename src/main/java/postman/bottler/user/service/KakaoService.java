package postman.bottler.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import postman.bottler.user.exception.KakaoException;

@Service
public class KakaoService {
    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client.secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;


    public String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URL);
        params.add("code", code);
        params.add("client_secret", KAKAO_CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();

        try {
            ResponseEntity<String> accessTokenResponse = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            if (accessTokenResponse.getBody() == null) {
                throw new KakaoException("Kakao 서버로부터 응답을 받을 수 없습니다.");
            }

            JsonElement element = JsonParser.parseString(accessTokenResponse.getBody());
            JsonObject jsonObject = element.getAsJsonObject();

            return jsonObject.get("access_token").getAsString();
        } catch (HttpClientErrorException.BadRequest e) {
            throw new KakaoException("잘못된 인증 토큰입니다.");
        } catch (Exception e) {
            throw new KakaoException("카카오 인증 과정에서 오류가 발생했습니다.");
        }
    }

    public Map<String, String> getUserInfo(String accessToken) {
        Map<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder result = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                JsonElement element = JsonParser.parseString(result.toString());
                JsonObject properties = element.getAsJsonObject().getAsJsonObject("properties");

                String kakaoId = element.getAsJsonObject().getAsJsonPrimitive("id").getAsString();
                String nickname = properties.getAsJsonPrimitive("nickname").getAsString();

                userInfo.put("kakaoId", kakaoId);
                userInfo.put("nickname", nickname);
            }

        } catch (IOException e) {
            throw new KakaoException("카카오 서버에서 사용자 정보를 받아올 수 없습니다.");
        }
        return userInfo;
    }
}
