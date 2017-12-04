package com.warcraftII;

// TODO: move this Singleton class to a more appropriate location/package
public class Volume {
    private static final Volume volume = new Volume();

    private static float fxVolume;
    private static float musicVolume;

    static Volume getInstance() {
        return volume;
    }

    private Volume() {
        this.fxVolume = 100;
        this.musicVolume = 100;
    }

    public static float getFxVolume() {
        return fxVolume;
    }

    public static void setFxVolume(float fxVolume) {
        Volume.fxVolume = fxVolume;
    }

    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(float musicVolume) {
        Volume.musicVolume = musicVolume;
    }
}
