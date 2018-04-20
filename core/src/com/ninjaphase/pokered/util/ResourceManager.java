package com.ninjaphase.pokered.util;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     The {@code ResourceManager} is used to dispose of all loaded resources and to reuse resources.
 * </p>
 */
public class ResourceManager implements Disposable {

    private ResourceManager parent;
    private final List<ResourceManager> children;
    private final Map<Class<? extends Disposable>, Map<String, Disposable>> resources;

    /**
     * <p>
     *     Constructs a new {@code ResourceManager}.
     * </p>
     */
    public ResourceManager() {
        this.resources = new HashMap<>();
        this.children = new ArrayList<>();
    }

    /**
     * <p>
     *     Constructs a new {@code ResourceManager} with a parent.
     * </p>
     *
     * @param parent The parent
     */
    public ResourceManager(ResourceManager parent) {
        this();
        this.parent = parent;
        this.parent.children.add(this);
    }

    /**
     * <p>
     *     Adds a resource to the resource manager.
     * </p>
     *
     * @param resourceName The resource name.
     * @param resource The resource.
     * @param <T> The resource type.
     * @return The resource that was just added.
     */
    public <T extends Disposable> T add(String resourceName, T resource) {
        if(!resources.containsKey(resource.getClass()))
            resources.put(resource.getClass(), new HashMap<>());
        if(resources.get(resource.getClass()).containsKey(resourceName))
            remove(resource.getClass(), resourceName);
        resources.get(resource.getClass()).put(resourceName, resource);
        return resource;
    }

    /**
     * <p>
     *     Attempts to get a resource.
     * </p>
     *
     * @param resourceClass The resource class.
     * @param resourceName The name of the resource.
     * @param <T> The resource class type.
     * @return The resource if it exists, otherwise {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T extends Disposable> T get(Class<T> resourceClass, String resourceName) {
        if(!resources.containsKey(resourceClass))
            return null;
        if(!resources.get(resourceClass).containsKey(resourceName))
            return null;
        return (T) resources.get(resourceClass).get(resourceName);
    }

    /**
     * <p>
     *     Attempts to remove the resource.
     * </p>
     *
     * @param resourceName The resource name.
     * @return Whether the resource was removed.
     */
    public <T extends Disposable> boolean remove(Class<T> resourceClass, String resourceName) {
        if(!resources.containsKey(resourceClass))
            return false;
        if(!resources.get(resourceClass).containsKey(resourceName))
            return false;
        resources.get(resourceClass).get(resourceName).dispose();
        return resources.get(resourceClass).remove(resourceName) != null;
    }

    @Override
    public void dispose() {
        for(ResourceManager child : this.children) {
            child.dispose();
        }
        this.children.clear();
        for(Map<String, Disposable> disposableMap : resources.values()) {
            for(Disposable d : disposableMap.values()) {
                d.dispose();
            }
        }
        this.resources.clear();
    }
}
