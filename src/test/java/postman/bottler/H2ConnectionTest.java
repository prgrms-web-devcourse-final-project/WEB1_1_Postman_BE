package postman.bottler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class H2ConnectionTest extends TestBase {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("H2 데이터베이스 연결 확인")
    void h2DatabaseConnectionTest() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String dbProductName = metaData.getDatabaseProductName();
            assertThat(dbProductName).isEqualTo("H2"); // H2 데이터베이스가 연결되었는지 확인
            System.out.println("Connected to: " + dbProductName);
        }
    }
}
