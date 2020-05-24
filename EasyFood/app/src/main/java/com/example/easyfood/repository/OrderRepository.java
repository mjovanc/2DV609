package com.example.easyfood.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.easyfood.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a OrderRepository Singleton
 */
public class OrderRepository {
    private String TAG = "OrderRepository";
    private static OrderRepository instance;
    private FirebaseFirestore database = FirebaseFirestore.getInstance(); // TODO Move down queries to Firebase class

    // For the restaurant/manager to be able to observe all incoming orders.
    private MutableLiveData<List<Order>> orders = new MutableLiveData<>();

    // One specific order
    private MutableLiveData<Order> order = new MutableLiveData<>();

    private ArrayList<Order> orderList;
    private Order currentOrder;

    /**
     * Returns an instance of the OrderRepository
     *
     * @return instance : OrderRepository - The instance of the OrderRepository
     */
    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }

        return instance;
    }

    /**
     * Returns a LiveData list of all orders
     *
     * @param eateryId : String - The id of the eatery
     * @return orders : MutableLiveData<List<Order>> - The list of orders
     */
    public MutableLiveData<List<Order>> getAllOrders(String eateryId) {
        orderList = new ArrayList<>();

        getOrdersFromDatabase(eateryId, new IOrdersCallback() {
            @Override
            public void send(ArrayList<Order> list) {
                orderList.addAll(list);
                orders.setValue(orderList);
            }
        });

        orders.setValue(orderList);
        return orders;
    }

    /**
     * Returns a LiveData of an order
     *
     * @param orderId : String - The id of the order
     * @return order : MutableLiveData<Order> - The order
     */
    public MutableLiveData<Order> getOrder(String orderId) {
        currentOrder = new Order();

        getOrderFromDatabase(orderId, new IOrderCallback() {
            @Override
            public void send(Order newOrder) {
                currentOrder = newOrder;
                order.setValue(currentOrder);
            }
        });

        order.setValue(currentOrder);
        return order;
    }

    /**
     * Gets all orders from the database that belongs to the eatery
     *
     * @param eateryId : String - The id of the eatery
     * @param callback : IOrdersCallback - Callback
     */
    private void getOrdersFromDatabase(final String eateryId, final IOrdersCallback callback) {
        database.collection("orders").whereEqualTo("eateryId", eateryId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Order> orders = new ArrayList<>();

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Order order = document.toObject(Order.class);
                                orders.add(order);
                            }

                            callback.send(orders);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /**
     * Get the order with the specific orderId
     *
     * @param orderId : String - The id of the order
     * @param callback : IOrderCallback - Callback
     */
    private void getOrderFromDatabase(final String orderId, final IOrderCallback callback) {
        database.collection("orders").document(orderId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        Order order = document.toObject(Order.class);
                        callback.send(order);
                    }
                });
    }

    /**
     * Sends the order to the database
     *
     * @param order : Order - The order
     */
    public void sendOrder(final Order order) {
        final String orderId = getGeneratedOrderIdFromDatabase();
        order.setId(orderId);

        database.collection("orders").document(orderId)
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setOrderStatus(orderId, Order.Status.SENT);
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * Updates the orderStatus of the order in the database
     *
     * @param orderId : String - The id of the order
     * @param newStatus : OrderStatusEnum - The order status
     */
    private void setOrderStatus(String orderId, Order.Status newStatus) {
        Map<String, Object> status = new HashMap<>();
        status.put("orderStatus", newStatus);

        database.collection("orders").document(orderId).update(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * Returns a new generated order id from the database
     *
     * @return String: id - New id from the database
     */
    private String getGeneratedOrderIdFromDatabase() {
        return database.collection("orders").document().getId();
    }


    /**
     * Interface
     */
    private interface IOrderCallback {
        void send(Order newOrder);
    }


    /**
     * Interface
     */
    private interface IOrdersCallback {
        void send(ArrayList<Order> list);
    }

}
