const productImages = {

    "HP Laptop":
        "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=500&q=80",

    "Dell Laptop":
        "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=500&q=80",

    "Lenovo Laptop":
        "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=500&q=80",

    "ASUS Laptop":
        "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=500&q=80",

    "Samsung Galaxy":
        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=500&q=80",

    "OnePlus Mobile":
        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=500&q=80",

    "Wireless Headphones":
        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=500&q=80",

    "Wireless Mouse":
        "https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=500&q=80"

};


let allProducts = [];

let cart = [];


// =====================================
// LOAD CART
// =====================================

try {

    const savedCart =
        localStorage.getItem("ramyaCart");

    if (savedCart) {

        cart = JSON.parse(savedCart);

    }

}
catch (error) {

    console.log(
        "Cart loading error:",
        error
    );

}


// =====================================
// SAVE CART
// =====================================

function saveCart() {

    localStorage.setItem(
        "ramyaCart",
        JSON.stringify(cart)
    );

}


// =====================================
// LOAD PRODUCTS
// =====================================

async function loadProducts() {

    try {

        const response =
            await fetch(
                "https://ramyamart-backend.onrender.com/api/products"
            );


        if (!response.ok) {

            throw new Error(
                "Products could not be loaded"
            );

        }


        allProducts =
            await response.json();


        displayProducts(
            allProducts
        );


        updateCart();

    }
    catch (error) {

        console.error(
            "Product loading error:",
            error
        );


        const container =
            document.querySelector(
                ".product-container"
            );


        if (container) {

            container.innerHTML = `

                <div style="
                    text-align:center;
                    padding:30px;
                    color:red;
                ">

                    Unable to load products.

                    <br>

                    Please make sure
                    backend is running.

                </div>

            `;

        }

    }

}


// =====================================
// DISPLAY PRODUCTS
// =====================================

function displayProducts(products) {

    const container =
        document.querySelector(
            ".product-container"
        );


    if (!container) {

        return;

    }


    container.innerHTML = "";


    if (
        !products ||
        products.length === 0
    ) {

        container.innerHTML = `

            <div style="
                text-align:center;
                padding:30px;
                font-size:20px;
            ">

                ❌ No products found.

            </div>

        `;

        return;

    }


    products.forEach(
        function(product) {

            const card =
                document.createElement(
                    "div"
                );


            card.className =
                "product-card";


            // =================================
            // PRODUCT DETAILS CLICK
            // =================================

            card.style.cursor =
                "pointer";


            card.addEventListener(
                "click",
                function() {

                    window.location.href =
                        "product-details.html?id="
                        + product.id;

                }
            );


            const image =
                productImages[
                    product.name
                ]
                ||
                "https://via.placeholder.com/500x350?text=Ramya+Mart";


            card.innerHTML = `

                <div class="product-image">

                    <img
                        src="${image}"
                        alt="${product.name}"

                        style="
                            width:100%;
                            height:250px;
                            object-fit:cover;
                            border-radius:10px;
                        "
                    >

                </div>


                <span class="discount">

                    Special Offer

                </span>


                <h3>
                    ${product.name}
                </h3>


                <p>
                    ${product.description || ""}
                </p>


                <div class="rating">

                    ⭐⭐⭐⭐⭐

                </div>


                <h4>

                    ₹${Number(
                        product.price
                    ).toLocaleString("en-IN")}

                </h4>


                <p>

                    Stock:
                    ${product.stock}

                </p>


                <button
                    class="add-cart-btn"
                    onclick="
                        event.stopPropagation();
                        addToCart(
                            ${JSON.stringify(product)}
                        );
                    "
                >

                    🛒 Add to Cart

                </button>


                <button
                    class="buy-btn"
                    onclick="
                        event.stopPropagation();
                        buyNow(
                            ${JSON.stringify(product)}
                        );
                    "
                >

                    🛍️ Buy Now

                </button>

            `;


            container.appendChild(
                card
            );

        }
    );

}


