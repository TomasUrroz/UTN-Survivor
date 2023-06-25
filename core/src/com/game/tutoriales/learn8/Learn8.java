package com.game.tutoriales.learn8;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.game.Assets;
import com.game.Entities.Enemies.Zombie;
import com.game.Entities.Player;
import com.game.MainLearn;
import com.game.Screens;

import java.nio.channels.ScatteringByteChannel;


public class Learn8 extends Screens {

    World oWorld;

    Player player;
    Player player2;

    Array<Body> arrBodies;

    Box2DDebugRenderer renderer;

    public Learn8(MainLearn game) {
        super(game);
        AssetsLearn8.load(0);

        Vector2 gravity = new Vector2(0, 0);
        oWorld = new World(gravity, true);

        arrBodies = new Array<>();


        renderer = new Box2DDebugRenderer();

        createFloor();
        createWalls();
        createPlayer(player);
        createPlayer(player2);
    }

    private void createFloor() {

        BodyDef bd = new BodyDef();
        bd.position.set(0, .6f);
        bd.type = BodyType.StaticBody;

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, WORLD_WIDTH, 0);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 1f;

        Body oBody = oWorld.createBody(bd);
        oBody.createFixture(fixDef);
        BodyDef bd2 = new BodyDef();
        bd2.position.set(0, .6f);
        bd2.type = BodyType.StaticBody;

        EdgeShape shape2 = new EdgeShape();
        shape2.set(0, WORLD_HEIGHT-0.58F, WORLD_WIDTH, WORLD_HEIGHT-0.58F);

        FixtureDef fixDef2 = new FixtureDef();
        fixDef2.shape = shape2;
        fixDef2.friction = 1f;

        Body oBody2 = oWorld.createBody(bd2);
        oBody2.createFixture(fixDef2);

        shape2.dispose();
        shape.dispose();
    }

    private void createWalls() {

        BodyDef bd = new BodyDef();
        bd.position.set(0, .6f);
        bd.type = BodyType.StaticBody;

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, 0, WORLD_HEIGHT);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 1f;

        Body oBody = oWorld.createBody(bd);
        oBody.createFixture(fixDef);

        BodyDef bd2 = new BodyDef();
        bd2.position.set(0, .6f);
        bd2.type = BodyType.StaticBody;

        EdgeShape shape2 = new EdgeShape();
        shape2.set(WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT);

        FixtureDef fixDef2 = new FixtureDef();
        fixDef2.shape = shape2;
        fixDef2.friction = 1f;

        Body oBody2 = oWorld.createBody(bd2);
        oBody2.createFixture(fixDef2);

        shape.dispose();
        shape2.dispose();

    }

    private void createPlayer(Player player) {
        player = new Player(4f,1f);
        BodyDef bd = new BodyDef();
        bd.position.x = player.getX();
        bd.position.y = player.getY();
        bd.type = BodyType.DynamicBody;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(player.getWidth(), player.getHeight());

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 0;
        fixDef.restitution = 0;
        fixDef.density = 15;

        Body oBody = oWorld.createBody(bd);
        oBody.createFixture(fixDef);
        oBody.setUserData(player);

        shape.dispose();
    }


    @Override
    public void update(float delta) {
        float accelX = 0;
        float accelY = 0;
        float accelX2 = 0;
        float accelY2 = 0;
        String action = "";
        String action2 = "";

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            accelX = -1;

        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            accelX = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            accelY = -1;

        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            accelY = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            action = "attk";




        if (Gdx.input.isKeyPressed(Input.Keys.A))
            accelX2 = -1;

        else if (Gdx.input.isKeyPressed(Input.Keys.D))
            accelX2 = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            accelY2 = -1;

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            accelY2 = 1;

        if (Gdx.input.isKeyPressed(Input.Keys.F))
            action2 = "attk";



        oWorld.step(delta, 8, 6);
        oWorld.getBodies(arrBodies);

        for (Body body : arrBodies) {
            if ((body.getUserData() instanceof Player) && ((Player) body.getUserData()).getId() ==0) {
                Player obj = (Player) body.getUserData();
                obj.update(body, delta, accelX, accelY, action);
            }else if((body.getUserData() instanceof Player) && ((Player) body.getUserData()).getId() ==1){
                Player obj = (Player) body.getUserData();
                obj.update(body, delta, accelX2, accelY2, action2);
            }
        }
    }

    @Override
    public void draw(float delta) {
        oCamUI.update();
        spriteBatch.setProjectionMatrix(oCamUI.combined);

        spriteBatch.begin();
        Assets.font.draw(spriteBatch, "fps:" + Gdx.graphics.getFramesPerSecond(), 0, 20);
        spriteBatch.end();

        oCamBox2D.update();

        spriteBatch.setProjectionMatrix(oCamBox2D.combined);
        spriteBatch.begin();

        drawplayer();

        spriteBatch.end();
        renderer.render(oWorld, oCamBox2D.combined);
    }

    private void drawplayer() {
        Sprite keyframe = AssetsLearn8.entity;

        try{
            if (player.isIsWalking()) {
                keyframe = AssetsLearn8.walk.getKeyFrame(player.getStateTime(), true);
            }else if (player.isIsAttacking()) {
                keyframe = AssetsLearn8.attack1.getKeyFrame(player.getStateTime(), true);
                player.setIsAttacking(false);
            }

            if (player.getVelocityX() < 0) {
                keyframe.setPosition(player.getX() + player.getDraw_width() / 2, player.getY() - player.getDraw_height() / 2 +.25f);
                keyframe.setSize(-player.getDraw_width(), player.getDraw_height());
            } else {
                keyframe.setPosition(player.getX() - player.getDraw_width() / 2, player.getY() - player.getDraw_height() / 2 + .25f);
                keyframe.setSize(player.getDraw_width(), player.getDraw_height());
            }
        }catch (NullPointerException npe){
            keyframe = AssetsLearn8.entity;
        }finally {
            keyframe.draw(spriteBatch);
        }

    }

    @Override
    public void dispose() {
        AssetsLearn8.dispose();
        oWorld.dispose();
        super.dispose();
    }
}
