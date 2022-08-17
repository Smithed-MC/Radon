package dev.smithed.radon.utils;

import java.util.HashSet;
import java.util.Set;

public class SelectorContainer {

    public final Set<String> selectorTags = new HashSet<>();
    public final Set<String> notSelectorTags = new HashSet<>();
    public Set<String> entityTypes = null;
    public String type = "";
    public boolean isTypeTag = false;
    public boolean isNotType = false;

}