// =====================================
// SEARCH PRODUCTS
// =====================================

function searchProducts() {

    const searchInput =
        document.getElementById(
            "searchInput"
        );


    if (!searchInput) {

        alert(
            "Search box not found!"
        );

        return;

    }


    const searchText =
        searchInput.value
            .toLowerCase()
            .trim();


    if (searchText === "") {

        displayProducts(
            allProducts
        );

        return;

    }


    const filteredProducts =
        allProducts.filter(
            function(product) {

                const name =
                    String(
                        product.name || ""
                    ).toLowerCase();


                const description =
                    String(
                        product.description || ""
                    ).toLowerCase();


                const category =
                    String(
                        product.category || ""
                    ).toLowerCase();


                const brand =
                    String(
                        product.brand || ""
                    ).toLowerCase();


                return (

                    name.includes(
                        searchText
                    )

                    ||

                    description.includes(
                        searchText
                    )

                    ||

                    category.includes(
                        searchText
                    )

                    ||

                    brand.includes(
                        searchText
                    )

                );

            }
        );


    displayProducts(
        filteredProducts
    );


    if (
        filteredProducts.length === 0
    ) {

        const container =
            document.querySelector(
                ".product-container"
            );


        if (container) {

            container.innerHTML = `

                <div style="
                    text-align:center;
                    padding:40px;
                    font-size:20px;
                ">

                    🔍 No products found for

                    <strong>
                        "${searchText}"
                    </strong>

                </div>

            `;

        }

    }


    const productsSection =
        document.getElementById(
            "products"
        );


    if (productsSection) {

        productsSection.scrollIntoView({
            behavior: "smooth"
        });

    }

}


// =====================================
// SEARCH ENTER KEY
// =====================================

document.addEventListener(
    "DOMContentLoaded",
    function() {

        const searchInput =
            document.getElementById(
                "searchInput"
            );


        if (searchInput) {

            searchInput.addEventListener(
                "keyup",
                function(event) {

                    if (
                        event.key === "Enter"
                    ) {

                        searchProducts();

                    }

                }
            );

        }

    }
);


// =====================================
// CATEGORY
// =====================================

function showCategory(category) {

    const filteredProducts =
        allProducts.filter(
            function(product) {

                return (

                    String(
                        product.category || ""
                    ).toLowerCase()

                    ===

                    category.toLowerCase()

                );

            }
        );


    displayProducts(
        filteredProducts
    );


    const productsSection =
        document.getElementById(
            "products"
        );


    if (productsSection) {

        productsSection.scrollIntoView({
            behavior: "smooth"
        });

    }

}


// =====================================
// ADD TO CART
// =====================================

function addToCart(product) {

    const existingProduct =
        cart.find(
            function(item) {

                return (
                    item.id === product.id
                );

            }
        );


    if (existingProduct) {

        existingProduct.quantity++;

    }
    else {

        cart.push({

            id:
                product.id,

            name:
                product.name,

            price:
                Number(
                    product.price
                ),

            quantity:
                1

        });

    }


    saveCart();

    updateCart();


    alert(
        product.name
        + " added to your cart! 🛒"
    );

}


// =====================================
// UPDATE CART
// =====================================

