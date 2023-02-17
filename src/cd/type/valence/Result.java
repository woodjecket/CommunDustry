package cd.type.valence;

import arc.func.Func;
import arc.struct.Seq;
import mindustry.type.Item;
import mindustry.type.ItemStack;

public class Result {
    public ResultFunc func;
    public Seq<Item> potentialItem = new Seq<>();

    public Result(ItemStack stack) {
        this(stack.item, stack.amount);
    }

    public Result(Item item, int amount) {
        func = new ResultFunc(item, amount);
        potentialItem.add(item);
    }

    public Result(ResultFunc func, Seq<Item> potentialItem) {
        this.func = func;
        this.potentialItem.addAll(potentialItem);
    }

    public Result(Func<Formula, Item> itemFunc, Seq<Item> potentialItem) {
        this(new ResultFunc(itemFunc, f -> 1), potentialItem);
    }

    public Result(Func<Formula, Item> itemFunc, Func<Formula, Integer> amountFunc, Seq<Item> potentialItem) {
        this(new ResultFunc(itemFunc, amountFunc), potentialItem);
    }

    @Override
    public String toString() {
        return "Result{" +
                "potentialItem=" + potentialItem +
                '}';
    }

    public static class ResultFunc {
        public Func<Formula, Item> itemFunc;
        public Func<Formula, Integer> amountFunc;

        public ResultFunc(Item item, int amount) {
            this(f -> item, f -> amount);
        }

        public ResultFunc(Func<Formula, Item> itemFunc, Func<Formula, Integer> amountFunc) {
            this.itemFunc = itemFunc;
            this.amountFunc = amountFunc;
        }

        public Item getItem(Formula formula) {
            return itemFunc.get(formula);
        }

        public int getAmount(Formula formula) {
            return amountFunc.get(formula);
        }

        public ItemStack getStack(Formula formula) {
            return new ItemStack(getItem(formula), getAmount(formula));
        }
    }
}
