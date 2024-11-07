package ru.yandex.practicum.catsgram.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.catsgram.model.Image;

import java.util.Optional;

@Repository
public class ImageRepository extends BaseRepository<Image> {
    private static final String FIND_BY_POST_ID_QUERY = "SELECT * FROM images WHERE post_id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM images WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO images(post_id, original_file_name," +
            " file_path) VALUES (?, ?, ?)";

    public ImageRepository(JdbcTemplate jdbc, RowMapper<Image> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Image> findByPostId(long postId) {
        return findOne(FIND_BY_POST_ID_QUERY, postId);
    }

    public Optional<Image> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Image save(Image image) {
        insert(
                INSERT_QUERY,
                image.getPostId(),
                image.getOriginalFileName(),
                image.getFilePath()
        );
        return image;
    }
}
