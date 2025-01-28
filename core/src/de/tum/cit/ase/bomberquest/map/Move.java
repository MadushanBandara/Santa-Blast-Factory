package de.tum.cit.ase.bomberquest.map;


public enum Move {
    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

    public final int dx; // Change in x-coordinate
    public final int dy; // Change in y-coordinate

    Move(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
