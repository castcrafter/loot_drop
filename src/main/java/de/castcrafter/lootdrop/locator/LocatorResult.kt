package de.castcrafter.lootdrop.locator

enum class LocatorResult {

    /*
     * Using the UP and DOWN marker we can tell the user if he needs to go up or down in the terraian
     */
    UP,
    DOWN,

    /*
     * Using the NORTH, SOUTH, EAST and WEST marker we can tell the user in which direction he needs to go
     */
    FRONT,
    BACK,
    RIGHT,
    LEFT,
    FRONT_RIGHT,
    FRONT_LEFT,
    BACK_RIGHT,
    BACK_LEFT,

    /*
     * Using the WORLD marker we can tell the user that the location is in a different world
     */
    WORLD,

    /*
     * The none marker is either an error or that the user is already a specific state. For example when reaching the correct Y level
     */
    NONE

}