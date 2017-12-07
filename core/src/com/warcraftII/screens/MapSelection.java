package com.warcraftII.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.warcraftII.terrain_map.AssetDecoratedMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

        // set background texture image
        // code adapted from https://libgdx.info/basic_image/
        Texture backgroundImageTexture = new Texture("img/Texture.png");
        backgroundImageTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion backgroundImageTextureRegion = new TextureRegion(backgroundImageTexture);
        backgroundImageTextureRegion.setRegion(0, 0, stage.getWidth(), stage.getHeight());
        Image backgroundImage = new Image(backgroundImageTextureRegion);
        backgroundImage.setPosition(0, 0);

        stage.addActor(backgroundImage);
        stage.addActor(menuTable);
    }


    private Table createMenuTable() {
        //Maps the .png filename to the image button
        final Map<String, ImageButton> fileNameToImageButtonMap = new HashMap<String, ImageButton>();
        final Map<String, String> mapNameToFileName = new HashMap<String, String>();
        Vector<TextButton> textButtons = new Vector<TextButton>();
        Vector<String> orderedMapNames = new Vector<String>();
        Table menuTable = new Table();
        Table container = new Table(skin);

        //NOTE: To add a new map, give the map's name in this block,
        // and map (using mapNameToFileName) to its fileName in the next block
        //Map names
        final String BAY_GAME_NAME = "Three ways to cross";
        final String MOUNTAINS_GAME_NAME = "One way in one way out";
        final String HEDGES_GAME_NAME = "No way out of this maze";
        final String NWHR2RN_GAME_NAME = "Nowhere to run, nowhere to hide";

        mapNameToFileName.put(BAY_GAME_NAME, "bay.PNG");
        mapNameToFileName.put(MOUNTAINS_GAME_NAME, "mountain.PNG");
        mapNameToFileName.put(HEDGES_GAME_NAME, "hedges.PNG");
        mapNameToFileName.put(NWHR2RN_GAME_NAME, "nwhr2rn.PNG");

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

        //Create the ImageButtons and map the filenames to their ImageButton
        for (FileHandle entry : dirHandle.list()) {
            //Create the ImageButton using the "map_preview"
            Texture mapPreview = new Texture(entry);
            TextureRegion myTextureRegion= new TextureRegion(mapPreview);
            TextureRegionDrawable drawable = new TextureRegionDrawable(myTextureRegion);
            ImageButton button = new ImageButton(drawable);

            //remove the directory prefix of the file name
            String fileName = String.valueOf(entry).split("/")[1];

            //Map the fileName to the ImageButton
            fileNameToImageButtonMap.put(fileName, button);
        }

        for (final String mapName : AssetDecoratedMap.GetMapNames()) {
            //AssetDecoratedMap.GetMapNames() returns a string with a \r at the end, gotta remove it
            orderedMapNames.add(mapName.trim());

            //Set a ClickListener on the TextButton
            TextButton textButton = new TextButton(mapName, skin);
            textButton.getLabel().setFontScale(1, 1);
            textButton.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.DMapName = mapName;
                    game.setScreen(new SinglePlayer(game));
                }
            });

            textButtons.add(textButton);

            //Set a ClickListener on the ImageButton
            //`mapNameToFileName.get(MapName))` returns the filename e.g. "bay.PNG"
            String fileName = mapNameToFileName.get(mapName.trim());
            ImageButton temp = fileNameToImageButtonMap.get(fileName);
            temp.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.DMapName = mapName;
                    game.setScreen(new SinglePlayer(game));
                }
            });

            fileNameToImageButtonMap.put(fileName, temp);
        }

        //Top row has the name of the map, and you can click on it
        for (TextButton textButton : textButtons) {
            menuTable.add(textButton).pad(0,50,100,50).uniformX().height(150);
        }

        menuTable.row().pad(20);

        //Bottom row has a preview of the map, and you can click on it
        for (String mapName : orderedMapNames) {
            String fileName =  mapNameToFileName.get(mapName.trim());
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
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            game.setScreen(new MainMenu(game));
        }
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
//        music.play();
    }

    @Override
    public void hide() {
//        music.pause();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        stage.dispose();
        //music.dispose();
    }
}
