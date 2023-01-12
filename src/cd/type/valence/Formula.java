package cd.type.valence;

import arc.struct.*;
import arc.util.serialization.*;
import mindustry.*;
import mindustry.type.*;

import java.util.concurrent.atomic.*;

import static cd.type.valence.ItemsValence.*;


public class Formula{
    public Seq<Item> items = new Seq<>();
    public AtomicInteger valenceAtomicInteger = new AtomicInteger(0);

    public Formula(){
    }

    public Formula(Seq<Item> seq){
        items = seq;
    }

    public static Formula readFromBytes(byte[] bytes){
        Seq<Item> seq = new Seq<>();
        for(byte i : bytes){
            seq.add(Vars.content.item(i));
        }
        return new Formula(seq);
    }

    public static Formula readFromBase64(String string){
        if(!string.startsWith("Zm9ybXVsYSA=")){
            Vars.ui.showErrorMessage("it is not a formula");
            return null;
        }
        String tmpString = string.replace("Zm9ybXVsYSA=", "");
        Seq<Item> seq = new Seq<>();
        String decode = Base64Coder.decodeString(tmpString);
        for(String i : decode.substring(1, decode.length() - 1).split(", ")){
            if(i.isEmpty()){
                Vars.ui.showErrorMessage("got clipboard formula is empty!");
                return null;
            }
            seq.add(Vars.content.item(Integer.parseInt(i)));
        }
        return new Formula(seq);
    }

    public Formula copy(){
        return new Formula(items);
    }

    public void putFormulaItem(Item item){
        items.add(item);
    }

    public void subFormulaItem(){
        if(items.any()) items.remove(items.size - 1);
    }

    public void clear(){
        items.clear();
        updateValence(new AtomicInteger(0));
    }

    public boolean any(){
        return items.any();
    }

    public int getValence(){
        AtomicInteger valence = new AtomicInteger(0),
        index = new AtomicInteger(0);
        AtomicReference<ValenceFunc> handleFunc = new AtomicReference<>();
        items.each(i -> {
            Seq<Item> tmpItems = items.copy();
            tmpItems.removeRange(index.get(), items.size - 1);
            Formula tmpFormula = new Formula(tmpItems);
            int itemValence = ItemsValence.getValence(i, new ValenceModule(valence, tmpFormula, 0));
            if(handleFunc.get() != null) valence.getAndAdd(handleFunc.get().getNormal(new ValenceModule(valence, tmpFormula, itemValence)));
            else valence.getAndAdd(itemValence);
            if(getFunc(i) != null) handleFunc.set(getFunc(i).handleFunc);
            index.getAndIncrement();
        });
        updateValence(valence);
        return valence.get();
    }

    private void updateValence(AtomicInteger valence){
        valenceAtomicInteger.set(valence.get());
    }

    public Seq<ItemStack> toStack(){
        Seq<ItemStack> result = new Seq<>();
        if(!items.any()) return result;
        result.add(new ItemStack(items.get(0), 0));
        for(Item i : items){
            if(result.peek().item == i) result.peek().amount += 1;
            else result.add(new ItemStack(i, 1));
        }
        return result;
    }

    public int count(Item item){
        return items.count(i -> i == item);
    }

    public Item last(){
        return items.peek();
    }

    public boolean lastIs(Item item){
        return last() == item;
    }

    public boolean has(Item item){
        return items.contains(item);
    }

    public byte[] toBytes(){
        byte[] result = new byte[items.size];
        for(int i = 0; i < items.size; i++){
            result[i] = (byte)items.get(i).id;
        }
        return result;
    }

    public String toBase64(){
        Seq<Short> ids = new Seq<>();
        items.each(i -> ids.add(i.id));
        return "Zm9ybXVsYSA=" + Base64Coder.encodeString(ids.toString());
    }
}
