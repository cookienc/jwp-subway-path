package subway.domain;

public class Section {
    private final Long id;
    private final int distance;
    private final Long upStationId;
    private final Long downStationId;
    private final Long lineId;

    public Section(int distance, Long upStationId, Long downStationId, Long lineId) {
        this(null, distance, upStationId, downStationId, lineId);
    }

    public Section(Long id, int distance, Long upStationId, Long downStationId, Long lineId) {
        this.id = id;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getLineId() {
        return lineId;
    }
}
