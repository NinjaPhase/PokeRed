package com.ninjaphase.pokered.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.ninjaphase.pokered.util.audio.MidiPlayerInterface;

/**
 * <p>
 *     The {@code AndroidMidiPlayer} is used to play MIDI files on an Android device.
 * </p>
 */

class AndroidMidiPlayer implements MidiPlayerInterface {
    private static final int MAX_MUSIC_COUNT = 5;

    private Music[] musics;

    AndroidMidiPlayer() {
        this.musics = new Music[MAX_MUSIC_COUNT];
    }

    @Override
    public void setMidiFile(int id, FileHandle fileHandle) {
        this.musics[id] = Gdx.audio.newMusic(fileHandle);
    }

    @Override
    public void play(int id) {
        this.musics[id].play();
    }

    @Override
    public void stop(int id) {
        this.musics[id].stop();
    }

    @Override
    public void setLoopPosition(int id, long position) {

    }

    @Override
    public void setLooping(int id, boolean looping) {
        this.musics[id].setLooping(looping);
    }
}
