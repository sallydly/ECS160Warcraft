package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
import com.warcraftII.GameData;
import com.warcraftII.GameDataTypes;
import com.warcraftII.Warcraft;

import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.player_asset.PlayerData;
import com.warcraftII.position.*;
import com.warcraftII.units.Unit;

import java.util.ArrayList;
import java.util.Vector;

import static java.lang.Math.round;


public class SinglePlayer implements Screen, GestureDetector.GestureListener{
    private Logger log = new Logger("SinglePlayer", 2);
    private Warcraft game;
    private GameData gameData;
    // More concise access to data members of gameData:
    private Unit allUnits;
    public Vector<Unit.IndividualUnit> selectedUnits = new Vector<Unit.IndividualUnit>();
    private SpriteBatch batch;
    private SpriteBatch sb;


    private Music readySound;

    private int movement;
    public int attack;
    public int patrol;
    public int mine;
    public int ability;
    private InputMultiplexer multiplexer;

    private TextButton stopButton;
    private TextButton patrolButton;
    private TextButton attackButton;
    private TextButton newAbility;
    private TextButton menuButton;
    private TextButton pauseButton;
    private TextButton moveButton;
    private TextButton selectButton;
    private TextButton buildButton;
    private Label selectCount;


    public OrthogonalTiledMapRenderer orthomaprenderer;
    private OrthographicCamera mapCamera;
    private FitViewport mapViewport;
    private Stage mapStage;

    private OrthographicCamera sidebarCamera;
    private FitViewport sidebarViewport;
    private Stage sidebarStage;

    private Table sidebarTable;


    private float prevZoom = 1;
    // mapCamera zoom levels to fit map height/width
    private float heightZoomRatio;
    private float widthZoomRatio;

    private double prevDistance = 0;

    // for multi-selection rectangle
    private ShapeRenderer shapeRenderer;
    private float touchStartX = 0;
    private float touchStartY = 0;
    private float touchEndX = 0;
    private float touchEndY = 0;

    private int lastbuiltasset = 0; //DEBUG


    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        gameData = new GameData(game.DMapName); // IMPORTANT

        // Fucking leave this on. Nothing updates without constant input if it's off.
        Gdx.graphics.setContinuousRendering(true);

        // initialize easy-access reference variables.
        batch = gameData.batch = game.batch;
        allUnits = gameData.allUnits;
        sb = gameData.sb;

        Gdx.graphics.setContinuousRendering(true);

        //Implemented just to achieve hard goal. Not needed
        this.readySound = Gdx.audio.newMusic(Gdx.files.internal("data/snd/basic/ready.wav"));
        this.shapeRenderer = new ShapeRenderer();
        //add the menu and pause buttons
        TextureAtlas atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        Skin skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        moveButton = new TextButton("Move", skin);
        stopButton = new TextButton("Stop", skin);
        patrolButton = new TextButton("Patrol", skin);
        attackButton = new TextButton("Attack", skin);
        menuButton = new TextButton("Menu", skin);
        pauseButton = new TextButton("Pause", skin);
        selectButton = new TextButton("Select", skin);
        buildButton = new TextButton("Build", skin);
        selectCount = new Label("", skin);
 /*       stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //allUnits.stopMovement();
                movement = 0;
                patrol = 0;
                attack = 0;
                mine = 0;
                ability = 0;
            }
        });*/
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        // TODO: add if statement for if multi-select button is activated
        // get current finger position for drag select rectangle
        // convert x and y from screen coordinates to viewport coordinates
        Vector3 clickCoordinates = new Vector3(x, y, 0);
        Vector3 position = mapViewport.unproject(clickCoordinates);
        touchEndX = position.x;
        touchEndY = position.y;

        // TODO: uncomment following lines to enable panning when multi select button is not activated (else statement)
        // adjust pointer drag amount by mapCamera zoom level
        if(!selectButton.isPressed()) {
            deltaX *= mapCamera.zoom;
            deltaY *= mapCamera.zoom;

            // move mapCamera based on distance of pointer drag
            mapCamera.translate(-deltaX, deltaY);

            // limit panning to edge of map
            calculateCameraBounds();
            mapCamera.update();
        }

