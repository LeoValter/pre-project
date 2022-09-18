package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.dropUsersTable();
        userService.createUsersTable();
        userService.saveUser("Ivan", "Ivanov", (byte) 25);
        System.out.println("User с именем - " + userService.getAllUsers().get(0).getName() + " добавлен в базу данных");
        userService.saveUser("Petr", "Petrov", (byte) 30);
        System.out.println("User с именем - " + userService.getAllUsers().get(1).getName() + " добавлен в базу данных");
        userService.saveUser("Sergey", "Sergeev", (byte) 35);
        System.out.println("User с именем - " + userService.getAllUsers().get(2).getName() + " добавлен в базу данных");
        userService.saveUser("Vladimir", "Vladimirov", (byte) 40);
        System.out.println("User с именем - " + userService.getAllUsers().get(3).getName() + " добавлен в базу данных");
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
