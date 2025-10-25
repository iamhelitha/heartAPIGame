package org.helitha.heartapigame.models;

/**
 * GameData - Data model for Heart Game API responses
 *
 * INTEROPERABILITY:
 * This record defines the "contract" for data exchange between:
 * - The Java client application
 * - The PHP-based Heart Game API server
 *
 * JSON Structure from API:
 * {
 *   "question": "https://example.com/image.jpg",  // Image URL
 *   "solution": 5,                                 // Number of hearts in image
 *   "carrots": 3                                   // Number of carrots in image
 * }
 *
 * The Jackson library automatically maps this JSON to a GameData object
 * This is a Java 16+ record - immutable data class with automatic getters
 */
public record GameData(String question, int solution, int carrots) {
    // Compact record - automatically generates constructor, getters, equals, hashCode, and toString
}
