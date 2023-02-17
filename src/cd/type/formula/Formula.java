package cd.type.formula;

import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import cd.type.valence.ItemValence;
import cd.type.valence.ItemValence.EffectEvent;
import cd.type.valence.ItemValenceMap;
import mindustry.gen.Icon;
import mindustry.gen.Tex;

import java.util.concurrent.atomic.AtomicReference;

public class Formula {
    public Seq<Step> steps = new Seq<>();
    public AtomicReference<Float> valence = new AtomicReference<>(0f);
    public AtomicReference<Float> psionicValence = new AtomicReference<>(0f);
    public ItemValenceMap itemValenceMap;
    private ObjectFloatMap<Step> valenceMap = new ObjectFloatMap<>();

    public Formula(ItemValenceMap map) {
        itemValenceMap = map;
    }

    public void addStep(Step step) {
        steps.add(step);
    }

    public void subStep() {
        steps.remove(steps.peek());
    }

    public void clearStep() {
        steps.clear();
    }

    //TODO
    public Vec2 getValencePos() {
        float result = 0;
        float psionicResult = 0;
        FormulaEnv env = new FormulaEnv();
        Step before = null;
        Step after = null;
        for (var i : steps) {
            env.reset(this, before, after, i, EffectEvent.MakeSelf);
            ItemValence iv = i.getValenceFunc(env);
            if (before != null) {
                env.env = EffectEvent.ChangeByBefore;
                env.before = before;
                result += iv.effectValence.get(env);
                psionicResult += iv.effectPsionicValence.get(env);
            }
            env.env = EffectEvent.MakeSelf;
            result += iv.effectValence.get(env);
            psionicResult += iv.effectPsionicValence.get(env);
            result += iv.valence.get(env);
            psionicResult += iv.psionicValence.get(env);
            if (after != null) {
                env.env = EffectEvent.ChangeAfter;
            }
        }
        return null;
    }

    public Table displayUI() {
        return new Table(t -> {
            int index = 1;
            for (Step i : steps) {
                t.add(i.displayRequirement()).size(t.getWidth()).get().setBackground(Tex.pane);
                if (index < steps.size) t.table(Tex.pane, tt -> tt.image(Icon.add)).size(t.getWidth());
                index += 1;
            }
        });
    }

    public static class FormulaEnv {
        public Formula all;
        public Step before;
        public Step after;
        public Step self;
        public EffectEvent env;

        public FormulaEnv() {

        }

        public FormulaEnv(Formula formula, Step before, Step after, Step self, EffectEvent env) {
            reset(formula, before, after, self, env);
        }

        public void reset(Formula formula, Step before, Step after, Step self, EffectEvent env) {
            all = formula;
            this.before = before;
            this.after = after;
            this.self = self;
            this.env = env;
        }
    }
}
