package com.ninjaphase.pokered.editor.data.story;

import javafx.scene.image.Image;

/**
 * <p>
 *     The {@code ResourceType} defines a resource within the game and the category it belongs to.
 * </p>
 */
public enum ResourceType {
    IMAGE_CHARACTERS("Graphics/Characters", Image.class),
    IMAGE_TILESET("Graphics/TileSets", Image.class);

    private String resourceTypeName;
    private Class<?> classType;

    /**
     * <p>
     *     Constructs a new {@code ResourceType}.
     * </p>
     *
     * @param resourceTypeName The resource types name.
     */
    ResourceType(String resourceTypeName, Class<?> classType) {
        this.resourceTypeName = resourceTypeName;
        this.classType = classType;
    }

    /**
     * @return The class this should be.
     */
    public Class<?> getClassType() {
        return this.classType;
    }

    @Override
    public String toString() {
        return this.resourceTypeName;
    }

}
