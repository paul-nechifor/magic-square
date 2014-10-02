package net.nechifor.magic_square;

public interface Listener {
    public void currentState(int[][] square);
    public void restart();
    public void finish();
}
