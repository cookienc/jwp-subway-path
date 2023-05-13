package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Section> rowMapper = (rs, num) -> new Section(
            rs.getLong("id"),
            rs.getInt("distance"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getLong("line_id")
    );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Section section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Section> findAllByLineId(Long lineId) {
        final String sql = "SELECT * FROM section where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void update(Section updateSection) {
        final String sql = "UPDATE section SET distance = ?, up_station_id = ?, down_station_id = ?";
        jdbcTemplate.update(sql, updateSection.getDistance(), updateSection.getUpStationId(), updateSection.getDownStationId());
    }

    public boolean exists(Long upStationId, Long downStationId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM section WHERE up_station_id = ? AND down_station_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, upStationId, downStationId);
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
