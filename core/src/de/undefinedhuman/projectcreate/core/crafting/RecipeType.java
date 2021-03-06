package de.undefinedhuman.projectcreate.core.crafting;

public enum RecipeType {
    BLOCK("Blocks"),
    STRUCTURE("Structure"),
    TOOL("Tools"),
    WEAPON("Weapons"),
    ARMOR("Armor");

    private final String previewTexture;

    RecipeType(String previewTexture) {
        this.previewTexture = "gui/preview/crafting/" + previewTexture + ".png";
    }

    public String getPreviewTexture() {
        return previewTexture;
    }
}
