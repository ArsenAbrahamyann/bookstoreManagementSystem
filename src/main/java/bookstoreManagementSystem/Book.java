package bookstoreManagementSystem;

public class Book {
    private String title;
    private String author;
    private String genre;
    private double price;
    private int quantityInStock;


    /**
     * Constructs a new {@code Book} with the specified details.
     *
     * @param title           the title of the book.
     * @param author          the author of the book.
     * @param genre           the genre of the book.
     * @param price           the price of the book.
     * @param quantityInStock the quantity of the book in stock.
     */
    public Book(String title, String author, String genre, double price, int quantityInStock) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    /**
     * Gets the title of the book.
     *
     * @return the title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the genre of the book.
     *
     * @return the genre of the book.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Gets the price of the book.
     *
     * @return the price of the book.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the quantityInStock of the book.
     *
     * @return the quantityInStock of the book.
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }
}

