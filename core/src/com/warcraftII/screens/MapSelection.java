package com.warcraftII.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.warcraftII.Warcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.warcraftII.terrain_map.AssetDecoratedMap;

public class MapSelection implements Screen {
    private Logger log = new Logger("MapSelection", 2);
    private Warcraft game;
    private Texture texture;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Music music;

    public MapSelection(Warcraft game) {
        this.game = game;
        this.texture = new Texture("img/warcraft_icon.png");
        this.atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        this.skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        ScreenViewport port = new ScreenViewport();
        port.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage = new Stage(port, game.batch);
        //this.music = Gdx.audio.newMusic(Gdx.files.internal("data/snd/music/menu.mp3"));
        //this.music.setLooping(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table menuTable = createMenuTable();
        //music.play();
        stage.addActor(menuTable);
    }

    private Table createMenuTable() {
        //Maps the .png filename to the image button
        final Map<String, ImageButton> fileNameToImageButtonMap = new HashMap<String, ImageButton>();
        final String BAY_GAME_NAME = "Three ways to cross";
        final String MOUNTAINS_GAME_NAME = "One way in one way out";
        final String HEDGES_GAME_NAME = "No way out of this maze";
        final String NWHR2RN_GAME_NAME = "Nowhere to run, nowhere to hide";

        Table menuTable = new Table();
        menuTable.setFillParent(true);
        AssetDecoratedMap.LoadMaps(Gdx.files.internal("map"));
        Vector<TextButton> MapButtons = new Vector<TextButton>();
        Vector<ImageButton> imageButtons = new Vector<ImageButton>();

        FileHandle dirHandle;
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            dirHandle = Gdx.files.internal("map_previews");
        } else {
            dirHandle = Gdx.files.internal("./bin/map_previews");
        }

        for (FileHandle entry : dirHandle.list()) {
            Texture mapPreview = new Texture(entry);
            TextureRegion myTextureRegion= new TextureRegion(mapPreview);
            TextureRegionDrawable drawable = new TextureRegionDrawable(myTextureRegion);
            ImageButton button = new ImageButton(drawable);
            imageButtons.add(button);
            fileNameToImageButtonMap.put(String.valueOf(entry).split("/")[1], button);
        }

        for (final String MapName : AssetDecoratedMap.GetMapNames()) {
            TextButton Button = new TextButton(MapName, skin);
            Button.getLabel().setFontScale(1, 1);
            Button.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.DMapName = MapName;
                    game.setScreen(new SinglePlayer(game));
                }
            });

            //When switch statements can't handle strings, you get this shit >:(
            if(MapName.trim().equals(BAY_GAME_NAME)) {
                fileNameToImageButtonMap.get("bay.PNG").addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.DMapName = MapName;
                        game.setScreen(new SinglePlayer(game));
                    }
                });
            } else if (MapName.trim().equals(HEDGES_GAME_NAME)) {
                fileNameToImageButtonMap.get("hedges.PNG").addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.DMapName = MapName;
                        game.setScreen(new SinglePlayer(game));
                    }
                });
            } else if (MapName.trim().equals(MOUNTAINS_GAME_NAME)) {
                fileNameToImageButtonMap.get("mountain.PNG").addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.DMapName = MapName;
                        game.setScreen(new SinglePlayer(game));
                    }
                });
            } else if (MapName.trim().equals(NWHR2RN_GAME_NAME)) {
                fileNameToImageButtonMap.get("nwhr2rn.PNG").addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.DMapName = MapName;
                        game.setScreen(new SinglePlayer(game));
                    }
                });
            }
            MapButtons.add(Button);
        }

        //TODO: Recheck after zoom
        menuTable.add(MapButtons.get(0)).expandX();
        menuTable.add(MapButtons.get(1)).expandX();
        menuTable.row().pad(10, 0 ,0 ,0);
        menuTable.add(imageButtons.get(2)).uniformX();
        menuTable.add(imageButtons.get(0)).uniformX();
        menuTable.row().pad(20, 0 ,0 ,0);
        menuTable.add(MapButtons.get(2)).uniformX();
        menuTable.add(MapButtons.get(3)).uniformX();
        menuTable.row().pad(10, 0 ,0 ,0);
        menuTable.add(imageButtons.get(3)).uniformX();
        menuTable.add(imageButtons.get(1)).uniformX();

        return menuTable;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        game.batch.begin();
        game.batch.draw(texture, 0, 0);
        game.batch.end();

        
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        //music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
//        music.pause();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        //music.dispose();
    }
}
