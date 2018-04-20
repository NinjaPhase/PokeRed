package com.ninjaphase.pokered.data.map.random;

import com.badlogic.gdx.utils.JsonValue;
import com.ninjaphase.pokered.PokemonApplication;
import com.ninjaphase.pokered.data.map.TileMap;
import com.ninjaphase.pokered.entity.EntityDirection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 *     A {@code RandomRegion} is a randomly generated region within the game.
 * </p>
 */
public class RandomRegion {
    private static final int CELL_TOWN = 1, CELL_ROUTE = 2;
    private static final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
    private static final int[] directions = {UP, DOWN, LEFT, RIGHT};
    private static final int[] xDir = {0, 0, -1, 1};
    private static final int[] yDir = {1, -1, 0, 0};

    private Random random;
    private List<TileMap> mapList;
    private List<RandomTileMapData> mapData;
    private LinkedList<RandomTileMapData> randomMapSearchList = new LinkedList<>();

    private TileMap startMap;
    private String name;
    private int startX, startY;

    /**
     * <p>
     *     Constructs a new {@code RandomRegion}.
     * </p>
     */
    public RandomRegion(List<RandomTileMapData> mapData) {
        this.random = ThreadLocalRandom.current();
        this.mapList = new ArrayList<>();
        this.mapData = mapData;
    }

    /**
     * <p>
     *     Generates a random region.
     * </p>
     */
    public void generate(int maxWidth, int maxHeight, int cityCount) {
        JsonValue randNames = PokemonApplication.getApplication().getDataManager().baseData.get("random_phrase");
        int start = random.nextInt(randNames.get(0).size);
        int end = random.nextInt(randNames.get(1).size);
        this.name = randNames.get(0).getString(start) + randNames.get(1).getString(end);
        this.name = Character.toUpperCase(this.name.charAt(0)) + this.name.substring(1, this.name.length()).toLowerCase();
        List<RandomRegionData> regionData = generateRegionData(maxWidth, maxHeight, cityCount);
        Map<RandomRegionData, RandomTileMap> mapByData = new HashMap<>();

        // Create the actual tilemap data.
        for(RandomRegionData data : regionData) {
            RandomTileMapData rtmd = mapData.get(0);
            if(data.type == CELL_ROUTE) {
                rtmd = this.startTagFilter("route").get(0);
            } else if(data.type == CELL_TOWN) {
                rtmd = this.startTagFilter("city").get(0);
            }
            RandomTileMap randomTileMap = new RandomTileMap("", rtmd, data.wildLevel);
            if(data.start) {
                startMap = randomTileMap;
                startX = rtmd.startX;
                startY = rtmd.startY;
            }
            this.mapList.add(randomTileMap);
            mapByData.put(data, randomTileMap);
        }

        // Links connections to the tilemap.
        for(RandomRegionData data : regionData) {
            RandomTileMap map = mapByData.get(data);

            for(int dir : RandomRegion.directions) {
                RandomRegionData rrd = getDataAt(regionData, data.x+xDir[dir], data.y+yDir[dir]);
                if(rrd == null)
                    continue;
                RandomTileMap otherMap = mapByData.get(rrd);
                int offset = 0;
                if(dir == UP) {
                    if(map.data.connectionOffsets.get("north") > otherMap.data.connectionOffsets.get("south")) {
                        offset = map.data.connectionOffsets.get("north") - otherMap.data.connectionOffsets.get("south");
                    } else {
                        offset = otherMap.data.connectionOffsets.get("south") - map.data.connectionOffsets.get("north");
                    }
                    map.addConnection(EntityDirection.UP, otherMap, map.getWidth() > otherMap.getWidth() ? offset : -offset);
                } else if(dir == LEFT) {
                    if(map.data.connectionOffsets.get("west") > otherMap.data.connectionOffsets.get("east")) {
                        offset = map.data.connectionOffsets.get("west") - otherMap.data.connectionOffsets.get("east");
                    } else {
                        offset = otherMap.data.connectionOffsets.get("east") - map.data.connectionOffsets.get("west");
                    }
                    map.addConnection(EntityDirection.LEFT, otherMap, map.getHeight() > otherMap.getHeight() ? offset : -offset);
                }
            }
        }
    }

