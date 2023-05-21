package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Bukva {
    MyGdxGame mgg;
    float x, y;
    float vx, vy;
    float width, height;
    int faza, nFaz = 33;
    boolean isAlive = true;

    public Bukva(MyGdxGame myGdxGame){
        mgg = myGdxGame;
        width = height = mgg.sizeBukva+50;
        x = SCR_WIDTH / 2f - width / 2;
        y = SCR_HEIGHT / 2f - height / 2;
        vx = MathUtils.random(-mgg.speedMosquitos, mgg.speedMosquitos);
        vy = MathUtils.random(-mgg.speedMosquitos, mgg.speedMosquitos);
        faza = MathUtils.random(0, nFaz);
    }

    void fly(){
        x += vx;
        y += vy;
        if(isAlive) {
            outOfBounds2();
            changePhase();
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

    void changePhase(){
        if(++faza == nFaz) faza = 0;
        //faza = ++faza % nFaz;
    }

    boolean isFlip(){
        return vx>0;
    }

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            isAlive = false;
            //faza = 10;
            vx = 0;
            vy = -8;
            return true;
        }
        return false;
    }
}
