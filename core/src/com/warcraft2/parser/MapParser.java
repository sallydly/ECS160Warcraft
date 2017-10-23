package com.warcraft2.parser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Created by hqmai on 10/22/17.
 */

public class MapParser {

    private String name;
    private int height, width;
    private char [][]tileCodes;
    private Sprite [][]spriteMap;

    public MapParser(FileHandle file) {
        String fileAsString = file.readString();
        String []fileAsLines;
        fileAsLines = fileAsString.split("\n");

        //  name of the map is on second line of .map file
        name = fileAsLines[1];
        //  map's width and height are on 4th line of .map file
        width = Integer.parseInt(fileAsLines[3].split(" ")[0]);
        height = Integer.parseInt(fileAsLines[3].split(" ")[1]);

        TextureAtlas terrain = new TextureAtlas(Gdx.files.internal("atlas/Terrain.atlas"));
        spriteMap = new Sprite[height][width];
        tileCodes = new char[height][width];
        for(int i = 0; i < height ; i++) {
            //tileCodes[i] = new char[width];
            for (int j = 0; j < width; j++) {
                //  map presentation starts on line 6
                tileCodes[i][j] = fileAsLines[i + 5].charAt(j);
                switch(fileAsLines[i + 5].charAt(j)) {
                    case 'w': //shallow water
                        spriteMap[i][j] = new Sprite(terrain.findRegion("shallow-water-F-0"));
                        break;
                    case 'W': //deep water
                        spriteMap[i][j] = new Sprite(terrain.findRegion("deep-water-F-0"));
                        break;
                    case 'd': //light dirt
                        spriteMap[i][j] = new Sprite(terrain.findRegion("light-dirt-F-0"));
                        break;
                    case 'D': //dark dirt
                        spriteMap[i][j] = new Sprite(terrain.findRegion("dark-dirt-F-0"));
                        break;
                    case 'g': //light grass
                        spriteMap[i][j] = new Sprite(terrain.findRegion("light-grass-F-0"));
                        break;
                    case 'G': //dart grass
                        spriteMap[i][j] = new Sprite(terrain.findRegion("dark-grass-F-0"));
                        break;
                    case 'F': //forest
                        spriteMap[i][j] = new Sprite(terrain.findRegion("forest-F-0"));
                        break;
                    case 'R': //rock
                        spriteMap[i][j] = new Sprite(terrain.findRegion("rock-F-0"));
                        break;
                    default:
                }
                spriteMap[i][j].setPosition(j*32, i*32);
            }
        }









    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Sprite spriteAt(int i, int j) {
        return spriteMap[i][j];
    }

}
