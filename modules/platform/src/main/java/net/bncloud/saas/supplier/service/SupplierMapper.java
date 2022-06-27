package net.bncloud.saas.supplier.service;

import net.bncloud.saas.supplier.service.dto.SupplierDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierMapper implements RowMapper<SupplierDTO> {

    @Override
    public SupplierDTO mapRow(ResultSet resultSet, int i) throws SQLException {
        SupplierDTO order = new SupplierDTO();
    return order;
    }
}
