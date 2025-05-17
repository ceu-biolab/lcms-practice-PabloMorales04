package lipid;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class AdductDetectionTest {
    @Test
    public void shouldDetectAdductBasedOnMzDifference() {
        // Assume real M = (700.500 - 1.0073) = 699.492724
        // So [M+2H]2+ = (M + 22.989218) = 722.482 With the internet formula
        // So [M+2H]2+ = M + 22.989218 = 722.482 With the original formula
        // Given two peaks with ~21.98 Da difference (e.g., [M+H]+ and [M+Na]+)
        Peak mH = new Peak(700.500, 100000.0); // [M+H]+
        Peak mNa = new Peak(722.482 , 80000.0);  // [M+Na]+
        Lipid lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", "PC", 34, 1);

        //double annotationMZ = 700.500d;
        double annotationMZ = 700.499999d;
        double annotationIntensity = 80000.0;
        double annotationRT = 6.5d;
        Annotation annotation = new Annotation(lipid, annotationMZ, annotationIntensity, annotationRT, IoniationMode.POSITIVE, Set.of(mH, mNa));
        //When
        annotation.detectAdductByPairComparison(0.001);

        // Then
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }

    @Test
    public void shouldDetectLossOfWaterAdduct() {
        Peak mh = new Peak(700.500, 90000.0);        // [M+H]+
        Peak mhH2O = new Peak(682.4894, 70000.0);     // [M+H–H₂O]+, ~18.0106 Da less

        Lipid lipid = new Lipid(1, "PE 36:2", "C41H78NO8P", "PE", 36, 2);
        Annotation annotation = new Annotation(lipid, mh.getMz(), mh.getIntensity(), 7.5d, IoniationMode.POSITIVE,  Set.of(mh, mhH2O));


        //When
        annotation.detectAdductByPairComparison(0.001);
        //Then
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }

    @Test
    public void shouldDetectSinglyChargedAdduct() { //Añadido, ya que no era coherente el titulo shouldDetectDoublyChargedAdduct con lo que realizaba
        // Assume real M = (700.500 - 1.0073) = 699.492724
        // So [M+2H]2+ = (M + 2.0146) / 2 = 350.7536 With the internet formula
        // So [M+2H]2+ = (M / 2) + 2.0146 = 351.760914 With the original formula
        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.7536, 85000.0);   // [M+2H]2+

        Lipid lipid = new Lipid(3,  "TG 54:3", "C57H104O6", "TG", 54, 3);
        Annotation annotation = new Annotation(lipid, singlyCharged.getMz(), singlyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));
        System.out.println("Mz" + annotation.getMz());
        annotation.detectAdductByPairComparison(0.001);

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }
    @Test
    public void shouldDetectDoublyChargedAdduct() {
        // Assume real M = (700.500 - 1.0073) = 699.4927
        // So [M+2H]2+ = (M + 2.0146) / 2 = 350.7536 With the internet formula
        // So [M+2H]2+ = (M / 2) + 2.0146 = 351.760914 With the original formula
        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.7536, 85000.0);   // [M+2H]2+

        Lipid lipid = new Lipid(3, "TG 54:3", "C57H104O6", "TG", 54, 3);
        Annotation annotation = new Annotation(lipid, doublyCharged.getMz(), doublyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));
        annotation.detectAdductByPairComparison(0.001);

        assertNotNull("[M+2H]2+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+2H]2+", annotation.getAdduct());
    }
}