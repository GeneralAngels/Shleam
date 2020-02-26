# Shleam

### What is Shleam?
Shleam is an overlay library, created by General Angels, to give FRC teams a simple yet powerful platform to base their robots on.
 
 It gives you a recursive project structure with special capabilities such as scripting and network communications.

#### What does is actually do?
Shleam gives you the following interfaces:
1. The `Module` base class.
2. The `Function` interface.
3. The `Server` communications class.
4. The `Runtime` script interpreter and runtime manager.

#### 1. The `Module` base class
Let's say you want to create a new project that has a `Parent->Child` structure, such as a Robot.
You'd want to have a main class which combines many smaller classes each responsible for one part of the robot.

With Shleam, you'll make your main class inherit (extend) the `Module` class, then make every robot part also inherit from `Module`, then connect the parts with `adopt()`.

##### Why is it so important?
It allows you to write a relatively complex code in mere seconds.

#### 2. The `Function` interface
This interface, connected to a `Module` inherited class, allows you to expose certain functionality of your program to outside control, and not just what you coded your program to do.

For example, if I want to allow people to view the current cash a person has in my banking application, all I have to do is write the following code:

```java
public static void main(String[] arguments) {
    Shleam.begin(8000, new MyBank());
}

static class MyBank extends Module {

    HashMap<String, Integer> list = new HashMap<>();

    public MyBank() {
        super("bank");

        // Add people to my list
        list.put("shleam", 1337);

        // Expose my function
        register("get_cash", name -> Result.finished(list.get(name).toString()));
    }
}
```
The `register()` function allows you to register a new function, and takes a function name (`get_cash`), and an interface implementation. (`name -> Result.finished(list.get(name).toString())`)

#### 3. The `Server` communications class
This class is responsible for accepting new client sockets (over TCP/IP) and handling their input, passing them to their target and executing the requested `Function`.

For the example given in `2`, `Shleam.begin(8000, new MyBank());` initiates the server, and passes it a new `MyBank` module as a root module.

The `Server` class is especially useful for `FIRST FRC` usage, where you'd want to connect an additional computer to your robot and communicate with your main controller (`roboRIO`).
You can move information between the two and call functions to enhance your gameplay.

#### 4. The `Runtime` interface
This interface lets you take all that code that you already wrote for exposing functions and write a script for executing them.

Let's say I have the following setup:
```
robot
 drive - Takes distance as input
 shoot - Takes speed as input
 rgb   - Takes R, G, B as input
```
I can now write the following script and run it autonomously:
```shleam
// Set the LEDs to red
b robot rgb 255 0 0
// Wait for 1 second
b robot sleep 1000
// Drive and shoot at the same time (1 meter, 30rps)
a robot drive 1
a robot shoot 30
// Wait 2 seconds then change LEDs to blue
b robot sleep 2000
b robot rgb 0 0 255
```
I can upload this file to the program through the `Server` interface, by just typing `runtime load [script contents]` when I connect to my robot.

### Examples
TBD

### Installation
This project is synced to Maven Central, meaning all you have to do in order to install it is to add it to your `build.grade` file:

```groovy
dependencies{
    compile group: 'com.ga2230', name: 'shleam'
    compile group: 'org.json', name: 'json', version: '20190722'
}
```

### Contributing
You are more then welcome to contribute to the project.

Make Pull-Requests for smaller changes and open Issues for larger changes that need discussion.

### License
This project is licensed under the [MIT](https://choosealicense.com/licenses/mit/) license, meaning you can pretty much do what-ever you want with it.