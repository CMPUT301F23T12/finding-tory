package com.example.finding_tory.model;

/**
 * Represents a tag that can be used to categorize items
 */
public class Tag {
    private String name;

    /**
     * Constructs a new Tag with the given name.
     *
     * @param name The name of the tag.
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the tag.
     *
     * @return The nbame of the tag
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name != null ? name.equals(tag.name) : tag.name == null;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }


}
