package cd.type.valence;

import arc.util.Nullable;

public class ItemValence {
    public ItemValenceFunc valence;
    public ItemValenceFunc psionicValence;
    public ItemValenceFunc effectValence;
    public ItemValenceFunc effectPsionicValence;

    public ItemValence(
            @Nullable ItemValenceFunc valence,
            @Nullable ItemValenceFunc psionicValence,
            @Nullable ItemValenceFunc effectValence,
            @Nullable ItemValenceFunc effectPsionicValence
    ) {
        this.valence = setNull(valence);
        this.psionicValence = setNull(psionicValence);
        this.effectValence = setNull(effectValence);
        this.effectPsionicValence = setNull(effectPsionicValence);
    }

    private ItemValenceFunc setNull(ItemValenceFunc valence) {
        return valence != null ? valence : f -> 0;
    }

    public enum EffectEvent {
        Non,
        MakeSelf,
        ChangeByBefore,
        ChangeAfter,
    }
}
