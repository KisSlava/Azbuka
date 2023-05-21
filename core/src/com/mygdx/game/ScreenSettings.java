package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.MODE_EASY;
import static com.mygdx.game.MyGdxGame.MODE_HARD;
import static com.mygdx.game.MyGdxGame.MODE_NORMAL;
import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фон
    MyButton btnMode, btnSound, btnMusic, btnClearRecords, btnBack;

    public ScreenSettings(MyGdxGame myGdxGame){
        mgg = myGdxGame;
        imgBackGround = new Texture("backgrounds/bg_shkola2.jpeg");
        // создаём кнопки
        btnMode = new MyButton(mgg.fontLarge, "Сложность: Легко", 500, 550);
        btnSound = new MyButton(mgg.fontLarge, "Звук: Вкл", 500, 450);
        btnMusic = new MyButton(mgg.fontLarge, "Музыка: Вкл", 500, 350);
        btnClearRecords = new MyButton(mgg.fontLarge, "Очистить рекорды", 500, 250);
        btnBack = new MyButton(mgg.fontLarge, "Назад", 500, 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана/клики мышью
        if(Gdx.input.justTouched()) {
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);
            if(btnMode.hit(mgg.touch.x, mgg.touch.y)){
                if(mgg.modeOfGame == MODE_EASY){
                    mgg.modeOfGame = MODE_NORMAL;
                    btnMode.text = "Сложность: Нормально";
                    mgg.sizeBukva=300;
                    mgg.speedBukva = 5;
                } else if(mgg.modeOfGame == MODE_NORMAL){
                    mgg.modeOfGame = MODE_HARD;
                    btnMode.text = "Сложность: Трудно";
                    mgg.sizeBukva=200;
                    mgg.speedBukva = 8;
                } else if(mgg.modeOfGame == MODE_HARD){
                    mgg.modeOfGame = MODE_EASY;
                    btnMode.text = "Сложность: Легко";
                    mgg.sizeBukva=400;
                    mgg.speedBukva = 2;
                }
            }
            if(btnSound.hit(mgg.touch.x, mgg.touch.y)){
                mgg.soundOn = !mgg.soundOn;
                if(mgg.soundOn) btnSound.text = "Звук: Вкл";
                else btnSound.text = "Звук: Выкл";
            }
            if(btnMusic.hit(mgg.touch.x, mgg.touch.y)){
                mgg.musicOn = !mgg.musicOn;
                if(mgg.musicOn) {
                    btnMusic.text = "Музыка: Вкл";
                    mgg.screenGame.sndMusic.play();
                }
                else {
                    btnMusic.text = "Музыка: Выкл";
                    mgg.screenGame.sndMusic.stop();
                }
            }
            if(btnClearRecords.hit(mgg.touch.x, mgg.touch.y)){
                btnClearRecords.text = "Рекорды очищены";
                mgg.screenGame.clearTableOfRecords();
                mgg.screenGame.saveTableOfRecords();
            }
            if(btnBack.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenIntro);
            }
        }

        // события игры
        // ------------

        // отрисовка всего
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnMode.font.draw(mgg.batch, btnMode.text, btnMode.x, btnMode.y);
        btnSound.font.draw(mgg.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(mgg.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnClearRecords.font.draw(mgg.batch, btnClearRecords.text, btnClearRecords.x, btnClearRecords.y);
        btnBack.font.draw(mgg.batch, btnBack.text, btnBack.x, btnBack.y);
        mgg.batch.end();
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
        btnClearRecords.text = "Очистить рекорды";
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
