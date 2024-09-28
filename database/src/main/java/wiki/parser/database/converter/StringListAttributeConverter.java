package wiki.parser.database.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import wiki.parser.core.util.StringList;

//Converters do not work with custom db types because the converter standard types usually convert to standard db types
@Converter
public class StringListAttributeConverter implements AttributeConverter<StringList,String> {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(StringList attribute) {
        try {
            return MAPPER.writeValueAsString(attribute);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert StringList to Json string: " + ex.getMessage(), ex);
        }
    }

    @Override
    public StringList convertToEntityAttribute(String dbData) {
        try {
            return MAPPER.readValue(dbData, StringList.class);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to StringList: " + ex.getMessage(), ex);
        }

    }
}
