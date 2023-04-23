package racingcar.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import racingcar.dao.RacingCarDao;
import racingcar.domain.Name;
import racingcar.domain.RacingCar;
import racingcar.domain.RacingCars;
import racingcar.domain.TryCount;
import racingcar.dto.RacingCarDto;
import racingcar.dto.RacingCarResponse;

@Service
public class RacingGameService {

    private final RacingCarDao racingCarDao;

    public RacingGameService(final RacingCarDao racingCarDao) {
        this.racingCarDao = racingCarDao;
    }

    public RacingCarResponse play(final List<String> inputNames, final int inputCount) {
        final RacingCars racingCars = createRacingCars(inputNames);
        final TryCount tryCount = createTryCount(inputCount);

        moveAllCars(racingCars, tryCount);

        racingCarDao.insertGame(racingCars, tryCount);

        return createRacingCarResponse(racingCars);
    }

    private RacingCars createRacingCars(final List<String> names) {
        return new RacingCars(names.stream()
                .map(Name::new)
                .map(RacingCar::new)
                .collect(Collectors.toUnmodifiableList()));
    }

    private TryCount createTryCount(final int tryCount) {
        return new TryCount(tryCount);
    }

    private void moveAllCars(final RacingCars racingCars, TryCount tryCount) {
        while (tryCount.isOpportunity()) {
            racingCars.moveAll();
            tryCount = tryCount.deduct();
        }
    }

    private RacingCarResponse createRacingCarResponse(final RacingCars racingCars) {
        final List<RacingCarDto> racingCarDtos = racingCars.getRacingCars()
                .stream()
                .map(racingCar -> new RacingCarDto(racingCar.getName(), racingCar.getPosition()))
                .collect(Collectors.toUnmodifiableList());

        return new RacingCarResponse(racingCars.getWinnerNames(), racingCarDtos);
    }
}
