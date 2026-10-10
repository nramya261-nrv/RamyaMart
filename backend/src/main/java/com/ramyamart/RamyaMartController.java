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

    // =========================
    // CHAT SUPPORT ASSISTANT
    // =========================

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.getOrDefault("message", "").trim();
        Map<String, String> response = new HashMap<>();

        if (userMessage.isEmpty()) {
            response.put("reply", "Hello! How can I help you with your shopping today?");
            return ResponseEntity.ok(response);
        }

        String lower = userMessage.toLowerCase();
        String reply;

        if (lower.contains("hi") || lower.contains("hello") || lower.contains("hey")) {
            reply = "Hello! 👋 Welcome to Ramya Mart Support. How can I assist you today? You can ask me about products, orders, delivery, or payment methods!";
        } else if (lower.contains("order") || lower.contains("track") || lower.contains("status")) {
            long totalOrders = orderRepository.count();
            reply = "📦 You can view and track your orders in the Orders section. We currently have " + totalOrders + " total orders in the system. Delivery typically takes 2 to 4 business days!";
        } else if (lower.contains("pay") || lower.contains("cod") || lower.contains("upi") || lower.contains("card")) {
            reply = "💳 We support Cash on Delivery (COD), UPI (Google Pay, PhonePe, Paytm), and Credit/Debit Cards!";
        } else if (lower.contains("return") || lower.contains("refund")) {
            reply = "🔄 Ramya Mart offers a hassle-free 7-day return and exchange policy on all products!";
        } else if (lower.contains("contact") || lower.contains("support") || lower.contains("help") || lower.contains("phone") || lower.contains("email")) {
            reply = "📞 Customer Support: support@ramyamart.com | Helpline: +91 98765 43210 (Mon-Sat, 9 AM - 8 PM).";
        } else if (lower.contains("laptop") || lower.contains("hp") || lower.contains("dell") || lower.contains("lenovo") || lower.contains("asus")) {
            reply = "💻 We have high-performance laptops from HP, Dell, Lenovo, and ASUS starting from ₹48,000! Click the 'Laptops' category above to view them.";
        } else if (lower.contains("mobile") || lower.contains("phone") || lower.contains("samsung") || lower.contains("oneplus")) {
            reply = "📱 We feature the latest 5G smartphones: Samsung Galaxy (₹25,000) and OnePlus Mobile (₹30,000)!";
        } else if (lower.contains("headphone") || lower.contains("audio") || lower.contains("boat") || lower.contains("earbud")) {
            reply = "🎧 We have Wireless Noise-Cancelling Bluetooth Headphones by Boat for ₹2,000 with crystal clear sound!";
        } else if (lower.contains("mouse") || lower.contains("accessory") || lower.contains("accessories")) {
            reply = "🖱️ Check out our Logitech Ergonomic Wireless Optical Mouse for ₹800 in the Accessories category!";
        } else if (lower.contains("product") || lower.contains("item") || lower.contains("price") || lower.contains("offer") || lower.contains("discount") || lower.contains("catalog") || lower.contains("shop")) {
            long count = productRepository.count();
            reply = "🛍️ We currently have " + count + " featured items across Laptops, Mobiles, Audio, and Accessories with special offers available today!";
        } else {
            reply = "Thank you for asking! I'm your Ramya Mart shopping assistant. You can ask me about our products (Laptops, Mobiles, Audio, Accessories), payment methods, order tracking, or support contacts!";
        }

        response.put("reply", reply);
        return ResponseEntity.ok(response);
    }
}