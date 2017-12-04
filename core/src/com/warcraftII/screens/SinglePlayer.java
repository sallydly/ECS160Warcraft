package com.warcraftII.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.warcraftII.GameData;
import com.warcraftII.GameDataTypes;
import com.warcraftII.Volume;
import com.warcraftII.Warcraft;

import com.warcraftII.player_asset.PlayerAssetType;
import com.warcraftII.player_asset.PlayerCapability;
import com.warcraftII.player_asset.StaticAsset;
import com.warcraftII.position.CameraPosition;
import com.warcraftII.position.Position;
import com.warcraftII.position.TilePosition;
import com.warcraftII.position.UnitPosition;
import com.warcraftII.terrain_map.TileTypes;
import com.warcraftII.units.Unit;
import com.warcraftII.units.UnitActionRenderer;

import java.util.Vector;


import static java.lang.Math.min;
import static java.lang.Math.round;


public class SinglePlayer implements Screen, GestureDetector.GestureListener{
    private Logger log = new Logger("SinglePlayer", 2);
    private Warcraft game;
    private GameData gameData;
    private Music music;
    // More concise access to data members of gameData:
    private Unit allUnits;
    public Vector<Unit.IndividualUnit> selectedUnits;
    private SpriteBatch batch;
    private SpriteBatch sb;

    private int movement;
    public int attack;
    public int patrol;
    public int mine;
    public int ability;
    private InputMultiplexer multiplexer;

    Skin skin;

    private TextButton moveButton;
    private TextButton standGroundButton;
    private TextButton attackButton;
    private TextButton patrolButton;
    private TextButton repairButton;
    private TextButton mineButton;
    private TextButton buildSimpleButton;
    private TextButton buildAdvancedButton;
    private TextButton buildKeepButton;
    private TextButton buildLumberMillButton;
    private TextButton buildFarmButton;

    private TextButton newAbility;

    private TextButton selectButton;
    private TextButton cancelButton;
    private Label selectCount;
    private TextureAtlas sidebarIconAtlas;

    public OrthogonalTiledMapRenderer orthomaprenderer;
    private OrthographicCamera mapCamera;
    private FitViewport mapViewport;
    private Stage mapStage;

    private OrthographicCamera sidebarCamera;
    private FitViewport sidebarViewport;
    private Stage sidebarStage;

    private OrthographicCamera topbarCamera;
    private FitViewport topbarViewport;
    private Stage topbarStage;

    //For topbar:
    private TextField lumberCount,goldCount,stoneCount;

    private Table sidebarTable;
    private Table sidebarIconTable;
    private Table topbarTable;

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


    UnitActionRenderer unitActionRenderer;
    private Vector<GameDataTypes.EAssetCapabilityType> capabilities;

    private GameDataTypes.EStaticAssetType assetToBuild;
    private GameDataTypes.EUnitType unitToBuild;

    private boolean isAssetSelected;
    private StaticAsset selectedAsset;

    private boolean buildSimpleButtonIsPressed;
    private boolean buildAdvancedButtonIsPressed;
    private boolean isPlacingBuilding;



    //For wall building:
     private boolean wallStarted = false;

     //DEBUG:
     //TODO: Determine this from the button
     GameDataTypes.EStaticAssetType typetobebuilt = GameDataTypes.EStaticAssetType.TownHall;



    SinglePlayer(com.warcraftII.Warcraft game) {
        this.game = game;
        gameData = new GameData(game.DMapName); // IMPORTANT
        selectedUnits = gameData.selectedUnits;

        // Leave this on. Nothing updates without constant input if it's off.
        Gdx.graphics.setContinuousRendering(true);

        // initialize easy-access reference variables.
        batch = gameData.batch = game.batch;
        allUnits = gameData.allUnits;
        sb = gameData.sb;

        Gdx.graphics.setContinuousRendering(true);

        //randomly play one of 5 game.mp3 files
        int musicNum = (int) (Math.random() * 5) + 1;
        assert musicNum >=1 && musicNum <= 5;
        this.music = Gdx.audio.newMusic(Gdx.files.internal("data/snd/music/game" + musicNum + ".mp3"));
        this.music.setVolume( (Volume.getMusicVolume() / 100));
        this.music.setLooping(true);

        this.shapeRenderer = new ShapeRenderer();
        //add the menu and pause buttons
        TextureAtlas atlas = new TextureAtlas("skin/craftacular-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"), atlas);
        moveButton = new TextButton("Move", skin);
        standGroundButton = new TextButton("Stand Ground", skin);
        patrolButton = new TextButton("Patrol", skin);
        attackButton = new TextButton("Attack", skin);
        selectButton = new TextButton("Select", skin);
        cancelButton = new TextButton("Cancel", skin);
        repairButton = new TextButton("Repair", skin);
        mineButton = new TextButton("Mine", skin);
        buildSimpleButton = new TextButton("Build", skin);
        buildAdvancedButton = new TextButton("Upgrade",skin);
        buildKeepButton = new TextButton("Build Keep", skin);
        buildLumberMillButton = new TextButton("Build Lumber Mill", skin);
        buildFarmButton = new TextButton("Build Farm", skin);

