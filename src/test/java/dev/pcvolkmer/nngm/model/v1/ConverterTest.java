package dev.pcvolkmer.nngm.model.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationModule;

import java.text.SimpleDateFormat;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterTest {

    public static final XmlMapper XML_MAPPER =
            XmlMapper.builder()
                    .defaultUseWrapper(false)
                    .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                    .addModule(new JakartaXmlBindAnnotationModule())
                    .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .changeDefaultPropertyInclusion(
                            incl -> incl.withValueInclusion(JsonInclude.Include.NON_EMPTY))
                    .build();

    @Test
    void testSerialization() {
        final var model = new DataType();
        model.identifyingData = new IdentifyingDataType();
        model.identifyingData.patient = new IdentifyingPatientType();
        model.identifyingData.patient.vorname = "Patrick";
        model.identifyingData.patient.nachname = "Tester";

        final var actual = Converter.toXmlString(model);
        assertThat(actual).isNotNull();
    }

    @Test
    void testDeserialization() throws Exception {
        final var xml = new String(
                Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("test.xml")).readAllBytes()
        );

        final var actual = Converter.fromXmlString(xml);
        assertThat(actual).isNotNull().satisfies(model -> {
            assertThat(model.identifyingData.patient.vorname).isEqualTo("Patrick");
            assertThat(model.identifyingData.patient.nachname).isEqualTo("Tester");
        });
    }

}
