package ru.numbDev.wildberries.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.numbDev.wildberries.convertor.ProductConvertor;
import ru.numbDev.wildberries.db.entity.RequestEntity;
import ru.numbDev.wildberries.db.repository.ProductRepository;
import ru.numbDev.wildberries.db.repository.RequestRepository;
import ru.numbDev.wildberries.error.WBException;
import ru.numbDev.wildberries.POJO.json.ProductFront;
import ru.numbDev.wildberries.POJO.json.RegisterRequest;

import java.util.List;

@Service
public class ApiService {

    private final ProductRepository productRepository;
    private final RequestRepository requestRepository;
    private final ProductConvertor productConvertor;

    public ApiService(ProductRepository productRepository, RequestRepository requestRepository, ProductConvertor productConvertor) {
        this.productRepository = productRepository;
        this.requestRepository = requestRepository;
        this.productConvertor = productConvertor;
    }

    @Transactional
    public List<ProductFront> getAllStatistics(int page) {
        return productConvertor.entityToFront(productRepository.getProductsWithPagging(page));
    }

    @Transactional
    public void registerRequest(RegisterRequest request) throws WBException {
        if (StringUtils.isBlank(request.getRequestStr())) {
            throw new WBException("Param is incorrect: request");
        }

        if (requestRepository.findEqualRequest(request.getRequestStr())) {
            throw new WBException("Request is exist");
        }

        var newRequest = new RequestEntity()
                .setRequest(request.getRequestStr())
                .setInfo(request.getInfo());

        requestRepository.save(newRequest);
    }
}
