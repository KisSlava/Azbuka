package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Bukva {
    MyGdxGame mgg;
    float x, y;
    float vx, vy;
    float width, height;
    boolean isAlive = true;
    Texture img;
    Sound snd;
    int type;

    public Bukva(MyGdxGame myGdxGame, Texture img, Sound snd, int type){
        this.img = img;
        this.snd = snd;
        this.type = type;
        mgg = myGdxGame;
        width = height = mgg.sizeBukva;
        x = SCR_WIDTH / 2f - width / 2;
        y = SCR_HEIGHT / 2f - height / 2;
        vx = MathUtils.random(-mgg.speedBukva, mgg.speedBukva);
        vy = MathUtils.random(-mgg.speedBukva, mgg.speedBukva);
    }

    void fly(){
        x += vx;
        y += vy;
        if(isAlive) {
            outOfBounds1();
        }
    }

    void outOfBounds1(){
        if(x<0 || x> SCR_WIDTH -width) vx = -vx;
        if(y<0 || y> SCR_HEIGHT -height) vy = -vy;
    }

    void outOfBounds2(){
        if(x<0-width) x = SCR_WIDTH;
        if(x> SCR_WIDTH) x = 0-width;
        if(y<0-height) y = SCR_HEIGHT;
        if(y> SCR_HEIGHT) y = 0-height;
    }

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            isAlive = false;
            vx = 0;
            vy = -10;
            return true;
        }
        return false;
    }
}
