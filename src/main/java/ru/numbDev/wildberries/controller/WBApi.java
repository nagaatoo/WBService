package ru.numbDev.wildberries.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.numbDev.wildberries.error.WBException;
import ru.numbDev.wildberries.POJO.json.ProductFront;
import ru.numbDev.wildberries.POJO.json.ListRequest;
import ru.numbDev.wildberries.POJO.json.RegisterRequest;
import ru.numbDev.wildberries.service.ApiService;

import java.util.List;

@RestController
public class WBApi {

    private final ApiService apiService;

    public WBApi(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductFront> getAllProducts(@RequestBody ListRequest test) {
        return apiService.getAllStatistics(test.getPage());
    }

    @RequestMapping(path = "/all", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewRequest(@RequestBody RegisterRequest request) throws WBException {
        apiService.registerRequest(request);
    }
}
