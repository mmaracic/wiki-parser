package wiki.parser.database.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;
import wiki.parser.core.util.StringList;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//For custom db types to use in Java we need userType which is registered using @TypeRegistration as on WikiIndexEntity
//Using db custom types might even to use generic SqlTypes.OTHER type in case: Unsupported Types value: 3,001
//This is because prepared statement object does not support custom SqlTypes and relies upon generic OTHER to map custom type
public class StringListType implements UserType<StringList> {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public int getSqlType() {
        // Can not be SqlTypes.Json because PgPreparedStatement does not support it
        return SqlTypes.OTHER;
    }

    @Override
    public Class<StringList> returnedClass() {
        return StringList.class;
    }

    @Override
    public boolean equals(StringList x, StringList y) {
        return x.content().equals(y.content());
    }

    @Override
    public int hashCode(StringList x) {
        return x.content().hashCode();
    }

    @Override
    public StringList nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        final String cellContent = rs.getString(position);
        if (cellContent == null) {
            return null;
        }
        try {
            return MAPPER.readValue(cellContent.getBytes(StandardCharsets.UTF_8), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to StringList: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, StringList value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, getSqlType());
            return;
        }
        try {
            String s = MAPPER.writeValueAsString(value);
            st.setObject(index, s, getSqlType());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert StringList to Json string: " + ex.getMessage(), ex);
        }
    }

    @Override
    public StringList deepCopy(StringList value) {
        return new StringList(value.content());
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(StringList value) {
        try {
            return MAPPER.writeValueAsString(value.content());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deconstructing StringList to json", e);
        }
    }

    @Override
    public StringList assemble(Serializable cached, Object owner) {
        try {
            return MAPPER.readValue(cached.toString(), StringList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reconstructing StringList from json", e);
        }
    }
}
