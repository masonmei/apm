package com.baidu.oped.apm.collector.dao.jdbc;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.google.common.base.CaseFormat;

/**
 * Created by mason on 8/15/15.
 */
public abstract class BaseRepository<T> implements RowMapper<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRepository.class);

    private static final String INSERT_PATTERN = "INSERT into %s (%s ) values (%s );";
    private static final String DELETE_PATTERN = "DELETE FROM %s %s";
    private static final String UPDATE_PATTERN = "UPDATE %s SET %s %s";
    private static final String QUERY_PATTERN = "SELECT %s FROM %s %s";

    private static final String WIlDCARD = "*";
    private static final String EMPTY_CONDITION = "";
    private static final String ID_CONDITION = "id = ?";

    private final Class<T> objectClass;
    private final String tableName;
    private final Map<String, String> fieldColumnMapper = new HashMap<String, String>();
    private final Map<String, PropertyDescriptor> mappedFields = new HashMap<String, PropertyDescriptor>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public BaseRepository(Class<T> objectClass, String tableName) {
        this.objectClass = objectClass;
        this.tableName = tableName;

        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(objectClass);
        for (PropertyDescriptor pd : pds) {
            mappedFields.put(pd.getName(), pd);
        }

        for (PropertyDescriptor pd : pds) {
            String lowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pd.getName());
            fieldColumnMapper.put(lowerUnderscore, pd.getName());
        }
    }

    private static StringBuilder conditionBuilder() {
        return new StringBuilder("WHERE ");
    }

    public T findById(long id) {
        String sql =
                String.format(QUERY_PATTERN, WIlDCARD, tableName, conditionBuilder().append(" ").append(ID_CONDITION));
        return jdbcTemplate.queryForObject(sql, new Object[] {id}, this);
    }

    public List<T> find() {
        String sql = String.format(QUERY_PATTERN, WIlDCARD, tableName, EMPTY_CONDITION);
        return jdbcTemplate.query(sql, this);
    }

    public int save(T entity) {
        List<String> columns = new ArrayList<String>(fieldColumnMapper.size());
        List<String> wildcards = new ArrayList<String>(fieldColumnMapper.size());
        List<Object> params = new ArrayList<Object>(fieldColumnMapper.size());

        for (Map.Entry<String, String> entry : fieldColumnMapper.entrySet()) {
            try {
                PropertyDescriptor descriptor = mappedFields.get(entry.getValue());
                Object value = descriptor.getReadMethod().invoke(entity);
                columns.add(entry.getKey());
                wildcards.add("?");
                params.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        String sql = String.format(INSERT_PATTERN, tableName, String.join(", ", columns), String.join(", ", wildcards));

        int update = jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
        return update;
    }

    public int deleteById(long id) {
        String sql = String.format(DELETE_PATTERN, tableName, conditionBuilder().append(" ").append(ID_CONDITION));
        int update = jdbcTemplate.update(sql, id);
        return update;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T entry = BeanUtils.instantiate(objectClass);
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(entry);

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int index = 1; index <= columnCount; index++) {
            String column = JdbcUtils.lookupColumnName(metaData, index);
            String field = fieldColumnMapper.get(column);
            if (field == null) {
                continue;
            }
            PropertyDescriptor pd = this.mappedFields.get(field);
            try {
                Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
                try {
                    wrapper.setPropertyValue(pd.getName(), value);
                } catch (TypeMismatchException e) {
                    LOG.error("unable to map column " + column + " to property " + pd.getName(), e);
                    throw e;
                }
            } catch (NotWritablePropertyException ex) {
                LOG.error("unable to map column " + column + " to property " + pd.getName(), ex);
                throw ex;
            }
        }

        return entry;
    }
}
