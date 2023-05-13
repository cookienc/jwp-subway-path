package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long saveLine(LineRequest request) {
        final Line line = new Line(request.getName(), request.getColor());
        final Long lineId = lineDao.insert(line);

        final Section section = new Section(request.getDistance(), request.getUpStationId(), request.getDownStationId(), lineId);
        sectionDao.insert(section);

        return lineId;
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();

        return persistLines.stream()
                .map(line -> findLineResponseById(line.getId()))
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        final List<Section> sections = sectionDao.findAllByLineId(id);
        List<Section> sortedSections = Sections.from(sections).getSections();

        final List<Long> stationsIds = sortedSections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());
        stationsIds.add(sortedSections.get(sortedSections.size() - 1).getDownStationId());

        final List<Station> stations = stationsIds.stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());

        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine, stations);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 노선을 찾을 수 없습니다."));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
