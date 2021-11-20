package ru.numbDev.wildberries.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.numbDev.wildberries.POJO.ParseResult;
import ru.numbDev.wildberries.error.WBException;
import ru.numbDev.wildberries.POJO.json.ProducsList;
import ru.numbDev.wildberries.POJO.json.WBSearch;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Utils {

    private final static Logger log = LoggerFactory.getLogger(Utils.class);

    // Ожидание перед новым вызовом
    private final static long HOLD_TIME = 500000; // 8,3 минуты

    private final static String SCHEME = "https";
    private final static String HOST_SEARCH = "wbxsearch.wildberries.ru";
    private final static String HOST_BUCKET = "wbxcatalog-ru.wildberries.ru";
    private final static String HOST = "www.wildberries.ru";

    private final static RestTemplate template = new RestTemplate();

    // Выгружаем бакет продуктов по запросу
    public ProducsList loadListProducts(String request, int page) {

        // Выполняем шаг 1 - ищем параметры для запроса товара
        var bucket = searchProductsBucket(request);

        if (StringUtils.isBlank(bucket.getShardKey()) || StringUtils.isBlank(bucket.getQuery())) {
            return new ProducsList();
        }

        // Выполняем шаг 2 - выгружаем бакет продуктов (со всех доступных страниц)
        return loadProductsBucket(bucket, page);
    }

    private WBSearch searchProductsBucket(String request) {
        UriComponents builder = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST_SEARCH)
                .path("/exactmatch/v2/common")
                .queryParam("query", request)
                .build();

        try {
            var res = template.getForEntity(builder.toUriString(), WBSearch.class);

            if (res.getStatusCode() != HttpStatus.OK) {
                throw new WBException("Cannot get bucket product. Status: " + res.getStatusCode());
            }

            return res.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new WBSearch();
        }
    }

    private ProducsList loadProductsBucket(WBSearch wbSearchResult, int page) {

        UriComponents builder = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST_BUCKET)
                .path(wbSearchResult.getShardKey() + "/catalog")

                // Параметры
//                .queryParam("regions", "64,75,4,38,30,33,70,1,71,22,31,66,80,69,48,40,68")
//                .queryParam("stores", "119261,122252,122256,117673,122258,122259,121631,122466,122467,122495,122496,122498,122590,122591,122592,123816,123817,123818,123820,123821,123822,124093,124094,124095,124096,124097,124098,124099,124100,124101,124583,124584,127466,126679,126680,127014,126675,126670,126667,125186,125611,116433,6159,507,3158,117501,120762,119400,120602,6158,121709,1699,2737,117986,1733,686,117413,119070,118106,119781")
//                .queryParam("pricemarginCoeff", 1.0)
//                .queryParam("reg", 0)
//                .queryParam("appType", 1)
//                .queryParam("offlineBonus", 0)
//                .queryParam("onlineBonus", 0)
//                .queryParam("emp", 0)
//                .queryParam("curr", "rub")
//                .queryParam("couponsGeo", "12,3,18,15,21")
                .queryParam("locale", "ru")
                .queryParam("lang", "ru")
                .queryParam("page", page)
                .query(wbSearchResult.getQuery())
//                .queryParam("search", request)
//                .queryParam("xshard", "")
//                .queryParam("sort", "popular")

                .build();

        try {


            var res = template.getForEntity(builder.toUriString(), String.class);

            if (res.getStatusCode() != HttpStatus.OK) {
                throw new WBException("Cannot get list products. Status: " + res.getStatusCode());
            }

            return new ObjectMapper().readValue(res.getBody(), ProducsList.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ProducsList();
        }
    }

    // Выгружаем страницу продукта
    public String getProductPage(long productId) {
        UriComponents builder = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(HOST)
                .path("/catalog/" + productId + "/detail.aspx")
                .build();

        var res = loopRequest(builder);
        return res.getBody();
    }

    private ResponseEntity<String> loopRequest(UriComponents builder) {
        while (true) {
            try {
                var res = template.getForEntity(builder.toUriString(), String.class);
                if (res.getStatusCode() != HttpStatus.OK) {
                    log.error("Cannot get list products. Status: " + res.getStatusCode());
                }

                return res;
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());

                // Предполагаем, что 429-ая
                threadPause(HOLD_TIME);
            }
        }
    }

    // Высчитать из страницы суммарные продажи продукта (key) и его количество (val)
    public ParseResult parseProductNomenclature(String page) {
        try {
            Pattern pattern = Pattern.compile("ssrModel:(.*)");
            Matcher matcher = pattern.matcher(page);

            if (matcher.find()) {
                return parseJSONData(matcher);
            } else {
                return new ParseResult();
            }

        } catch (Exception e) {
            // Ошибка парсинга - игнорим продукт
            e.printStackTrace();
            log.error(e.getMessage());
            return new ParseResult();
        }
    }

    // Парсим json с информацией о продукте
    private ParseResult parseJSONData(Matcher matcher) {

        // Матчим со страницы
        var res = matcher.group()
                .substring(0, matcher.group().length() - 1)
                .replace("ssrModel:", "");

        // Корень
        JSONObject obj = new JSONObject(res);

        // id номенклатур продукта
        List<Long> nomenclatures = obj
                .getJSONObject("product")
                .getJSONArray("nomenclatures")
                .toList()
                .stream()
                .map(n -> (HashMap<String, Object>) n)
                .map(j -> Long.valueOf((Integer) j.get("nmId")))
                .collect(Collectors.toList());

        // Сам продукт
        JSONObject selected = obj.getJSONObject("selectedNomenclature");

        return new ParseResult()
                .addNomenclatures(nomenclatures)
                .setSalesResult((int) selected.get("ordersCount"))
                .setPage(res);
    }

    // Пауза на 4 секунды
    public static void threadPause() {
        threadPause(4000);
    }

    public static void threadPause(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }
}