function updateCart() {

    const cartItems =
        document.getElementById(
            "cart-items"
        );


    const cartTotal =
        document.getElementById(
            "cart-total"
        );


    if (
        !cartItems ||
        !cartTotal
    ) {

        return;

    }


    cartItems.innerHTML = "";


    if (cart.length === 0) {

        cartItems.innerHTML = `

            <p>
                Your cart is empty.
            </p>

        `;


        cartTotal.innerText =
            "Total: ₹0";


        return;

    }


    let total = 0;


    cart.forEach(
        function(item, index) {

            const itemTotal =
                Number(item.price)
                *
                Number(item.quantity);


            total += itemTotal;


            cartItems.innerHTML += `

                <div class="cart-item">

                    <div>

                        <strong>
                            ${item.name}
                        </strong>

                        <p>

                            ₹${Number(
                                item.price
                            ).toLocaleString("en-IN")}

                        </p>

                    </div>


                    <div>

                        <button
                            onclick="
                                decreaseQuantity(
                                    ${index}
                                )
                            "
                        >
                            −
                        </button>


                        <span
                            style="margin:0 10px;"
                        >

                            ${item.quantity}

                        </span>


                        <button
                            onclick="
                                increaseQuantity(
                                    ${index}
                                )
                            "
                        >
                            +
                        </button>

                    </div>


                    <strong>

                        ₹${itemTotal.toLocaleString(
                            "en-IN"
                        )}

                    </strong>


                    <button
                        onclick="
                            removeFromCart(
                                ${index}
                            )
                        "
                    >

                        Remove

                    </button>

                </div>

            `;

        }
    );


    cartTotal.innerText =
        "Total: ₹"
        +
        total.toLocaleString(
            "en-IN"
        );

}


// =====================================
// INCREASE QUANTITY
// =====================================

function increaseQuantity(index) {

    if (!cart[index]) {

        return;

    }


    cart[index].quantity++;


    saveCart();

    updateCart();

}


// =====================================
// DECREASE QUANTITY
// =====================================

function decreaseQuantity(index) {

    if (!cart[index]) {

        return;

    }


    if (
        cart[index].quantity > 1
    ) {

        cart[index].quantity--;

    }
    else {

        cart.splice(
            index,
            1
        );

    }


    saveCart();

    updateCart();

}


// =====================================
// REMOVE FROM CART
// =====================================

function removeFromCart(index) {

    cart.splice(
        index,
        1
    );


    saveCart();

    updateCart();

}


// =====================================
// BUY NOW
// =====================================

function buyNow(product) {

    cart = [

        {

            id:
                product.id,

            name:
                product.name,

            price:
                Number(
                    product.price
                ),

            quantity:
                1

        }

    ];


    saveCart();

    updateCart();


    const cartSection =
        document.getElementById(
            "cart"
        );


    if (cartSection) {

        cartSection.scrollIntoView({
            behavior: "smooth"
        });

    }


    alert(
        product.name
        + " selected for purchase! 🛍️"
    );

}


// =====================================
// CHECKOUT
// =====================================

function checkout() {

    if (cart.length === 0) {

        alert(
            "Your cart is empty!"
        );

        return;

    }


    const checkoutSection =
        document.getElementById(
            "checkout"
        );


    if (!checkoutSection) {

        alert(
            "Checkout section not found!"
        );

        return;

    }


    checkoutSection.scrollIntoView({
        behavior: "smooth"
    });

}


// =====================================
// PLACE ORDER
// =====================================