    /**
     * <p>
     *     Generates the full region data ready to be filled with map data.
     * </p>
     *
     * @param maxWidth The width.
     * @param maxHeight the height.
     * @param cityCount The city count.
     * @return The region data.
     */
    private List<RandomRegionData> generateRegionData(int maxWidth, int maxHeight, int cityCount) {
        List<RandomRegionData> regionData = new ArrayList<>();
        Queue<RandomRegionData> toProcess = new ArrayDeque<>();
        RandomRegionData start = null;

        // Create independent cities.
        for(int i = 0; i < cityCount; i++) {
            int nX = 0, nY = 0;
            boolean stop = false;
            while (!stop) {
                nX = random.nextInt(maxWidth);
                nY = random.nextInt(maxHeight);
                stop = true;

                for (int dir : directions) {
                    stop = stop && getDataAt(regionData, nX + xDir[dir], +nY + yDir[dir]) == null;
                }
            }

            RandomRegionData next = new RandomRegionData(nX, nY, CELL_TOWN);
            if(start == null) {
                start = next;
                start.start = true;
                start.wildLevel = 2;
            }
            regionData.add(next);
            toProcess.add(next);
        }

        RandomRegionData current;
        // Link those cities and create branches.
        while ((current = toProcess.poll()) != null) {

            RandomRegionData nearest = null;
            int dist = Integer.MAX_VALUE;
            for (RandomRegionData data : regionData) {
                if (data == current)
                    continue;
                if (current.connected.contains(data))
                    continue;
                if (data.type != CELL_TOWN)
                    continue;
                int newDist = Math.abs(((data.x - current.x) * (data.x - current.x)) + ((data.y - current.y) * (data.y - current.y)));
                if (newDist < dist) {
                    dist = newDist;
                    nearest = data;
                }
            }

            if (nearest == null)
                continue;

            connectTo(regionData, current, nearest, CELL_ROUTE);

            if (current.connected.size() == 1 && random.nextInt(4) == 0)
                toProcess.add(current);
        }

        // Process and connect the different islands.
        toProcess.clear();
        List<RandomRegionData> unprocessed = new ArrayList<>();
        for(RandomRegionData data : regionData) {
            if(data.type != CELL_TOWN)
                continue;
            unprocessed.add(data);
        }
        List<List<RandomRegionData>> islands = new ArrayList<>();

        while(unprocessed.size() > 0) {
            toProcess.add(unprocessed.get(0));

            List<RandomRegionData> island = new ArrayList<>();
            while ((current = toProcess.poll()) != null) {
                if (current.type != CELL_TOWN)
                    continue;

                island.add(current);
                unprocessed.remove(current);
                for (RandomRegionData data : current.connected) {
                    if (!island.contains(data) && !toProcess.contains(data))
                        toProcess.add(data);
                }
            }

            islands.add(island);
        }

        if(islands.size() > 1) {
            for (List<RandomRegionData> island : islands) {
                int connectionsToDo = Math.max(1, island.size()/3);
                while(connectionsToDo > 0) {
                    List<RandomRegionData> otherIsland;
                    do {
                        otherIsland = islands.get(random.nextInt(islands.size()));
                    } while (otherIsland == island);
                    RandomRegionData first = island.get(random.nextInt(island.size()));
                    RandomRegionData second = otherIsland.get(random.nextInt(otherIsland.size()));
                    connectTo(regionData, first, second, CELL_ROUTE);
                    connectionsToDo--;
                }
            }
        }

        int minX = 0, minY = 0;
        int maxX = 0, maxY = 0;
        for (RandomRegionData data : regionData) {
            minX = Math.min(data.x, minX);
            minY = Math.min(data.y, minY);
        }

        for (RandomRegionData data : regionData) {
            data.x += Math.abs(minX);
            data.y += Math.abs(minY);
            maxX = Math.max(data.x, maxX);
            maxY = Math.max(data.y, maxY);
        }

        // Add the REAL connections.
        for (RandomRegionData data : regionData) {
            data.connected.clear();

            for(int dir : directions) {
                RandomRegionData other = getDataAt(regionData, data.x+xDir[dir], data.y+yDir[dir]);
                if(other != null)
                    data.connected.add(other);
            }
        }

        // Sort out the wild levels.

        toProcess.clear();
        toProcess.add(start);
        unprocessed.clear();

        while((current = toProcess.poll()) != null) {
            unprocessed.add(current);

            if(current.from == null) {
                current.wildLevel = 2;
            } else {
                current.wildLevel = current.from.wildLevel + (int)((Math.random() * 2)+1);
            }

            for(RandomRegionData data : current.connected) {
                if(toProcess.contains(data) || unprocessed.contains(data))
                    continue;
                data.from = current;
                toProcess.add(data);
            }
        }
        return regionData;
    }

