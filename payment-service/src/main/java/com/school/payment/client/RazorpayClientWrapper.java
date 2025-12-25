package com.school.payment.client;

import com.school.payment.config.RazorpayConfig;
import com.school.payment.exception.PaymentException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class RazorpayClientWrapper {

    // keep as Object so the class doesn't require the Razorpay SDK at compile time
    private final Object client;
    private final Object orders;

    public RazorpayClientWrapper(RazorpayConfig config) {
        Object tmpClient = null;
        Object tmpOrders = null;
        try {
            // Try to load RazorpayClient via reflection
            Class<?> clientClass = Class.forName("com.razorpay.RazorpayClient");
            Constructor<?> ctor = clientClass.getConstructor(String.class, String.class);
            tmpClient = ctor.newInstance(config.getKeyId(), config.getKeySecret());

            // The SDK exposes a public field 'Orders' on the client instance
            Field ordersField = clientClass.getField("Orders");
            tmpOrders = ordersField.get(tmpClient);
        } catch (ClassNotFoundException e) {
            // Razorpay SDK not on classpath - keep client/orders null and operate in degraded mode
        } catch (Exception e) {
            throw new PaymentException("Failed to create Razorpay client", e);
        }

        this.client = tmpClient;
        this.orders = tmpOrders;
    }

    /**
     * Create an order. Returns a Map representation of the order JSON. This method is resilient:
     * - If Razorpay SDK and org.json are present it will call Orders.create and convert the returned JSONObject to Map.
     * - If SDK is absent it will return a Map with an error message.
     */
    public Map<String, Object> createOrder(long amount, String currency, String receipt, Map<String, String> notes) {
        if (orders == null) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("error", "Razorpay SDK not available at runtime");
            fallback.put("amount", amount);
            fallback.put("currency", currency);
            fallback.put("receipt", receipt);
            fallback.put("notes", notes);
            return fallback;
        }

        try {
            // Build a request object. Prefer org.json.JSONObject if available, otherwise use a Map.
            Object orderRequest;
            Class<?> jsonClass = null;
            try {
                jsonClass = Class.forName("org.json.JSONObject");
            } catch (ClassNotFoundException ignored) {
                jsonClass = null;
            }

            if (jsonClass != null) {
                // Use JSONObject
                try {
                    // Try constructor with Map first
                    Constructor<?> mapCtor = null;
                    try {
                        mapCtor = jsonClass.getConstructor(java.util.Map.class);
                    } catch (NoSuchMethodException ignored) {
                        // fall back to default constructor
                    }

                    if (mapCtor != null) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("amount", amount);
                        m.put("currency", currency);
                        if (receipt != null) m.put("receipt", receipt);
                        if (notes != null) m.put("notes", notes);
                        orderRequest = mapCtor.newInstance(m);
                    } else {
                        orderRequest = jsonClass.getDeclaredConstructor().newInstance();
                        Method put = jsonClass.getMethod("put", String.class, Object.class);
                        put.invoke(orderRequest, "amount", amount);
                        put.invoke(orderRequest, "currency", currency);
                        if (receipt != null) put.invoke(orderRequest, "receipt", receipt);
                        if (notes != null) {
                            // create JSONObject from notes map
                            Constructor<?> notesCtor = null;
                            try {
                                notesCtor = jsonClass.getConstructor(java.util.Map.class);
                            } catch (NoSuchMethodException ignored) {
                            }
                            if (notesCtor != null) {
                                Object notesObj = notesCtor.newInstance(notes);
                                put.invoke(orderRequest, "notes", notesObj);
                            } else {
                                Object notesObj = jsonClass.getDeclaredConstructor().newInstance();
                                Method putNotes = jsonClass.getMethod("put", String.class, Object.class);
                                for (Map.Entry<String, String> e : notes.entrySet()) {
                                    putNotes.invoke(notesObj, e.getKey(), e.getValue());
                                }
                                put.invoke(orderRequest, "notes", notesObj);
                            }
                        }
                    }
                } catch (NoSuchMethodException nsme) {
                    throw new RuntimeException(nsme);
                }
            } else {
                // No org.json available - use a Map as request
                Map<String, Object> m = new HashMap<>();
                m.put("amount", amount);
                m.put("currency", currency);
                if (receipt != null) m.put("receipt", receipt);
                if (notes != null) m.put("notes", notes);
                orderRequest = m;
            }

            // Find a suitable 'create' method on the Orders object
            Method createMethod = null;
            for (Method method : orders.getClass().getMethods()) {
                if ("create".equals(method.getName()) && method.getParameterCount() == 1) {
                    createMethod = method;
                    break;
                }
            }

            if (createMethod == null) {
                throw new PaymentException("Razorpay Orders.create method not found", null);
            }

            Object result;
            try {
                result = createMethod.invoke(orders, orderRequest);
            } catch (IllegalArgumentException iae) {
                // Parameter type mismatch: try to convert Map -> JSONObject if possible
                if (orderRequest instanceof Map && jsonClass != null) {
                    Constructor<?> jsonMapCtor = jsonClass.getConstructor(java.util.Map.class);
                    Object jsonReq = jsonMapCtor.newInstance(orderRequest);
                    result = createMethod.invoke(orders, jsonReq);
                } else {
                    throw iae;
                }
            }

            // Convert result to Map<String,Object>
            if (result == null) return new HashMap<>();

            String json = result.toString();

            // Prefer org.json.JSONObject.toMap() if available
            try {
                Class<?> jsonClass2 = Class.forName("org.json.JSONObject");
                Constructor<?> jsonCtor = jsonClass2.getConstructor(String.class);
                Object jsonObj = jsonCtor.newInstance(json);
                Method toMap = jsonClass2.getMethod("toMap");
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = (Map<String, Object>) toMap.invoke(jsonObj);
                return responseMap;
            } catch (ClassNotFoundException cnfe) {
                // org.json not present; try Jackson via reflection
            }

            try {
                Class<?> omClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                Object om = omClass.getDeclaredConstructor().newInstance();
                Method readValue = omClass.getMethod("readValue", String.class, Class.class);
                @SuppressWarnings("unchecked")
                Map<String, Object> responseMap = (Map<String, Object>) readValue.invoke(om, json, Map.class);
                return responseMap;
            } catch (ClassNotFoundException cnfe) {
                // Jackson not present either - fall back to returning raw JSON string
            }

            Map<String, Object> fallback = new HashMap<>();
            fallback.put("raw", json);
            return fallback;

        } catch (PaymentException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PaymentException("Failed to create order", e);
        }
    }

}
