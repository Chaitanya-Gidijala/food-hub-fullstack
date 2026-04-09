# FoodHub API Documentation (Unified via API Gateway)

## Base URL
- **API Gateway:** `http://localhost:9001`
- All frontend calls should use the gateway base URL and then the paths listed below.

## Auth (user-service)
1. `POST /api/auth/register`
   - Body: `RegisterRequest`
   - Notes: returns registration result (`String`)
2. `POST /api/auth/login`
   - Body: `LoginRequest`
   - Notes: returns `AuthResponse` (contains `token`)
3. `GET /api/auth/profile?email=...`
   - Notes: query param `email` is required

### Request Models
- `RegisterRequest`: `username`, `email`, `password`, `role`, `panCardNumber`, `fssaiLicenseNumber`
- `LoginRequest`: `emailOrPhone`, `password`
- `AuthResponse`: `message`, `id`, `username`, `email`, `role`, `token`

## Users (user-service)
1. `GET /api/users/all`
   - Returns: `List<User>`
2. `PUT /api/users/{id}/approve?status=...`
   - Path: `{id}` = user id
   - Notes: query param `status` is required

## Addresses (user-service)
> These endpoints are served via the API Gateway after updating gateway route predicates to include `/api/addresses/**`.
1. `GET /api/users/{userId}/addresses`
2. `POST /api/users/{userId}/addresses`
   - Body: `AddressRequest`
3. `PUT /api/addresses/{addressId}/users/{userId}`
   - Body: `AddressRequest`
4. `DELETE /api/addresses/{addressId}/users/{userId}`
   - Notes: returns a confirmation message (`String`)
5. `PUT /api/addresses/{addressId}/users/{userId}/default`
   - Notes: no request body is required

### Request Models
- `AddressRequest`: `addressType`, `fullName`, `phoneNumber`, `addressLine1`, `addressLine2`, `city`, `state`, `pincode`, `landmark`, `isDefault`, `label`

## Restaurants (restaurant-service)
1. `POST /api/restaurants`
   - Body: `Restaurant`
2. `GET /api/restaurants`
   - Returns: `List<Restaurant>`
3. `GET /api/restaurants/{name}`
4. `GET /api/restaurants/id/{id}`
5. `GET /api/restaurants/owner/{ownerId}`
6. `PUT /api/restaurants/{id}/verify?status=...`
   - Notes: query param `status` is required
7. `PUT /api/restaurants/{name}`
   - Body: `Restaurant`
8. `DELETE /api/restaurants/{name}`

## Menu Items (restaurant-service)
1. `POST /api/menu-items`
   - Body: `MenuItem`
2. `GET /api/restaurants/{name}/menu-items`
   - Returns: `List<MenuItem>`
3. `PUT /api/menu-items/{id}`
   - Body: `MenuItem`
4. `DELETE /api/menu-items/{id}`

### Models (selected fields)
- `Restaurant`: `id`, `restaurantName`, `ownerId`, `location`, `phone`, `email`, `openingHoursJson`, `verificationStatus`
- `MenuItem`: `id`, `dishName`, `description`, `price`, `categoryName`, `restaurantId`, `available`

## Cart (order-cart-service)
1. `POST /cart/add`
   - Body: `CartItem`
2. `GET /cart/user/{userId}`
   - Returns: `List<CartItem>`
3. `PATCH /cart/update/{cartItemId}?quantity=...`
4. `DELETE /cart/remove/{cartItemId}`
5. `DELETE /cart/clear/{userId}`

## Orders (order-cart-service)
1. `POST /orders/place/{userId}`
   - Body: optional map; can include `addressId`
2. `GET /orders/user/{userId}`
3. `GET /orders/{orderId}`
4. `GET /orders/restaurant/{restaurantId}`
5. `PATCH /orders/{orderId}/status?status=...`
   - Notes: query param `status` is required

### Models (selected fields)
- `CartItem`: `cartItemId`, `userId`, `menuItemId`, `name`, `price`, `quantity`, `restaurantId`, `orderId`
- `Order`: `orderId`, `userId`, `restaurantId`, `restaurantName`, `totalPrice`, `status`, `addressId`, `cartItems`

## Payments (payment-service)
1. `POST /api/payments/create-session`
   - Body map expects:
     - `bookingId`
     - `amount`
   - Returns: payment URL (`String`)
2. `GET /api/payments/status/{sessionId}`
   - Returns: `Payment`
3. `GET /api/payments/all` (extra)

### Model (selected fields)
- `Payment`: `id`, `bookingId`, `stripeSessionId`, `amount`, `paymentStatus`

## Gateway Notes (important for frontend)
- Frontend must call `http://localhost:9001` (gateway) + the full paths above.
- If you still see `404 Not Found` for any `/api/addresses/**` call, confirm the API Gateway route predicates include:
  - `/api/addresses/**` -> `user-service`

