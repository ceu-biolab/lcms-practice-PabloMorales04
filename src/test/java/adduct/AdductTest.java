package adduct;

import lipid.IoniationMode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdductTest {

    @Test
    public void testGetMonoisotopicMassFromMZ_positive() {
        double mz = 760.585;
        String adduct = "[M+H]+";
        IoniationMode ionMode = IoniationMode.POSITIVE;
        double expected = (mz - 1.007276d); // masa aproximada
        double result = MassTransformation.getMonoisotopicMassFromMZ(mz, adduct, ionMode);

        assertEquals(expected, result, 0.01); // tolerancia 0.01 Da
    }
    @Test
    public void testGetMonoisotopicMassFromMZ_negative() {
        double mz = 760.585;
        String adduct = "[M-2H]2-";
        IoniationMode ionMode = IoniationMode.NEGATIVE;
        double expected = (mz * 2) + 1.007276d; // masa aproximada
        double result = MassTransformation.getMonoisotopicMassFromMZ(mz, adduct, ionMode);

        assertEquals(expected, result, 0.01); // tolerancia 0.01 Da
    }

    @Test
    public void testGetMonoisotopicMassFromMZ_doubleCharge() {
        double mz = 400.292;
        String adduct = "[M+2H]2+";
        IoniationMode ionMode = IoniationMode.POSITIVE;
        double expected = (mz * 2) - 2.014552d; // resultado debe ser cercano a M
        double result = MassTransformation.getMonoisotopicMassFromMZ(mz, adduct, ionMode);

        assertEquals(expected, result, 0.01);
    }

    @Test
    public void testGetMonoisotopicMassFromMZ_dimer() {
        double mz = 400.292;
        String adduct = "[2M-H]-";
        IoniationMode ionMode = IoniationMode.NEGATIVE;
        double expected = mz + 1.007276d / 2; // resultado debe ser cercano a M
        double result = MassTransformation.getMonoisotopicMassFromMZ(mz, adduct, ionMode);

        assertEquals(expected, result, 0.01);
    }

    @Test
    public void testInvalidAdductFormat() {
        String adduct = "[AM+X]Q+";
        IoniationMode ionMode = IoniationMode.POSITIVE;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MassTransformation.getMonoisotopicMassFromMZ(700.0, adduct, ionMode);
        });

        assertTrue(exception.getMessage().contains("inválido"));
    }

    @Test
    public void testUnknownAdduct() {
        String adduct = "[M+CC]+";
        IoniationMode ionMode = IoniationMode.POSITIVE;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MassTransformation.getMonoisotopicMassFromMZ(700.0, adduct, ionMode);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }
}