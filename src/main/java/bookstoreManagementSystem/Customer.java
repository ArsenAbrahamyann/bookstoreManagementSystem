package bookstoreManagementSystem;

public class Customer {
    private String name;
    private String email;
    private String phone;


    /**
     * Constructs a new Customer with the specified name, email, and phone number.
     *
     * @param name  the name of the customer
     * @param email the email address of the customer
     * @param phone the phone number of the customer
     */
    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Retrieves the name of the customer.
     *
     * @return the name of the customer
     */
    public String getName() {
        return name;
    }


    /**
     * Retrieves the email of the customer.
     *
     * @return the email of the customer
     */
    public String getEmail() {
        return email;
    }


    /**
     * Retrieves the phone of the customer.
     *
     * @return the phone of the customer
     */
    public String getPhone() {
        return phone;
    }


}

