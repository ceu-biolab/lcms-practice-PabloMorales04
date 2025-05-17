package lipid;

import java.util.*;
import adduct.*;
public class Annotation {

    private final Lipid lipid;
    private final double mz;
    private final double intensity;
    private final double rtMin;
    private final IoniationMode ionizationMode;
    private String adduct; // !Done!TODO The adduct will be detected based on the groupedSignals
    private final Set<Peak> groupedSignals;
    private int score = 0;
    private int totalScoresApplied;

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode) {
        this(lipid, mz, intensity, retentionTime, ionizationMode, Collections.emptySet());
    }

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     * @param groupedSignals
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode, Set<Peak> groupedSignals) {
        this.lipid = lipid;
        this.mz = mz;
        this.rtMin = retentionTime;
        this.intensity = intensity;
        this.ionizationMode = ionizationMode;
        this.score = 0;
        this.totalScoresApplied = 0;

        // !Done!TODO This set should be sorted according to help the program to deisotope the signals plus detect the adduct
        this.groupedSignals = new TreeSet<>(groupedSignals);
    }

    public void detectAdductByPairComparison(double tolerance) {
        Map<String, Double> adductMap;
        System.out.println(this.mz);
        if (ionizationMode == IoniationMode.POSITIVE) {
            adductMap = AdductList.MAPMZPOSITIVEADDUCTS;
        } else {
            adductMap = AdductList.MAPMZNEGATIVEADDUCTS;
        }

        List<Peak> peaks = new ArrayList<>(groupedSignals);
        double bestDiff = Double.MAX_VALUE;
        String bestAdduct = "Unknown";
        System.out.println("Ordered peaks (mz values): ");
        for (Peak peak : groupedSignals) {
            System.out.println(peak.getMz());
        }

        // we compare all peaks between them
        for (int i = 0; i < peaks.size(); i++) {
            for (int j = i + 1; j < peaks.size(); j++) {
                Peak p1 = peaks.get(i);
                Peak p2 = peaks.get(j);

                // We try all possible combinations of adducts in a pair of peaks
                for (String ad1 : adductMap.keySet()) {
                    double m1 = MassTransformation.getMonoisotopicMassFromMZ(p1.getMz(), ad1, this.getIonizationMode());
                    for (String ad2 : adductMap.keySet()) {
                        if (ad1.equals(ad2)) continue;
                        double m2 = MassTransformation.getMonoisotopicMassFromMZ(p2.getMz(), ad2, this.getIonizationMode());
                        double diff = Math.abs(m1 - m2);
                        System.out.println(ad1 + "- " + ad2 + " - " + diff);     
                        if (diff < bestDiff && diff <= tolerance) {


                            // we select the adduct that corresponds with this.m
                            if (Math.abs(p1.getMz() - this.mz) < 1e-6) {
                               this.adduct = ad1;
                                return;
                            } else if (Math.abs(p2.getMz() - this.mz) < 1e-6) {
                                this.adduct = ad2;
                                return;
                            }else {
                                //neigther of both is exactly this.mz;
                                this.adduct= "Unknown";
                                return;
                            }
                        }
                    }
                }
            }
        }
    }


    public Lipid getLipid() {
        return lipid;
    }

    public double getMz() {
        return mz;
    }

    public double getRtMin() {
        return rtMin;
    }

    public String getAdduct() {
        return adduct;
    }

    public void setAdduct(String adduct) {
        this.adduct = adduct;
    }

    public double getIntensity() {
        return intensity;
    }

    public IoniationMode getIonizationMode() {
        return ionizationMode;
    }

    public Set<Peak> getGroupedSignals() {
        return Collections.unmodifiableSet(groupedSignals);
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {this.score = (int) Math.min(1.0, Math.max(-1.0, score));}

    // !Done!TODO Take into account that the score should be normalized between -1 and 1
    public void addScore(int delta) {
        this.score += delta;
        this.totalScoresApplied++;
    }

    /**
     * @return The normalized score between 0 and 1 that consists on the final number divided into the times that the rule
     * has been applied.
     */
    public double getNormalizedScore() {
        if (totalScoresApplied == 0) {return 0.0;}
        return (double) this.score / this.totalScoresApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation that)) return false;
        return Double.compare(that.mz, mz) == 0 &&
                Double.compare(that.rtMin, rtMin) == 0 &&
                Objects.equals(lipid, that.lipid);
    }


    @Override
    public int hashCode() {
        return Objects.hash(lipid, mz, rtMin);
    }

    @Override
    public String toString() {
        return String.format("Annotation(%s, mz=%.4f, RT=%.2f, adduct=%s, intensity=%.1f, score=%d)",
                lipid.getName(), mz, rtMin, adduct, intensity, score);
    }

    // !!TODO Detect the adduct with an algorithm or with drools, up to the user.
}
