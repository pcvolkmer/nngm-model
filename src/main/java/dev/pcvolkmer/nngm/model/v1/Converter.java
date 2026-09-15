package dev.pcvolkmer.nngm.model.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import java.text.SimpleDateFormat;

public class Converter {

    private static final XmlMapper XML_MAPPER =
            XmlMapper.builder()
                    .defaultUseWrapper(false)
                    .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                    .addModule(new JakartaXmlBindAnnotationModule())
                    .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .changeDefaultPropertyInclusion(
                            incl -> incl.withValueInclusion(JsonInclude.Include.NON_EMPTY))
                    .build();

    public static DataType fromXmlString(String json) {
        return XML_MAPPER.readValue(json, DataType.class);
    }

    public static String toXmlString(DataType obj) {
        return XML_MAPPER.writeValueAsString(obj);
    }

}
