package com.ramyamart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RamyaMartController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DataSource dataSource;

    public RamyaMartController(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            DataSource dataSource) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.dataSource = dataSource;
    }

    // =========================
    // HOME & HEALTH
    // =========================

    @GetMapping("/home")
    public String home() {
        return "Welcome to Ramya Mart Backend!";
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "Ramya Mart Backend");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/db-status")
    public ResponseEntity<Map<String, Object>> dbStatus() {
        Map<String, Object> status = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            status.put("status", "UP");
            status.put("database", conn.getMetaData().getDatabaseProductName());
            status.put("version", conn.getMetaData().getDatabaseProductVersion());
            status.put("catalog", conn.getCatalog());
            status.put("productsCount", productRepository.count());
            status.put("usersCount", userRepository.count());
            status.put("ordersCount", orderRepository.count());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", e.getClass().getName());
            status.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(status);
        }
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
                && existingUser.getRole() != null
                && existingUser.getRole().equalsIgnoreCase(user.getRole())) {

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