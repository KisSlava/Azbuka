package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Bukva {
    float x, y;
    float vx, vy;
    float width, height;
    float r;
    boolean isAlive = true;
    Texture img;
    Sound snd;
    int type;

    public Bukva(Texture img, Sound snd, int type){
        this.img = img;
        this.snd = snd;
        this.type = type;
        width = height = sizeBukva;
        r = width/2;
        x = SCR_WIDTH / 2f - width / 2;
        y = SCR_HEIGHT / 2f - height / 2;
        vx = MathUtils.random(-speedBukva, speedBukva);
        vy = MathUtils.random(-speedBukva, speedBukva);
    }

    void fly(){
        x += vx;
        y += vy;
        if(isAlive) {
            outOfBounds1();
        }
    }

    void outOfBounds1(){
        if(x<width/2 || x> SCR_WIDTH-width/2) vx = -vx;
        if(y<height/2 || y> SCR_HEIGHT-height/2) vy = -vy;
    }

    void outOfBounds2(){
        if(x<0-width) x = SCR_WIDTH;
        if(x> SCR_WIDTH) x = 0-width;
        if(y<0-height) y = SCR_HEIGHT;
        if(y> SCR_HEIGHT) y = 0-height;
    }

    boolean hit(float tx, float ty){
        //if(x < tx && tx < x+width && y < ty && ty < y+height){
        if(Math.pow(tx-x,2) + Math.pow(ty-y,2) < r*r){
            isAlive = false;
            vx = 0;
            vy = -10;
            return true;
        }
        return false;
    }

    public float scrX() {
        return x-width/2;
    }

    public float scrY() {
        return y-height/2;
    }
}