        return true;
    }

    @Override
    public void show() {
        movement = 0;
        attack = 0;
        patrol = 0;
        ability = 0;
        mine = 0;
/*
        TextureAtlas[] unitTextures = {
                new TextureAtlas(Gdx.files.internal("atlas/Peasant.atlas")),
                new TextureAtlas(Gdx.files.internal("atlas/Footman.atlas")),
                new TextureAtlas(Gdx.files.internal("atlas/Archer.atlas")),
                new TextureAtlas(Gdx.files.internal("atlas/Ranger.atlas"))
        };
*/

/* Do we need this code???
        tile = new Sprite(terrain.findRegion("shallow-water-F-0"));
        tile.setScale(5);
        tile.setPosition(300, 300);
        table = new Table(skin);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.bottomLeft);
        */

//        stage = new Stage(new ScreenViewport());


//        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Make Buttons for the Unit Actions
        gameData.unitActions.createBasicSkin();


        moveButton.setPosition(5 , 10);
        stopButton.setPosition(5 , 30+(1*Gdx.graphics.getHeight() / 10));
        patrolButton.setPosition(5 , 50+(2*Gdx.graphics.getHeight() / 10));
        attackButton.setPosition(5, 70+(3*Gdx.graphics.getHeight() / 10));
        buildButton.setPosition(5, 90+(4*Gdx.graphics.getHeight() / 10));
        /*moveButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                movement = 1;
                return true;
            }
        });
        */
        stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
                    for (Unit.IndividualUnit cur : allUnits.unitMap.get(color)) {
                        if (cur.selected) {
                            cur.stopMovement();
                        }
                    }
                }
                //movement = 0;
                //patrol = 0;
                //attack = 0;
                //mine = 0;
                //ability = 0;
            }
        });
        /*
        attackButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                attack = 1;
                return true;
            }
        });

        patrolButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                patrol = 1;
                return true;
            }
        });*/

//        stage.addActor(movementButton);
//        stage.addActor(stopButton);
//        stage.addActor(patrolButton);
//        stage.addActor(attackButton);

	    mapCamera = new OrthographicCamera();
        mapViewport = new FitViewport(Gdx.graphics.getWidth() * .75f, Gdx.graphics.getHeight(), mapCamera);
        mapStage = new Stage(mapViewport);

        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();
        // set size of map viewport to 75% of the screen width and 100% height
        mapStage.getViewport().update(Math.round(Gdx.graphics.getWidth() * .75f), Gdx.graphics.getHeight(), false);
        // position map viewport on right 75% of the screen
        mapStage.getViewport().setScreenBounds(Math.round(Gdx.graphics.getWidth() * .25f), 0, Math.round(Gdx.graphics.getWidth() * .75f), Gdx.graphics.getHeight());
        mapStage.getViewport().apply();

        sidebarCamera = new OrthographicCamera();
