package adduct;

import lipid.IoniationMode;

import java.util.Map;

public class MassTransformation {
    // !Done! TODO create functions to transform the mass of the mzs to monoisotopic masses and vice versa.
/*
        // formato de la expresión: [AM+X]Q+/-
        // "A" corresponde a el multimer. Si es un 1, no es un multimer. Si es 2 es un dimer, si es 3 es un trimer.
        // "M" corresponde al compuesto/lípido. que corresponde con la masa indicada
        // "X" define la formula del aducto. Puede ser NH4, COOHCN, H, etc.
        // "Q" corresponde a la carga.
        // "mz"= mass / charge
         */

    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param mz experimentalMass
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return the mass difference within the tolerance respecting to the
     * massToSearch
     */
    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct, IoniationMode ioniationMode) {
        Double massToSearch;
        int A = Adduct.getMultimerFromAdduct(adduct); // "A" corresponde a el multimer. Si es un 1, no es un multimer. Si es 2 es un dimer, si es 3 es un trimer.
        int Q = Adduct.getChargeFromAdduct(adduct); // "Q" corresponde a la carga.

        Map<String, Double> adductMap;
        if(ioniationMode == IoniationMode.POSITIVE) {
            adductMap = AdductList.MAPMZPOSITIVEADDUCTS;
        }
        else {
            adductMap = AdductList.MAPMZNEGATIVEADDUCTS;
        }

        Double adductMass = adductMap.get(adduct);
        if (adductMass == null) {
            throw new IllegalArgumentException("Adduct not found in the list: " + adduct);
        }


        /*
        if Adduct is single charge the formula is M = m/z +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is M =( mz - adductMass ) * charge

        if adduct is a dimer the formula is M =  (mz - adductMass) / numberOfMultimer
        */
        // formato de la expresión: [AM+X]Q+/-

        massToSearch = (mz * Q) + adductMass / A;; //internet formula
        return massToSearch;
    }

    /**
     * Calculate the mz of a monoisotopic mass with the corresponding adduct
     *
     * @param monoisotopicMass
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return
     */
    public static Double getMZFromMonoisotopicMass(Double monoisotopicMass, String adduct, IoniationMode ioniationMode) {
        Double massToSearch;
        int A = Adduct.getMultimerFromAdduct(adduct); // "A" corresponde a el multimer. Si es un 1, no es un multimer. Si es 2 es un dimer, si es 3 es un trimer.
        int Q = Adduct.getChargeFromAdduct(adduct); // "Q" corresponde a la carga.

        Map<String, Double> adductMap;
        if(ioniationMode == IoniationMode.POSITIVE) {
            adductMap = AdductList.MAPMZPOSITIVEADDUCTS;
        }
        else {
            adductMap = AdductList.MAPMZNEGATIVEADDUCTS;
        }

        Double adductMass = adductMap.get(adduct);
        if (adductMass == null) {
            throw new IllegalArgumentException("Adduct not found in the list: " + adduct);
        }

 /*
        if Adduct is single charge the formula is m/z = M +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is mz = M/charge +- adductMass

        if adduct is a dimer or multimer the formula is mz = M * numberOfMultimer +- adductMass

        return monoisotopicMass;

         */

        // formato de la expresión: [AM+X]Q+/-

        massToSearch = (monoisotopicMass * A) - adductMass / Q;  //Internet formula
        return massToSearch;
        // !Done! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).

    }
}