        selectCount = new Label("", skin);

        sidebarIconAtlas = new TextureAtlas(Gdx.files.internal("atlas/icons.atlas"));
        unitActionRenderer = new UnitActionRenderer(gameData.playerData.get(1).Color(), gameData.playerData.get(1));
        buildSimpleButtonIsPressed = false;
        buildAdvancedButtonIsPressed = false;
        /*standGroundButton.addListener(new ClickListener() {
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
        // get current finger position for drag select rectangle
        // convert x and y from screen coordinates to viewport coordinates
        Vector3 clickCoordinates = new Vector3(x, y, 0);
        Vector3 position = mapViewport.unproject(clickCoordinates);
        touchEndX = position.x;
        touchEndY = position.y;

        // adjust pointer drag amount by mapCamera zoom level
        if(!selectButton.isPressed() && !isPlacingBuilding) {
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
        music.play();

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

        // Make Buttons for the Unit Actions
        gameData.unitActions.createBasicSkin();

        moveButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                movement = 1;
                return true;
            }
        });

        standGroundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (Unit.IndividualUnit cur : selectedUnits) {
                    cur.stopMovement();
                }
                //movement = 0;
                //patrol = 0;
                //attack = 0;
                //mine = 0;
                //ability = 0;
            }
        });
        buildSimpleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildSimpleButtonIsPressed = true;
                buildAdvancedButtonIsPressed = false;
                fillSideBarTable();
            }
        });
        buildAdvancedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildSimpleButtonIsPressed = false;
                buildAdvancedButtonIsPressed = true;
                fillSideBarTable();
            }
        });
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buildSimpleButtonIsPressed = false;
                buildAdvancedButtonIsPressed = false;
                fillSideBarTable();
            }
        });

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
        });

        mapCamera = new OrthographicCamera();
        mapViewport = new FitViewport(Gdx.graphics.getWidth() * .75f, Gdx.graphics.getHeight() * .95f, mapCamera);
        mapStage = new Stage(mapViewport);

        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();
        // set size of map viewport to 75% of the screen width and 93% height
        mapStage.getViewport().update(Math.round(Gdx.graphics.getWidth() * .75f), Math.round(Gdx.graphics.getHeight() * .95f), false);
        // position map viewport on right 75% of the screen
        mapStage.getViewport().setScreenBounds(Math.round(Gdx.graphics.getWidth() * .25f), 0, Math.round(Gdx.graphics.getWidth() * .75f), Math.round(Gdx.graphics.getHeight() * .95f));
        mapStage.getViewport().apply();

        sidebarCamera = new OrthographicCamera();
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

        //topbar
        topbarCamera = new OrthographicCamera();
        topbarViewport = new FitViewport(Gdx.graphics.getWidth() * .75f, Math.round(Gdx.graphics.getHeight() * .05f), topbarCamera);
        topbarStage = new Stage(topbarViewport);

        topbarStage.getViewport().apply();
        topbarStage.act();
        topbarStage.draw();
        // set size of topbar viewport to 75% of the screen width and 7% height
        topbarStage.getViewport().update(Math.round(Gdx.graphics.getWidth() * .75f), Math.round(Gdx.graphics.getHeight() * .05f), false);
        // position topbar viewport on right 75% of the screen and 7% on top
        topbarStage.getViewport().setScreenBounds(Math.round(Gdx.graphics.getWidth() * .25f), Math.round(Gdx.graphics.getHeight() * .95f), Math.round(Gdx.graphics.getWidth() * .75f), Math.round(Gdx.graphics.getHeight() * .05f));
        topbarStage.getViewport().apply();

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

        // set background texture image for topbar
        TextureRegion topbackgroundImageTextureRegion = new TextureRegion(backgroundImageTexture);
        topbackgroundImageTextureRegion.setRegion(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Image topbackgroundImage = new Image(topbackgroundImageTextureRegion);
        topbarStage.addActor(topbackgroundImage);


        // table for layout of sidebar
        sidebarTable = new Table();
        sidebarTable.setDebug(true, true); // TODO: remove when done laying out table
        sidebarTable.setFillParent(true);
        sidebarTable.align(Align.top);
        sidebarStage.addActor(sidebarTable);
        sidebarStage.draw();
        fillSideBarTable();

//        sidebarIconTable = new Table();
//        TextureAtlas.AtlasRegion region = sidebarIconAtlas.findRegion("build-simple");
//        Image sidebarIconImage = new Image(region);
//        sidebarIconTable.add(sidebarIconImage).width(sidebarStage.getWidth() / 3).height(sidebarStage.getWidth() / 3);
//        region = sidebarIconAtlas.findRegion("alchemist");
//        sidebarIconImage = new Image(region);
//        sidebarIconTable.add(sidebarIconImage).width(sidebarStage.getWidth() / 3).height(sidebarStage.getWidth() / 3);
//
//        if (selectedUnits.size() > 0) {
//            region = sidebarIconAtlas.findRegion("altar");
//            sidebarIconImage = new Image(region);
//            sidebarIconTable.add(sidebarIconImage).width(sidebarStage.getWidth() / 3).height(sidebarStage.getWidth() / 3);
//        }
//        sidebarIconTable.row();
//        sidebarTable.add(sidebarIconTable).width(sidebarStage.getWidth()).height(sidebarStage.getWidth()).colspan(2);
//        sidebarTable.row();

        //Table for the topbar
        topbarTable = new Table();
        topbarTable.setDebug(true, true); // TODO: remove when done laying out table
        topbarTable.setFillParent(true);
        topbarStage.addActor(topbarTable);

        //TextureRegions to split the mini icons for the topbar
        Texture miniIcons = new Texture(Gdx.files.internal("img/MiniIcons.png"));
        TextureRegion gold = new TextureRegion(miniIcons, 0, 0,16,16);
        TextureRegion lumber = new TextureRegion(miniIcons, 0, 16,16,16);
        //TextureRegion food = new TextureRegion(miniIcons, 0, 32,16,16); // We ain't doing food either
        //TextureRegion oil = new TextureRegion(miniIcons, 0, 48,16,16); // We ain't using oil.
        TextureRegion stone = new TextureRegion(miniIcons, 0, 64,16,16); // But we are using stone

        //Images of gold, lumber, food and oil
        Image goldImage = new Image(gold);
        Image lumberImage = new Image(lumber);
        //Image foodImage = new Image(food); // No food
        //Image oilImage = new Image(oil); // We ain't using oil.
        Image stoneImage = new Image(stone); // But we are using stone


        //Textfields to keep track for gold, lumber, food and oil
        goldCount = new TextField("", skin);
        lumberCount = new TextField("", skin);
        //foodCount = new TextField("", skin); no food
        //oilCount = new TextField("", skin); // We ain't using oil.
        stoneCount = new TextField("", skin); // But we are using stone.



        topbarTable.add(goldImage).width(topbarStage.getHeight()).height(topbarStage.getHeight());
        topbarTable.add(goldCount).height(topbarStage.getHeight());
        topbarTable.add(lumberImage).width(topbarStage.getHeight()).height(topbarStage.getHeight());
        topbarTable.add(lumberCount).height(topbarStage.getHeight());
        //Not using food or oil
        /*topbarTable.add(foodImage).width(topbarStage.getHeight()).height(topbarStage.getHeight());
        topbarTable.add(foodCount).height(topbarStage.getHeight());
        topbarTable.add(oilImage).width(topbarStage.getHeight()).height(topbarStage.getHeight());
        topbarTable.add(oilCount).height(topbarStage.getHeight());*/
        // But we are using stone.
        topbarTable.add(stoneImage).width(topbarStage.getHeight()).height(topbarStage.getHeight());
        topbarTable.add(stoneCount).height(topbarStage.getHeight());

