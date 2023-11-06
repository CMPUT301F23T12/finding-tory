package com.example.finding_tory.repository;

import com.example.finding_tory.model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing tag data.
 */
public class TagRepository {

    // Assuming tags are stored in a list for simplicity.
    // This would be replaced by a database.
    private final List<Tag> tags = new ArrayList<>();

    /**
     * Adds a tag to the repository.
     *
     * @param tag The tag to add.
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * Retrieves a tag by its name.
     *
     * @param name The name of the tag to retrieve.
     * @return An Optional containing the found tag or an empty Optional if not found.
     */
    public Optional<Tag> getTagByName(String name) {
        return tags.stream()
                .filter(tag -> tag.getName().equals(name))
                .findFirst();
    }

    /**
     * Retrieves all tags in the repository.
     *
     * @return A list of all tags.
     */
    public List<Tag> getAllTags() {
        return new ArrayList<>(tags);
    }

    /**
     * Updates an existing tag in the repository.
     * This would typically involve finding a tag by some identifier and updating its details.
     *
     * @param tag The tag with updated information.
     */
    public void updateTag(Tag tag) {
        // In a real database, you'd update the tag where the identifiers match.
        // Here, we just replace the tag with the same name.
        getTagByName(tag.getName()).ifPresent(existingTag -> {
            tags.remove(existingTag);
            tags.add(tag);
        });
    }

    /**
     * Deletes a tag from the repository.
     *
     * @param tag The tag to delete.
     */
    public void deleteTag(Tag tag) {
        tags.removeIf(existingTag -> existingTag.equals(tag));
    }
}
