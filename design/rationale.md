# Design Rational

I decided to have the abstract class entity which can be either satellite or device, because they are "Is-a" relationship and i can use polymorphism by doing this way. The common variables include entity type name, files, id, height, position, entity_type and maximum range.

I choose to create a sepearte class for File and an entity can have file, so entity can choose to store file, upload, download files.
In the file class, there are multiple file related operations can be done, such as create/remove file, check the progress on the transfer, update content, get progress of transfer and check if it has quantum for the relay satellite.

For the abstract class satellite, it does have all satellite common features such as linear velocity, support devices, send rate, receive rate, satellite type, angular velocity and is_move_up. Methods include operating satellite (move satellite to different directions), checking the if the num of files are too many, bandiwidh is full etc.

Three subclasses extend from satellite, each has different methods based on their functionalities. Relay satellite has special pattern of movements, so it have a method called "moveSatellite", shrinkingsatellite have "has_quantum".

The abstract class Devices have device_type, it also have "getDevice_type()" and "get_supported_device".

Three classes extend from it and each don't have method because their operations are similar, and i have implemented hte operation on the abstract class device.

Entities: Due to the time constraint, i implemented this class to save some space for implementation in the class blackoutController, it has all methods that involve the interaction of each entities. If the time is allowed, i would delete this class and instead put "is_reachable" into entity, so every time when checking if it's satellite or device we can use "instanceOf()" to figure out what type of the classes instead of if statement.
