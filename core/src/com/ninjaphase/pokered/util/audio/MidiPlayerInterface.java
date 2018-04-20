package com.ninjaphase.pokered.util.audio;

import com.badlogic.gdx.files.FileHandle;

/**
 * <p>
 *     The {@code MidiPlayerInterface} is used to play MIDI files on different platforms.
 * </p>
 */
public interface MidiPlayerInterface {

    /**
     * <p>
     *     Sets the midi file the midi player is going to play.
     * </p>
     *
     * @param id The sequencer to set.
     * @param fileHandle The file handle.
     */
    void setMidiFile(int id, FileHandle fileHandle);

    /**
     * <p>
     *     Attempts to play the loaded midi sequence.
     * </p>
     *
     * @param id The sequencer to play on.
     */
    void play(int id);

    /**
     * <p>
     *     Attempts to stop the midi sequence.
     * </p>
     *
     * @param id The id.
     */
    void stop(int id);

    /**
     * <p>
     *     Sets the looping position.
     * </p>
     *
     * @param id The sequencer id.
     * @param position The position.
     */
    void setLoopPosition(int id, long position);

    /**
     * <p>
     *     Sets the track to be looping or not.
     * </p>
     *
     * @param id The sequencer id.
     * @param looping Whether to loop the track.
     */
    void setLooping(int id, boolean looping);

}
