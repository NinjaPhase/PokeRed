package com.ninjaphase.pokered.editor.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;

/**
 * <p>
 *     The {@code Preferences} class stores the users preferences, and previous options.
 * </p>
 */
public class Preferences {
    public static final File PREFERENCES_PATH = new File("./preferences.json");

    private File f;
    private JSONObject preferences;

    /**
     * <p>
     *     Constructs a new {@code Preferences}.
     * </p>
     */
    public Preferences() {
        this.f = PREFERENCES_PATH;
        this.preferences = new JSONObject();
        try {
            this.preferences.put("last_open", JSONObject.NULL);
            this.preferences.put("last_cwd", JSONObject.NULL);
        } catch (JSONException e) {
            System.err.println("Unable to set default values.");
        }
        this.save();
    }

    /**
     * <p>
     *     Constructs a new {@code Preferences} from a file.
     * </p>
     */
    public Preferences(File f) {
        this.f = f;
        try {
            this.preferences = new JSONObject(new JSONTokener(new FileReader(f)));
        } catch (IOException | JSONException e) {
            System.err.println("Unable to load preferences file.");
        }
    }

    /**
     * <p>
     *     Puts a string.
     * </p>
     *
     * @param key The key.
     * @param str The string.
     */
    public void putString(String key, String str) {
        try {
            this.preferences.put(key, str);
        } catch (JSONException e) {}
    }

    /**
     * <p>
     *     Puts a null value.
     * </p>
     *
     * @param key The key.
     */
    public void putNull(String key) {
        try {
            this.preferences.put(key, JSONObject.NULL);
        } catch (JSONException e) {}
    }

    /**
     * <p>
     *     Saves the preferences to the file.
     * </p>
     */
    public synchronized void save() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(this.f));
            bw.write(this.preferences.toString(2));
            bw.flush();
        } catch (IOException | JSONException e) {
            System.err.println("Unable to save preferences file.");
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {}
            }
        }
    }

    /**
     * @return The JSON values.
     */
    public JSONObject getValues() {
        return preferences;
    }
}
