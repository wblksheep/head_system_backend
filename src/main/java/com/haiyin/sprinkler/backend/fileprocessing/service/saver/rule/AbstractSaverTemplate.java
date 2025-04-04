package com.haiyin.sprinkler.backend.fileprocessing.service.saver.rule;

import com.google.common.base.Preconditions;
import com.haiyin.sprinkler.backend.fileprocessing.dao.SprinklerDAO;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public abstract class AbstractSaverTemplate implements SprinklerSaverRule{
    protected NamedParameterJdbcTemplate namedJdbc;
    protected JdbcTemplate jdbc;
    protected EntityManager entityManager;

    // 公共配置方法
    @Autowired
    public final void setJdbcTemplates(
             JdbcTemplate jdbc,
             NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
    }

    @Autowired
    public final void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // 模板方法
    @Transactional
    @Override
    public List<Long> parse(List<SprinklerDAO> daos) {
        // 1. 参数校验
        validateDAOs(daos);

        // 2. 执行批量操作
        batchOperation(daos);

        // 3. 清理缓存
        clearCache();

        // 4. 获取持久化ID
        return fetchPersistedIds(daos);
    }

    // 抽象方法（必须实现）
    protected abstract String getInsertSQL();
    protected abstract Object[] mapDAOToParams(SprinklerDAO dao);

    // 可重写方法
    protected String getSelectSQL(){
        return "SELECT id FROM tb_sprinklers WHERE head_serial IN (:serials)";
    }
    protected MapSqlParameterSource createSelectParams(List<SprinklerDAO> daos){
        return new MapSqlParameterSource().addValue("serials", daos.stream()
                .map(SprinklerDAO::getHeadSerial)
                .collect(Collectors.toList()));
    }
    protected void validateDAOs(List<SprinklerDAO> daos) {
        Preconditions.checkArgument(!daos.isEmpty(), "DAO list cannot be empty");
    }
    protected void batchOperation(List<SprinklerDAO> daos) {
        final int batchSize = getBatchSize();
        IntStream.range(0, (daos.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> daos.subList(i * batchSize, Math.min((i + 1) * batchSize, daos.size())))
                .forEach(batch -> executeBatch(batch));
    }

    // 批次处理方法
    private void executeBatch(List<SprinklerDAO> batch) {
        jdbc.batchUpdate(getInsertSQL(), createBatchArgs(batch));

        // 针对Hibernate的二级缓存刷新
        if (needInterimFlush()) {
            entityManager.flush();
            entityManager.clear();
        }
    }

    protected int getBatchSize() {
        return 500; // 默认批次大小
    }

    protected boolean needInterimFlush() {
        return false; // 默认不刷新缓存
    }


    // 私有方法
    private List<Object[]> createBatchArgs(List<SprinklerDAO> daos) {
        return daos.stream()
                .map(this::mapDAOToParams)
                .collect(Collectors.toList());
    }

    private void clearCache() {
        entityManager.flush();
        entityManager.clear();
    }

    private List<Long> fetchPersistedIds(List<SprinklerDAO> daos) {
        return namedJdbc.queryForList(
                getSelectSQL(),
                createSelectParams(daos),
                Long.class
        );
    }
}
