package com.warcraftII;

// TODO: move this Singleton class to a more appropriate location/package
public class Volume {
    private static final Volume volume = new Volume();

    private static int fxVolume;
    private static int musicVolume;

    static Volume getInstance() {
        return volume;
    }

    private Volume() {
        this.fxVolume = 100;
        this.musicVolume = 50;
    }

    public static int getFxVolume() {
        return fxVolume;
    }

    public static void setFxVolume(int fxVolume) {
        Volume.fxVolume = fxVolume;
    }

    public static int getMusicVolume() {
        return musicVolume;
    }

    public static void setMusicVolume(int musicVolume) {
        Volume.musicVolume = musicVolume;
    }
}
