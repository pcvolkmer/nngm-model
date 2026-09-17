package dev.pcvolkmer.nngm.model.v1;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterTest {

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
            assertThat(model.identifyingData).satisfies(identifyingDataType -> {
                assertThat(identifyingDataType.patient.vorname).isEqualTo("Patrick");
                assertThat(identifyingDataType.patient.nachname).isEqualTo("Tester");
            });
            assertThat(model.medicalData).satisfies(medicalData -> {
                assertThat(medicalData.nngmCase).hasSize(1);
                assertThat(medicalData.nngmCase.getFirst()).satisfies(nngmCaseType -> {
                    assertThat(nngmCaseType.nngmCaseId).isEqualTo("012345");
                    assertThat(nngmCaseType.ecog.ecogGroup.ecog).isEqualTo(EcogEnumType.LA_9625_0);
                });
            });
        });
    }

}
