package com.baidu.oped.apm;

import com.baidu.oped.apm.common.annotation.Table;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public static final String INSERT_PATTERN = "INSERT into %s (%s ) values (%s );";
    public static final String DELETE_PATTERN = "DELETE FROM %s %s";
    public static final String UPDATE_PATTERN = "UPDATE %s SET %s %s";
    public static final String QUERY_PATTERN = "SELECT %s FROM %s %s";
    public static final String WIlDCARD = "*";
    public static final String EMPTY_CONDITION = "";
    public static final String ATTR_CONDITION = "`%s` = ?";
    public static final String CLASS_FIELD_NAME = "class";
    public static final String DEFAULT_FIELD_NAME = "id";
    public static final String CONDITION_HEADER = "WHERE ";
    public static final String UPDATE_HEADER = "UPDATE ";
    public static final String AND_DELIMITER = " AND ";
    public static final String COLUMN_FORMAT = "`%s`";
    private static final Logger LOG = LoggerFactory.getLogger(BaseRepository.class);
    private final Class<T> objectClass;
    private final String tableName;
    private final String idFieldName;
    private final Map<String, String> fieldColumnMapper = new HashMap<>();
    private final Map<String, PropertyDescriptor> mappedFields = new HashMap<>();

    @Autowired
    protected JdbcTemplate jdbcTemplate;

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
        if (!StringUtils.isEmpty(name)) {
            this.tableName = name;
        } else {
            String className = objectClass.getName();
            this.tableName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,
                                                              className.substring(className.lastIndexOf(".") + 1));
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

    private static StringBuilder updateBuilder() {
        return new StringBuilder(UPDATE_HEADER);
    }

    public T findOneById(long id) {
        return findOneByAttr(idFieldName, id);
    }

    public T findOneByAttr(String attrName, Object attrValue) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(attrName, attrValue);
        return findOneByAttrs(attrs);
    }

    public T findOneByAttrs(Map<String, Object> attrs) {
        StringBuilder conditionBuilder = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (attrs != null) {
            List<String> conditions = new ArrayList<>(attrs.size());

            for (Map.Entry<String, Object> entry : attrs.entrySet()) {
                processConditions(conditions, params, entry);
            }
            conditionBuilder.append(CONDITION_HEADER);
            conditionBuilder.append(join(AND_DELIMITER, conditions));
        }
        String sql = format(QUERY_PATTERN, WIlDCARD, tableName, conditionBuilder.toString());
        try {
            return jdbcTemplate.queryForObject(sql, this, params.toArray(new Object[params.size()]));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void processConditions(List<String> conditions, List<Object> params, Map.Entry<String, Object> entry) {
        String key = entry.getKey();
        if (mappedFields.containsKey(key)) {
            conditions.add(format(ATTR_CONDITION, toDatabaseName(key)));
            params.add(entry.getValue());
        }
    }

    public List<T> findByAttr(String attrName, Object attrValue) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(attrName, attrValue);
        return findByAttrs(attrs);
    }

    public List<T> find(String conditionSql, Object... values) {
        if (StringUtils.isEmpty(conditionSql)) {
            throw new UnsupportedOperationException("conditionSql can't be empty");
        }

        StringBuilder conditionBuilder = new StringBuilder(CONDITION_HEADER).append(conditionSql);

        String sql = format(QUERY_PATTERN, WIlDCARD, tableName, conditionBuilder.toString());

        System.out.println("sql = " + sql);
        System.out.println("values:");
        for (Object one :  values) {
            System.out.println("one : " + one.toString());
        }
        return jdbcTemplate.query(sql, this, values);
    }

    public List<T> findByAttrs(Map<String, Object> attrs) {
        return findByAttrs(WIlDCARD, attrs);
    }

    public List<T> findByAttrs(String select, Map<String, Object> attrs) {
        List<Object> params = new ArrayList<>();

        StringBuilder conditionBuilder = new StringBuilder();
        if (attrs != null) {
            List<String> conditions = new ArrayList<>();
            for (Map.Entry<String, Object> entry : attrs.entrySet()) {
                String key = entry.getKey();
                if (mappedFields.containsKey(key)) {
                    conditions.add(format(ATTR_CONDITION, toDatabaseName(key)));
                    params.add(entry.getValue());
                }
            }
            conditionBuilder.append(CONDITION_HEADER);
            conditionBuilder.append(join(AND_DELIMITER, conditions)).toString();
        }
        String sql = format(QUERY_PATTERN, select, tableName, conditionBuilder.toString());

        return jdbcTemplate.query(sql, this, params.toArray());
    }

    public List<T> findBy(String select, String condition, List<Object> params){
        if(StringUtils.isEmpty(select)){
            select = WIlDCARD;
        }
        if(!StringUtils.isEmpty(condition) && !StringUtils.startsWithIgnoreCase(condition, CONDITION_HEADER)){
            condition = format(CONDITION_HEADER.concat("%s"), condition);
        }

        String sql = format(QUERY_PATTERN, select, tableName, condition);
        return jdbcTemplate.query(sql, this, params.toArray());
    }

    public List<T> findAll() {
        String sql = format(QUERY_PATTERN, WIlDCARD, tableName, EMPTY_CONDITION);
        return jdbcTemplate.query(sql, this);
    }

    public int save(T entity) {
        List<String> columns = new ArrayList<>(fieldColumnMapper.size());
        List<String> wildcards = new ArrayList<>(fieldColumnMapper.size());
        List<Object> params = new ArrayList<>(fieldColumnMapper.size());

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
        return deleteByAttr(idFieldName, id);
    }

    public int deleteByAttr(String attrName, Object attrValue) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(attrName, attrValue);
        return deleteByAttrs(attrs);
    }

    public int deleteByAttrs(Map<String, Object> attrs) {
        if (attrs == null || attrs.isEmpty()) {
            throw new UnsupportedOperationException("delete operation must match some conditions");
        }
        StringBuilder conditionBuilder = new StringBuilder(CONDITION_HEADER);
        List<String> conditions = new ArrayList<>(attrs.size());
        List<Object> params = new ArrayList<>(attrs.size());
        for (Map.Entry<String, Object> entry : attrs.entrySet()) {
            processConditions(conditions, params, entry);
        }

        String sql =
                format(DELETE_PATTERN, tableName, conditionBuilder.append(join(AND_DELIMITER, conditions)).toString());
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
        if (updateAttrs == null || updateAttrs.isEmpty()) {
            throw new UnsupportedOperationException("the needed update params must not be empty ");
        }
        int len = updateAttrs.size();
        List<Object> attrValues = new ArrayList<>(len);
        List<String> kvs = new ArrayList<>(len);
        for (String attr : updateAttrs.keySet()) {
            kvs.add(attr + " = ?");
            attrValues.add(updateAttrs.get(attr));
        }

        List<Object> params = new ArrayList<>();
        StringBuilder updatesBuilder = updateBuilder();
        List<String> updates = new ArrayList<>(updateAttrs.size());
        for (Map.Entry<String, Object> entry : updateAttrs.entrySet()) {
            processConditions(updates, params, entry);
        }
        updatesBuilder.append(join(AND_DELIMITER, updates));

        StringBuilder conditionBuilder = new StringBuilder();
        if (conditionAttrs != null) {
            List<String> conditions = new ArrayList<>(conditionAttrs.size());
            for (Map.Entry<String, Object> entry : conditionAttrs.entrySet()) {
                processConditions(conditions, params, entry);
            }
            conditionBuilder.append(UPDATE_HEADER);
            conditionBuilder.append(join(AND_DELIMITER, conditions));
        }
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

    private String toDatabaseName(String fieldName) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
    }
}
