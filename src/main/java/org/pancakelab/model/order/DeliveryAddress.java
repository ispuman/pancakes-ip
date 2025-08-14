package org.pancakelab.model.order;

public record DeliveryAddress(int buildingNumber, int roomNumber) {

    private final static int MAX_BUILDING_NUMBER = 25;
    private final static int MAX_ROOM_NUMBER = 20;

    public final static String ERROR_ADDRESS_NUMBERS_SHOULD_BE_POSITIVE = "Building and room numbers must be positive.";
    public final static String ERROR_BUILDING_NUMBER_TOO_LARGE =
            "Building number must be less than or equal to %s.".formatted(MAX_BUILDING_NUMBER);
    public final static String ERROR_ROOM_NUMBER_TOO_LARGE =
            "Room number must be less than or equal to %s.".formatted(MAX_ROOM_NUMBER);

    public DeliveryAddress {
        if (buildingNumber <= 0 || roomNumber <= 0) {
            throw new IllegalArgumentException(ERROR_ADDRESS_NUMBERS_SHOULD_BE_POSITIVE);
        } else if (buildingNumber > MAX_BUILDING_NUMBER) {
            throw new IllegalArgumentException(ERROR_BUILDING_NUMBER_TOO_LARGE);
        } else if (roomNumber > MAX_ROOM_NUMBER) {
            throw new IllegalArgumentException(ERROR_ROOM_NUMBER_TOO_LARGE);
        }
    }
}
