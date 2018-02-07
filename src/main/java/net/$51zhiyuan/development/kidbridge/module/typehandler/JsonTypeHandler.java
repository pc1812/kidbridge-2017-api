package net.$51zhiyuan.development.kidbridge.module.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 自定义mybatis json格式数据类型转换
 */
public class JsonTypeHandler extends BaseTypeHandler {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        try {
            if (o != null) {
                // json数据序列化
                String value = this.mapper.writeValueAsString(o);
                preparedStatement.setString(i, ((value.equals("{}") || value.equals("[]")) ? "" : value));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Object o = null;
        String value = resultSet.getString(s);

        try {
            if(!StringUtils.isBlank(value)){
                // json数据反序列化
                o = value.indexOf("[") == 0 ? this.mapper.readValue(value, List.class) : this.mapper.readValue(value, Map.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) {
        return null;
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) {
        return null;
    }
}
