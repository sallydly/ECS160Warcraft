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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
        final Map<String, String> mapNameToFileName = new HashMap<String, String>();
        Vector<TextButton> textButtons = new Vector<TextButton>();
        Vector<ImageButton> imageButtons = new Vector<ImageButton>();
        Vector<String> orderedMapNames = new Vector<String>();

        //Map names
        final String BAY_GAME_NAME = "Three ways to cross";
        final String MOUNTAINS_GAME_NAME = "One way in one way out";
        final String HEDGES_GAME_NAME = "No way out of this maze";
        final String NWHR2RN_GAME_NAME = "Nowhere to run, nowhere to hide";

        mapNameToFileName.put(BAY_GAME_NAME, "bay.PNG");
        mapNameToFileName.put(MOUNTAINS_GAME_NAME, "mountain.PNG");
        mapNameToFileName.put(HEDGES_GAME_NAME, "hedges.PNG");
        mapNameToFileName.put(NWHR2RN_GAME_NAME, "nwhr2rn.PNG");

        Table menuTable = new Table();
        Table container = new Table(skin);
        container.setFillParent(true);
        ScrollPane scrollPane = new ScrollPane(menuTable);
        container.add(scrollPane).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight());

        AssetDecoratedMap.LoadMaps(Gdx.files.internal("map"));


        FileHandle dirHandle;
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            dirHandle = Gdx.files.internal("map_previews");
        } else {
            dirHandle = Gdx.files.internal("./bin/map_previews");
        }

        //Map the filenames to their ImageButton
        for (FileHandle entry : dirHandle.list()) {
            Texture mapPreview = new Texture(entry);
            TextureRegion myTextureRegion= new TextureRegion(mapPreview);
            TextureRegionDrawable drawable = new TextureRegionDrawable(myTextureRegion);
            ImageButton button = new ImageButton(drawable);
            imageButtons.add(button);
            String splitFileName = String.valueOf(entry).split("/")[1];
            fileNameToImageButtonMap.put(splitFileName, button);
        }


        for (final String MapName : AssetDecoratedMap.GetMapNames()) {
            orderedMapNames.add(MapName);

            TextButton textButton = new TextButton(MapName, skin);
            textButton.getLabel().setFontScale(1, 1);
            textButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.DMapName = MapName;
                    game.setScreen(new SinglePlayer(game));
                }
            });

            textButtons.add(textButton);

            //This block sets ClickListeners on the images
            //`mapNameToFileName.get(MapName))` returns a the filename e.g. "bay.PNG"
            fileNameToImageButtonMap.get(mapNameToFileName.get(MapName)).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.DMapName = MapName;
                    game.setScreen(new SinglePlayer(game));
                }
            });


        }

        for (TextButton textButton : textButtons) {
            menuTable.add(textButton).pad(0,50,100,50).uniformX();
        }

        menuTable.row().pad(20);

        for (String mapName : orderedMapNames) {
            String fileName =  mapNameToFileName.get(mapName);
            ImageButton imageButton = fileNameToImageButtonMap.get(fileName);
            menuTable.add(imageButton);
        }

        return container;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        game.batch.begin();
        //game.batch.draw(texture, 0, 0);
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
