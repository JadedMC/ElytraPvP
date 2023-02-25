package net.jadedmc.elytrapvp.game.seasons;

public enum Season {
    CHRISTMAS("&cChristmas"),
    VALENTINES_DAY("&dValentine's Day"),
    SAINT_PATRICKS_DAY("&aSt. Patrick's Day"),
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
