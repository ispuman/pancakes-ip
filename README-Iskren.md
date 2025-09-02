The pancakes implementation is encapsulated via the class Pancake (composition - interface PancakeRecipe), so it can be updated / changed without any issues, as the user accesses only Pancake class.
Because of the common functionality between different PancakeRecipe implementation classes / types, it is introduced
and abstract class AbstractPancake (the way how it is with the Collection framework).
All the hardcoded recipes are removed (and also the rigid class inheritance used is replaced with
polymorphic composition).

The initial PancakeService class has too many responsibilities / reasons to change and applying Interface Segregation Principle,
we have separate interfaces for every of the 5 main actions / services in the use case.

Continuing with the above, the DAO / DB layer is also extracted from PancakeService into its one repository package
with usage of interfaces (do not depend on concrete classes, but only on abstraction), so if the project later needs
a new DAO implementation (in memory, relational DB), we can adhere to Open Closed Principle and just provide new implementation class
for the interface methods, without updating existing code.

The Facade design pattern is used, and that's what the end user / Disciple has access to. We're flexible to change / provide new PancakeService implementations,
while adhering to Open Closed Principle.

The API / Facade does not expose any domain objects (like Order, PancakeRecipe / *Pancake classes).

The Singleton design pattern is used for the repository implementation class PancakeOrderRepositoryImpl.

For DB / storage mechanism is used ConcurrentHashMap, the operations are only adding and removing single entity
Entity<UUID, Order>
There are compound operations like 1.removing entity from pending-ConcurrentHashMap and putting the same entity
into completed-ConcurrentHashMap.
While the compound operation is not guarded by a Lock or synchronised, there is no danger of data-race issues here
as the UUID of every Order is unique ID and such operation can be executed only by one thread / Disciple.
As every disciple (thread) with every new request, creates unique ID for the ConcurrentHashMap key.

The logging is updated from StringBuilder to use Logger class from the JDK API.

Object Factory is used for adhering to Dependency Inversion Principle and they are used only in the Main() method
of the application, to initialize all the services, etc. 
Another approach here is to use Java Modular System and the ServiceLoader class.

From SOLID principles are used 4, except the Liskov Substitution Principle.
This may be like overkill for such a small application (as with greater flexibility achieved here, comes
greater complexity of usage), but the main idea is to be shown different ways of organising in modular, clean, and OOP way
the source code of huge, real codebase.
SOLID principles should not be applied everywhere (and classical design patterns too) as greater flexibility of change
brings with it greater complexity of usage.
They are best applied / with best results to module / component boundaries, where a greater flexibility is required.

New (concurrent) test cases can be written as a couple of Disciples (threads) can start pancake requests
at the same time. Test coverage is minimal, and I'm sending the project the way it is now as too much time
has passed for its implementation.
Java 24 has been used.



