package com.example.easyfood.model;

/**
 * Represents an Eatery
 */
public class Eatery {
    private String name;

    /**
     * Creates an instance of Eatery
     *
     * @param name: String - The name of the Eatery
     */
    public Eatery (String name) {
        setName(name);
    }

    /**
     * Returns the name of the Eatery
     *
     * @return name: String - The name of the Eatery
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Eatery
     *
     * @param name: String - The name of the Eatery
     */
    private void setName(String name) {
        this.name = name;
    }
}
