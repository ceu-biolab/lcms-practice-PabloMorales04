package main;

import lipid.Annotation;
import lipid.IoniationMode;
import lipid.Lipid;
import lipid.LipidScoreUnit;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        // Sample lipids
        Lipid L1 = new Lipid(1, "LX 20:2", "C20H40O2", "PE", 20, 2);
        Lipid L2 = new Lipid(2, "LX 22:2", "C22H44O2", "PE", 22, 2);
        Lipid L3 = new Lipid(3, "LY 18:1", "C18H36O2", "PA", 18, 1);
        Lipid L4 = new Lipid(4, "LY 18:2", "C18H34O2", "PA", 18, 2);
        Lipid L5 = new Lipid(11, "PS 34:0", "C42H80NO10P", "PS", 34, 0);
        Lipid L6 = new Lipid(12, "PI 34:0", "C43H83O13P", "PI", 34, 0);

        Annotation A1 = new Annotation(L1, 400.0, 1000.0, 5.0, IoniationMode.POSITIVE);
        Annotation A2 = new Annotation(L2, 420.0,  900.0, 6.0, IoniationMode.POSITIVE);
        Annotation A3 = new Annotation(L3, 350.0, 1100.0, 8.0, IoniationMode.POSITIVE);
        Annotation A4 = new Annotation(L4, 352.0, 1000.0, 7.0, IoniationMode.POSITIVE);
        Annotation A5 = new Annotation(L5,839.56, 1500.0, 11.0, IoniationMode.POSITIVE);
        Annotation A6 = new Annotation(L6,839.56, 1600.0, 12.0, IoniationMode.POSITIVE);

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);


        try {
            // TODO INTRODUCE THE CODE IF DESIRED TO INSERT FACTS AND TRIGGER RULES
            // Insert facts into unit's DataStores
            lipidScoreUnit.getLipids().add(L1);
            lipidScoreUnit.getLipids().add(L2);
            lipidScoreUnit.getLipids().add(L3);
            lipidScoreUnit.getLipids().add(L4);
            lipidScoreUnit.getLipids().add(L5);
            lipidScoreUnit.getLipids().add(L6);

            lipidScoreUnit.getAnnotations().add(A1);
            lipidScoreUnit.getAnnotations().add(A2);
            lipidScoreUnit.getAnnotations().add(A3);
            lipidScoreUnit.getAnnotations().add(A4);
            lipidScoreUnit.getAnnotations().add(A5);
            lipidScoreUnit.getAnnotations().add(A6);
            instance.fire();
            // TODO INTRODUCE THE QUERIES IF DESIRED

        } finally {
            instance.close();
        }
    }
}