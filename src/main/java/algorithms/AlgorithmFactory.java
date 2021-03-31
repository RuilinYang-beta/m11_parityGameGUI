package algorithms;

import modelStep.Attribute;

import java.util.Collection;

public class AlgorithmFactory {

    public static Collection<Attribute> getAttribute(String algorithmName) {
        if (algorithmName.equals("Priority Promotion")) {
            return PriorityPromotion.getAttributes();
        } else if (algorithmName.equals("DFI")) {
            return DFI.getAttributes();
        } else if (algorithmName.equals("Zielonka")) {
            return Zielonka.getAttributes();
        } else {
            return null;
        }
    }

    public static Algorithm getAlgorithm(String algorithmName) {
        if (algorithmName.equals("Priority Promotion")) {
            return new PriorityPromotion();
        } else if (algorithmName.equals("DFI")) {
            return new DFI();
        } else if (algorithmName.equals("Zielonka")) {
            return new Zielonka();
        } else {
            return null;
        }
    }

}