        topbarStage.draw();

        gameData.RenderMap(); // renders the map.
        orthomaprenderer = new OrthogonalTiledMapRenderer(gameData.tiledMap);

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

                if(isPlacingBuilding) {
                    TilePosition tpos = new TilePosition(new UnitPosition((int) touchEndX,(int) touchEndY));

                    //centering the staticasset about the touch:
                    tpos.Y(tpos.Y() - (int) (PlayerAssetType.StaticAssetSize(typetobebuilt)/2));
                    tpos.X(tpos.X() - (int) (PlayerAssetType.StaticAssetSize(typetobebuilt)/2));

                    if(typetobebuilt == GameDataTypes.EStaticAssetType.Wall)
                    {
                        gameData.staticAssetRenderer.DestroyShadowAsset(gameData.tiledMap, gameData.map);
                        wallStarted = false;
                        return false;
                    }


                    if (gameData.staticAssetRenderer.MoveShadowAsset(tpos, gameData.tiledMap, gameData.map)) {
                        gameData.playerData.get(1).ConstructStaticAsset(tpos, typetobebuilt, gameData.map);
                    }
                    gameData.staticAssetRenderer.DestroyShadowAsset(gameData.tiledMap, gameData.map);

//                    // TODO: adapt this to match types
//                    for (Unit.IndividualUnit sUnit : selectedUnits) {
//
                }



