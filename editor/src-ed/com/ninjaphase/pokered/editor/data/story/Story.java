package com.ninjaphase.pokered.editor.data.story;

import com.ninjaphase.pokered.editor.components.MapNode;
import com.ninjaphase.pokered.editor.data.exception.StoryLoadException;
import com.ninjaphase.pokered.editor.data.exception.StoryWriteException;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileMap;
import com.ninjaphase.pokered.editor.data.story.tilemap.TileSet;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 *     A {@code Story} holds game data for an individual single game.
 * </p>
 * <p>
 *     Each {@code Story} has it's own maps, events, tilesets and other extended data.
 * </p>
 */
public class Story implements MapNode {

    private File f;
    private DatabaseManager dbManager;
    private ResourceManager resourceManager;

    private ImageView treeIcon;

    /**
     * <p>
     *     Sets up data for the story.
     * </p>
     */
    private Story() {
        this.resourceManager = new ResourceManager();
        this.dbManager = new DatabaseManager();
        this.treeIcon = new ImageView("/icons/icon_project.png");
    }

    /**
     * <p>
     *     Constructs a new {@code Story}.
     * </p>
     *
     * @param name The name of the {@code Story}.
     */
    public Story(String name, String description) {
        this();
        this.dbManager.setStoryName(name);
        this.dbManager.setStoryDescription(description);
        this.resourceManager.addResource(ResourceType.IMAGE_TILESET, "Outside",
                new Image(getClass().getResourceAsStream("/default_tileset.png")));
        TileSet t = new TileSet(this, 0);
        t.setName("Outside");
        t.setTilesetImage("Outside");
        this.dbManager.getTileSets().add(t);
    }

    /**
     * <p>
     *     Constructs a new {@code Story} from a given file.
     * </p>
     *
     * @param f The file.
     */
    public Story(File f) throws StoryLoadException {
        this();
        this.load(f);
    }

    /**
     * <p>
     *     Saves this story as a file.
     * </p>
     *
     * @param f The file to save as.
     * @throws StoryWriteException Thrown if there is a problem saving the story.
     */
    public void save(File f) throws StoryWriteException {
        this.f = f;
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry ze = new ZipEntry("story.json");
            zos.putNextEntry(ze);
            zos.write(this.getDatabaseManager().getDetailsAsJSON().toString(2).getBytes());
            zos.closeEntry();

            for(ResourceType rt : ResourceType.values()) {
                Set<String> resources = this.resourceManager.getResources(rt);
                for(String key : resources) {
                    this.saveResource(zos, rt, key);
                }
            }

            for(TileSet tileSet : this.dbManager.getTileSets()) {
                this.saveTileSet(zos, tileSet);
            }

            for(TileMap tileMap : this.dbManager.getRootMaps()) {
                this.saveMap(zos, tileMap);
            }
        } catch (IOException | JSONException e) {
            throw new StoryWriteException("Unable to write to OutputStream: " + e.getMessage(), e);
        } finally {
            if(zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    throw new StoryWriteException("Unable to close OutputStream: " + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * <p>
     *     Loads the story as a file.
     * </p>
     *
     * @param f The file.
     * @throws StoryLoadException Thrown if there is a problem loading the {@code Story}.
     */
    private void load(File f) throws StoryLoadException {
        this.f = f;
        ZipInputStream zis = null;
        StringBuilder json = new StringBuilder();
        List<JSONObject> tileSets = new ArrayList<>();
        List<JSONObject> maps = new ArrayList<>();
        try {
            zis = new ZipInputStream(new FileInputStream(f));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory())
                    continue;
                boolean loaded = false;
                String directory = "";
                String name = ze.getName();
                if (name.contains("/")) {
                    directory = name.substring(0, name.lastIndexOf('/'));
                    name = name.substring(name.lastIndexOf('/') + 1, name.length());
                }
                for (ResourceType rt : ResourceType.values()) {
                    if (!directory.equalsIgnoreCase(rt.toString()))
                        continue;
                    this.loadResource(rt, name.substring(0, name.lastIndexOf('.')), zis);
                    loaded = true;
                    break;
                }
                if (loaded) continue;
                if (name.equalsIgnoreCase("story.json") && directory.equalsIgnoreCase("")) {
                    json.setLength(0);
                    int c;
                    while ((c = zis.read()) != -1) {
                        json.append((char) c);
                    }
                    JSONObject jsonObject = new JSONObject(new JSONTokener(json.toString()));
                    this.dbManager.setStoryName(jsonObject.optString("name", "Untitled"));
                    this.dbManager.setStoryDescription(jsonObject.optString("description", ""));
                }
                if (name.startsWith("tileset.") && name.endsWith(".json")) {
                    json.setLength(0);
                    int c;
                    while ((c = zis.read()) != -1) {
                        json.append((char) c);
                    }
                    tileSets.add(new JSONObject(new JSONTokener(json.toString())));
                }
                if (name.startsWith("maps.") && name.endsWith(".json")) {
                    json.setLength(0);
                    int c;
                    while ((c = zis.read()) != -1) {
                        json.append((char) c);
                    }
                    maps.add(new JSONObject(new JSONTokener(json.toString())));
                }
            }
        } catch (JSONException e) {
            throw new StoryLoadException("Unable to read JSON from InputStream: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new StoryLoadException("Unable to read from InputStream: " + e.getMessage(), e);
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    throw new StoryLoadException("Unable to close InputStream: " + e.getMessage(), e);
                }
            }
        }

        for(JSONObject jsonObject : tileSets) {
            TileSet ts = new TileSet(this, jsonObject);
            this.getDatabaseManager().getTileSets().add(ts);
        }

        Map<Integer, TileMap> mapsById = new HashMap<>();

        for(JSONObject jsonObject : maps) {
            TileMap map = new TileMap(this, jsonObject);
            mapsById.put(jsonObject.optInt("id"), map);
        }

        for(TileMap map : mapsById.values()) {
            if(map.getParentId() == -1) {
                this.getDatabaseManager().getRootMaps().add(map);
            } else {
                mapsById.get(map.getParentId()).getChildren().add(map);
            }
        }
    }

