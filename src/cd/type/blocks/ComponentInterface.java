package cd.type.blocks;

import cd.entities.component.*;

public interface ComponentInterface{
    <C extends BaseComponent> C getComp(Class<C> type);
    void addComp(BaseComponent... c);
    <T extends BaseComponent> void removeComp(Class<T> type);
    Iterable<BaseComponent> listComps();
}
