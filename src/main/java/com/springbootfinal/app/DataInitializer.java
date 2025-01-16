package com.springbootfinal.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM admin_users WHERE admin_id = ?";
            String insertSql = "INSERT INTO admin_users (admin_id, admin_passwd, admin_name, role) VALUES (?, ?, ?, ?)";

            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                 PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

                checkStatement.setString(1, "admin03");
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    insertStatement.setString(1, "admin03");
                    insertStatement.setString(2, passwordEncoder.encode("adminpass789"));
                    insertStatement.setString(3, "관리자1");
                    insertStatement.setString(4, "ROLE_ADMIN");
                    insertStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to execute CommandLineRunner", e);
        }
    }
}