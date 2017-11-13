package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.warcraftII.Warcraft;

public class MainMenu implements Screen {
    private Warcraft game;
    private Texture texture;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Music music;

    private BitmapFont kingthings8;
    private BitmapFont kingthings10;
    private BitmapFont kingthings12;
    private BitmapFont kingthings16;
    private BitmapFont kingthings24;

    public MainMenu(Warcraft game) {
        // disable continuous rendering to improve performance
        Gdx.graphics.setContinuousRendering(false);

        this.game = game;
        this.texture = new Texture("img/warcraft_icon.png");
        this.atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        this.skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        ScreenViewport port = new ScreenViewport();
        port.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage = new Stage(port, game.batch);
        this.music = Gdx.audio.newMusic(Gdx.files.internal("data/snd/music/menu.mp3"));
        this.music.setLooping(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // set background texture image
        // code adapted from https://libgdx.info/basic_image/
        Texture backgroundImageTexture = new Texture("img/Texture.png");
        backgroundImageTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion backgroundImageTextureRegion = new TextureRegion(backgroundImageTexture);
        backgroundImageTextureRegion.setRegion(0, 0, stage.getWidth(), stage.getHeight());

        //backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image backgroundImage = new Image(backgroundImageTextureRegion);
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        Table menuTable = createMenuTable();
        music.play();
        stage.addActor(menuTable);

        Gdx.graphics.requestRendering();
    }

    private Table createMenuTable() {
        // generate Kingthings font
        // TODO: may need to move this to static class/singleton
        // code adapted from https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kingthings/Kingthings_Calligraphica_2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 8;
        kingthings8 = generator.generateFont(parameter);
        parameter.size = 10;
        kingthings10 = generator.generateFont(parameter);
        parameter.size = 12;
        kingthings12 = generator.generateFont(parameter);
        parameter.size = 16;
        kingthings16 = generator.generateFont(parameter);
        parameter.size = 24;
        kingthings24 = generator.generateFont(parameter);
        generator.dispose();

        Table menuTable = new Table();
        menuTable.setFillParent(true);

        // code adapted from https://libgdx.info/basic-label/
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = kingthings24;
        titleLabelStyle.fontColor = Color.WHITE;

        Label titleLabel = new Label("SJACraft 2: MOSS of Darkness", titleLabelStyle);
        titleLabel.setFontScale(4);
        menuTable.add(titleLabel).expandY().align(Align.top);
        menuTable.row();

        TextButton singlePlayerButton = new TextButton("Single Player Game", skin);
        TextButton multiPlayerButton = new TextButton("Multi Player Game", skin);
        TextButton optionsButton = new TextButton("Options", skin);

        Label.LabelStyle buttonLabelStyle = new Label.LabelStyle();
        buttonLabelStyle.font = kingthings16;
        buttonLabelStyle.fontColor = Color.YELLOW;

        singlePlayerButton.getLabel().setFontScale(2, 2);
        singlePlayerButton.getLabel().setStyle(buttonLabelStyle);
        multiPlayerButton.getLabel().setFontScale(2, 2);
        multiPlayerButton.getLabel().setStyle(buttonLabelStyle);
        optionsButton.getLabel().setFontScale(2, 2);
        optionsButton.getLabel().setStyle(buttonLabelStyle);

        singlePlayerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MapSelection(game));
            }
        });

        multiPlayerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiPlayer(game));
            }
        });

        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Options());
            }
        });

        menuTable.add(singlePlayerButton).uniformX();
        menuTable.row().pad(100, 0 , 100, 0);
        menuTable.add(multiPlayerButton).uniformX();
        menuTable.row();
        menuTable.add(optionsButton).uniformX();
        menuTable.row();
        // empty row for more space
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
        game.batch.draw(texture, 0, 0);
        game.batch.end();
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
