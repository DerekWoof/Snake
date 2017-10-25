package pl.sdacademy.snake;

/**
 * Klasa reprezentująca punkt (parę współrzędnych x i y).
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Metoda zwróci punkt powstały przez przesunięcie punktu w zadanym kierunku.
     * @param direction kierunek przesunięcia punktu
     * @return punkt powstały w wyniku przesunięcia.
     */
    public Point moveTo(Direction direction) {
        switch (direction) {
            case DOWN:
                return new Point(x, y + 1);
            case UP:
                return new Point(x, y - 1);
            case RIGHT:
                return new Point(x + 1, y);
            case LEFT:
                return new Point(x - 1, y);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Point) {
            Point other = (Point) obj;
            return getX() == other.getX() && getY() == other.getY();
        } else return false;
    }

    @Override
    public String toString() {
        return "(" + x +", " + y + ")";
    }
}
