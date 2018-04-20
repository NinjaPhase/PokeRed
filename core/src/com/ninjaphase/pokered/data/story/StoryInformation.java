package com.ninjaphase.pokered.data.story;

/**
 * <p>
 *     The {@code StoryInformation} contains basic information about a story to display to the player.
 * </p>
 */
public class StoryInformation {

    final String name, author;
    final String playerGraphic;
    final String startMapName;
    public final int startMapX, startMapY;

    /**
     * <p>
     *     Constructs a new {@code StoryInformation}.
     * </p>
     *
     * @param name The name of the story.
     * @param author The author of the story.
     */
    public StoryInformation(String name, String author, String playerGraphic,
                            String startMapName, int startMapX, int startMapY) {
        this.name = name;
        this.author = author;
        this.playerGraphic = playerGraphic;
        this.startMapName = startMapName;
        this.startMapX = startMapX;
        this.startMapY = startMapY;
    }

}
