package com.org.auth.config;

import org.hibernate.dialect.MySQLDialect;

public class DialectConfig extends MySQLDialect {
    @Override
    public boolean dropConstraints() {
        return false;
    }
}
