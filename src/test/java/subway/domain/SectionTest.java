package subway.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 같은_구간인지_확인한다() {
        // given
        final Station 잠실역 = new Station(1L, "잠실역");
        final Station 잠실새내역 = new Station(2L, "잠실새내역");
        final Section 구간 = new Section(1L, Distance.from(10), 잠실역, 잠실새내역, 1L);
        final Sections sections = Sections.from(List.of(구간));

        // when, then
        assertThat(sections.hasSection(잠실역, 잠실새내역)).isTrue();
    }
}