//        sidebarCamera.setToOrtho(false, Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight());
        sidebarViewport = new FitViewport(Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight(), sidebarCamera);
        sidebarStage = new Stage(sidebarViewport);

        sidebarStage.getViewport().apply();
        sidebarStage.act();
        sidebarStage.draw();
        // set size of sidebar viewport to 25% of the screen width and 100% height
        sidebarStage.getViewport().update(Math.round(Gdx.graphics.getWidth() * .25f), Gdx.graphics.getHeight(), false);
        // position sidebar viewport on left 25% of the screen
        sidebarStage.getViewport().setScreenBounds(0, 0, Math.round(Gdx.graphics.getWidth() * .25f), Gdx.graphics.getHeight());
        sidebarStage.getViewport().apply();

        // set background texture image for sidebar menu
        // code adapted from https://libgdx.info/basic_image/
        Texture backgroundImageTexture = new Texture("img/Texture.png");
        backgroundImageTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion backgroundImageTextureRegion = new TextureRegion(backgroundImageTexture);
        backgroundImageTextureRegion.setRegion(0, 0, sidebarStage.getWidth(), sidebarStage.getHeight());

        //backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Image backgroundImage = new Image(backgroundImageTextureRegion);
        backgroundImage.setPosition(0, 0);
        sidebarStage.addActor(backgroundImage);


        // table for layout of sidebar
        sidebarTable = new Table();
        sidebarTable.setDebug(true, true); // TODO: remove when done laying out table
        sidebarTable.setFillParent(true);
        sidebarTable.align(Align.top);
        sidebarStage.addActor(sidebarTable);
        sidebarStage.draw();

        /*
        Label nameLabel = new Label("Name:", skin);
        TextField nameText = new TextField("", skin);
        Label addressLabel = new Label("Address:", skin);
        TextField addressText = new TextField("", skin);
         */

        //add buttons to the sidebar menu
        sidebarTable.add(menuButton).width(sidebarStage.getWidth() * .5f);
        sidebarTable.add(pauseButton).width(sidebarStage.getWidth() * .5f);
        sidebarTable.row();
        sidebarTable.add(attackButton).width(sidebarStage.getWidth()).height(200).colspan(2);
        sidebarTable.row();
        sidebarTable.add(patrolButton).width(sidebarStage.getWidth()).height(200).colspan(2);
        sidebarTable.row();
        sidebarTable.add(stopButton).width(sidebarStage.getWidth()).height(200).colspan(2);
        sidebarTable.row();
        sidebarTable.add(moveButton).width(sidebarStage.getWidth()).height(200).colspan(2);
        sidebarTable.row();
        sidebarTable.add(selectCount).width(sidebarStage.getWidth()).height(200).colspan(2);
        sidebarTable.row();
        sidebarTable.add(selectButton).width(sidebarStage.getWidth()).height(200).colspan(2);

        /// REMOVE
        sidebarTable.row();
        sidebarTable.add(buildButton).width(sidebarStage.getWidth()).height(200).colspan(2);

        sidebarStage.draw();

//        sidebarStage.getViewport().getCamera().lookAt(0,0,0);
//        sidebarStage.getViewport().getCamera().update();


        gameData.RenderMap(); // renders the map.
        orthomaprenderer = new OrthogonalTiledMapRenderer(gameData.tiledMap);

//        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);

        multiplexer = new InputMultiplexer(mapStage, sidebarStage);

        multiplexer.addProcessor(new GestureDetector(this));
        multiplexer.addProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                // get start finger position for drag select rectangle
                // convert x and y from screen coordinates to viewport coordinates
                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = mapViewport.unproject(clickCoordinates);

                // set start position of multi-selection rectangle
                touchEndX = position.x;
                touchEndY = position.y;
                touchStartX = position.x;
                touchStartY = position.y;

                if (selectButton.isPressed()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // get end finger position for drag select rectangle
                // convert x and y from screen coordinates to viewport coordinates
                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = mapViewport.unproject(clickCoordinates);
                touchEndX = position.x;
                touchEndY = position.y;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return true;
            }

            @Override
            public boolean scrolled(int amount) {
                return true;
            }
        });

        //multiplexer.addProcessor(new InputMultiplexer(mapStage));

        Gdx.input.setInputProcessor(multiplexer);
        // Gdx.input.setInputProcessor(stage);
        //Gdx.input.setInputProcessor(new GestureDetector(this));

        // calculate zoom levels to show entire map height/width
        heightZoomRatio = gameData.map.Height() * gameData.TILE_HEIGHT / mapCamera.viewportHeight;
        widthZoomRatio = gameData.map.Width() * gameData.TILE_WIDTH / mapCamera.viewportWidth;
        gameData.elapsedTime = 0;

        gameData.allUnits.AddUnit(690,3, GameDataTypes.EUnitType.Archer, GameDataTypes.EPlayerColor.Black);
        gameData.allUnits.AddUnit(600,4, GameDataTypes.EUnitType.Footman, GameDataTypes.EPlayerColor.Green);
        gameData.allUnits.AddUnit(770,40, GameDataTypes.EUnitType.Peasant, GameDataTypes.EPlayerColor.Orange);
        gameData.allUnits.AddUnit(900,68, GameDataTypes.EUnitType.Ranger, GameDataTypes.EPlayerColor.Purple);
        gameData.allUnits.AddUnit(690,90, GameDataTypes.EUnitType.Knight, GameDataTypes.EPlayerColor.White);

        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for (Unit.IndividualUnit cur : allUnits.unitMap.get(color)) {
                mapStage.addActor(cur);
            }
        }
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameData.elapsedTime += Gdx.graphics.getDeltaTime();
        gameData.cumulativeTime += Gdx.graphics.getRawDeltaTime();

        gameData.TimeStep();

