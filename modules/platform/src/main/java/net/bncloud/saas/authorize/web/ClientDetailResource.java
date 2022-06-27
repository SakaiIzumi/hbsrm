package net.bncloud.saas.authorize.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.authorize.domain.ClientDetail;
import net.bncloud.saas.authorize.service.ClientDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/clients")
public class ClientDetailResource {

    private final ClientDetailService clientDetailService;

    public ClientDetailResource(ClientDetailService clientDetailService) {
        this.clientDetailService = clientDetailService;
    }

    @GetMapping("/{clientId}")
    public R<ClientDetail> getById(@PathVariable String clientId) {
        return R.data(clientDetailService.getById(clientId).orElse(null));
    }
}
