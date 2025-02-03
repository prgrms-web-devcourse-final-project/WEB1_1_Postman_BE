package postman.bottler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class EnvTest extends TestBase {

    @Test
    void envLoadTest() {
        String redisHost = System.getProperty("REDIS_HOST");
        String redisPort = System.getProperty("REDIS_PORT");

        assertThat(redisHost).isNotNull();
        assertThat(redisPort).isNotNull();
    }
}
