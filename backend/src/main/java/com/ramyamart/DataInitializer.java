package com.ramyamart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public DataInitializer(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        try {
            if (productRepository.count() == 0) {
                productRepository.save(new Product("HP Laptop", "HP", "Intel Core i5, 8GB RAM, 512GB SSD", 50000.0, "Laptops", 10));
                productRepository.save(new Product("Dell Laptop", "Dell", "Intel Core i5, 16GB RAM, 512GB SSD", 60000.0, "Laptops", 8));
                productRepository.save(new Product("Lenovo Laptop", "Lenovo", "Intel Core i5, 8GB RAM, 512GB SSD", 52000.0, "Laptops", 12));
                productRepository.save(new Product("ASUS Laptop", "ASUS", "Intel Core i5, 16GB RAM, 512GB SSD", 65000.0, "Laptops", 7));
                productRepository.save(new Product("Samsung Galaxy", "Samsung", "8GB RAM, 128GB Storage, 5G", 25000.0, "Mobiles", 15));
                productRepository.save(new Product("OnePlus Mobile", "OnePlus", "12GB RAM, 256GB Storage, 5G", 30000.0, "Mobiles", 10));
                productRepository.save(new Product("Wireless Headphones", "Boat", "Bluetooth Noise Cancellation", 2000.0, "Audio", 25));
                productRepository.save(new Product("Wireless Mouse", "Logitech", "Ergonomic Wireless USB Mouse", 800.0, "Accessories", 30));
                System.out.println("Default products initialized successfully.");
            }

            if (userRepository.count() == 0) {
                userRepository.save(new User("Ramya", "12345", "Buyer", "nramya261@gmail.com"));
                userRepository.save(new User("Nithya", "12345", "Seller", "nithya123@gamil.com"));
                userRepository.save(new User("Admin", "12345", "Admin", "admin@ramyamart.com"));
                System.out.println("Default users initialized successfully.");
            }
        } catch (Exception e) {
            System.err.println("Notice: Data initialization skipped or failed: " + e.getMessage());
        }
    }
}
