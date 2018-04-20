package com.ninjaphase.pokered.data.story.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.data.map.TileSet;
import com.ninjaphase.pokered.data.map.json.JsonTileMap;
import com.ninjaphase.pokered.data.story.StoryInformation;
import com.ninjaphase.pokered.data.story.Story;
import com.ninjaphase.pokered.data.story.exception.StoryReadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>
 *     The {@code ZipStoryReader} is used to read a story from a Zip File.
 * </p>
 */
public final class ZipStoryReader {
    private static final int MAX_IMAGE_SIZE = 512*1024;
    private static final JsonReader JSON_READER = new JsonReader();

    private ZipStoryReader() {}

    /**
     * <p>
     *     Reads the basic general information from the story.
     * </p>
     *
     * @param handle The handle.
     * @return The story that was just loaded.
     * @throws StoryReadException Thrown if there is an error loading the story.
     */
    public static StoryInformation readGeneralData(FileHandle handle) throws StoryReadException {
        StoryInformation info;
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(handle.read());
            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null) {
                if(!ze.getName().equalsIgnoreCase("story.json"))
                    continue;
                break;
            }
            if(ze == null || !ze.getName().equalsIgnoreCase("story.json"))
                throw new StoryReadException("No story.json for story to load.");
            JsonValue jv = new JsonReader().parse(zis);
            info = new StoryInformation(
                    jv.getString("name", "Untitled"),
                    jv.getString("author", "Unknown"),
                    jv.getString("player"),
                    jv.get("start_map").getString(0),
                    jv.get("start_map").getInt(1),
                    jv.get("start_map").getInt(2)
            );
        } catch (IOException e) {
            throw new StoryReadException("Unable to read story due to input error.", e);
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    System.err.println("Unable to close input stream: " + e.getMessage());
                }
            }
        }
        return info;
    }

    /**
     * <p>
     *     Reads the full story from a file handle.
     * </p>
     *
     * @param story The story to load information into.
     * @return The story that was laoded.
     * @throws StoryReadException Thrown if there is an error loading the story.
     */
    public static Story readFull(Story story) throws StoryReadException {
        if(story.isLoaded())
            return story;
        ZipInputStream zis = null;
        Queue<JsonValue> tileSets = new ArrayDeque<>();
        Queue<JsonValue> maps = new ArrayDeque<>();
        try {

            zis = new ZipInputStream(story.getFileHandle().read());
            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null) {
                if(ze.isDirectory())
                    continue;
                String name = ze.getName();
                if(name.contains("/"))
                    name = name.substring(name.lastIndexOf('/')+1, name.length());
                if(name.startsWith("maps.")) {
                    maps.add(parseNoClose(zis));
                } else if(name.startsWith("tileset.")) {
                    tileSets.add(parseNoClose(zis));
                } else if(name.startsWith("graphic.")) {
                    byte[] bytes = new byte[MAX_IMAGE_SIZE];
                    int readBytes = 0;
                    while(true) {
                        int length = zis.read(bytes, readBytes, bytes.length - readBytes);
                        if(length == -1) break;
                        readBytes += length;
                    }

                    story.getResourceManager().add(name, new Texture(new Pixmap(bytes, 0, readBytes)));
                }
            }
        } catch (IOException e) {
            throw new StoryReadException("Unable to read InputStream", e);
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    System.err.println("Unable to close input stream: " + e.getMessage());
                }
            }
        }

        Map<String, TileSet> tileSetMap = new HashMap<>();
        for(JsonValue jsonValue : tileSets) {
            tileSetMap.put(jsonValue.getString("internal_name"),
                    new TileSet(story.getResourceManager(), jsonValue));
        }

        List<JsonTileMap> tileMaps = new ArrayList<>();
        for(JsonValue jsonValue : maps) {
            JsonTileMap jtm = new JsonTileMap(tileSetMap.get(jsonValue.getString("tileset")), jsonValue);
            story.addMap(jtm);
            tileMaps.add(jtm);
        }

        for(JsonTileMap tm : tileMaps) {
            tm.linkConnections(story);
        }

        story.setLoaded(true);
        return story;
    }

    /**
     * <p>
     *     Parses a JsonValue but doesn't close the stream.
     * </p>
     *
     * @param in The input stream.
     * @return The JsonValue just read.
     */
    public static JsonValue parseNoClose(InputStream in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int i;
        while((i = in.read()) != -1)
            stringBuilder.append((char)i);
        return JSON_READER.parse(stringBuilder.toString());
    }

}
