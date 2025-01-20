package com.springbootfinal.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

//
//    여기는 샘플데이터의 passwordEncoder 가 되어있지 않아 발생하는 문제를 해결하기 위해
//            자동으로 샘플데이터를 paswordEncoder해서 넣어주는 메서드 입니다.
//    현제는 관리자 계정에 대한 샘플 이 존재합니다.
//

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
                    log.info("관리자 계정이 삽입되었습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to execute CommandLineRunner", e);
        }
    }
}