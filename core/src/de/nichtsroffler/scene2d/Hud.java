package de.nichtsroffler.scene2d;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.nichtsroffler.dynamicworld.players.Player;
import de.nichtsroffler.main.FinalStatics;
import de.nichtsroffler.main.NotFinalStatics;
import de.nichtsroffler.screens.PlayScreen;

public class Hud implements Disposable {

    public Stage stage;

    private final PlayScreen playScreen;
    private final Player player;

    //Tables
    private final Table timerTable;
    private final Table livesTable;
    private final Table tableDiamonds;

    //Textures
    private final Texture digitsTexture;
    private final Texture livesTexture;
    private final Texture diamondTexture;

    private final TextureRegion[][] digits;
    private final TextureRegion[][] lives;
    private final TextureRegion[][] diamonds;

    private Image liveImage;
    private Image diamondImage;

    private int time;
    private int frameCount = 0;

    private int lastHudLives = 0;
    private int lastHudDiamonds = -1;


    public Hud (PlayScreen playScreen, SpriteBatch batch) {

        this.playScreen = playScreen;
        player = playScreen.getPlayer();
        Viewport viewport = new ExtendViewport(FinalStatics.VIRTUAL_WIDTH, FinalStatics.VIRTUAL_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        //Level timer
        time = 300;
        timerTable = new Table();
        timerTable.top();
        timerTable.right();
        timerTable.setFillParent(true);

        digitsTexture = new Texture("Hud/numbersSmall.png");
        digits = TextureRegion.split(digitsTexture, 18, 16);


        //Lives
        livesTable = new Table();
        livesTable.top();
        livesTable.left();
        livesTable.setFillParent(true);

        livesTexture = new Texture("Hud/hearts.png");
        lives = TextureRegion.split(livesTexture, 17,16);

        liveImage = new Image(lives[0][0]);


        //Diamonds
        tableDiamonds = new Table();
        tableDiamonds.left();
        tableDiamonds.top();
        tableDiamonds.setFillParent(true);

        diamondTexture = new Texture("Hud/diamonds.png");
        diamonds = TextureRegion.split(diamondTexture, 16, 16);
    }

    public void update(float dt) {

        //Level timer Management -----------------------------------------------------
        frameCount++;
        if ((frameCount % FinalStatics.FPS) == 0) {
            time--;
        }

        int timerDigit3 = time % 10;
        int timerDigit2 = (time / 10) % 10;
        int timerDigit1 = (time / 100) % 10;

        if (time >= 0) {
            timerTable.clear();

            Image timerImage1 = new Image(digits[0][timerDigit1]);
            Image timerImage2 = new Image(digits[0][timerDigit2]);
            Image timerImage3 = new Image(digits[0][timerDigit3]);

            timerTable.add(timerImage1).padTop(4f).padRight(-5f);
            timerTable.add(timerImage2).padTop(4f).padRight(-5f);
            timerTable.add(timerImage3).padTop(4f).padRight(4f);
        } else {
            //loose Game
        }

        stage.addActor(timerTable);


        //Lives Management ------------------------------------------------------------

        if (player.getLives() != lastHudLives) {
            System.out.println("update Hud");
            livesTable.clear();

            if ((player.getLives()) % 2 == 0) {

                for (int i = 0; i < player.getLives(); i += 2) {
                    liveImage = new Image(lives[0][0]);
                    livesTable.add(liveImage).padLeft(3).padTop(2f);
                }

                for (int i = player.getLives(); i < 6; i += 2) {
                    liveImage = new Image(lives[0][2]);
                    livesTable.add(liveImage).padLeft(3).padTop(2f);
                }

            } else {

                for (int i = 0; i <= player.getLives() - 2; i += 2) {
                    liveImage = new Image(lives[0][0]);
                    livesTable.add(liveImage).padLeft(3).padTop(2f);
                }

                liveImage = new Image(lives[0][1]);
                livesTable.add(liveImage).padLeft(3).padTop(2f);

                for (int i = player.getLives() + 1; i < 6; i += 2) {
                    liveImage = new Image(lives[0][2]);
                    livesTable.add(liveImage).padLeft(3).padTop(2f);
                }
            }
            stage.addActor(livesTable);
        }
        lastHudLives = player.getLives();


        //Diamond Management ------------------------------------------------------------
        if (player.getDiamonds() != lastHudDiamonds) {
            System.out.println("update Hud");
            tableDiamonds.clear();

            if (player.getDiamonds() == 0) {
                diamondImage = new Image(diamonds[0][0]);
                tableDiamonds.add(diamondImage).padLeft(2f).padTop(-10f);

                diamondImage = new Image(diamonds[0][0]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(20f);

                diamondImage = new Image(diamonds[0][0]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(50f);
            } else if (player.getDiamonds() == 1) {
                diamondImage = new Image(diamonds[0][1]);
                tableDiamonds.add(diamondImage).padLeft(2f).padTop(-10f);

                diamondImage = new Image(diamonds[0][0]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(20f);

                diamondImage = new Image(diamonds[0][0]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(50f);
            } else if (player.getDiamonds() == 2) {
                diamondImage = new Image(diamonds[0][1]);
                tableDiamonds.add(diamondImage).padLeft(2f).padTop(-10f);

                diamondImage = new Image(diamonds[0][1]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(20f);

                diamondImage = new Image(diamonds[0][0]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(50f);
            } else if (player.getDiamonds() == 3) {
                diamondImage = new Image(diamonds[0][1]);
                tableDiamonds.add(diamondImage).padLeft(2f).padTop(-10f);

                diamondImage = new Image(diamonds[0][1]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(20f);

                diamondImage = new Image(diamonds[0][1]);
                tableDiamonds.add(diamondImage).padLeft(-16f).padTop(50f);
            }

            stage.addActor(tableDiamonds);
        }
        lastHudDiamonds = player.getDiamonds();


        //------------
        stage.act(dt);
    }

    public void renderStage() {
        stage.draw();
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
