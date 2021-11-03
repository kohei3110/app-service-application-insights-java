package com.example.springboot;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class DemoController {

	static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");
    }	

	@GetMapping("/hello")
	public String hello() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("message", "Hello World");
		return "index";
	}

	@RequestMapping("/todo")
	public void getToDos(Model model) throws IOException, SQLException {
		model.addAttribute("message", "Data Load");
        Logger log = Logger.getLogger(DemoController.class.getName());

		log.info("Loading application properties");
		Properties properties = new Properties();
		properties.load(DemoController.class.getClassLoader().getResourceAsStream("application.properties"));

        log.info("Connecting to the database");
        Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties);
        log.info("Database connection test: " + connection.getCatalog());

		log.info("Create database schema");
		Scanner scanner = new Scanner(DemoController.class.getClassLoader().getResourceAsStream("schema.sql"));
		Statement statement = connection.createStatement();
		while (scanner.hasNextLine()) {
			statement.execute(scanner.nextLine());
		}

        Todo todo = new Todo(1L, "configuration", "congratulations, you have set up JDBC correctly!", true);
        insertData(todo, connection);
        todo = readData(connection);
        todo.setDetails("congratulations, you have updated data!");
        updateData(todo, connection);
        deleteData(todo, connection);

		log.info("Closing database connection");
        connection.close();		
	}

	private static void insertData(Todo todo, Connection connection) throws SQLException {
        Logger log = Logger.getLogger(DemoController.class.getName());

		log.info("Insert data");
		PreparedStatement insertStatement = connection
				.prepareStatement("INSERT INTO todo (id, description, details, done) VALUES (?, ?, ?, ?);");
	
		insertStatement.setLong(1, todo.getId());
		insertStatement.setString(2, todo.getDescription());
		insertStatement.setString(3, todo.getDetails());
		insertStatement.setBoolean(4, todo.isDone());
		insertStatement.executeUpdate();
	}	

	private static Todo readData(Connection connection) throws SQLException {
        Logger log = Logger.getLogger(DemoController.class.getName());

		log.info("Read data");
		PreparedStatement readStatement = connection.prepareStatement("SELECT * FROM todo;");
		ResultSet resultSet = readStatement.executeQuery();
		if (!resultSet.next()) {
			log.info("There is no data in the database!");
			return null;
		}
		Todo todo = new Todo();
		todo.setId(resultSet.getLong("id"));
		todo.setDescription(resultSet.getString("description"));
		todo.setDetails(resultSet.getString("details"));
		todo.setDone(resultSet.getBoolean("done"));
		log.info("Data read from the database: " + todo.toString());
		return todo;
	}

	private static void updateData(Todo todo, Connection connection) throws SQLException {
        Logger log = Logger.getLogger(DemoController.class.getName());

		log.info("Update data");
		PreparedStatement updateStatement = connection
				.prepareStatement("UPDATE todo SET description = ?, details = ?, done = ? WHERE id = ?;");
	
		updateStatement.setString(1, todo.getDescription());
		updateStatement.setString(2, todo.getDetails());
		updateStatement.setBoolean(3, todo.isDone());
		updateStatement.setLong(4, todo.getId());
		updateStatement.executeUpdate();
		readData(connection);
	}

	private static void deleteData(Todo todo, Connection connection) throws SQLException {
        Logger log = Logger.getLogger(DemoController.class.getName());

		log.info("Delete data");
		PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM todo WHERE id = ?;");
		deleteStatement.setLong(1, todo.getId());
		deleteStatement.executeUpdate();
		readData(connection);
	}	

}
