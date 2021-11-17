package ru.numbDev.wildberries.POJO;

import lombok.Data;
import ru.numbDev.wildberries.db.entity.RequestEntity;

@Data
public class RequestLoop {

    private RequestEntity request;
    private int page;
}
