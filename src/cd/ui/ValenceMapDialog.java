package cd.ui;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import cd.world.blocks.valence.*;
import cd.world.blocks.valence.ValenceCrafter.*;
import cd.type.valence.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

import java.util.concurrent.atomic.*;

public class ValenceMapDialog extends BaseDialog{
    private final ValenceBuild building;
    public Formula formula;
    public ResultMap resultMap;
    public Table itemsTable, formulaTable, mapTable;
    public int allValence = 0;
    private ScrollPane pane, itemPane;

    public ValenceMapDialog(ValenceBuild building){
        super("valence");
        this.building = building;
        shown(() -> {
            resultMap = building.getMap();
            formula = building.getFormula();
            rebuild();
            building.updateDialog();
            allValence = formula.getValence();
        });
    }

    private void rebuild(){
        cont.clearChildren();
        Table contTable = cont.table().grow().get();
        AtomicReference<Table>
        AItemsTable = new AtomicReference<>(),
        AFormulaTable = new AtomicReference<>(),
        AMapTable = new AtomicReference<>();
        contTable.table().growY().width(50);
        contTable.table(table -> {
            table.button(Icon.leftOpen, this::hide).size(100).row();
            table.table().growX().height(10).row();
            table.table(Tex.pane, table1 -> {
                Table line = new Table(Tex.whiteui){
                    float rHeight = getHeight();

                    @Override
                    public void draw(){
                        super.draw();
                        float max = parent.getHeight() / 2 - 10;
                        float speed = (Math.abs(allValence - rHeight) / resultMap.max) * max;
                        rHeight = Mathf.approach(rHeight, allValence, speed * 0.002f);
                        float reallyHeight = (rHeight / resultMap.max) * max;
                        Draw.color(Pal.accent);
                        Draw.rect("white", x + getWidth() / 2, y + Mathf.clamp(reallyHeight, -max, max), getWidth(), getHeight());
                    }
                };
                table1.add(line).growX();
            }).growY().width(100).get();
        }).growY().width(100);
        contTable.table().growY().width(10);
        contTable.table(aTable -> {
            AMapTable.set(aTable.table(Tex.pane).growX().height(100).get());
            aTable.row();
            aTable.table().growX().height(10).row();
            aTable.table(bTable -> {
                AItemsTable.set(bTable.table(Tex.pane).growY().width(400).get());
                bTable.table().growY().width(10);
                bTable.table(Tex.pane).growY().get().image(Icon.right);
                bTable.table().growY().width(10);
                AFormulaTable.set(bTable.table(Tex.pane).grow().get());
                bTable.table().growY().width(10);
                bTable.table(table -> {
                    table.button(Icon.redo, () -> {
                        ((ValenceCrafterBuild)building).configure(0);
                        ((ValenceCrafterBuild)building).configure(1);
                    }).growX().height(75).padBottom(10).row();
                    table.button(Icon.cancel, () -> {
                        formula.clear();
                        ((ValenceCrafterBuild)building).configure(null);
                        ((ValenceCrafterBuild)building).configure(1);
                    }).growX().height(75).padBottom(10).row();
                    table.button(Icon.copy, () -> {
                        Vars.ui.showInfoFade("@copied");
                        Core.app.setClipboardText(formula.toBase64());
                    }).growX().height(75).padBottom(10).row();
                    table.button(Icon.download, () -> {
                        Formula tmpFormula = Formula.readFromBase64(Core.app.getClipboardText());
                        if(tmpFormula == null) return;
                        formula = tmpFormula;
                        ((ValenceCrafterBuild)building).configure(2);
                    }).growX().height(75).padBottom(10).row();
                    table.table(Tex.pane).grow().padBottom(10).row();
                    table.button(Icon.crafting, () -> {
                        building.updateValence();
                        ((ValenceCrafterBuild)building).configure(1f);
                        ((ValenceCrafterBuild)building).configure(1);
                        hide();
                    }).growX().height(75);
                }).growY().width(75);
            }).grow();
        }).grow();
        contTable.table().growY().width(10);
        contTable.table().growY().width(10);
        contTable.table(Tex.buttonSideRight).growY().width(100);
        contTable.table().growY().width(50);
        itemsTable = AItemsTable.get();
        formulaTable = AFormulaTable.get();
        mapTable = AMapTable.get();
        pane = formulaTable.pane(buildFormula()).grow().get();
        updateFormula();
        buildMap();
        itemPane = itemsTable.pane(buildItems()).grow().get();
        updateItem();
    }

