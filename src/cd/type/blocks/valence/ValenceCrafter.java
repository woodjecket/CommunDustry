package cd.type.blocks.valence;

import arc.math.Mathf;
import arc.struct.Seq;
import cd.type.valence.ItemsValence;
import cd.type.valence.ResultMap;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.ConsumeItemFilter;

import java.util.concurrent.atomic.AtomicInteger;

import static mindustry.Vars.content;

//TODO 完善
public class ValenceCrafter extends GenericCrafter {
    public ResultMap resultMap;
    public ValenceCrafter(String name,ResultMap resultMap) {
        super(name);
        this.resultMap = resultMap;
        consume(new ConsumeValenceItem());
        buildType = ValenceCrafterBuild::new;
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("valence",entity-> new Bar(
                ()->"valence:"+((ValenceCrafterBuild) entity).getItemsValence(),
                ()->Pal.ammo,
                ()-> (float) ((ValenceCrafterBuild) entity).getItemsValence() / resultMap.mapKeys.size
                ));
    }

    public class ValenceCrafterBuild extends GenericCrafterBuild{
        public float heat;

        @Override
        public void updateTile() {
            super.updateTile();
            heat = Mathf.approach(heat,0,0.001f);
        }

        @Override
        public void tapped() {
            heat += 0.2;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return ItemsValence.hasValence(item) && this.items.get(item) < this.getMaximumAccepted(item);
        }
        public int getItemsValence(){
            AtomicInteger itemsValence = new AtomicInteger();
            this.items.each((Item item,int amount) -> itemsValence.addAndGet(ItemsValence.getValence(item,items) * amount));
            return itemsValence.get();
        }
        public Item output(){
            return resultMap.getResult(getItemsValence());
        }
        public boolean canMakeItem(){
            return output()!=null;
        }

        @Override
        public void craft() {
            if(canMakeItem()) offload((output()));
            if(wasVisible){
                craftEffect.at(x, y);
            }
            progress %= 1f;
            heat = 0;
            consume();
        }

        @Override
        public void dumpOutputs() {
            if (canMakeItem()) dump(output());
        }

        @Override
        public float getProgressIncrease(float baseTime) {
            return super.getProgressIncrease(baseTime)*heat;
        }
    }
    static class ConsumeValenceItem extends ConsumeItemFilter{
        public Seq<Item> items = new Seq<>();
        public ConsumeValenceItem(){
            this.filter = ItemsValence::hasValence;
            content.items().each(filter, item -> items.add(item));
        }

        @Override
        public void apply(Block block) {
            block.hasItems = true;
            block.acceptsItems = true;
            items.each(item -> block.itemFilter[item.id] = true);
        }

        @Override
        public void trigger(Building build) {
            items.each(item -> build.items.remove(item,build.items.get(item)));
        }
    }
}
