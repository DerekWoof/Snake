package pl.sdacademy.snake;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.Random;

public class Controller {

    @FXML
    private Button upButton;
    @FXML
    private Button downButton;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    // Rozmiar "kwadraciku"
    private final static int POINT_SIZE = 10;
    // Punkt reprezentujący jabłko
    private Point apple;
    // Kierunek ruchu węża
    private Direction direction;
    // Czy gra jest kontynuowana
    private boolean gameOn;
    // Obiekt reprezentujący węża
    private Snake snake;

    public void initialize() {
        // Będziemy używali kontekstu graficznego kontrolki canvas, w związku z czym przypiszemy go do pola.
        graphicsContext = canvas.getGraphicsContext2D();

        // Gdy klikniemy klawisz wtedy, kiedy canvas będzie sfocusowany
        canvas.setOnKeyPressed(event -> {
            // to w zależności od wciśniętego klawisza ustawimy kierunek ruchu węża.
            switch (event.getCode()) {
                case LEFT:
                    direction = Direction.LEFT;
                    break;
                case RIGHT:
                    direction = Direction.RIGHT;
                    break;
                case UP:
                    direction = Direction.UP;
                    break;
                case DOWN:
                    direction = Direction.DOWN;
                    break;
            }
        });
        // analogicznie dla przycisków - kiedy odpowiedni zostanie wciśnięty, ustawiamy odpowiedni kierunek ruchu.
        leftButton.setOnAction(event -> direction = Direction.LEFT);
        rightButton.setOnAction(event -> direction = Direction.RIGHT);
        downButton.setOnAction(event -> direction = Direction.DOWN);
        upButton.setOnAction(event -> direction = Direction.UP);

        // Startujemy grę.
        startGame();
    }

    /**
     * Metoda startująca grę.
     */
    private void startGame() {
        // Początkowym kierunkiem ruchu będzie prawo.
        direction = Direction.RIGHT;
        // Flaga mówiąca o tym, czy gra jest kontynuowana.
        gameOn = true;
        // Tworzymy węża, który będzie składał się tylko z głowy umieszczonej w lewym górnym rogu.
        snake = new Snake(new Point(0, 0));
        // Losujemy jabłko
        randomizeApple();

        // Tworzymy obiekt wątku
        Thread thread = new Thread(() -> {
            // W ramach którego dopóki gra jest kontynuowana
            while (gameOn) {
                // czekamy ćwierć sekundy
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    gameOn = false;
                }
                // po czym ruszamy węża
                moveSnake();
                // sprawdzamy, czy wąż się nie rozbił
                if(checkIfSnakeIsCrashed()) {
                    gameOn = false;
                }
                // rysujemy aktualną sytuację z gry na canvasie.
                draw();
            }
        });
        // Wątek będzie deamonem (nie chcemy, aby wątek główny czekał, aż ten zakończy pracę.
        thread.setDaemon(true);
        // Startujemy wątek.
        thread.start();
    }

    /**
     * Metoda sprawdza, czy wąż się nie rozbił (o ścianę albo o swoje ciało)
     * @return czy wąz się rozbił.
     */
    private boolean checkIfSnakeIsCrashed() {
        // Jeśli ciało zawiera głowę węża, to znaczy, że wąż rozbija się o swoje ciało
        if (snake.getBody().contains(snake.getHead())) {
            return true;
        } else {
            // W innym wypadku, sprawdzamy, czy wąż się nie rozbił o krawędź pola gry.
            // Wyliczamy górne zakresy współrzędnych x i y (to będzie szerokość/wysokość canvasa przez rozmiar punktu)
            int xUpperBound = (int) canvas.getWidth() / POINT_SIZE;
            int yUpperBound = (int) canvas.getHeight() / POINT_SIZE;

            int headX = snake.getHead().getX();
            int headY = snake.getHead().getY();
            // Sprawdzamy, czy współrzędne głowy węża mieszczą się w polu gry.
            return headX < 0 || headY < 0 || headX >= xUpperBound || headY >= yUpperBound;
        }
    }

    /**
     * Metoda wykonująca ruch węża.
     */
    private void moveSnake() {
        // Jeśli głowa węża poruszy się na jabłko
        if (snake.getHead().moveTo(direction).equals(apple)) {
            // to ruszamy węża nie ucinając jego ogona,
            snake.move(direction, true);
            // po czym losujemy nowe jabłko.
            randomizeApple();
        } else {
            // W innym wypadku poruszamy węża w standardowy sposób (dokładając nowy punkt i ucinając ostatni).
            snake.move(direction, false);
        }
    }

    /*
     * Metoda rysująca sytuację z gry.
     */
    private void draw() {
        // Czyścimy płótno,
        clearCanvas();
        // rysujemy węża
        drawSnake();
        // i jabłko.
        drawApple();
    }

    private void clearCanvas() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawApple() {
        graphicsContext.setFill(new Color(0, 1, 0, 1));
        drawPoint(apple);
    }

    private void drawSnake() {
        graphicsContext.setFill(new Color(1, 0, 0, 1));
        drawPoint(snake.getHead());
        graphicsContext.setFill(new Color(0.8, 0, 0, 1));
        snake.getBody().forEach(p -> drawPoint(p));
    }

    private void drawPoint(Point point) {
        graphicsContext.fillRect(point.getX() * POINT_SIZE, point.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    /**
     * Metoda losująca jabłko
     */
    private void randomizeApple() {
        // Będziemy losowali wartości, więc przyda się obiekt typu Random.
        Random random = new Random();
        // Wyznaczamy górne zakresy współrzędnych
        int xUpperBound = (int) canvas.getWidth() / POINT_SIZE;
        int yUpperBound = (int) canvas.getHeight() / POINT_SIZE;
        do {
            // Losujemy nową propozycję jabłka
            apple = new Point(random.nextInt(xUpperBound), random.nextInt(yUpperBound));
            // dopóki jabłko leży na wężu (chcemy wylosować jabłko, które na wężu nie leży)
        } while (snake.contains(apple));
    }
}
