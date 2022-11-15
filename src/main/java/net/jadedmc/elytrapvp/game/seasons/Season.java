package net.jadedmc.elytrapvp.game.seasons;

public enum Season {
    CHRISTMAS("&cChristmas"),
    EASTER("&bEaster"),
    SUMMER("&eSummer"),
    HALLOWEEN("&6Halloween"),
    NONE("");

    private final String name;

    Season(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
