package co.ke.whatsappcommerce.events;

public enum EventType {
    // Incoming Events
    CATALOG_UPDATED,
    PAYMENT_STATUS_UPDATED,
    DELIVERY_STATUS_UPDATED,

    // Outgoing Events
    ORDER_CREATED,
    ORDER_CANCELLED,
    SUPPORT_REQUESTED
}