async function placeOrder(event) {

    if (event) {

        event.preventDefault();

    }


    if (cart.length === 0) {

        alert(
            "Your cart is empty!"
        );

        return;

    }


    const nameElement =
        document.getElementById(
            "customerName"
        );


    const phoneElement =
        document.getElementById(
            "phone"
        );


    const addressElement =
        document.getElementById(
            "address"
        );


    const pincodeElement =
        document.getElementById(
            "pincode"
        );


    const paymentElement =
        document.getElementById(
            "paymentMethod"
        );


    if (

        !nameElement ||

        !phoneElement ||

        !addressElement ||

        !pincodeElement ||

        !paymentElement

    ) {

        alert(
            "Checkout form not found!"
        );

        return;

    }


    const name =
        nameElement.value.trim();


    const phone =
        phoneElement.value.trim();


    const address =
        addressElement.value.trim();


    const pincode =
        pincodeElement.value.trim();


    const payment =
        paymentElement.value;


    if (

        name === "" ||

        phone === "" ||

        address === "" ||

        pincode === "" ||

        payment === ""

    ) {

        alert(
            "Please fill all delivery details."
        );

        return;

    }


    try {

        for (
            const item of cart
        ) {

            const order = {

                customerName:
                    name,

                phone:
                    phone,

                address:
                    address,

                pincode:
                    pincode,

                productName:
                    item.name,

                price:
                    Number(
                        item.price
                    ),

                quantity:
                    Number(
                        item.quantity
                    ),

                paymentMethod:
                    payment,

                status:
                    "Order Placed"

            };


            console.log(
                "Sending order:",
                order
            );


            const response =
                await fetch(
                    "https://ramyamart-backend.onrender.com/api/orders",
                    {

                        method:
                            "POST",

                        headers: {

                            "Content-Type":
                                "application/json"

                        },

                        body:
                            JSON.stringify(
                                order
                            )

                    }
                );


            const result =
                await response.text();


            console.log(
                "Backend status:",
                response.status
            );


            console.log(
                "Backend response:",
                result
            );


            if (!response.ok) {

                alert(

                    "Order Error!\n\n"

                    +

                    "Status: "

                    +

                    response.status

                    +

                    "\n\n"

                    +

                    result

                );


                return;

            }

        }


        alert(

            "🎉 Order placed successfully!\n\n"

            +

            "Thank you for shopping with Ramya Mart!"

        );


        cart = [];


        saveCart();

        updateCart();


        if (
            event &&
            event.target
        ) {

            event.target.reset();

        }


        window.location.href =
            "orders.html";

    }
    catch (error) {

        console.error(
            "ORDER ERROR:",
            error
        );


        alert(

            "Connection Error!\n\n"

            +

            error.message

        );

    }

}


// =====================================
// REGISTER
// =====================================

async function registerUser(event) {

    event.preventDefault();


    const username =
        document.getElementById(
            "name"
        ).value.trim();


    const email =
        document.getElementById(
            "email"
        ).value.trim();


    const password =
        document.getElementById(
            "password"
        ).value;


    const role =
        document.getElementById(
            "role"
        ).value;


    try {

        const response =
            await fetch(
                "https://ramyamart-backend.onrender.com/api/register",
                {

                    method:
                        "POST",

                    headers: {

                        "Content-Type":
                            "application/json"

                    },

                    body:
                        JSON.stringify({

                            username:
                                username,

                            email:
                                email,

                            password:
                                password,

                            role:
                                role

                        })

                }
            );


        const message =
            await response.text();


        alert(message);


        if (response.ok) {

            event.target.reset();

        }

    }
    catch (error) {

        console.error(error);

        alert(
            "Backend is not connected!"
        );

    }

}


// =====================================
// LOGIN
// =====================================

async function loginUser(event) {

    event.preventDefault();


    const username =
        document.getElementById(
            "username"
        ).value.trim();


    const password =
        document.getElementById(
            "loginPassword"
        ).value;


    const role =
        document.getElementById(
            "loginRole"
        ).value;


    try {

        const response =
            await fetch(
                "https://ramyamart-backend.onrender.com/api/login",
                {

                    method:
                        "POST",

                    headers: {

                        "Content-Type":
                            "application/json"

                    },

                    body:
                        JSON.stringify({

                            username:
                                username,

                            password:
                                password,

                            role:
                                role

                        })

                }
            );


        const message =
            await response.text();


        if (

            message
                .toLowerCase()
                .includes(
                    "login successful"
                )

        ) {

            alert(
                "Login successful! 🎉"
            );


            if (
                role === "Buyer"
            ) {

                window.location.href =
                    "buyer.html";

            }
            else if (
                role === "Seller"
            ) {

                window.location.href =
                    "seller.html";

            }
            else if (
                role === "Admin"
            ) {

                window.location.href =
                    "admin.html";

            }

        }
        else {

            alert(message);

        }

    }
    catch (error) {

        console.error(error);

        alert(
            "Backend is not connected!"
        );

    }

}


// =====================================
// PAGE LOAD
// =====================================

document.addEventListener(
    "DOMContentLoaded",
    function() {

        loadProducts();

        updateCart();

    }
);
