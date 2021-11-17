package ru.numbDev.wildberries.POJO.json;

@lombok.Data
public class ProducsList {

    private Integer state;
    private Integer version;
    private Data data = new Data();

}

//{
//        "state": 0,
//        "version": 2,
//        "data": {
//        "products": [
//        {
//        "id": 28790068,
//        "name": "Игрушка антистресс",
//        "brand": "Попит",
//        "siteBrandId": 257786,
//        "brandId": 247786,
//        "priceU": 20000,
//        "price": 200,
//        "salePriceU": 14400,
//        "salePrice": 144,
//        "sale": 27,
//        "diffPrice": false,
//        "rating": 1,
//        "feedbacks": 1,
//        "pics": 1,
//        "isNew": false,
//        "colors": [],
//        "sizes": [
//        {
//        "name": "",
//        "origName": "0",
//        "rank": 0,
//        "optionId": 64760173
//        }
//        ],
//        "isAdult": false,
//        "isDigital": false,
//        "subjectId": 2547,
//        "root": 21180855
//        },
//        {
//        "id": 30955504,
//        "name": "Игрушка антистресс",
//        "brand": "Попит",
//        "siteBrandId": 257786,
//        "brandId": 247786,
//        "priceU": 28500,
//        "price": 285,
//        "salePriceU": 21300,
//        "salePrice": 213,
//        "sale": 25,
//        "diffPrice": false,
//        "rating": 5,
//        "feedbacks": 30,
//        "pics": 3,
//        "isNew": false,
//        "colors": [],
//        "sizes": [
//        {
//        "name": "14",
//        "origName": "0",
//        "rank": 2302,
//        "optionId": 67749139
//        }
//        ],
//        "isAdult": false,
//        "isDigital": false,
//        "subjectId": 2547,
//        "root": 20973448,
//        "promoTextCat": "FINAL SALE"
//        },