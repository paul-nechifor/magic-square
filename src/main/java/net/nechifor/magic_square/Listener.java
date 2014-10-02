package net.nechifor.magic_square;

public interface Listener
{
    public void currentState(int[][] patratul);
    public void restart();
    public void finish();
}