                if (selectButton.isPressed()) {
                    boolean newSelection = multiSelectUpdate(position);
                    updateSelected(position);
                }

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = mapViewport.unproject(clickCoordinates);
                touchEndX = position.x;
                touchEndY = position.y;


                if(buildSimpleButton.isPressed()) {
                    UnitPosition upos = new UnitPosition((int) position.x, (int) position.y);
                    TilePosition tpos = new TilePosition(upos);
                    //centering the staticasset about the touch:
                    tpos.Y(tpos.Y() - (int) (PlayerAssetType.StaticAssetSize(typetobebuilt) / 2));
                    tpos.X(tpos.X() - (int) (PlayerAssetType.StaticAssetSize(typetobebuilt) / 2));

                    if (typetobebuilt == GameDataTypes.EStaticAssetType.Wall) {

                        if (!wallStarted) {
                            if (gameData.map.CanPlaceStaticAsset(tpos, GameDataTypes.EStaticAssetType.Wall)) {
                                //shouldnt have a wall started if you can't afford it.  No check here
                                gameData.playerData.get(1).ConstructStaticAsset(tpos, GameDataTypes.EStaticAssetType.Wall, gameData.map);
                                gameData.staticAssetRenderer.DestroyShadowAsset(gameData.tiledMap, gameData.map);
                                wallStarted = true;
                            } else {
                                gameData.staticAssetRenderer.MoveShadowAsset(tpos, gameData.tiledMap, gameData.map);
                                wallStarted = false;
                            }
                        } else //wall already started
                        {
                            if (gameData.map.CanPlaceStaticAsset(tpos, GameDataTypes.EStaticAssetType.Wall) &&
                                    gameData.playerData.get(1).PlayerCanAffordAsset(GameDataTypes.EAssetType.Wall) == 0) {
                                gameData.playerData.get(1).ConstructStaticAsset(tpos, GameDataTypes.EStaticAssetType.Wall, gameData.map);
                            }
                        }
                        return false;
                    } else {

                        gameData.staticAssetRenderer.MoveShadowAsset(tpos, gameData.tiledMap, gameData.map);
                    }
                }
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

        gameData.allUnits.AddUnit(690,690, GameDataTypes.EUnitType.Archer, GameDataTypes.EPlayerColor.Red);
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

