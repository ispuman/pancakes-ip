After going through the task README again, the following updates have been implemented:

1. New Users / DOs of the PancakeLab have been introduced - Chef and DeliveryService as obviously,
   it's not the disciples responsibility to prepare or to deliver pancakes.
2. DTOs have been introduced for domain objects Order and Pancake (and the corresponding Mappers).
3. The initial Facade have been split into PancakeClientFacade (create, cancel, complete), used by Disciple 
   and PancakeServiceFacade (prepare, deliver).
4. Client API has been moved to its own .client package (Disciple class and the PancakeShopCustomer interface).
5. Authorization checks for -cancel and -complete operations have been introduced to Order DO, 
   so only the Disciple who created the given order can cancel it or complete it.
6. Services use DO, while client and user interfaces and classes (Disciple, DeliveryService) use DTOs.
7. Test coverage has been split into per class bases as it has to be.
8. Currently, a business rule / limitation has been placed in createOrder() of the service CreateOrderService
   to ensure that a disciple cannot create a new pancake order / request, unless the previous one is canceled
   or delivered. Obviously, this can be improved upon with like maximum of 4 pending orders per disciple, for
   example, with adding of additional business logic for distinguishing between different disciple's orders 
   (introducing pancake -create -complete etc. dates, storing them in ArrayList for chronological order).