package racingcar.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SetCollectionTest {
    private Set<Integer> numbers;

    @BeforeEach
    void setUp() {
        numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
    }

    @Test
    @DisplayName("Set의 Size 메서드 사용 테스트")
    void sizeTest() {
        //when
        int size = numbers.size();

        //then
        assertEquals(size, 3);
        assertThat(numbers).hasSize(3);
    }

    @ParameterizedTest
    @DisplayName("Parameterized 활용 테스트")
    @ValueSource(ints = {1, 2, 3})
    void containsCorrectValues(int value) {
        assertThat(numbers.contains(value)).isTrue();
    }

    @ParameterizedTest
    @DisplayName("CsvSource 어노테이션 활용 테스트")
    @CsvSource(value = {"1:true", "2:true", "3:true", "4:false", "5:false"}, delimiter = ':')
    void containsRandomValues(int value, boolean expected) {
        assertThat(numbers.contains(value)).isEqualTo(expected);
    }
}
