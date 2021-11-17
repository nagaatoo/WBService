package ru.numbDev.wildberries.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.numbDev.wildberries.POJO.RequestLoop;
import ru.numbDev.wildberries.db.entity.RequestEntity;
import ru.numbDev.wildberries.db.repository.RequestRepository;

import java.util.*;

@Service
public class LoopService {

    private final static Logger log = LoggerFactory.getLogger(LoopService.class);

    // Страницы запросов.
    private final List<RequestLoop> pageRequest = Collections.synchronizedList(new ArrayList<>());

    private final WBService wbService;
    private final RequestRepository requestRepository;

    public LoopService(WBService wbService, RequestRepository requestRepository) {
        this.wbService = wbService;
        this.requestRepository = requestRepository;
    }

    public void addRequestToQueue(RequestEntity request) {
        pageRequest.add(
                new RequestLoop()
                        .setPage(0)
                        .setRequest(request)
        );
    }

    @Scheduled(fixedDelay = 400000) //400000
    public void mainLoop() {
        initRequests();

        for (RequestLoop request : pageRequest) {
            try {
                int newPage = wbService.run(request.getRequest(), request.getPage());
                request.setPage(newPage);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private static boolean flag = true;

    @Transactional
    public void initRequests() {

        if (flag) {
//            var one = new RequestEntity().setRequest("Рисовая лапша");
            var one = new RequestEntity().setRequest("Носки");
            var two = new RequestEntity().setRequest("Кошачий корм");
            var three = new RequestEntity().setRequest("Шоколадки");
            var four = new RequestEntity().setRequest("Специи");
            var five = new RequestEntity().setRequest("Крем");

            addRequestToQueue(requestRepository.save(one));
            addRequestToQueue(requestRepository.save(two));
            addRequestToQueue(requestRepository.save(three));
            addRequestToQueue(requestRepository.save(four));
            addRequestToQueue(requestRepository.save(five));
            flag = false;
        }

        if (pageRequest.isEmpty()) {
            requestRepository.findAll().forEach(this::addRequestToQueue);
        }
    }
}
