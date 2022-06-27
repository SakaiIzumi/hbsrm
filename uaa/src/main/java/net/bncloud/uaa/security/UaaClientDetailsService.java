package net.bncloud.uaa.security;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

//@Service
public class UaaClientDetailsService extends JdbcClientDetailsService {

    private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove";

    private static final String CLIENT_FIELDS = "client_secret, " + CLIENT_FIELDS_FOR_UPDATE;

    private static final String BASE_FIND_STATEMENT = "select client_id, " + CLIENT_FIELDS
            + " from oauth_client_details";
    private static final String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ? AND is_deleted=0 AND disabled=0";


    public UaaClientDetailsService(DataSource dataSource) {
        super(dataSource);
        setSelectClientDetailsSql(DEFAULT_SELECT_STATEMENT);
    }
}
