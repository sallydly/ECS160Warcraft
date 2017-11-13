package com.warcraftII.screens.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.warcraftII.Warcraft;
import com.warcraftII.screens.MainMenu;

public class Options implements Screen {
    private Logger log = new Logger("Options", 2);

    private Warcraft game;
    private Stage stage;
    private Skin skin;

    public Options(Warcraft game) {
        // disable continuous rendering to improve performance
        Gdx.graphics.setContinuousRendering(false);

        this.game = game;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
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

        // generate Kingthings font
        // TODO: may need to move this to static class/singleton
        // code adapted from https://github.com/libgdx/libgdx/wiki/Gdx-freetype
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kingthings/Kingthings_Calligraphica_2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        BitmapFont kingthings16 = generator.generateFont(parameter);
        parameter.size = 24;
        BitmapFont kingthings24 = generator.generateFont(parameter);
        generator.dispose();

        // layout Options screen using Table
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // code adapted from https://libgdx.info/basic-label/
        Label.LabelStyle titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = kingthings24;
        titleLabelStyle.fontColor = Color.WHITE;

        Label titleLabel = new Label("Options", titleLabelStyle);
        titleLabel.setFontScale(4);
        table.add(titleLabel).expandY().align(Align.top);
        table.row();

        Label.LabelStyle buttonLabelStyle = new Label.LabelStyle();
        buttonLabelStyle.font = kingthings16;
        buttonLabelStyle.fontColor = Color.YELLOW;

        // TODO: implement button actions for Sound, Network, and Back
        TextButton soundOptionsButton = new TextButton("Sound Options", skin);
        soundOptionsButton.getLabel().setStyle(buttonLabelStyle);
        soundOptionsButton.getLabel().setFontScale(2);
        soundOptionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                log.info("Sound Options button clicked.");
                game.setScreen(new SoundOptions(game));
            }
        });
        table.add(soundOptionsButton).uniformX().expandY();
        table.row();

        TextButton networkOptionsButton = new TextButton("Network Options", skin);
        networkOptionsButton.getLabel().setStyle(buttonLabelStyle);
        networkOptionsButton.getLabel().setFontScale(2);
        table.add(networkOptionsButton).uniformX().expandY();
        table.row();

        // empty row for more space
        table.add().expandY();
        table.row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setStyle(buttonLabelStyle);
        backButton.getLabel().setFontScale(2);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                log.info("Back button clicked.");
                game.setScreen(new MainMenu(game));
            }
        });
        table.add(backButton).uniformX().expandY();
        table.row();

        // empty row for more space
        table.add().expandY();
        table.row();

        Gdx.graphics.requestRendering();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
