package cd.ui;

import arc.func.*;
import arc.math.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import cd.world.blocks.multi.craft.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.*;

import static mindustry.Vars.control;

public class MultiCrafterSelection{
    private static TextField search;
    private static int rowCount;
    public static void buildTable(@Nullable Block block, Table table, Seq<RecipePair> recipes,
                                                         Prov<RecipePair> holder, Cons<RecipePair> consumer, boolean closeSelect,
                                                         int rows, int columns){
        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        group.setMinCheckCount(0);
        Table cont = new Table().top();
        cont.defaults().size(40);
        if(search != null) search.clearText();

        //A lamdba for (re)build a selection table
        Runnable rebuild = () -> {
            group.clear();
            cont.clearChildren();

            var text = search != null ? search.getText() : "";
            int i = 0;
            rowCount = 0;

            //Select recipes by the searcher
            Seq<RecipePair> recipeList = recipes.select(u -> (text.isEmpty() ||
            u.toString().toLowerCase().contains(text.toLowerCase())));


            for(RecipePair recipe : recipeList){
                if(!recipe.isAllUnlocked()) continue;

                ImageButton button = cont.button(Tex.whiteui, Styles.clearNoneTogglei,
                Mathf.clamp(recipe.iconItem().selectionSize, 0f, 40f),
                () -> {
                    if(closeSelect) control.input.config.hideConfig();
                }
                )
                .tooltip(recipe.iconItem().localizedName).group(group).get();

                button.changed(() -> consumer.get(button.isChecked() ? recipe : null));
                button.getStyle().imageUp = new TextureRegionDrawable(recipe.iconItem().uiIcon);
                button.update(() -> button.setChecked(holder.get() == recipe));

                //noinspection ValueOfIncrementOrDecrementUsed
                if(i++ % columns == (columns - 1)){
                    cont.row();
                    rowCount++;
                }
            }
        };

        rebuild.run();

        Table main = new Table().background(Styles.black6);
        if(rowCount > rows * 1.5f){
            search = main.field(null, text -> rebuild.run()).width(40 * columns)
            .padBottom(4).left().growX().get();
            search.setMessageText("@players.search");
            main.row();
        }

        ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
        pane.setScrollingDisabled(true, false);

        if(block != null){
            pane.setScrollYForce(block.selectScroll);
            pane.update(() -> {
                block.selectScroll = pane.getScrollY();
            });
        }

        pane.setOverscroll(false, false);
        main.add(pane).maxHeight(40 * rows);
        table.top().add(main);
    }

    public static void buildTable(Block block, Table table, Seq<RecipePair> source, Prov<IntSeq> holder,
                                  Cons<RecipePair> consumer,boolean closeSelect){
        ButtonGroup<ImageButton> group = new ButtonGroup<>();
        group.setMinCheckCount(0);
        Table cont = new Table().top();
        cont.defaults().size(40);
        if(search != null) search.clearText();

        Runnable rebuild = () -> {
            group.clear();
            cont.clearChildren();

            var text = search != null ? search.getText() : "";
            rowCount = 0;

            //Select recipes by the searcher
            Seq<RecipePair> recipeList = source.select(u -> (text.isEmpty() ||
            u.toString().toLowerCase().contains(text.toLowerCase())));

            for(RecipePair recipe : recipeList){
                if(!recipe.isAllUnlocked()) continue;

                ImageButton button = cont.button(Tex.whiteui, Styles.clearNoneTogglei,
                40f,
                () -> {
                    if(closeSelect) control.input.config.hideConfig();
                }
                )
                .tooltip(recipe.iconItem().localizedName).group(group).get();

                button.changed(() -> consumer.get(button.isChecked() ? recipe : null));
                button.getStyle().imageUp = new TextureRegionDrawable(recipe.iconItem().uiIcon);
                button.update(() -> button.setChecked(holder.get().contains(source.indexOf(recipe))));

            }
        };

        rebuild.run();

        Table main = new Table().background(Styles.black6);
        if(rowCount > 4f * 1.5f){
            search = main.field(null, text -> rebuild.run()).width(160)
            .padBottom(4).left().growX().get();
            search.setMessageText("@players.search");
            main.row();
        }
        ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
        pane.setScrollingDisabled(true, false);
        if(block != null){
            pane.setScrollYForce(block.selectScroll);
            pane.update(() -> {
                block.selectScroll = pane.getScrollY();
            });
        }
        pane.setOverscroll(false, false);
        main.add(pane).maxHeight(160);
        table.top().add(main);
    }
}
