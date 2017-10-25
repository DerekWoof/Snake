package pl.sdacademy.snake;


import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca węża posiadającego głowę (punkt) oraz ciało (listę punktów)
 */
public class Snake {
    // Głowa węża (punkt, który poruszy się jako pierwszy).
    private Point head;
    // Lista pozostałych punktów węża.
    private List<Point> body;

    // Konstruktor, którego możemy użyć do utworzenia węża o dowolnym ciele.
    public Snake(Point head, List<Point> body) {
        this.head = head;
        this.body = body;
    }

    // Konstruktor uzyty w aplikacji - na początku wąż składa się z jednego punktu - głowy.
    public Snake(Point head) {
        this.head = head;
        this.body = new ArrayList<>();
    }

    /**
     * Poruszamy węża w zadanym kierunku. Jeśli przekażemy informację, że wąż zjadł jabłko,
     * to nie utniemy ogonu węża.
     *
     * @param direction  kierunek ruchu węża
     * @param appleEaten czy wąż zjadł jabłko
     */
    public void move(Direction direction, boolean appleEaten) {
        // Ruch węża możemy opisać następująco:
        // głowa trafia do ciała,
        body.add(head);
        // nowa głowa będzie wynikiem przesunięcia starej głowy w kierunku ruchu węża.
        head = head.moveTo(direction);
        // Jeśli wąż nie je jabłka, to ucinamy ogon.
        if (!appleEaten) {
            body.remove(0);
        }
    }

    public Point getHead() {
        return head;
    }

    public List<Point> getBody() {
        return body;
    }

    /**
     * Czy któryś punkt węża równy jest zadanemu punktowi (do sprawdzenia, czy wylosowane jabłko leży na wężu).
     *
     * @param point
     * @return
     */
    public boolean contains(Point point) {
        return head.equals(point) || body.contains(point);
    }
}