    /**
     * <p>
     *     Loads a given resource into the resource manager.
     * </p>
     *
     * @param rt The resource type.
     * @param zis The zip input stream.
     */
    private void loadResource(ResourceType rt, String name, ZipInputStream zis) throws IOException {
        Image img = new Image(zis);
        this.getResourceManager().addResource(rt, name, img);
        zis.closeEntry();
    }

    /**
     * <p>
     *     Saves a resource as a file.
     * </p>
     *
     * @param zos The ZipOutputStream.
     */
    private void saveResource(ZipOutputStream zos, ResourceType rt, String key) throws IOException {
        zos.putNextEntry(new ZipEntry(rt.toString() + "/" + key + ".png"));
        Image image = (Image) this.resourceManager.getResource(rt, key);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", zos);
        zos.closeEntry();
    }

    /**
     * <p>
     *     Saves a tileset as a json file.
     * </p>
     *
     * @param zos The ZipOutputStream.
     * @param tileSet The TileSet.
     * @throws IOException
     * @throws JSONException
     */
    private void saveTileSet(ZipOutputStream zos, TileSet tileSet) throws IOException, JSONException {
        zos.putNextEntry(new ZipEntry("tilesets/tileset." + tileSet.getInternalName() + ".json"));
        zos.write(tileSet.getTileSetAsJSON().toString().getBytes());
        zos.closeEntry();
    }

    /**
     * <p>
     *     Saves a map as a json file.
     * </p>
     *
     * @param zos The ZipOutputStream.
     * @param tileMap The tilemap.
     */
    private void saveMap(ZipOutputStream zos, TileMap tileMap) throws IOException, JSONException {
        zos.putNextEntry(new ZipEntry("maps/maps." + tileMap.getIdName() + ".json"));
        zos.write(tileMap.getMapAsJSON().toString().getBytes());
        zos.closeEntry();
        for(TileMap child : tileMap.getChildren()) {
            this.saveMap(zos, child);
        }
    }

    /**
     * @return Gets the database manager of the story.
     */
    public DatabaseManager getDatabaseManager() {
        return this.dbManager;
    }

    /**
     * @return The current working file of the story.
     */
    public File getFile() {
        return this.f;
    }

    /**
     * @return Gets the resource manager for the story.
     */
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    @Override
    public String toString() {
        return this.dbManager.getStoryName();
    }

    @Override
    public ImageView getIcon() {
        return this.treeIcon;
    }
}
