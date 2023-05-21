package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
    MyGdxGame mgg;

    Texture[] imgBukva = new Texture[33];
    Texture imgBackGround; // фон
    Texture imgBtnMenu;

    Sound[] sndBukva = new Sound[33];
    Music sndMusic;

    // создание массива ссылок на объекты
    ArrayList<Bukva> bukva = new ArrayList<>();
    int letters;
    long timeStart, timeCurrent;

    // состояние игры
    public static final int PLAY_GAME = 0, ENTER_NAME = 1, SHOW_TABLE = 2;
    int gameState;

    Player[] players = new Player[5];

    MyButton btnRestart, btnExit;
    MyButton btnMenu;

    long timeBukvaLastSpawn, timeBukvaSpawnInterval = 500;

    public ScreenGame(MyGdxGame myGdxGame) {
        mgg = myGdxGame;

        // создаём объекты изображений
        for (int i = 0; i < imgBukva.length; i++) {
            imgBukva[i] = new Texture("bukvas/bukva" + i + ".png");
        }
        imgBackGround = new Texture("backgrounds/bg_shkola.png");
        imgBtnMenu = new Texture("menu.png");

        // создаём объекты звуков
        for (int i = 0; i < sndBukva.length; i++) {
            sndBukva[i] = Gdx.audio.newSound(Gdx.files.internal("sound/buk" + i + ".mp3"));
        }
        sndMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/soundcrazymosquitos.mp3"));
        sndMusic.setLooping(true);
        sndMusic.setVolume(0.2f);
        if (mgg.musicOn) sndMusic.play();

        // создаём объекты игроков для таблицы рекордов
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Без имени", 0);
        }
        loadTableOfRecords();

        // создаём кнопки
        btnRestart = new MyButton(mgg.font, "Играть снова", 450, 200);
        btnExit = new MyButton(mgg.font, "Выход", 750, 200);
        btnMenu = new MyButton(SCR_WIDTH - 60, SCR_HEIGHT - 60, 50, 50);
    }

    @Override
    public void show() {
        gameStart();
    }

    @Override
    public void render(float delta) {// повторяется с частотой 60 fps
        // касания экрана/клики мышью
        if (Gdx.input.justTouched()) {
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);
            if (gameState == PLAY_GAME) {
                for (int i = bukva.size() - 1; i >= 0; i--) {
                    if (bukva.get(i).isAlive) {
                        if (bukva.get(i).hit(mgg.touch.x, mgg.touch.y)) {
                            letters++;
                            if (mgg.soundOn) bukva.get(i).snd.play();
                            break;
                        }
                    }
                }
            }
            if (gameState == SHOW_TABLE) {
                if (btnExit.hit(mgg.touch.x, mgg.touch.y)) {
                    mgg.setScreen(mgg.screenIntro);
                }
                if (btnRestart.hit(mgg.touch.x, mgg.touch.y)) {
                    gameStart();
                }
            }
            if (gameState == ENTER_NAME) {
                if (mgg.keyboard.endOfEdit(mgg.touch.x, mgg.touch.y)) {
                    gameOver();
                }
            }
            if (btnMenu.hit(mgg.touch.x, mgg.touch.y)) {
                mgg.setScreen(mgg.screenIntro);
            }
        }
        /*for (char c = 'А'; c <= 'Я'; c++) {
            if (Gdx.input.(Input.Keys.)) {
                System.out.println('и');
            }
        }*/


        // события игры
        spawnBukvas();
        if (bukva.size() > 33) {
            gameState = ENTER_NAME;
        }
        for (int i = 0; i < bukva.size(); i++) {
            bukva.get(i).fly();
        }
        if (gameState == PLAY_GAME) {
            timeCurrent = TimeUtils.millis() - timeStart;
        }

        // отрисовка всего
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        for (int i = 0; i < bukva.size(); i++) {
            mgg.batch.draw(bukva.get(i).img, bukva.get(i).x, bukva.get(i).y, bukva.get(i).width, bukva.get(i).height);
        }
        mgg.font.draw(mgg.batch, "Угадано: " + letters, 10, SCR_HEIGHT - 10);
        mgg.font.draw(mgg.batch, "Время: " + timeToString(timeCurrent), SCR_WIDTH - 500, SCR_HEIGHT - 10);
        if (gameState == SHOW_TABLE) {
            mgg.fontLarge.draw(mgg.batch, "Игра окончена", 0, 600, SCR_WIDTH, Align.center, true);
            for (int i = 0; i < players.length; i++) {
                String s = players[i].name + "......." + timeToString(players[i].letters);
                mgg.font.draw(mgg.batch, s, 0, 500 - i * 50, SCR_WIDTH, Align.center, true);
            }
            mgg.font.draw(mgg.batch, btnRestart.text, btnRestart.x, btnRestart.y);
            mgg.font.draw(mgg.batch, btnExit.text, btnExit.x, btnExit.y);
        }
        if (gameState == ENTER_NAME) {
            mgg.keyboard.draw(mgg.batch);
        }
        mgg.batch.draw(imgBtnMenu, btnMenu.x, btnMenu.y, btnMenu.width, btnMenu.height);
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

    }

    @Override
    public void dispose() {
        for (int i = 0; i < imgBukva.length; i++) {
            imgBukva[i].dispose();
        }
        for (int i = 0; i < sndBukva.length; i++) {
            sndBukva[i].dispose();
        }
        sndMusic.dispose();
        imgBackGround.dispose();
    }

    String timeToString(long time) {
        String min = "" + time / 1000 / 60 / 10 + time / 1000 / 60 % 10;
        String sec = "" + time / 1000 % 60 / 10 + time / 1000 % 60 % 10;
        return min + ":" + sec;
    }

    void gameStart() {
        letters = 0;
        gameState = PLAY_GAME;
        timeStart = TimeUtils.millis();
    }

    void gameOver() {
        gameState = SHOW_TABLE;
        players[players.length - 1].name = mgg.keyboard.getText();
        players[players.length - 1].letters = letters;
        sortTableOfRecords();
        saveTableOfRecords();
    }

    void saveTableOfRecords() {
        Preferences prefs = Gdx.app.getPreferences("Table Of Records");
        for (int i = 0; i < players.length; i++) {
            prefs.putString("name" + i, players[i].name);
            prefs.putInteger("letters" + i, players[i].letters);
        }
        prefs.flush();
    }

    void loadTableOfRecords() {
        Preferences prefs = Gdx.app.getPreferences("Table Of Records");
        for (int i = 0; i < players.length; i++) {
            if (prefs.contains("name" + i)) players[i].name = prefs.getString("name" + i);
            if (prefs.contains("letters" + i)) players[i].letters = prefs.getInteger("letters" + i);
        }
    }

    void sortTableOfRecords() {
        for (int j = 0; j < players.length - 1; j++) {
            for (int i = 0; i < players.length - 1; i++) {
                if (players[i].letters < players[i + 1].letters) {
                    Player c = players[i];
                    players[i] = players[i + 1];
                    players[i + 1] = c;
                }
            }
        }
    }

    void clearTableOfRecords() {
        for (int i = 0; i < players.length; i++) {
            players[i].name = "Noname";
            players[i].letters = 0;
        }
    }

    void spawnBukvas() {
        if (TimeUtils.millis() > timeBukvaLastSpawn + timeBukvaSpawnInterval) {
            int rnd = MathUtils.random(0, 32);
            bukva.add(new Bukva(mgg, imgBukva[rnd], sndBukva[rnd], rnd));
            timeBukvaLastSpawn = TimeUtils.millis();
        }
    }
}
