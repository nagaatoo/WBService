package ru.numbDev.wildberries.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.numbDev.wildberries.db.entity.MetaEntity;
import ru.numbDev.wildberries.db.entity.ProductEntity;
import ru.numbDev.wildberries.db.entity.RequestEntity;
import ru.numbDev.wildberries.db.entity.StatisticEntity;
import ru.numbDev.wildberries.db.repository.MetaRepository;
import ru.numbDev.wildberries.db.repository.ProductRepository;
import ru.numbDev.wildberries.POJO.ParseResult;
import ru.numbDev.wildberries.POJO.json.Product;
import ru.numbDev.wildberries.util.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WBService {

    private final static Logger log = LoggerFactory.getLogger(WBService.class);

    // Буфер для номенклатур
    private final Map<ProductEntity, Set<Long>> nomenclatureBuffer = new HashMap<>();

    private final Utils utils;
    private final ProductRepository productRepository;
    private final MetaRepository metaRepository;

    public WBService(Utils utils, ProductRepository productRepository, MetaRepository metaRepository) {
        this.utils = utils;
        this.productRepository = productRepository;
        this.metaRepository = metaRepository;
    }

    // Основная логика
    @Transactional
    public int run(RequestEntity request, int page) {

        // TODO
        // Надо делать 3 запроса:
        // 1) Запрос на бакет (https://wbxsearch.wildberries.ru/exactmatch/v2/common?query=Цыпа)
        //      В нем будут интересовать 2 поля: query и shardKey
        //      query - параметр, который указывает на индекс фильтра искомых товаров (возможно) (пример: product или presets/bucket_<id>)
        //      shardKey - ключ, который вставляется в url запроса товаров
        //          (например https://wbxcatalog-ru.wildberries.ru/ + product + /catalog?locale=ru&lang=ru&subject=5186;1054;3568;3569;2878;3511&sort=sale)
        //          (или https://wbxcatalog-ru.wildberries.ru/ + presets/bucket_52 + /catalog?locale=ru&lang=ru&preset=10025922&sort=sale)
        //      p.s. надо поправить ProductsList
        //
        // 2) Модифицируя url на основе запроса выше, сделать запрос в https://wbxcatalog-ru.wildberries.ru/ с требуемым параметром (lang и locale необходимы. sort - не обязателен)
        // 3) Запрос на страничку (пока работает как есть)

        // Выгрузим страницу со списком продуктов
        var productPOJO = utils.loadListProducts(request.getRequest(), page);

        // Если получили ошибку - попробуем позже ту же страницу
        if (productPOJO.getData() == null) {
            return page;
        }

        var listOfProducts = productPOJO
                .getData()
                .getProducts();

        // Если пусто - значит конец списка, обнуляем счетчик
        if (listOfProducts.isEmpty()) {
            return 0;
        }

        // Обрабатываем и маппируем
        var entities = listOfProducts
                // Нельзя параллельный стрим - WB банит
                .stream()
                .map(buildWBData())
                .parallel()
                .map(buildEntity(request))
                .collect(Collectors.toList());

        // Добавим номенклатуры
        addNomenclatures(entities);

        // Сохраняем отдельно
        productRepository.saveAll(entities);

        log.info("==================================================");

        // Инкрементим пагинацию
        return page + 1;

    }

    // Лямбда скачивает и парсит необходимые данные
    private Function<Product, ParseResult> buildWBData() {
        return (p) -> {
            Utils.threadPause();

            var page = utils.getProductPage(p.getId());
            var res = utils.parseProductNomenclature(page);

            return res
                    .setProductId(p.getId())
                    .setBrand(p.getBrand())
                    .setBrandId(p.getBrandId())
                    .setPriceU(p.getPriceU())
                    .setSalePriceU(p.getSalePriceU())
                    .setRating(p.getRating())
                    .setName(p.getName());
        };
    }

    // Лямбда сохраняет в базу результат
    private Function<ParseResult, ProductEntity> buildEntity(RequestEntity request) {
        return (p) -> {
            var product = productRepository.findByProductId(p.getProductId()).orElseGet(ProductEntity::new);

            if (product.getId() == null) {
                product
                        .setProductId(p.getProductId());
            }

            // Добавим запрос, по которому нашли продукт, по необходимости
            addRequest(request, product);

            // Добавим метаданные, по необходимости
            addMetaDataToProduct(p, product);

            // Запоминаем номенклатуры
            nomenclatureBuffer.put(product, p.getNomenclatures());

            // Добавим статистику
            product.getStatistics().add(
                    new StatisticEntity()
                            .setProduct(product)
                            .setCountOfSales(p.getSalesResult())
                            .setPriceU(p.getPriceU())
                            .setRating(p.getRating())
                            .setSalePriceU(p.getSalePriceU())
                            .setCreatedDate(new Date())
            );

            System.out.println(p.getProductId() + " : " + p.getSalesResult() + " : " + p.getBrand());
            return product;
        };
    }

    private void addRequest(RequestEntity request, ProductEntity product) {

        // Добавляем запрос, если новый
        var requestContains = product.getRequests()
                .stream()
                .anyMatch(r -> r.getId().equals(request.getId()));

        if (!requestContains) {
            product.getRequests().add(request);
        }
    }

    private void addMetaDataToProduct(ParseResult p, ProductEntity product) {

        // Создаем объект метаданных если:
        // 1) Продукт новый и их нет вообще
        // 2) Какие-либо данные, которые мы отслеживаем, изменились у последней версии
        boolean isChangedMeta = product.getMetaData().isEmpty()
                || product
                .getMetaData()
                .stream()
                .max(Comparator.comparingInt(MetaEntity::getVersion))
                .stream()
                .anyMatch(m ->
                        !m.getBrand().equalsIgnoreCase(p.getBrand()) ||
                                !m.getProductName().equalsIgnoreCase(p.getName()) ||
                                m.getBrandId() != p.getBrandId()
                );

        if (isChangedMeta) {
            MetaEntity meta = new MetaEntity()
                    .setProduct(product)
                    .setProductName(p.getName())
                    .setBrand(p.getBrand())
                    .setBrandId(p.getBrandId())
                    .setPageJson(p.getPage())
                    .setVersion(
                            product
                                    .getMetaData()
                                    .stream()
                                    .map(MetaEntity::getVersion)
                                    .max(Integer::compare)
                                    .orElse(0)
                    );

            meta.setProduct(product);
            metaRepository.save(meta);
            //            product.getMetaData().add(meta);
        }
    }

    private void addNomenclatures(List<ProductEntity> products) {

        for (var es : nomenclatureBuffer.entrySet()) {
            var product = es.getKey();
            var nomenclaturesId = es.getValue();

            for (Long id : nomenclaturesId) {
                //  Сначала ищем в БД
                ProductEntity nomenclature = productRepository
                        .findByProductId(id)

                        // Если не нашли, то ищем среди новых
                        .orElseGet(() -> products
                                .stream()
                                .filter(p -> Objects.equals(p.getProductId(), id))
                                .findAny()

                                // Иначе - создаем новый
                                .orElseGet(() -> new ProductEntity().setProductId(id)));

                // Сохраняем в кеш, привязываем к продукту и закидываем в пулл продуктов
                // TODO
                var saved = productRepository.save(nomenclature);
                product.getNomenclatures().add(saved);
                products.add(saved);
            }
        }
    }

}
