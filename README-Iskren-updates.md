After going through the task README again, the following updates have been implemented:

01. New Users / DOs of the PancakeLab have been introduced - Chef and DeliveryService as
    it's not the disciple responsibility to prepare or to deliver pancakes.
02. DTOs have been introduced for domain objects Order and Pancake (and the corresponding Mappers).
03. The initial Facade have been split into PancakeClientFacade (create, cancel, complete), used by Disciple 
    and PancakeServiceFacade (prepare, deliver) used by Chef and DeliveryService.
04. Client API has been moved to its own .client package (Disciple class).
05. Authorization checks for -cancel() and -complete() operations have been introduced to Order DO, 
    so only the Disciple who created the given order can cancel it or complete it.
06. Services use DO, while client and user interfaces and classes (Disciple, DeliveryService) use DTOs.
07. orderId (of type UUID) has been removed from the Disciple class.
08. In Order DO, the customer type has been changed from UUID to PancakeShopCustomer (the Disciple interface type).
09. In the DB layer a new ConcurrentHashMap<Disciple, Order> has been introduced — discipleOrders.
10. Test coverage has been improved and split into per-class bases as it has to be.
11. Currently, a business rule / limitation has been placed in createOrder() of the service CreateOrderService
    to ensure that a disciple cannot create a new pancake order / request, unless the previous one is canceled
    or delivered (see 09). This can be improved upon with a maximum of 4 orders per disciple, for example
    (ConcurrentHashMap<Disciple, ArrayList<Order>> discipleOrders), with adding of additional business logic 
    for distinguishing between different disciple's orders and storing them in ArrayList for chronological order.