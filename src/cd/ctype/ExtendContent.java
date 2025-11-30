package cd.ctype;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Nullable;
import cd.CDMod;
import mindustry.Vars;
import mindustry.ctype.Content;

public abstract class ExtendContent {
    public short id ;
    public final String name;
    /**
     * Info on which mod this content was loaded from.
     */
    public Content.ModContentInfo minfo = new Content.ModContentInfo();
    /**
     * Localized, formal name. Never null. Set to internal name if not found in bundle.
     */
    public String localizedName;
    /**
     * Localized description & details. May be null.
     */
    public @Nullable String description, details;
    /**
     * Whether this content is always unlocked in the tech tree.
     */
    public boolean alwaysUnlocked = false;
    /**
     * Icon of the content to use in UI.
     */
    public TextureRegion uiIcon;
    /**
     * Icon of the full content. Unscaled.
     */
    public TextureRegion fullIcon;

    /**
     * Override for the full icon. Useful for mod content with duplicate icons. Overrides any other full icon.
     */
    public String fullOverride = "";

    /**
     * The extension of vanilla `unlocked`.<br>
     * Use 0 for locked, 1 for unlocked.<br>
     * For multiphased, feel free.
     */
    protected int unlockedPhase;

    public ExtendContent(String name) {
        this.name = Vars.content.transformName(name);
        this.id = (short)CDMod.xcontent.getByType(getExtendContentType()).size;
        CDMod.xcontent.handleExtendContent(this);
        this.localizedName = Core.bundle.get(getExtendContentType() + "." + this.name + ".name", this.name);
        this.description = Core.bundle.getOrNull(getExtendContentType() + "." + this.name + ".description");
        this.details = Core.bundle.getOrNull(getExtendContentType() + "." + this.name + ".details");
        this.unlockedPhase = Core.settings != null ? Core.settings.getInt(this.name + "-unlocked", 0) : 0;
    }

    public abstract ExtendContentType getExtendContentType();

    public void init() {

    }

    public void postInit() {

    }

    public void loadIcon() {
        fullIcon =
                Core.atlas.find(fullOverride == null ? "" : fullOverride,
                        Core.atlas.find(getExtendContentType().name() + "-" + name + "-full",
                                Core.atlas.find(name + "-full",
                                        Core.atlas.find(name,
                                                Core.atlas.find(getExtendContentType().name() + "-" + name,
                                                        Core.atlas.find(name + "1"))))));

    }

    public void load() {

    }

    public void unlock(int newPhase){
        if(unlockedPhase < newPhase && !alwaysUnlocked){
            unlockedPhase = newPhase;
            Core.settings.put(name + "-unlocked", true);
            onUnlock();
            //Events.fire(new EventType.UnlockEvent(this));
        }
    }

    protected void onUnlock() {

    }

    @Override
    public String toString() {
        return localizedName;
    }
}
