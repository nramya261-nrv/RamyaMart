package com.ramyamart;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RamyaMartController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public RamyaMartController(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    // =========================
    // HOME
    // =========================

    @GetMapping("/home")
    public String home() {
        return "Welcome to Ramya Mart Backend!";
    }

    // =========================
    // PRODUCTS
    // =========================

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable int id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(
            @PathVariable int id,
            @RequestBody Product product) {

        Product existingProduct =
                productRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(product.getName());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());

        return productRepository.save(existingProduct);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable int id) {

        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Product deleted successfully!";
        }

        return "Product not found!";
    }

    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        User existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            return "Username already exists!";
        }

        userRepository.save(user);

        return "Registration successful!";
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser != null
                && existingUser.getPassword().equals(user.getPassword())
                && existingUser.getRole().equals(user.getRole())) {

            return "Login successful!";
        }

        return "Invalid username, password or role!";
    }

    // =========================
    // USERS
    // =========================

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // =========================
    // ORDERS
    // =========================

    @PostMapping("/orders")
    public Order placeOrder(@RequestBody Order order) {

        if (order.getStatus() == null
                || order.getStatus().isEmpty()) {

            order.setStatus("Order Placed");
        }

        return orderRepository.save(order);
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @PutMapping("/orders/{id}/status")
    public Order updateOrderStatus(
            @PathVariable int id,
            @RequestParam String status) {

        Order order =
                orderRepository.findById(id).orElse(null);

        if (order == null) {
            return null;
        }

        order.setStatus(status);

        return orderRepository.save(order);
    }

    @DeleteMapping("/orders/{id}")
    public String deleteOrder(@PathVariable int id) {

        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return "Order deleted successfully!";
        }

        return "Order not found!";
    }
}