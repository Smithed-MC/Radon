package dev.smithed.radon.mixin_interface;

import java.util.Set;

public interface IEntitySelectorExtender {

    Set<String> getTags();
    void setTags(Set<String> tags);

}
