package com.baidu.oped.apm;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.CaseFormat;

/**
 * 和mysql交互基本Dao
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
        this.entityClass = null;
        Class c = getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<T>) p[0];
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
        sql.append(StringUtils.collectionToDelimitedString(kvs, ","));
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
        sql.append(StringUtils.collectionToDelimitedString(kvs, " and "));
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
        String fieldStr = StringUtils.collectionToDelimitedString(fields, ",");
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
        sql.append(StringUtils.collectionToDelimitedString(kvs, "and"));
        jdbcTemplate.update(sql.toString(), attrVals.toArray());
    }

    /*public <T extends POJO> boolean update() {
        if (getId() <= 0) {
            return false;
        }

        Map<String, Object> dbMsgs = this.listInsertableFields();
        dbMsgs.remove("id");

        return this.updateAttrs(dbMsgs);
    }

    public <T extends POJO> boolean update(T toUpdateObj) {
        if (getId() <= 0 || !getClass().equals(toUpdateObj.getClass())) {
            return false;
        }

        Map<String, Object> dbMsgs = this.listInsertableFields();
        Map<String, Object> toUpdateMsgs = toUpdateObj.listInsertableFields();
        if (toUpdateMsgs.containsKey("id")) { // 去除主键
            toUpdateMsgs.remove("id");
        }

        Map<String, Object> attrs = new HashMap<String, Object>();
        for (String field : toUpdateMsgs.keySet()) {
            Object dbMsgValue = dbMsgs.get(field);
            Object toUpdateValue = toUpdateMsgs.get(field);
            if (ObjectUtil.equals(dbMsgValue, toUpdateValue)) {
                continue;
            }
            attrs.put(field, toUpdateValue);
        }

        return this.updateAttrs(attrs);
    }

    public boolean updateAttrs(Map<String, Object> updateAttrs) {
        if (updateAttrs == null || updateAttrs.size() == 0) {
            return true;
        }
        int len = updateAttrs.size();
        List<Object> attrValues = new ArrayList<Object>(len);
        List<String> kvs = new ArrayList<String>(len);
        for (String attr : updateAttrs.keySet()) {
            kvs.add(attr + " = ?");
            attrValues.add(updateAttrs.get(attr));
        }

        String sql = "update " + tableName() + " set " + StringUtils.join(kvs.toArray(), ",") + " where id = ?";
        attrValues.add(id);

        int ret = getQueryHelper().update(sql, attrValues.toArray());
        try {
            if (ret > 0) {
                for (String fieldName : updateAttrs.keySet()) {
                    BeanUtils.setProperty(this, fieldName, updateAttrs.get(fieldName));
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean updateAttrs(Map<String, Object> updateAttrs, String conditionSql, Object... conditionParams) {
        int len = updateAttrs.size();
        List<Object> attrValues = new ArrayList<Object>(len);
        List<String> kvs = new ArrayList<String>(len);
        for (String attr : updateAttrs.keySet()) {
            kvs.add(attr + " = ?");
            attrValues.add(updateAttrs.get(attr));
        }

        String sql =
                "update " + tableName() + " set " + StringUtils.join(kvs.toArray(), ",") + " where " + conditionSql;
        for (Object param : conditionParams) {
            attrValues.add(param);
        }

        int ret = getQueryHelper().update(sql, attrValues.toArray());
        try {
            if (ret > 0) {
                for (String fieldName : updateAttrs.keySet()) {
                    BeanUtils.setProperty(this, fieldName, updateAttrs.get(fieldName));
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean updateAttrs(Map<String, Object> updateAttrs, String conditionAttrName, Object conditonValue) {
        int len = updateAttrs.size();
        List<Object> attrValues = new ArrayList<Object>(len);
        List<String> kvs = new ArrayList<String>(len);
        for (String attr : updateAttrs.keySet()) {
            kvs.add(attr + " = ?");
            attrValues.add(updateAttrs.get(attr));
        }

        String sql =
                "update " + tableName() + " set " + StringUtils.join(kvs.toArray(), ",") + " where "
                        + conditionAttrName + " = ?";
        attrValues.add(conditonValue);

        int ret = getQueryHelper().update(sql, attrValues.toArray());
        try {
            if (ret > 0) {
                for (String fieldName : updateAttrs.keySet()) {
                    BeanUtils.setProperty(this, fieldName, updateAttrs.get(fieldName));
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }*/

    public void batchSave(List<T> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return;
        }

        Map<String, String> pojoBean = this.listInsertableFields(instances.get(0));
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
                Map<String, String> fieldValues = this.listInsertableFields(instances.get(i + j));
                for (int k = 0; k < fieldNum; k++) {
                    params[j * fieldNum + k] = fieldValues.get(fields[k]);
                }
            }

            jdbcTemplate.update(sql.toString(), params);
        }
    }

    public long save(T one) {
        Map<String, String> pojo_bean = this.listInsertableFields(one);
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


    private Map<String, String> listInsertableFields(T one) {
        try {
            Map<String, String> props = BeanUtils.describe(one);
            if (getId(one) <= 0) {
                props.remove("id");
            }
            props.remove("class");
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Exception when Fetching fields of " + this);
        }
    }

    private long getId(T one) {
        return 0L;
    }

}