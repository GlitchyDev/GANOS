package test.mtgsaber.lib;

import net.mtgsaber.lib.events.AsynchronousEventManager;
import net.mtgsaber.lib.events.Event;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import net.mtgsaber.lib.threads.Clock;
import net.mtgsaber.lib.threads.Tickable;

public final class Test2 extends Application {
    private volatile long clickStart;
    private Thread thrGameTick;
    private Clock gameTick;
    private AsynchronousEventManager eventManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // set up resources
        eventManager = new AsynchronousEventManager();
        gameTick = new Clock(14, eventManager);
        thrGameTick = new Thread(gameTick, "Game Tick Thread");

        // set up visual
        Pane pane = new Pane();
        pane.setPrefSize(500, 500);

        Circle player1Sprite = new Circle();
        player1Sprite.setCenterX(20);
        player1Sprite.setCenterY(20);
        player1Sprite.setRadius(5);
        player1Sprite.setFill(Color.AQUA);

        // set up entities
        Player player1 = new Player("Player 1", player1Sprite, 1, false, false, null);
        pane.getChildren().add(player1.SPRITE);

        // set up structure
        primaryStage.setScene(new Scene(pane));

        // set up event triggers
        pane.setOnMousePressed(event -> {
            clickStart = System.currentTimeMillis();
        });
        pane.setOnMouseReleased(event -> {
            long clickDuration = System.currentTimeMillis() - clickStart;
            SoundEvent sound = new SoundEvent(
                    SoundEvent.class.getName(),
                    "ClickSound.mp3",
                    clickDuration*.25,
                    event.getX(), event.getY()
            );
            eventManager.push(sound);
        });

        // set up event handlers
        eventManager.addHandler(SoundEvent.class.getName(), e -> {
            synchronized (player1) {
                if (!player1.isDeaf) {
                    SoundEvent sound = ((SoundEvent) e);
                    double dist = Math.sqrt(
                            Math.pow(sound.POS_X - player1.SPRITE.getCenterX(), 2)
                                    + Math.pow(sound.POS_Y - player1.SPRITE.getCenterY(), 2)
                    );
                    if (dist <= sound.VOLUME) {
                        player1.isInvestigating = true;
                        player1.target = sound;
                        Platform.runLater(() -> {
                            player1.SPRITE.setFill(Color.CRIMSON);
                        });
                    }
                }
            }
        });
        primaryStage.setOnCloseRequest(event -> {
            gameTick.stop();
        });

        // add entities to clock
        gameTick.add(player1);

        // start
        gameTick.start();
        thrGameTick.start();
        primaryStage.show();
    }

    public static final class SoundEvent implements Event {
        public final String SOUND_RES, NAME;
        public final double VOLUME, POS_X, POS_Y;

        public SoundEvent(String name, String SOUND_RES, double VOLUME, double POS_x, double POS_Y) {
            this.NAME = name;
            this.SOUND_RES = SOUND_RES;
            this.VOLUME = VOLUME;
            this.POS_X = POS_x;
            this.POS_Y = POS_Y;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    public static final class Player implements Tickable {
        public final Circle SPRITE;
        public final String NAME;
        public volatile double runSpeed;
        public volatile boolean isDeaf, isInvestigating;
        public volatile SoundEvent target;

        public Player(
                String NAME, Circle sprite, double runSpeed, boolean isDeaf, boolean isInvestigating, SoundEvent target
        ) {
            this.NAME = NAME;
            this.SPRITE = sprite;
            this.runSpeed = runSpeed;
            this.isDeaf = isDeaf;
            this.isInvestigating = isInvestigating;
            this.target = target;
        }

        @Override
        public void tick() {
            synchronized (this) {
                if (isInvestigating) {
                    if (target == null || SPRITE.contains(new Point2D(target.POS_X, target.POS_Y))) {
                        isInvestigating = false;
                        target = null;
                        Platform.runLater(() -> {
                            SPRITE.setFill(Color.AQUA);
                        });
                    } else {
                        double distX = target.POS_X - SPRITE.getCenterX();
                        double distY = target.POS_Y - SPRITE.getCenterY();
                        double dist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
                        if (dist > runSpeed) {
                            double dispX = distX*runSpeed/dist, dispY = distY*runSpeed/dist;
                            Platform.runLater(() -> {
                                SPRITE.setCenterX(SPRITE.getCenterX() + dispX);
                                SPRITE.setCenterY(SPRITE.getCenterY() + dispY);
                            });
                        } else {
                            Platform.runLater(() -> {
                                SPRITE.setCenterX(target.POS_X);
                                SPRITE.setCenterY(target.POS_Y);
                            });
                        }
                    }
                }
            }
        }
    }
}
