package su.panfilov.piramida.components;

public interface SwipeViewDelegate {
    String leftTurn(int layer);
    String rightTurn(int layer);
    String getTitle(int layer);
    void setOneSide(int byLayer);
}