    private void fillSideBarTable() {
        sidebarTable.clearChildren();

        // determine context buttons based on selected units
        if (buildSimpleButtonIsPressed || buildAdvancedButtonIsPressed) {
            if(isAssetSelected){
                //capabilities = selectedAsset.assetType().CapabilitiesVector();
            }
            else {
                capabilities = unitActionRenderer.DrawUnitAction(selectedUnits, GameDataTypes.EAssetCapabilityType.BuildSimple);
            }
        } else {
            if (isAssetSelected) {
                capabilities = selectedAsset.assetType().CapabilitiesVector();
            } else {
                capabilities = unitActionRenderer.DrawUnitAction(selectedUnits, GameDataTypes.EAssetCapabilityType.None);

            }
        }
        log.info(capabilities.toString());

        if(isAssetSelected){
            if(buildSimpleButton.isPressed()) {
                for (final GameDataTypes.EAssetCapabilityType capabilityType : capabilities) {
                    TextButton newButton = null;
                    if (PlayerCapability.IsBuildingUnit(capabilityType) && PlayerCapability.AssetFromCapability(capabilityType) != GameDataTypes.EAssetType.None){
                        newButton = new TextButton(capabilityType.toString(), skin);
                    newButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            unitToBuild = GameDataTypes.to_unitType(PlayerCapability.AssetFromCapability(capabilityType));
                        }
                    });
                    sidebarTable.add(newButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                    sidebarTable.row();
                    }
                }
            }
            else if (buildAdvancedButtonIsPressed){
            for(final GameDataTypes.EAssetCapabilityType capabilityType : capabilities) {
                TextButton newButton = null;
                if (PlayerCapability.IsBuildingBuilding(capabilityType) && PlayerCapability.AssetFromCapability(capabilityType) != GameDataTypes.EAssetType.None) {
                    newButton = new TextButton(capabilityType.toString(), skin);
                    newButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            assetToBuild = GameDataTypes.to_staticAssetType(PlayerCapability.AssetFromCapability(capabilityType));
                        }
                    });
                    sidebarTable.add(newButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                    sidebarTable.row();
                }
            }
            }
            else{
                if (selectedAsset.assetType().UnitCapabilitiesVector().size()>0) {
                    sidebarTable.add(buildSimpleButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                    sidebarTable.row();
                }
                if(selectedAsset.assetType().BuildingCapabilitiesVector().size()>0) {
                    sidebarTable.add(buildAdvancedButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                    sidebarTable.row();
                }
            }

            // end case of static asset selected
        }else { // now dealing with unit being selected...
            if (buildSimpleButtonIsPressed){
                for (final GameDataTypes.EAssetCapabilityType capabilityType : capabilities) {
                    TextButton newButton = null;
                    if (PlayerCapability.IsBuildingBuilding(capabilityType) && PlayerCapability.AssetFromCapability(capabilityType) != GameDataTypes.EAssetType.None){
                        newButton = new TextButton(capabilityType.toString(), skin);
                        newButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                unitToBuild = GameDataTypes.to_unitType(PlayerCapability.AssetFromCapability(capabilityType));
                            }
                        });
                        sidebarTable.add(newButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                        sidebarTable.row();
                    }
                }
            }
            else{
                for (GameDataTypes.EAssetCapabilityType capabilityType : capabilities) {
                    switch (capabilityType) {
                        case None:
                            break;
                        case Move:
                            sidebarTable.add(moveButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case Repair:
                            sidebarTable.add(repairButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case Mine:
                            sidebarTable.add(mineButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case BuildSimple:
                            sidebarTable.add(buildSimpleButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case BuildAdvanced:
                            break;
                        case Convey:
                            break;
                        case Cancel:
                            sidebarTable.add(cancelButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case BuildWall:
                            break;
                        case Attack:
                            sidebarTable.add(attackButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case StandGround:
                            sidebarTable.add(standGroundButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case Patrol:
                            sidebarTable.add(patrolButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case WeaponUpgrade1:
                            break;
                        case WeaponUpgrade2:
                            break;
                        case WeaponUpgrade3:
                            break;
                        case ArrowUpgrade1:
                            break;
                        case ArrowUpgrade2:
                            break;
                        case ArrowUpgrade3:
                            break;
                        case ArmorUpgrade1:
                            break;
                        case ArmorUpgrade2:
                            break;
                        case ArmorUpgrade3:
                            break;
                        case Longbow:
                            break;
                        case RangerScouting:
                            break;
                        case Marksmanship:
                            break;
                        case Max:
                            break;

                        case BuildPeasant:
                            break;
                        case BuildFootman:
                            break;
                        case BuildArcher:
                            break;
                        case BuildRanger:
                            break;
                        case BuildFarm:
                            sidebarTable.add(buildFarmButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case BuildTownHall:
                            break;
                        case BuildBarracks:
                            break;
                        case BuildLumberMill:
                            sidebarTable.add(buildLumberMillButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case BuildBlacksmith:
                            break;
                        case BuildKeep:
                            sidebarTable.add(buildKeepButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight() / 10);
                            sidebarTable.row();
                            break;
                        case BuildCastle:
                            break;
                        case BuildScoutTower:
                            break;
                        case BuildGuardTower:
                            break;
                        case BuildCannonTower:
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (buildSimpleButtonIsPressed || buildAdvancedButtonIsPressed) {
            sidebarTable.add(cancelButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight()/10);
            sidebarTable.row();
        } else {
            sidebarTable.add(selectButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight()/10);
            sidebarTable.row();
        }

        sidebarTable.add(selectButton).width(sidebarStage.getWidth()).colspan(2).prefHeight(sidebarStage.getHeight()/10);
        sidebarStage.draw();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameData.elapsedTime += Gdx.graphics.getDeltaTime();
        gameData.cumulativeTime += Gdx.graphics.getRawDeltaTime();

        gameData.TimeStep();

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

        //This draws any fire/building explosion animations
        sb.setProjectionMatrix(mapCamera.combined);
        sb.begin();
        gameData.staticAssetRenderer.DrawEffects(sb,delta);
        sb.end();

        sidebarStage.getViewport().apply();
        sidebarStage.act();
        sidebarStage.draw();

        UpdateResourceCountDisplays();
        topbarStage.getViewport().apply();
        topbarStage.act();
        topbarStage.draw();

        allUnits.UnitStateHandler(gameData.elapsedTime, gameData);
        allUnits.updateVector();
        mapStage.getViewport().apply();
        mapStage.act();
        mapStage.draw();

        // Below are the selection boxes (they shouldn't fire at the same time)
        // Asset selection box drawing
        if (isAssetSelected == true) {

            int size = selectedAsset.Size();
            int xPos = selectedAsset.positionX();
            int yPos = gameData.map.Height() - selectedAsset.positionY() - size; //we want bottom left -> -1 for 0 index; -1 for size of box

            shapeRenderer.setProjectionMatrix(mapCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(xPos * gameData.TILE_HEIGHT , yPos * gameData.TILE_HEIGHT, size * gameData.TILE_HEIGHT, size * gameData.TILE_HEIGHT);
            shapeRenderer.end();
        }

        for (Unit.IndividualUnit sel : selectedUnits) {
            shapeRenderer.setProjectionMatrix(mapCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(sel.getX(), sel.getY(), sel.getWidth(), sel.getHeight());
            shapeRenderer.end();
        }
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

        touchEndX = position.x;
        touchEndY = position.y;
        touchStartX = position.x;
        touchStartY = position.y;


        if(isPlacingBuilding) {
            TilePosition tpos = new TilePosition(new UnitPosition((int) position.x,(int) position.y));

            typetobebuilt = GameDataTypes.EStaticAssetType.TownHall;

            //centering the staticasset about the touch:
            tpos.Y(tpos.Y() - (int) (PlayerAssetType.StaticAssetSize(typetobebuilt)/2));
            tpos.X(tpos.X() - (int) (PlayerAssetType.StaticAssetSize(typetobebuilt)/2));

            //TODO: determine the type to be built...and the player color
            gameData.staticAssetRenderer.CreateShadowAsset(typetobebuilt, GameDataTypes.EPlayerColor.values()[1], tpos, gameData.tiledMap, gameData.map);
        }


        //Asset Selection code here...I assume will override all others...?
        TilePosition tpos = new TilePosition(new UnitPosition((int) position.x, (int) position.y));

        //Vector<GameDataTypes.EAssetCapabilityType> capabilities;
        //or:
        //Vector<Boolean> capabilities;


        // move most of this to singleSelect?
        isAssetSelected = false;

        StaticAsset chosenStatAsset = gameData.map.StaticAssetAt(tpos);

        if (chosenStatAsset != null && !anyButtonHeld()) {
            isAssetSelected = true;
            selectedAsset = chosenStatAsset;
        } else {
            isAssetSelected = false;
            buildSimpleButtonIsPressed = false;
            selectedAsset = null;
            fillSideBarTable();
        }

        //Returns capabilities:
        if (isAssetSelected && (!attackButton.isPressed() && !selectedUnits.isEmpty())) {
            // Won't fire if selectedUnits are trying to attack it

            //capabilities = selectedAsset.assetType().CapabilitiesVector();//EAssetCapability
            //capabilities = selectedAsset.assetType().Capabilities();//booleans
            selectedUnits.removeAllElements(); // Removes all currently selected units?
            fillSideBarTable();
            return true; //Ignores all other asset selection?
        }

        // TODO: maybe move this to a element in GameData?
        boolean newSelection;
        if (selectButton.isPressed()) {
            newSelection = multiSelectUpdate(position);
        } else {
            newSelection = singleSelectUpdate();
        }

        if (updateSelected(position) && !newSelection) {
            selectedUnits.removeAllElements();
        } else if (!selectButton.isPressed()){
            fillSideBarTable();
        }


        return true;
    }

    private void UpdateResourceCountDisplays(){
        goldCount.setMessageText(String.valueOf(gameData.playerData.get(1).Gold()));
        lumberCount.setMessageText(String.valueOf(gameData.playerData.get(1).Lumber()));
        stoneCount.setMessageText(String.valueOf(gameData.playerData.get(1).Stone()));
    }

    private boolean singleSelectUpdate() {

        for (Unit.IndividualUnit cur : allUnits.GetAllUnits()) {
            // Second element in PlayerData is assumed to be the human player on this device (looks like it's blue)
             if (cur.touched) {
                if (moveButton.isPressed() || patrolButton.isPressed() || standGroundButton.isPressed() || repairButton.isPressed() || mineButton.isPressed() || buildSimpleButton.isPressed() || selectButton.isPressed()) {
                    // should be handled below
                } else if ((!selectedUnits.isEmpty()) && selectedUnits.firstElement().color != cur.color) {
                    for (Unit.IndividualUnit sel : selectedUnits) {
                        sel.target = cur;
                        sel.currentxmove = cur.getMidX();
                        sel.currentymove = cur.getMidY();
                        sel.curState = GameDataTypes.EUnitState.Attack;
                    }
                    cur.touched = false;
                } else if (cur.color == gameData.playerData.get(1).Color()){
                    cur.touched = false;
                    selectedUnits.removeAllElements();
                    selectedUnits.add(cur);
                }
                return true;
            }
        }
        return false;
    }

    private boolean multiSelectUpdate(Vector3 position) {

        // determine position of each edge of multi-select rectangle
        float leftX = Math.min(touchStartX, position.x);
        float rightX = Math.max(touchStartX, position.x);
        float topY = Math.min(touchStartY, position.y);
        float bottomY = Math.max(touchStartY, position.y);
        boolean newSelection = false;

        selectedUnits.removeAllElements();
        for (Unit.IndividualUnit cur : allUnits.GetAllUnits()){
            // if (clicked within peasant || part of peasant within multi-select rectangle)
            if (((cur.getX() <= position.x
                    && cur.getX() + cur.getWidth() >= position.x
                    && cur.getY() <= position.y
                    && cur.getY() + cur.getHeight() >= position.y)
                    ||
                    (cur.getX() <= rightX
                            && cur.getX() + cur.getWidth() >= leftX
                            && cur.getY() <= bottomY
                            && cur.getY() + cur.getHeight() >= topY))
                    && cur.color == gameData.playerData.get(1).Color()) {

                //if (!selectedUnits.isEmpty() && cur.color == selectedUnits.firstElement().color) {
                selectedUnits.add(cur);
                System.out.println("Unit in rectangle");
                //}
                //TODO
                //if (ability == 1) {
                //}
                newSelection = true;

            }
            cur.touched = false;
        }
/*
        Map<GameDataTypes.EPlayerColor, int> colorCount = new HashMap<GameDataTypes.EPlayerColor, int>();
        for (GameDataTypes.EPlayerColor color : GameDataTypes.EPlayerColor.values()) {
            for ()
        }
*/
        //Add to sidebar selected peasants
        //selectCount.setText(Integer.toString(selectedUnits.size()));
        //sidebarStage.draw();

        return newSelection;
    }

    // Returns true when selected isn't updated and should be deleted
    public boolean updateSelected(Vector3 position) {
        int usedCount = 0;
        if (!selectedUnits.isEmpty()) {
            for (Unit.IndividualUnit sUnit : selectedUnits) {
                if (moveButton.isPressed()) {
                    sUnit.currentxmove = round(position.x);
                    sUnit.currentymove = round(position.y);
                    sUnit.curState = GameDataTypes.EUnitState.Move;
                    usedCount += 1;
                } else if (patrolButton.isPressed()) {
                    sUnit.currentxmove = round(position.x);
                    sUnit.currentymove = round(position.y);
                    sUnit.patrolxmove = sUnit.getMidX();
                    sUnit.patrolymove = sUnit.getMidY();
                    sUnit.curState = GameDataTypes.EUnitState.Patrol;
                    usedCount += 1;
                } else if (standGroundButton.isPressed()) {
                    sUnit.stopMovement();
                    usedCount += 1;
                } else if (mineButton.isPressed()) {
                    TilePosition tilePos = new TilePosition(new UnitPosition(round(position.x), round(position.y)));
                    StaticAsset selectedAsset = gameData.map.StaticAssetAt(tilePos);
                    if (selectedAsset != null && selectedAsset.staticAssetType() == GameDataTypes.EStaticAssetType.GoldMine) {
                        sUnit.curState = GameDataTypes.EUnitState.Mine;
                        sUnit.currentxmove = round(position.x);
                        sUnit.currentymove = round(position.y);
                        sUnit.selectedAsset = selectedAsset;
                        sUnit.selectedTilePosition = tilePos;
                        usedCount += 1;
                    }
                    else if (gameData.map.TerrainTileType(tilePos) == TileTypes.ETerrainTileType.Forest) {
                        sUnit.curState = GameDataTypes.EUnitState.Lumber;
                        sUnit.currentxmove = round(position.x);
                        sUnit.currentymove = round(position.y);
                        sUnit.selectedTilePosition = tilePos;
                        usedCount += 1;
                    } else {
                        System.out.println("Can't mine that");
                    }
                } else if (attackButton.isPressed()) {
                    // This is handled in singleSelected because it needs to target whatever individual unit was touched
                    usedCount += 1;
                } else if (buildSimpleButton.isPressed()) {
                    // TODO: why
                    //CameraPosition cameraPosition = new CameraPosition((int)((round(position.x) - Gdx.graphics.getWidth()*.25)/.75), (int)round(position.y), mapCamera);
                    //sUnit.buildPos = cameraPosition.getTilePosition();
                    sUnit.buildPos = new TilePosition(new UnitPosition(round(position.x), round(position.y)));
                    sUnit.buildPos.Y(sUnit.buildPos.Y() - (PlayerAssetType.StaticAssetSize(typetobebuilt)/2));
                    sUnit.buildPos.X(sUnit.buildPos.X() - (PlayerAssetType.StaticAssetSize(typetobebuilt)/2));
                    //sUnit.buildPos = new TilePosition(new UnitPosition(round(position.x), round(position.y)+(2*Position.tileHeight())));
                    sUnit.currentxmove = round(position.x);
                    sUnit.currentymove = round(position.y);

                    // Determine based on typetobebuilt
                    sUnit.curState = GameDataTypes.EUnitState.BuildTownHall;
                } else if (repairButton.isPressed()) {
                    TilePosition tilePos = new TilePosition(new UnitPosition(round(position.x), round(position.y)));
                    StaticAsset selectedAsset = gameData.map.StaticAssetAt(tilePos);
                    if (selectedAsset != null) {
                        sUnit.curState = GameDataTypes.EUnitState.Repair;
                        sUnit.currentxmove = round(position.x);
                        sUnit.currentymove = round(position.y);
                        sUnit.selectedAsset = selectedAsset;
                    }
                    usedCount += 1;
                } else {
                    // still need to check for mine, forest, attack(ish), etc
                    // THIS SHOULD PROBABLY CHECK FOR IF ANY OF THE SELECTED HAVE THE CAPABILITY TO DO WHATEVER ACTION IT SHOULD BE
                }
            }
            if (usedCount > 0) {
                return false;
            } else {
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
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
        if (selectButton.isPressed() || attackButton.isPressed() || patrolButton.isPressed() || standGroundButton.isPressed() || moveButton.isPressed()) {

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

    public boolean anyButtonHeld() {
        return moveButton.isPressed() || standGroundButton.isPressed() || attackButton.isPressed() || repairButton.isPressed() || mineButton.isPressed() || buildSimpleButton.isPressed() || selectButton.isPressed();
    }
}