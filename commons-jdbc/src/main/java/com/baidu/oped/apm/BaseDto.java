package com.baidu.oped.apm;


import com.google.common.base.CaseFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 和mysql交互基本Dao <code>
 * 要求：
 * 1. 所有的Dto继承于BaseDao
 * 2. 对应的mysql-bo主键必须为id(Long)
 * </code>
 *
 * @author yangbolin
 */
public abstract class BaseDto<T> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Class<T> entityClass;
    private Field idField;

    // 构造方法，根据实例类自动获取实体类类型
    @SuppressWarnings({"rawtypes", "unchecked"})
    public BaseDto() {
        try {
            this.entityClass = null;
            Class c = getClass();
            Type t = c.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] p = ((ParameterizedType) t).getActualTypeArguments();
                this.entityClass = (Class<T>) p[0];
            }
        } catch (Exception e) {
            throw new RuntimeException("parse bo class type fail", e);
        }

        try {
            idField = entityClass.getDeclaredField("id");
            idField.setAccessible(true);
        } catch (Exception e) {
            String error = String.format("bo class %s missing field id", entityClass.getName());
            throw new RuntimeException(error, e);
        }
    }

    protected abstract String tableName();

    public T get(long id) {
        String sql = "SELECT * FROM " + tableName() + " WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, entityClass, id);
    }

    public T get(String attrName, Object attrValue) {
        String sql = "SELECT * FROM " + tableName() + " WHERE " + attrName + " = ?";
        return jdbcTemplate.queryForObject(sql, entityClass, attrValue);
    }

    public T get(Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            return null;
        }
        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName() + " WHERE ");
        int size = attrs.size();
        List<String> kvs = new ArrayList<String>(size);
        List<Object> attrVals = new ArrayList<Object>(size);
        for (String attrName : attrs.keySet()) {
            kvs.add(attrName + " = ?");
            attrVals.add(attrs.get(attrName));
        }
        sql.append(StringUtils.arrayToDelimitedString(kvs.toArray(), ","));
        return jdbcTemplate.queryForObject(sql.toString(), attrVals.toArray(), entityClass);
    }

    public T get(String conditionSql, Object... attrValues) {
        String sql = "SELECT * FROM " + tableName() + " WHERE " + conditionSql;
        return jdbcTemplate.queryForObject(sql, entityClass, attrValues);
    }

    public List<T> batchGetWithInCondition(String attrName, Collection<?> inValues) {
        if (CollectionUtils.isEmpty(inValues)) {
            return new ArrayList<T>();
        }

        List beans = new ArrayList();
        List values = new ArrayList(inValues);
        int limit = 1000;
        for (int i = 0; i < values.size(); i += limit) {
            int endkey = i + limit;
            if (i + limit >= values.size()) {
                endkey = values.size();
            }
            List subValues = values.subList(i, endkey);
            StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName() + " WHERE " + attrName + " IN (");
            for (int j = 1; j <= subValues.size(); j++) {
                sql.append('?');
                if (j < subValues.size())
                    sql.append(',');
            }
            sql.append(')');
            beans.addAll(jdbcTemplate.queryForList(sql.toString(), subValues.toArray(), entityClass));
        }
        return beans;
    }

    public List<T> batchGet(Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            return null;
        }
        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName() + " WHERE ");
        int size = attrs.size();
        List<String> kvs = new ArrayList<String>(size);
        List<Object> attrVals = new ArrayList<Object>(size);
        for (String attrName : attrs.keySet()) {
            kvs.add(attrName + " = ?");
            attrVals.add(attrs.get(attrName));
        }
        sql.append(StringUtils.arrayToDelimitedString(kvs.toArray(), " and "));
        return jdbcTemplate.queryForList(sql.toString(), attrVals.toArray(), entityClass);
    }

    public List<T> batchGet(String condtionSql, Object... params) {
        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName());
        if (!StringUtils.isEmpty(condtionSql.trim())) {
            sql.append(" WHERE " + condtionSql);
        }
        return jdbcTemplate.queryForList(sql.toString(), entityClass, params);
    }

    public List<T> batchGet(Collection<String> fields, String condtionSql, Object... params) {
        String fieldStr = StringUtils.arrayToDelimitedString(fields.toArray(), ",");
        StringBuffer sql = new StringBuffer("SELECT " + fieldStr + " FROM " + tableName() + " WHERE " + condtionSql);
        return jdbcTemplate.queryForList(sql.toString(), entityClass, params);
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM " + tableName() + " WHERE id = ?", id);
    }

    public void delete(String attrName, Object attrValue) {
        jdbcTemplate.update("DELETE FROM " + tableName() + " WHERE " + attrName + " = ?", attrValue);
    }

    public void delete(String condtionSql, Object... params) {
        jdbcTemplate.update("DELETE FROM " + tableName() + " WHERE " + condtionSql, params);
    }

    public void deleteWithInCondition(String attrName, Collection<?> inValues) {
        if (inValues == null || inValues.size() == 0) {
            return;
        }
        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName() + " WHERE " + attrName + " IN (");
        for (int i = 1; i <= inValues.size(); i++) {
            sql.append('?');
            if (i < inValues.size())
                sql.append(',');
        }
        sql.append(')');
        jdbcTemplate.update(sql.toString(), inValues.toArray());
    }

    public void delete(Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            return;
        }
        StringBuffer sql = new StringBuffer("DELETE FROM " + tableName() + " WHERE ");
        int size = attrs.size();
        List<Object> attrVals = new ArrayList<Object>(size);
        List<String> kvs = new ArrayList<String>(size);
        for (String attrName : attrs.keySet()) {
            kvs.add(attrName + " = ?");
            attrVals.add(attrs.get(attrName));
        }
        sql.append(StringUtils.arrayToDelimitedString(kvs.toArray(), "and"));
        jdbcTemplate.update(sql.toString(), attrVals.toArray());
    }

    public boolean update(T one) {
        long id = this.getId(one);
        if (id <= 0) {
            return false;
        }

        Map<String, String> toUpdateMsgs = this.listFieldValues(one);
        toUpdateMsgs.remove("id");

        return this.updateAttrs(toUpdateMsgs, id);
    }

    public boolean updateAttrs(Map<String, String> updateAttrs, long id) {
        if (id <= 0) {
            return false;
        }
        if (updateAttrs == null || updateAttrs.size() == 0) {
            return true;
        }

        return updateAttrs(updateAttrs, "id=?", id);
    }

    public boolean updateAttrs(Map<String, String> updateAttrs, String conditionSql, Object... conditionParams) {
        int len = updateAttrs.size();
        List<Object> attrValues = new ArrayList<Object>(len);
        List<String> kvs = new ArrayList<String>(len);
        for (String attr : updateAttrs.keySet()) {
            kvs.add(attr + " = ?");
            attrValues.add(updateAttrs.get(attr));
        }

        String sql =
                "update " + tableName() + " set " + StringUtils.arrayToDelimitedString(kvs.toArray(), ",") + " where " + conditionSql;
        for (Object param : conditionParams) {
            attrValues.add(param);
        }

        int ret = jdbcTemplate.update(sql, attrValues.toArray());
        return ret > 0 ? true : false;
    }

    public void batchSave(List<T> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }

        Map<String, String> pojoBean = this.listFieldValues(instances.get(0));
        String[] fields = pojoBean.keySet().toArray(new String[pojoBean.size()]);
        int fieldNum = fields.length;

        StringBuilder fieldsSql = new StringBuilder("INSERT INTO ");
        fieldsSql.append(this.tableName());
        fieldsSql.append('(');
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                fieldsSql.append(',');
            }
            fieldsSql.append(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fields[i]));
        }
        fieldsSql.append(") VALUES ");

        StringBuffer whereSql = new StringBuffer("(");
        for (int j = 0; j < fieldNum; j++) {
            if (j > 0) {
                whereSql.append(',');
            }
            whereSql.append('?');
        }
        whereSql.append(')');

        int len = instances.size();
        int step = 200;
        for (int i = 0; i < len; i = i + step) {
            int max = i + step;
            if (max > len) {
                max = len;
            }
            StringBuffer sql = new StringBuffer();
            sql.append(fieldsSql);
            Object[] params = new Object[(max - i) * fieldNum];

            for (int j = 0; j < max - i; j++) {
                if (j > 0) {
                    sql.append(",");
                }
                sql.append(whereSql);
                Map<String, String> fieldValues = this.listFieldValues(instances.get(i + j));
                for (int k = 0; k < fieldNum; k++) {
                    params[j * fieldNum + k] = fieldValues.get(fields[k]);
                }
            }

            jdbcTemplate.update(sql.toString(), params);
        }
    }

    public long save(T one) {
        Map<String, String> pojo_bean = this.listFieldValues(one);
        String[] fields = pojo_bean.keySet().toArray(new String[pojo_bean.size()]);
        Object[] params = new Object[pojo_bean.size()];
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(this.tableName());
        sql.append('(');
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sql.append(',');
            }
            sql.append(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fields[i]));
            params[i] = pojo_bean.get(fields[i]);
        }
        sql.append(") VALUES (");
        for (int j = 0; j < fields.length; j++) {
            if (j > 0)
                sql.append(',');
            sql.append('?');
        }
        sql.append(')');
        System.out.println("sql = " + sql.toString());
        jdbcTemplate.update(sql.toString(), params);
        return jdbcTemplate.queryForObject("select LAST_INSERT_ID() as id", Long.class);
    }

    private Map<String, String> listFieldValues(T one) {
        try {
            Map<String, String> props = BeanUtils.describe(one);
            if (getId(one) <= 0L) {
                props.remove("id");
            }
            props.remove("class");
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Exception when Fetching fields of " + this);
        }
    }

    private long getId(T one) {
        try {
            long value = Long.valueOf(idField.getLong(one));
            return value;
        } catch (Exception e) {
            String error = String.format("bo class %s get key id value fail", entityClass.getName());
            throw new RuntimeException(error);
        }
    }

}