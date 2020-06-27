package battleship;

import javafx.scene.media.AudioClip;

public enum Sound {
    INSTANCE;
    private AudioClip SHIP_PLACED;
    private AudioClip HIT;
    private AudioClip WATER_MISS;
    private AudioClip GAME_WON;
    private AudioClip GAME_LOST;
    private AudioClip ERROR;
    boolean isSoundEnabled = true;

    Sound() {
        this.SHIP_PLACED = getAudioClip("file:resources/audio/ship_placed.wav");
        this.HIT = getAudioClip("file:resources/audio/hit.wav");
        this.WATER_MISS = getAudioClip("file:resources/audio/water_miss.wav");
        this.GAME_WON = getAudioClip("file:resources/audio/game_won.wav");
        this.GAME_LOST = getAudioClip("file:resources/audio/lost_trumpet.wav");
        this.ERROR = getAudioClip("file:resources/audio/error.wav");
    }

    //region playback methods
    public void shipPlaced() {
        if (!isSoundEnabled) return;
        SHIP_PLACED.play();
    }

    public void hit() {
        if (!isSoundEnabled) return;
        HIT.play();
    }

    public void miss() {
        if (!isSoundEnabled) return;
        WATER_MISS.play();
    }

    public void gameWon() {
        if (!isSoundEnabled) return;
        GAME_WON.play();
    }

    public void gameLost() {
        if (!isSoundEnabled) return;
        GAME_LOST.play();
    }

    public void error() {
        if (!isSoundEnabled) return;
        ERROR.play();
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        isSoundEnabled = soundEnabled;
    }

    private AudioClip getAudioClip(String path) {
        return new AudioClip(path);
    }
}
