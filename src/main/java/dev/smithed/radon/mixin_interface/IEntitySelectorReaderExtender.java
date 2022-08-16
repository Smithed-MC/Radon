package dev.smithed.radon.mixin_interface;

import java.util.Set;

public interface IEntitySelectorReaderExtender {

    Set<String> getReaderTags();
    void addReaderTag(String tag);

}