    /**
     * <p>
     *     Connects a given point to a second point.
     * </p>
     *
     * @param regionData The regional data list.
     * @param first The first point.
     * @param second The second point.
     * @param type The type of connection.
     */
    private void connectTo(List<RandomRegionData> regionData, RandomRegionData first, RandomRegionData second, int type) {
        boolean goUp = random.nextBoolean();
        int nX = first.x, nY = first.y;

        while (nX != second.x || nY != second.y) {
            if (goUp) {
                if (nY < second.y) {
                    nY++;
                } else if (nY > second.y) {
                    nY--;
                } else if (nX < second.x) {
                    nX++;
                } else if (nX > second.x) {
                    nX--;
                }
            } else {
                if (nX < second.x) {
                    nX++;
                } else if (nX > second.x) {
                    nX--;
                } else if (nY < second.y) {
                    nY++;
                } else if (nY > second.y) {
                    nY--;
                }
            }

            if (random.nextInt(4) == 0)
                goUp = !goUp;

            if (getDataAt(regionData, nX, nY) == null)
                regionData.add(new RandomRegionData(nX, nY, type));
        }
        first.connected.add(second);
        second.connected.add(first);
    }

    /**
     * <p>
     *     Starts a tag filter with a list.
     * </p>
     *
     * @param tags The tag to find.
     * @return The result.
     */
    private List<RandomTileMapData> startTagFilter(String... tags) {
        this.randomMapSearchList.clear();
        this.randomMapSearchList.addAll(mapData);
        return filterWithTag(this.randomMapSearchList, tags);
    }

    /**
     * <p>
     *     Attempts to filter the list with a specific tag to look for.
     * </p>
     *
     * @param tileMapDataList The tile map data list.
     * @param tags The tags to look for.
     * @return The list itself with tags filtered.
     */
    private List<RandomTileMapData> filterWithTag(List<RandomTileMapData> tileMapDataList, String... tags) {
        Queue<RandomTileMapData> toRemove = new ArrayDeque<>();
        for(RandomTileMapData rtmd : tileMapDataList) {
            for(String tag : tags) {
                if(Arrays.binarySearch(rtmd.tags, tag) < 0 && !toRemove.contains(rtmd)) {
                    toRemove.add(rtmd);
                    break;
                }
            }
        }
        RandomTileMapData current;
        while((current = toRemove.poll()) != null)
            tileMapDataList.remove(current);
        return tileMapDataList;
    }

    /**
     * <p>
     *     Gets the data at x and y.
     * </p>
     *
     * @param regionData The region data list.
     * @param x The x position.
     * @param y The y position.
     * @return The data at that point.
     */
    private RandomRegionData getDataAt(List<RandomRegionData> regionData, int x, int y) {
        for(RandomRegionData data : regionData) {
            if(data.x != x || data.y != y)
                continue;
            return data;
        }
        return null;
    }

    /**
     * @return The start map.
     */
    public TileMap getStartMap() {
        return this.startMap;
    }
    public int getStartX() { return this.startX; }
    public int getStartY() { return this.startY; }

    public String getName() {
        return this.name;
    }

    /**
     * <p>
     *     The {@code RandomRegionData} holds the data for a point on the map.
     * </p>
     */
    private static class RandomRegionData {
        public int x;
        public int y;
        public int type;
        public int wildLevel;
        public boolean start;
        List<RandomRegionData> connected;
        RandomRegionData from;

        RandomRegionData(int x, int y, int type) {
            this.connected = new ArrayList<>();
            this.x = x;
            this.y = y;
            this.type = type;
            this.wildLevel = -1;
        }

        @Override
        public String toString() {
            return "<RandomRegionData " + x + ", " + y + ", " + type + ">";
        }
    }

}
