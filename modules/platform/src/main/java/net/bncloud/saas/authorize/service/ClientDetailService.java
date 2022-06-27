package net.bncloud.saas.authorize.service;

import net.bncloud.saas.authorize.domain.ClientDetail;
import net.bncloud.saas.authorize.repository.ClientDetailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientDetailService {

    private final ClientDetailRepository clientDetailRepository;

    public ClientDetailService(ClientDetailRepository clientDetailRepository) {
        this.clientDetailRepository = clientDetailRepository;
    }

    public Optional<ClientDetail> getById(String clientId) {

        return clientDetailRepository.findById(clientId);
    }

}