    public void updateItem(){
        itemPane.setWidget(buildItems());
        itemPane.invalidateHierarchy();
        allValence = formula.getValence();
    }

    public void updateFormula(){
        pane.setWidget(buildFormula());
        pane.invalidateHierarchy();
        allValence = formula.getValence();
    }

    public Table buildItems(){
        Table itemTable = new Table();
        ((Building)building).items.each((i, a) -> {
            itemTable.table(Tex.underline, table -> {
                table.add(new ItemDisplay(i, (int)Mathf.maxZero(a - formula.count(i))));
                table.table().grow();
                if(a - formula.count(i) > 0) table.button(Icon.rightOpenSmall, () -> {
                    ((ValenceCrafterBuild)building).configure(i);
                    ((ValenceCrafterBuild)building).configure(1);
                }).padBottom(10);
            }).growX().height(80);
            itemTable.row();
        });
        return itemTable;
    }

    public Table buildFormula(){
        Table pane = new Table();
        AtomicInteger index = new AtomicInteger(0);
        formula.toStack().each(i -> {
            if(formula.count(i.item) > ((Building)building).items.get(i.item)){
                formula.items.removeRange(formula.items.indexOf(i.item), formula.items.size - 1);
            }
            if(formula.items.any()){
                if(index.get() > 0) pane.table(Tex.button, b -> b.image(Icon.add));
                if(index.get() % 5 == 0) pane.row();
                pane.table(Tex.button, table -> {
                    ItemDisplay id = new ItemDisplay(i.item, i.amount, false);
                    table.add(id);
                }).size(100).padTop(10).padBottom(10);
                index.getAndIncrement();
            }
        });
        return pane;
    }

    public void buildMap(){
        Table mapTableA = new Table();
        AtomicInteger index = new AtomicInteger();
        mapTableA.table().growY().width(1000);
        if(resultMap.hasNegative) resultMap.negativeMapKeys.copy().reverse().each(i -> {
            if(index.get() == resultMap.max - 1){
                index.getAndIncrement();
                return;
            }
            if(i == null){
                mapTableA.table(Tex.pane).growY().width(80);
                index.getAndIncrement();
                return;
            }
            mapTableA.table(Tex.pane, table -> table.add(new ItemDisplay(i.item, i.amount, false))).growY().width(80);
            index.getAndIncrement();
        });
        index.set(0);
        resultMap.mapKeys.each(i -> {
            if(index.get() == 0){
                mapTableA.table(Tex.whitePane).growY().width(80);
                index.getAndIncrement();
                return;
            }
            if(i == null){
                mapTableA.table(Tex.pane).growY().width(80);
                index.getAndIncrement();
                return;
            }
            mapTableA.table(Tex.pane, table -> table.add(new ItemDisplay(i.item, i.amount, false))).growY().width(80);
            index.getAndIncrement();
        });
        mapTableA.table().growY().width(1000);
        ScrollPane mapPane = mapTable.pane(mapTableA).grow().update(p -> {
            float amount = (resultMap.hasNegative ? (resultMap.max + 4.5f) * 60 - 32.5f : 4.5f * 60 - 32.5f) + allValence * 60;
            p.setScrollX(amount);
        }).get();
        mapPane.clearListeners();
        Table selectTable = new Table(){
            @Override
            public void draw(){
                Tex.underline2.draw(x + getWidth() / 2 - 20, y, 40, 80);
            }
        };
        mapPane.clearListeners();
        mapTable.stack(mapPane, selectTable).growY();
    }
}
