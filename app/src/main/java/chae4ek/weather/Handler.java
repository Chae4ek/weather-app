package chae4ek.weather;

@FunctionalInterface
public interface Handler {
    void handle(String response);
}
