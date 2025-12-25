# payment-service

Payment gateway microservice integrating Razorpay.

Environment variables to set before running:

- RAZORPAY_KEY_ID
- RAZORPAY_KEY_SECRET
- RAZORPAY_WEBHOOK_SECRET

Build:

mvn -f payment-service clean package

Run:

mvn -f payment-service spring-boot:run

Endpoints:

- POST /api/payments/orders -> create order
- POST /api/payments/verify -> verify payment signature
- POST /api/payments/webhook -> webhook receiver (expects X-Razorpay-Signature header)

