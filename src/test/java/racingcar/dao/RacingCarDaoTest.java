package racingcar.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import racingcar.domain.Name;
import racingcar.domain.RacingCar;
import racingcar.domain.RacingCars;
import racingcar.domain.TryCount;

@SpringBootTest
class RacingCarDaoTest {
    private RacingCarDao racingCarDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        racingCarDao = new RacingCarDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE LOG IF EXISTS");
        jdbcTemplate.execute("DROP TABLE PLAY_RESULT IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE PLAY_RESULT("
                + "    id          INT UNSIGNED  NOT NULL AUTO_INCREMENT,"
                + "    winners     VARCHAR(50) NOT NULL,"
                + "    trial_count INT         NOT NULL,"
                + "    created_at  DATETIME    NOT NULL,"
                + "    PRIMARY KEY (id))");

        jdbcTemplate.execute("CREATE TABLE LOG("
                + "    id      INT UNSIGNED NOT NULL AUTO_INCREMENT,"
                + "    game_id INT UNSIGNED NOT NULL,"
                + "    name    VARCHAR(50) NOT NULL,"
                + "    move    INT         NOT NULL,"
                + "    PRIMARY KEY (id),"
                + "    FOREIGN KEY (game_id) REFERENCES PLAY_RESULT (id))");
    }

    @DisplayName("게임 플레이 인원수 만큼 LOG 테이블에 정보가 레코드된다.")
    @Test
    void insertDBTest() {
        //given
        final RacingCars racingCars = new RacingCars(
                List.of(new RacingCar(new Name("블랙캣")),
                        new RacingCar(new Name("리오"))
                ));
        final TryCount tryCount = new TryCount(3);
        final String sql = "SELECT COUNT(*) FROM LOG";

        //when
        racingCarDao.insertGame(racingCars, tryCount);
        final int count = jdbcTemplate.queryForObject(sql, Integer.class);

        //then
        assertThat(count).isEqualTo(2);
    }
}
