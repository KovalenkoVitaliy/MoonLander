import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private boolean isUpPressed;
    private boolean isLeftPressed ;
    private boolean isRightPressed;
    private GameObject platform;
    private boolean isGameStopped;
    private int score;


    private void createGameObjects() {
        rocket = new Rocket(WIDTH/2, 0);
        landscape = new GameObject(0,25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }

    private void check() {
        if (rocket.isCollision(landscape) && !rocket.isCollision(platform) ) {
            gameOver();
        } else if (rocket.isCollision(platform) && rocket.isStopped() ) {
            win();
        }

    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.AZURE, "YOU ARE WIN", Color.BLACK, 16);
        stopTurnTimer();

    }

    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.CADETBLUE, "YOU ARE LOSER", Color.RED, 16);
        stopTurnTimer();
        score =0;


    }


    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }

    public void onTurn(int i){
        if (score > 0) {
            score --;
        }
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        setScore(score);
        drawScene();

    }

    public void setCellColor(int x, int y, Color color) {
        if (x < 0 || y < 0 || x > 63 || y > 63) {
            return;
        } else {
            super.setCellColor(x, y, color);
        }
    }

    private void createGame(){
        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed  = false;
        isRightPressed = false;
        isGameStopped = false;
        score = 1000;
    }

    public void onKeyPress(Key key) {
        if (key == Key.UP) {
            isUpPressed = true;
        } else if (key == Key.LEFT) {
            isLeftPressed = true;
            isRightPressed = false;
        } else if (key == Key.RIGHT) {
            isLeftPressed = false;
            isRightPressed = true;
        }
        if (key==Key.SPACE && isGameStopped) {
            createGame();
        }
    }

    public void onKeyReleased(Key key) {
        if (key == Key.UP) {
            isUpPressed = false;
        } else if (key == Key.LEFT) {
            isLeftPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = false;
        }
    }

    private void drawScene(){
        for (int y =0; y < HEIGHT; y++) {
            for (int x=0; x < WIDTH; x++) {
                setCellColor(x, y, Color.GREY);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }
}
