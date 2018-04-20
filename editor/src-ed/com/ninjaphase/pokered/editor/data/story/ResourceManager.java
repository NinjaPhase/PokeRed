package com.ninjaphase.pokered.editor.data.story;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     The {@code ResourceManager} is used to manage the resources within the application.
 * </p>
 */
public class ResourceManager {

    private Map<ResourceType, ObservableMap<String, Object>> resources;

    /**
     * <p>
     *     Constructs a new {@code ResourceManager}.
     * </p>
     */
    ResourceManager() {
        this.resources = new HashMap<>();
        for(ResourceType resourceType : ResourceType.values()) {
            this.resources.put(resourceType, FXCollections.observableHashMap());
        }
    }

    /**
     * <p>
     *     Adds a resource to the resource manager.
     * </p>
     *
     * @param resourceType The resource type.
     * @param key The key.
     * @param obj The resource.
     */
    public void addResource(ResourceType resourceType, String key, Object obj) {
        if(!resourceType.getClassType().isAssignableFrom(obj.getClass())) {
            throw new IllegalArgumentException("Invalid object for ResourceType(" + resourceType.toString() + ")");
        }
        this.resources.get(resourceType).put(key, obj);
    }

    /**
     * <p>
     *     Removes a resource from the resource manager.
     * </p>
     *
     * @param resourceType The resource type.
     * @param key The key.
     */
    public void removeResource(ResourceType resourceType, String key) {
        this.resources.get(resourceType).remove(key);
    }

    /**
     * @param resourceType The resource type.
     * @return The keys within the resource.
     */
    public Set<String> getResources(ResourceType resourceType) {
        return this.resources.get(resourceType).keySet();
    }

    /**
     * @param resourceType The resource type.
     * @return The keys within the resource.
     */
    public Object getResource(ResourceType resourceType, String key) {
        return this.resources.get(resourceType).get(key);
    }

}
