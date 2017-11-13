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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.warcraftII.Warcraft;

public class SoundOptions implements Screen {
    private Logger log = new Logger("Sound Options", 2);

    private Warcraft game;
    private Stage stage;
    private Skin skin;

    public SoundOptions(Warcraft game) {
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

        Label titleLabel = new Label("Sound Options", titleLabelStyle);
        titleLabel.setFontScale(4);
        table.add(titleLabel).align(Align.top);
        table.row();

        Label.LabelStyle optionsStyle = new Label.LabelStyle();
        optionsStyle.font = kingthings16;
        optionsStyle.fontColor = Color.WHITE;

        Table optionsTable = new Table();

        Label fxVolumeLabel = new Label("FX Volume:", skin);
        fxVolumeLabel.setStyle(optionsStyle);
        fxVolumeLabel.setFontScale(2);
        TextField fxVolumeText = new TextField("100", skin);

        Label musicVolumeLabel = new Label("Music Volume:", skin);
        musicVolumeLabel.setStyle(optionsStyle);
        musicVolumeLabel.setFontScale(2);
        TextField musicVolumeText = new TextField("50", skin);

        optionsTable.add(fxVolumeLabel);
        optionsTable.add(fxVolumeText).prefWidth(200).expandY();
        optionsTable.row();

        optionsTable.add(musicVolumeLabel);
        optionsTable.add(musicVolumeText).prefWidth(200).expandY();
        optionsTable.row();

        table.add(optionsTable).expandY();
        table.row();

        Label.LabelStyle buttonLabelStyle = new Label.LabelStyle();
        buttonLabelStyle.font = kingthings16;
        buttonLabelStyle.fontColor = Color.YELLOW;

        // table for OK and Cancel buttons in bottom right of screen
        Table buttonsTable = new Table();

        TextButton okButton = new TextButton("OK", skin);
        okButton.getLabel().setStyle(buttonLabelStyle);
        okButton.getLabel().setFontScale(2);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                log.info("OK button clicked.");
                game.setScreen(new Options(game));
            }
        });
        buttonsTable.add(okButton).uniformX().pad(10);
        buttonsTable.row();

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.getLabel().setStyle(buttonLabelStyle);
        cancelButton.getLabel().setFontScale(2);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                log.info("Cancel button clicked.");
                game.setScreen(new Options(game));
            }
        });
        buttonsTable.add(cancelButton).uniformX().pad(10);
        buttonsTable.row();

        table.add(buttonsTable).expandX().align(Align.bottomRight);

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