/*        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();*/

        batch.begin();

        orthomaprenderer.setView(mapCamera);
        orthomaprenderer.render();

        // draw multi-selection rectangle
        mapCamera.update();
        if(selectButton.isPressed()) {
            shapeRenderer.setProjectionMatrix(mapCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(touchStartX, touchStartY, touchEndX - touchStartX, touchEndY - touchStartY);
            shapeRenderer.end();
        } else {
            touchEndX = 0;
            touchEndY = 0;
            touchStartY = 0;
            touchStartX = 0;
        }

        batch.end();


        allUnits.UnitStateHandler(gameData.elapsedTime, gameData.map);
        allUnits.updateVector();

/*
        sb.setProjectionMatrix(mapCamera.combined);
        sb.begin();
        gameData.staticAssetRenderer.DrawEffects(sb,delta);
        sb.end();
*/

	    sidebarStage.getViewport().apply();
        sidebarStage.act();
        sidebarStage.draw();
        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();
    }

    public void specialButtons() {
        int counter = 0;
//        for (Actor actor : stage.getActors()) {
//            if (counter > 3)
//                actor.remove();
//            counter = counter + 1;
//        }
        /*
        for (int i = 0; i < allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).abilities.size(); i++) {
            if (allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).abilities.elementAt(i) == GameDataTypes.EAssetCapabilityType.Mine) {
                newAbility = new TextButton("Mine", gameData.unitActions.skin);
                newAbility.setPosition(5, 70+(20*(i+1))+((3+i+1)*Gdx.graphics.getHeight() / 10));
                newAbility.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        mine = 1;
                        return true;
                    }
                });
//                stage.addActor(newAbility);
            }
            if (allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).abilities.elementAt(i) == GameDataTypes.EAssetCapabilityType.RangerScouting) {
                newAbility = new TextButton("Ranger Scouting", gameData.unitActions.skin);
                newAbility.setPosition(5, 70 + (20 * (i + 1)) + ((3 + i + 1) * Gdx.graphics.getHeight() / 10));
                newAbility.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        ability = 1;
                        return true;
                    }
                });
//                stage.addActor(newAbility);
            }
        }
        */
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
        gameData.dispose();
        mapStage.dispose();
        sidebarStage.dispose();
        orthomaprenderer.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(x,y,0);
        Vector3 position = mapViewport.unproject(clickCoordinates);

        // TODO: maybe move this to a element in GameData
        Unit.IndividualUnit sUnit = null;
        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for (Unit.IndividualUnit cur : allUnits.unitMap.get(color)) {
                if (cur.selected) {
                    sUnit = cur;
                }
            }
        }

        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for (Unit.IndividualUnit cur : allUnits.unitMap.get(color)) {
                if (cur.touched) {
                    System.out.println("Unit is selected");
                    if (moveButton.isPressed() || patrolButton.isPressed() || stopButton.isPressed()) {
                        // if moveButton etc pressed, move instead of selecting
                    } else if (sUnit != null && cur.color != sUnit.color) {
                        // if opposing teams, attack
                        sUnit.target = cur;
                        sUnit.currentxmove = cur.getMidX();
                        sUnit.currentymove = cur.getMidY();
                        sUnit.curState = GameDataTypes.EUnitState.Attack;
                    } else {
                        cur.touched = false;
                        cur.selected = true;
                        if (sUnit != null) {
                            sUnit.selected = false;
                        }
                    }
                }
            }
        }

        if (sUnit != null) {
            if (moveButton.isPressed()) {
                sUnit.currentxmove = round(position.x);
                sUnit.currentymove = round(position.y);
                sUnit.curState = GameDataTypes.EUnitState.Move;
            } else if (patrolButton.isPressed()) {
                sUnit.currentxmove = round(position.x);
                sUnit.currentymove = round(position.y);
                sUnit.patrolxmove = sUnit.getMidX();
                sUnit.patrolymove = sUnit.getMidY();
                sUnit.curState = GameDataTypes.EUnitState.Patrol;
            } else if (stopButton.isPressed()) {

            } else if (attackButton.isPressed()) {

            } else if (buildButton.isPressed()) { // Replace this when Camera gets us buttons
                // REMOVE WHEN DONE TESTING
                //sUnit.curState = GameDataTypes.EUnitState.BuildBarracks;
            } else {
                // still need to check for mine, forest, attack(ish), etc
                sUnit.selected = false;
            }
        }

        //specialButtons(); We still need a variant of this function, but not here

        //TODO
        //if (unit_selected == 0 && assetSelected == 1){
        //  if (assetSelected == Goldmine && mine == 1) {
        //    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Mine;
        //  allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
        // allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
        //mine = 1;
        //}
        //}
        return true;

        //return false;
    }

    private void selectUnits(Vector3 position) {
        int counter = 0;
        int unit_selected = 0;
        selectedUnits = new Vector<Unit.IndividualUnit>();
        // determine position of each edge of multi-select rectangle
        float leftX = Math.min(touchStartX, position.x);
        float rightX = Math.max(touchStartX, position.x);
        float topY = Math.min(touchStartY, position.y);
        float bottomY = Math.max(touchStartY, position.y);
/*
        while(counter < allUnits.unitVector.size()){
            Unit.IndividualUnit temp_peasant = allUnits.unitVector.elementAt(counter);
            // if (clicked within peasant || part of peasant within multi-select rectangle)
            if ((temp_peasant.getX() <= position.x
                    && temp_peasant.getX() + temp_peasant.getWidth() >= position.x
                    && temp_peasant.getY() <= position.y
                    && temp_peasant.getY() + temp_peasant.getHeight() >= position.y)
                    ||
                    (temp_peasant.getX() <= rightX
                            && temp_peasant.getX() + temp_peasant.getWidth() >= leftX
                            && temp_peasant.getY() <= bottomY
                            && temp_peasant.getY() + temp_peasant.getHeight() >= topY)) {
                //peasant.setPosition(peasant.getX()+1, peasant.getY()+1);
                // TODO Play Peasant Sound here - do this in the Peasant class? so diff units can play diff sounds -KT
                // PEASANT SELECTED ==
                selectedUnits.add(allUnits.unitVector.elementAt(counter));
                if (attack == 1) {
                    unit_selected = 1;
                    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).target = allUnits.unitVector.elementAt(counter);
                    attack = 0;
                    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Attack;
                    break;
                }
                //TODO
                //if (ability == 1) {
                //}
                allUnits.selectedUnitIndex = counter;
                unit_selected = 1;
                allUnits.unitVector.elementAt(counter).selected = true;
            } else {
                allUnits.unitVector.elementAt(counter).selected = false;
            }
            counter+=1;
        }
        specialButtons();
        //if asset is at position.x position.y then assetSelected = 1 and selectedAsset =  asset
        if (unit_selected == 0 && movement == 1) {
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Move;
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
	        movement = 0;
        }
        if (unit_selected == 0 && patrol == 1) {
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Patrol;
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).patrolxmove = allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).getMidX();
            allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).patrolymove = allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).getMidY();
            patrol = 0;
        }*/
        //TODO
        //if (unit_selected == 0 && assetSelected == 1){
        //  if (assetSelected == Goldmine && mine == 1) {
        //    allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).curState = GameDataTypes.EUnitState.Mine;
        //  allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentymove = round(position.y);
        // allUnits.unitVector.elementAt(allUnits.selectedUnitIndex).currentxmove = round(position.x);
        //mine = 1;
        //}
        //}

        //Add to sidebar selected peasants
        selectCount.setText(Integer.toString(selectedUnits.size()));
        sidebarStage.draw();
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        /*
        //Gdx.graphics.getWidth()*.25f is the space of the sidebar menu
        CameraPosition camerePosition = new CameraPosition((int)((x - Gdx.graphics.getWidth()*.25)/.75), (int)y, mapCamera);
        TilePosition tilePosition = camerePosition.getTilePosition();
        int xi = tilePosition.X();
        int yi = tilePosition.Y();
        PlayerData player1 = gameData.playerData.get(0);


        // REMOVING RESOURCES
        int resourceRemove = 100;
        gameData.RemoveLumber(new TilePosition(xi+1, yi), tilePosition, resourceRemove);
        gameData.RemoveLumber(new TilePosition(xi-1, yi), tilePosition, resourceRemove);
        gameData.RemoveLumber(new TilePosition(xi, yi+1), tilePosition, resourceRemove);
        gameData.RemoveLumber(new TilePosition(xi, yi-1), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi+1, yi), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi-1, yi), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi, yi+1), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi, yi-1), tilePosition, resourceRemove);


        StaticAsset chosenStatAsset = gameData.map.StaticAssetAt(tilePosition);
        if (chosenStatAsset == null){
            System.out.println("No asset here...building");
            //GameDataTypes.EStaticAssetType AssetTypeToBuild = GameDataTypes.EStaticAssetType.values()[(lastbuiltasset%11) +1];
            GameDataTypes.EStaticAssetType AssetTypeToBuild = GameDataTypes.EStaticAssetType.Wall;

            if (gameData.map.CanPlaceStaticAsset(tilePosition, AssetTypeToBuild)) {
                player1.ConstructStaticAsset(tilePosition, GameDataTypes.to_assetType(AssetTypeToBuild), gameData.map);
                lastbuiltasset++;
            }
        }
        else {
            System.out.println("Asset found." + chosenStatAsset.assetType().Name() + " HP: " + String.valueOf(chosenStatAsset.hitPoints()));
            chosenStatAsset.decrementHitPoints(75);
        }
        */
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        //Gdx.graphics.getWidth()*.25 is the space of the sidebar menu, /.75 to scale to the coordinates of the map
        CameraPosition camerePosition = new CameraPosition((int)((x - Gdx.graphics.getWidth()*.25)/.75), (int)y, mapCamera);
        TilePosition tilePosition = camerePosition.getTilePosition();
        int xi = tilePosition.X();
        int yi = tilePosition.Y();
        log.info("Tile position: " + xi +" " + yi);
        int resourceRemove = 200;
        gameData.RemoveLumber(new TilePosition(xi+1, yi), tilePosition, resourceRemove);
        gameData.RemoveLumber(new TilePosition(xi-1, yi), tilePosition, resourceRemove);
        gameData.RemoveLumber(new TilePosition(xi, yi+1), tilePosition, resourceRemove);
        gameData.RemoveLumber(new TilePosition(xi, yi-1), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi+1, yi), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi-1, yi), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi, yi+1), tilePosition, resourceRemove);
        gameData.RemoveStone(new TilePosition(xi, yi-1), tilePosition, resourceRemove);
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (selectButton.isPressed() || attackButton.isPressed() || patrolButton.isPressed() || stopButton.isPressed() || moveButton.isPressed()) {

            return false;
        }
        if (Math.abs(distance) <= 1) {
            return false;
        }
        if (initialDistance != prevDistance) {
            prevDistance = initialDistance;
            prevZoom = mapCamera.zoom;
        }

        float ratio = initialDistance / distance;
        float newZoomLevel = prevZoom * ratio;
        // change zoom level only if above minimum level

        if (.25f <= newZoomLevel) {
            mapCamera.zoom = newZoomLevel;
        }

        // limit zoom to showing full map
        calculateCameraBounds();
        mapCamera.update();

        return true;
    }

    private void calculateCameraBounds() {
        // code adapted from https://gamedev.stackexchange.com/questions/74926/libgdx-keep-camera-within-bounds-of-tiledmap
        // The left boundary of the map (x)
        int mapLeft = 0;
        // The right boundary of the map (x + width)
        int mapRight = gameData.map.Width() * gameData.TILE_WIDTH;
        // The bottom boundary of the map (y)
        int mapBottom = 0;
        // The top boundary of the map (y + height)
        int mapTop = gameData.map.Height() * gameData.TILE_HEIGHT;

        // The mapCamera dimensions, halved
        float cameraHalfWidth = mapCamera.viewportWidth * mapCamera.zoom * .5f;
        float cameraHalfHeight = mapCamera.viewportHeight * mapCamera.zoom * .5f;

        // calculate positions of boundaries of mapCamera
        float cameraLeft = mapCamera.position.x - cameraHalfWidth;
        float cameraRight = mapCamera.position.x + cameraHalfWidth;
        float cameraBottom = mapCamera.position.y - cameraHalfHeight;
        float cameraTop = mapCamera.position.y + cameraHalfHeight;

        // Horizontal axis
        // if map width is smaller than viewport width
        if (gameData.map.Width() * gameData.TILE_HEIGHT / mapCamera.zoom < mapCamera.viewportWidth) {
            // if can zoom out more to show entire map height
            if (widthZoomRatio < heightZoomRatio) {
                // position mapCamera at center of map horizontally
                mapCamera.position.x = mapRight / 2;
            } else {
                // limit zoom to fit width of map
                mapCamera.zoom = widthZoomRatio;
            }
        }
        // else if left boundary of mapCamera is outside of map's left boundary
        else if (cameraLeft <= mapLeft) {
            // align mapCamera and map's left edge
            mapCamera.position.x = mapLeft + cameraHalfWidth;
        }
        // else if right boundary of mapCamera is outside of map's right boundary
        else if (cameraRight >= mapRight) {
            // align mapCamera and map's right edge
            mapCamera.position.x = mapRight - cameraHalfWidth;
        }

        // Vertical axis
        // if map height is smaller than viewport height
        if (gameData.map.Height() * gameData.TILE_HEIGHT / mapCamera.zoom < mapCamera.viewportHeight) {
            // if can zoom out more to show entire map width
            if (widthZoomRatio > heightZoomRatio) {
                // position mapCamera at center of map vertically
                mapCamera.position.y = mapTop / 2;
            } else {
                // limit zoom to fit height of map
                mapCamera.zoom = heightZoomRatio;
            }
        }
        // else if bottom boundary of mapCamera is outside of map's bottom boundary
        else if (cameraBottom <= mapBottom) {
            // align mapCamera and map's bottom edge
            mapCamera.position.y = mapBottom + cameraHalfHeight;
        }
        // else if top boundary of mapCamera is outside of map's top boundary
        else if (cameraTop >= mapTop) {
            // align mapCamera and map's top edge
            mapCamera.position.y = mapTop - cameraHalfHeight;
        }

    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        if (selectButton.isPressed()) {
            Vector3 clickCoordinates = new Vector3(pointer2.x, pointer2.y, 0);
            Vector3 position = mapViewport.unproject(clickCoordinates);
            touchEndX = position.x;
            touchEndY = position.y;
        }
        return false;
    }

    @Override
    public void pinchStop() {

    }

    private void KimisTestFunction(){
      //TESTING REMOVELUMBER
        TilePosition tposunit = new TilePosition(12,1);
        TilePosition tree1 = new TilePosition(11,0);
        TilePosition tree2 = new TilePosition(12,1);
        TilePosition tree3 = new TilePosition(13,2);

        gameData.RemoveLumber(tree1,tposunit,400);
        gameData.RemoveLumber(tree2,tposunit,400);
        gameData.RemoveLumber(tree3,tposunit,400);

        // TESTING STATICASSETAT
        TilePosition sassetAt = new TilePosition(0,0);
        StaticAsset sasset = gameData.map.StaticAssetAt(sassetAt);
        if (sasset != null){
            System.out.println(sasset.assetType().Name());
        }
        else{
            System.out.println("no mr. asset here");
        }

        TilePosition sassetAt1 = new TilePosition(0,1);
        StaticAsset sasset1 = gameData.map.StaticAssetAt(sassetAt1);
        if (sasset1 != null){
            System.out.println(sasset1.assetType().Name());
        }
        else{
            System.out.println("no mr. asset 1 here");
        }

        TilePosition sassetAt2 = new TilePosition(15,1);
        StaticAsset sasset2 = gameData.map.StaticAssetAt(sassetAt2);
        if (sasset2 != null){
            System.out.println(sasset2.assetType().Name());
        }
        else{
            System.out.println("no mr. asset 2 here");
        }

        TilePosition sassetAt3 = new TilePosition(1,30);
        StaticAsset sasset3 = gameData.map.StaticAssetAt(sassetAt3);
        if (sasset3 != null){
            System.out.println(sasset3.assetType().Name());
        }
        else{
            System.out.println("no mr. asset 3 here");
        }
    }
}
