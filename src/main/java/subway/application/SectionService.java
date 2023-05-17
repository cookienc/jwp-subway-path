package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.strategy.delete.SectionDeleter;
import subway.application.strategy.insert.InsertSection;
import subway.application.strategy.insert.SectionInserter;
import subway.domain.SingleLineSections;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.Objects;

@Service
@Transactional
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final SectionInserter sectionInserter;
    private final SectionDeleter sectionDeleter;

    public SectionService(
            LineRepository lineRepository,
            StationRepository stationRepository,
            SectionRepository sectionRepository,
            SectionInserter sectionInserter,
            SectionDeleter sectionDeleter
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.sectionInserter = sectionInserter;
        this.sectionDeleter = sectionDeleter;
    }

    public Long insertSection(Long lineId, SectionRequest request) {
        validateInput(request, lineId);

        final Station upStation = findById(request.getUpStationId());
        final Station downStation = findById(request.getDownStationId());
        final SingleLineSections sections = sectionRepository.findAllByLineId(lineId);

        validateInsert(upStation, downStation, sections);

        final InsertSection insertSection = new InsertSection(upStation, downStation, request.getDistance(), lineId);
        return sectionInserter.insert(sections, insertSection);
    }

    private void validateInput(SectionRequest request, Long lineId) {
        if (Objects.equals(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }

        if (!lineRepository.exists(lineId)) {
            throw new IllegalArgumentException("존재 하지 않는 노선에는 구간을 추가 할 수 없습니다.");
        }
    }

    private Station findById(Long stationId) {
        return stationRepository.findById(stationId);
    }

    private void validateInsert(Station upStation, Station downStation, SingleLineSections sortedSections) {
        if (sortedSections.hasSection(upStation, downStation)) {
            throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
        }

        if (sortedSections.canInsert(upStation, downStation)) {
            throw new IllegalArgumentException("역이 존재하지 않으면 추가할 수 없습니다.");
        }
    }

    public void deleteStation(Long lineId, Long targetId) {
        final Station targetStation = stationRepository.findById(targetId);
        final SingleLineSections sections = sectionRepository.findAllByLineId(lineId);

        sectionDeleter.delete(sections, targetStation);
    }
}
