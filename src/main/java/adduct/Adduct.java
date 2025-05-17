package adduct;

import lipid.IoniationMode;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adduct {
    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass    Mass measured by MS
     * @param theoreticalMass Theoretical mass of the compound
     */
    public static int calculatePPMIncrement(Double experimentalMass, Double theoreticalMass) {
        int ppmIncrement;
        ppmIncrement = (int) Math.round(Math.abs((experimentalMass - theoreticalMass) * 1000000
                / theoreticalMass));
        return ppmIncrement;
    }

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass    Mass measured by MS
     * @param ppm ppm of tolerance
     */
    public static double calculateDeltaPPM(Double experimentalMass, int ppm) {
        double deltaPPM;
        deltaPPM =  Math.round(Math.abs((experimentalMass * ppm) / 1000000));
        return deltaPPM;

    }



    public static int getMultimerFromAdduct(String adduct) {
        Pattern pattern = Pattern.compile("\\[(\\d*)M[+-][^\\]]+\\](\\d*)?([+-])");
        Matcher matcher = pattern.matcher(adduct);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de aducto inválido: " + adduct);
        }

        String multimer = matcher.group(1);
        int result;
        if (multimer.isEmpty()) {
            result = 1;
        } else {
            result = Integer.parseInt(multimer);
        }

        return result;
    }

    public static int getChargeFromAdduct(String adduct) {
        Pattern pattern = Pattern.compile("\\[(\\d*)M[+-][^\\]]+\\](\\d*)?([+-])");
        Matcher matcher = pattern.matcher(adduct);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de aducto inválido: " + adduct);
        }

        String chargeStr = matcher.group(2);
        int charge;
        if (chargeStr.isEmpty()) {
            charge = 1;
        } else {
            charge = Integer.parseInt(chargeStr);
        }

        return charge;
    }
}
