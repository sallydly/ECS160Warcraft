package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.warcraftII.Volume;
import com.warcraftII.Warcraft;

public class WinScreen implements Screen {
    private Warcraft game;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private BitmapFont kingthings24;
    private Music music;
    //  set SCREEN_DURATION of splash screen
    private final float SCREEN_DURATION = 2;
    //  get time time when splash starts
    private float currentDuration = 0;

    public WinScreen(Warcraft game) {
        // disable continuous rendering to improve performance
        Gdx.graphics.setContinuousRendering(false);

        this.game = game;
        this.atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        this.skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        ScreenViewport port = new ScreenViewport();
        port.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage = new Stage(port, game.batch);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("data/snd/music/win.mp3"));
        this.music.setVolume( (Volume.getMusicVolume() / 100));
        this.music.setLooping(true);
    }

    @Override
    public void show() {


        // set background texture image
        // code adapted from https://libgdx.info/basic_image/
        Texture backgroundImageTexture = new Texture("img/win.png");
//        backgroundImageTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
//        TextureRegion backgroundImageTextureRegion = new TextureRegion(backgroundImageTexture);
//        backgroundImageTextureRegion.setRegion(0, 0, stage.getWidth(), stage.getHeight());
//        backgroundImageTextureRegion.set

        //backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image backgroundImage = new Image(backgroundImageTexture);
        backgroundImage.setPosition(0, 0);
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);

        Table menuTable = createMenuTable();
        stage.addActor(menuTable);

        Gdx.graphics.requestRendering();
    }

    private Table createMenuTable() {

        // generate Kingthings font
        // TODO: may need to move this to static class/singleton
        // code adapted from https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kingthings/Kingthings_Calligraphica_2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        kingthings24 = generator.generateFont(parameter);
        generator.dispose();

        Table menuTable = new Table();
        menuTable.setFillParent(true);

        // code adapted from https://libgdx.info/basic-label/
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = kingthings24;
        titleLabelStyle.fontColor = Color.WHITE;

        Label titleLabel = new Label("Congratulations! Victory is yours!", titleLabelStyle);
        titleLabel.setFontScale(4);
        menuTable.add(titleLabel).expandY().align(Align.top);
        menuTable.row();
        menuTable.add().expandY();

        return menuTable;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        game.batch.begin();
        game.batch.end();
        currentDuration += delta;
        if(currentDuration > SCREEN_DURATION) {
            game.setScreen(new MainMenu(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        music.dispose();
    }
}
