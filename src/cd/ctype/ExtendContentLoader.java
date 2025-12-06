package cd.ctype;

import arc.func.Cons;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import cd.content.*;
import cd.struct.recipe.Recipe;

import static cd.ctype.ExtendContentType.recipe;

public class ExtendContentLoader {
    private final ObjectMap<String, ExtendContent>[] xcontentNameMap = new ObjectMap[ExtendContentType.all.length];
    private final Seq<ExtendContent>[] xcontentMap = new Seq[ExtendContentType.all.length];
    private final ObjectMap<String, ExtendContent> xNameMap = new ObjectMap<>();

    public ExtendContentLoader() {
        for (ExtendContentType type : ExtendContentType.all) {
            xcontentMap[type.id] = new Seq<>();
            xcontentNameMap[type.id] = new ObjectMap<>();
        }
    }

    public void createContent() {
        CDItems.load();
        Avatars.load();
        Plots.load();
        CDRecipe.load();
        VeinTypes.load();
        CDBlocks.load();
    }

    public void init() {
        for (var seq : xcontentMap) {
            seq.remove((ExtendContent) null);
        }
        initialize(ExtendContent::init);
    }

    public void postInit() {
        initialize(ExtendContent::postInit);
    }

    public void load() {
        initialize(ExtendContent::load);
    }

    public void loadIcon() {
        initialize(ExtendContent::loadIcon);
    }


    private void initialize(Cons<ExtendContent> callable) {
        for (ExtendContentType type : ExtendContentType.all) {
            for (ExtendContent xContent : xcontentMap[type.id]) {
                callable.get(xContent);
            }
        }
    }

    public void handleExtendContent(ExtendContent xContent) {
        //Log.info("Loaded @", xContent.name);

        if (xcontentNameMap[xContent.getExtendContentType().id].containsKey(xContent.name)) {
            var list = xcontentMap[xContent.getExtendContentType().id];

            //this method is only called when registering xContent, and after handleContent.
            //If this is the last registered xContent, and it is invalid, make sure to remove it from the list to prevent invalid stuff from being registered
            if (list.size > 0 && list.peek() == xContent) {
                list.pop();
            }
            throw new IllegalArgumentException("Two xContent objects cannot have the same name! (issue: '" + xContent.name + "')");
        }

        xcontentNameMap[xContent.getExtendContentType().id].put(xContent.name, xContent);
        xNameMap.put(xContent.name, xContent);
        xcontentMap[xContent.getExtendContentType().id].addUnique(xContent);
    }

    public <T extends ExtendContent> Seq<T> getByType(ExtendContentType type) {
        return (Seq<T>) xcontentMap[type.id];
    }

    public Seq<Recipe> recipes() {
        return getByType(recipe);
    }
}
