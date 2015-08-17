package com.baidu.oped.apm;

import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.String.join;

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
    private static final String ATTR_CONDITION = "`%s` = ?";
    public static final String CLASS_FIELD_NAME = "class";
    public static final String DEFAULT_FIELD_NAME = "id";
    public static final String CONDITIN_HEADER = "WHERE ";
    public static final String AND_DELIMITER = " AND ";
    public static final String COLUMN_FORMAT = "`%s`";

    private final Class<T> objectClass;
    private final String tableName;
    private final String idFieldName;
    private final Map<String, String> fieldColumnMapper = new HashMap<String, String>();
    private final Map<String, PropertyDescriptor> mappedFields = new HashMap<String, PropertyDescriptor>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public BaseRepository() {
        try {
            Class c = getClass();
            Type t = c.getGenericSuperclass();
            if (!(t instanceof ParameterizedType)) {
                throw new Exception("generic class type error");
            }
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.objectClass = (Class<T>) p[0];
        } catch (Exception e) {
            throw new RuntimeException("parse generic class type fail", e);
        }

        Annotation annotation = objectClass.getAnnotation(Table.class);
        if (annotation == null) {
            throw new RuntimeException(String.format("class %s missing @interface Table, is not a pojo",
                    objectClass.getName()));
        }
        Table table = (Table) annotation;
        String name = table.name();
        if (StringUtils.isEmpty(name)) {
            this.tableName = name;
        } else {
            this.tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, objectClass.getName());
        }

        this.idFieldName = DEFAULT_FIELD_NAME;
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(objectClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals(CLASS_FIELD_NAME)) {
                continue;
            }
            mappedFields.put(pd.getName(), pd);
        }

        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals(CLASS_FIELD_NAME)) {
                continue;
            }
            String lowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pd.getName());
            fieldColumnMapper.put(lowerUnderscore, pd.getName());
        }
    }

    private static StringBuilder conditionBuilder() {
        return new StringBuilder(CONDITIN_HEADER);
    }

    public T findOneById(long id) {
        String sql = format(QUERY_PATTERN, WIlDCARD, tableName, conditionBuilder().append(ID_CONDITION));
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, this);
    }

    public T findOneByAttr(String attrName, Object attrValue) {
        String sql =
                format(QUERY_PATTERN, WIlDCARD, tableName, conditionBuilder().append(format(ATTR_CONDITION, attrName)));
        return jdbcTemplate.queryForObject(sql, this, attrValue);
    }

    public T findOneByAttrs(Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            return null;
        }
        StringBuilder conditionBuilder = conditionBuilder();
        List<String> conditions = new ArrayList<String>(attrs.size());
        List<Object> params = new ArrayList<Object>(attrs.size());

        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            if (mappedFields.containsKey(entry.getKey())) {
                conditions.add(format(ATTR_CONDITION, entry.getKey()));
                params.add(entry.getValue());
            }
        }

        String sql = format(QUERY_PATTERN, WIlDCARD, tableName,
                conditionBuilder.append(join(AND_DELIMITER, conditions)).toString());

        return jdbcTemplate.queryForObject(sql, this, params.toArray(new Object[params.size()]));
    }

    public List<T> findByAttr(String attrName, Object attrValue) {
        String sql =
                format(QUERY_PATTERN, WIlDCARD, tableName, conditionBuilder().append(format(ATTR_CONDITION, attrName)));
        return jdbcTemplate.query(sql, this, attrValue);
    }

    public List<T> findByAttrs(Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            return null;
        }
        StringBuilder conditionBuilder = conditionBuilder();
        List<String> conditions = new ArrayList<String>(attrs.size());
        List<Object> params = new ArrayList<Object>(attrs.size());

        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            if (mappedFields.containsKey(entry.getKey())) {
                conditions.add(format(ATTR_CONDITION, entry.getKey()));
                params.add(entry.getValue());
            }
        }

        String sql = format(QUERY_PATTERN, WIlDCARD, tableName,
                conditionBuilder.append(join(AND_DELIMITER, conditions)).toString());

        return jdbcTemplate.query(sql, this, params.toArray(new Object[params.size()]));
    }

    public List<T> findAll() {
        String sql = format(QUERY_PATTERN, WIlDCARD, tableName, EMPTY_CONDITION);
        return jdbcTemplate.query(sql, this);
    }

    public int save(T entity) {
        List<String> columns = new ArrayList<String>(fieldColumnMapper.size());
        List<String> wildcards = new ArrayList<String>(fieldColumnMapper.size());
        List<Object> params = new ArrayList<Object>(fieldColumnMapper.size());

        for (Map.Entry<String, String> entry : fieldColumnMapper.entrySet()) {
            try {
                PropertyDescriptor descriptor = mappedFields.get(entry.getValue());
                if (idFieldName.equals(entry.getKey())) {
                    continue;
                }
                Object value = descriptor.getReadMethod().invoke(entity);
                columns.add(String.format(COLUMN_FORMAT, entry.getKey()));
                wildcards.add("?");
                params.add(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        String sql = format(INSERT_PATTERN, tableName, String.join(", ", columns), String.join(", ", wildcards));

        int update = jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
        return update;
    }

    public int deleteById(long id) {
        String sql = format(DELETE_PATTERN, tableName, conditionBuilder().append(" ").append(ID_CONDITION));
        int update = jdbcTemplate.update(sql, id);
        return update;
    }

    public int deleteByAttr(String attrName, Object attrValue) {
        String sql = format(DELETE_PATTERN, tableName, conditionBuilder().append(format(ATTR_CONDITION, attrName)));
        return jdbcTemplate.update(sql, attrValue);
    }

    public int deleteByAttrs(Map<String, Object> attrs) {
        StringBuilder conditionBuilder = conditionBuilder();
        List<String> conditions = new ArrayList<String>(attrs.size());
        List<Object> params = new ArrayList<Object>(attrs.size());

        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            if (mappedFields.containsKey(entry.getKey())) {
                conditions.add(format(ATTR_CONDITION, entry.getKey()));
                params.add(entry.getValue());
            }
        }

        String sql = format(DELETE_PATTERN, tableName, conditionBuilder.append(join(AND_DELIMITER, conditions)).toString());
        return jdbcTemplate.update(sql, params.toArray(new Object[params.size()]));
    }

    public boolean update(T one) {
        Object id = this.getId(one);

        Map<String, Object> attrs = new HashMap<>();

        for (Map.Entry<String, String> entry : fieldColumnMapper.entrySet()) {
            try {
                PropertyDescriptor descriptor = mappedFields.get(entry.getValue());

                Object value = descriptor.getReadMethod().invoke(one);
                attrs.put(entry.getKey(), value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        attrs.remove(idFieldName);

        Map<String, Object> conditionAttrs = new HashMap<>();
        conditionAttrs.put(idFieldName, id);

        return updateAttrs(attrs, conditionAttrs);
    }

    public boolean updateAttrs(Map<String, Object> updateAttrs, Map<String, Object> conditionAttrs) {
        int len = updateAttrs.size();
        List<Object> attrValues = new ArrayList<Object>(len);
        List<String> kvs = new ArrayList<String>(len);
        for (String attr : updateAttrs.keySet()) {
            kvs.add(attr + " = ?");
            attrValues.add(updateAttrs.get(attr));
        }

        List<Object> params = new ArrayList<Object>(conditionAttrs.size());

        StringBuilder updatesBuilder = conditionBuilder();
        List<String> updates = new ArrayList<String>(updateAttrs.size());
        for (Map.Entry<String, Object> entry : updateAttrs.entrySet()) {
            if (mappedFields.containsKey(entry.getKey())) {
                updates.add(format(ATTR_CONDITION, entry.getKey()));
                params.add(entry.getValue());
            }
        }
        updatesBuilder.append(join(AND_DELIMITER, updates));

        StringBuilder conditionBuilder = conditionBuilder();
        List<String> conditions = new ArrayList<String>(conditionAttrs.size());
        for (Map.Entry<String, Object> entry : conditionAttrs.entrySet()) {
            if (mappedFields.containsKey(entry.getKey())) {
                conditions.add(format(ATTR_CONDITION, entry.getKey()));
                params.add(entry.getValue());
            }
        }
        conditionBuilder.append(join(AND_DELIMITER, conditions));

        String sql = format(UPDATE_PATTERN, tableName, updatesBuilder.toString(), conditionBuilder.toString());

        int ret = jdbcTemplate.update(sql, attrValues.toArray(new Object[params.size()]));
        return ret > 0;
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

    private Object getId(T one) {
        try {
            Object value = mappedFields.get(idFieldName).getReadMethod().invoke(one);
            return value;
        } catch (Exception e) {
            String error = String.format("get bo id of class %s failed", objectClass.getName());
            throw new RuntimeException(error);
        }
    }
}
