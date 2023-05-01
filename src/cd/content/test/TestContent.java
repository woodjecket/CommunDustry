package cd.content.test;

import arc.struct.*;
import cd.content.*;
import cd.type.*;
import cd.world.blocks.multi.craft.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.type.ItemStack.with;

@SuppressWarnings("unused")
public class TestContent{
    public static Seq<MetaDust> g = new Seq<>();
    /*
      Chinese：anepsion是这个版本的代号，直接打版本号太长了所以在测试内容前面加了这个名字
    */

    public static Block cdMultiA = new CDMultiCrafter("anepsion-multi-crafter-a"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        recipes = Seq.with(new RecipePair(){{
            in = new Recipe(){{
                liquids = Seq.with(LiquidStack.with(Liquids.water, 6f / 60f));
            }};
            out = new Recipe(){{
                items = Seq.with(with(CDItems.ice, 1));
            }};
            craftTime = 60f;
        }});
    }};

    public static Block cdMultiB = new CDMultiCrafter("anepsion-multi-crafter-b"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
        defaultSelection = new int[]{0, 1};
        recipes = Seq.with(new RecipePair(){{
                               in = new Recipe(){{
                                   liquids = Seq.with(LiquidStack.with(Liquids.water, 6f / 60f));
                               }};
                               out = new Recipe(){{
                                   items = Seq.with(with(CDItems.ice, 1));
                               }};
                               craftTime = 60f;
                           }},
        new RecipePair(){{
            in = new Recipe(){{
                items = Seq.with(with(CDItems.cerium, 2, Items.silicon, 3));
            }};
            out = new Recipe(){{
                liquids = Seq.with(LiquidStack.with(Liquids.slag, 12f / 60f));
            }};
            craftTime = 60f;
        }});
    }};

    public static CDMultiCrafter cdMultiC = new CDMultiCrafter("anepsion-multi-crafter-c"){{
        requirements(Category.crafting, with(Items.copper, 20, Items.silicon, 15, Items.titanium, 15));
    }};

    public static void load(){
        Seq<Item> fore = Vars.content.items();

        for(Item in : fore.copy()){
            if(in == Items.pyratite){
                g.add(new MetaDust(Items.pyratite, "ffaa5f", "D4A383", "d37f47"));
            }else{
                g.add(new MetaDust(in));
            }
        }
        g.each(i -> cdMultiC.recipes.add(new RecipePair(){{
            in = new Recipe(){{
                items.add(new ItemStack(i.item, 1));
                if(i.item.charge != 0f){
                    power = 1f;
                }
                if(i.item.explosiveness != 0f){
                    liquids.add(new LiquidStack(Liquids.cryofluid, 12f / 60f));
                }
            }};
            out = new Recipe(){{
                items.add(new ItemStack(i, 1));
                if(i.item.flammability != 0f){
                    liquids.add(new LiquidStack(Liquids.slag, 12f / 60f));
                }
            }};
            craftTime = 30f;
        }}));
    }


}

