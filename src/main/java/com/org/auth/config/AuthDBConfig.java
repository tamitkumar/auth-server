package com.org.auth.config;

import com.org.auth.exception.AuthException;
import com.org.auth.exception.ErrorCode;
import com.org.auth.exception.ErrorSeverity;
import com.org.auth.utils.AuthConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class AuthDBConfig {
    @Bean
    DataSource dataSource() {
        String dbUser = AuthConstant.DB_USER_NAME;
        String dbPassword = AuthConstant.DB_PASSWORD;
        String driverClassName = AuthConstant.DB_DRIVER_CLASS_NAME;
        DriverManagerDataSource ds = new DriverManagerDataSource(getDBUrl(), dbUser, dbPassword);
        try {
            ds.setDriverClassName(driverClassName);
        } catch (Exception e) {
            throw new AuthException(ErrorCode.ERR002.getErrorCode(), ErrorSeverity.FATAL,
                    ErrorCode.ERR002.getErrorMessage(), e);
        }
        try {
            ds.getConnection().close();
        } catch (SQLException e) {
            throw new AuthException(ErrorCode.ERR002.getErrorCode(), ErrorSeverity.FATAL,
                    ErrorCode.ERR002.getErrorMessage(), e);
        }
        return ds;
    }

    private String getDBUrl() {
        String dbHost = AuthConstant.DB_HOST;
        String dbPort = AuthConstant.DB_PORT;
        String dbName = AuthConstant.DB_NAME;
        String dbUrlPrefix = AuthConstant.DB_URL_PREFIX;
        StringBuilder baseUrl = new StringBuilder(dbUrlPrefix);
        baseUrl.append(dbHost);
        baseUrl.append(AuthConstant.COLON);
        baseUrl.append(dbPort);
//		baseUrl.append(EMPConstant.COLON);
        baseUrl.append(dbName);
        return baseUrl.toString();
    }
}
