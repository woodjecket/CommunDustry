package cd.type.valence;

import cd.type.formula.Formula;

@FunctionalInterface
public interface ItemValenceFunc {
    float get(Formula.FormulaEnv env);
}
