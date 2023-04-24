package racingcar.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import racingcar.domain.RacingCar;
import racingcar.domain.RacingCars;
import racingcar.domain.TryCount;

@Repository
public class RacingCarDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public RacingCarDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("PLAY_RESULT")
                .usingGeneratedKeyColumns("id");
    }

    @Transactional
    public void insertGame(final RacingCars racingCars, final TryCount tryCount) {
        final HashMap<String, String> parameter = new HashMap<>();

        parameter.put("winners", concat(racingCars.getWinnerNames()));
        parameter.put("trial_count", String.valueOf(tryCount.getCount()));
        parameter.put("created_at", LocalDateTime.now().toString());

        final int gameId = insertActor.executeAndReturnKey(parameter).intValue();

        insertGameLog(racingCars, gameId);
    }

    private void insertGameLog(final RacingCars racingCars, final int gameId) {
        final String sql = "INSERT INTO LOG (game_id, name, move) values (?,?,?)";

        for (RacingCar racingCar : racingCars.getRacingCars()) {
            jdbcTemplate.update(sql, gameId, racingCar.getName(), racingCar.getPosition());
        }
    }

    private String concat(final List<String> names) {
        return String.join(",", names);
    }
}
