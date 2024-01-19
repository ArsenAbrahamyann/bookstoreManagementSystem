package bookstoreManagementSystem;

public class Sales {
    private int bookId;
    private int customersId;
    private int quantitySold;
    private double totalPrice;


    /**
     * Constructs a new Sales instance with the specified book, customer, quantity sold, and total price.
     *
     * @param bookId      the ID of the book being sold.
     * @param customersId the ID of the customer making the purchase.
     * @param quantitySold the quantity of the book sold in the transaction.
     * @param totalPrice  the total price of the sales transaction.
     */
    public Sales(int bookId,int customersId, int quantitySold, double totalPrice) {
        this.bookId = bookId;
        this.customersId = customersId;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }


    /**
     * Gets the ID of the book being sold.
     *
     * @return the book ID.
     */
    public int getBookId() {
        return bookId;
    }


    /**
     * Gets the ID of the customer making the purchase.
     *
     * @return the customer ID.
     */
    public int getCustomersId() {
        return customersId;
    }


    /**
     * Gets the quantity of the book sold in the transaction.
     *
     * @return the quantity sold.
     */
    public int getQuantitySold() {
        return quantitySold;
    }


    /**
     * Gets the total price of the sales transaction.
     *
     * @return the total price.
     */
    public double getTotalPrice() {
        return totalPrice;
    }


}


