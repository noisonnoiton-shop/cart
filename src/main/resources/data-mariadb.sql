--sequence
select nextval(event_seq);

INSERT INTO cart
(account_id,product_id,product_active,product_quantity,product_info,created_at)
VALUES
(1
,1
,'active'
,1
,'{"id":1,"name":"Esprit Ruffle Shirt","categoryName":"women","amount":100,"originalPrice":80000,"salePercentage":5,"salePrice":4000,"resultPrice":76000,"img":"images/product-01.jpg"}'
,NOW());

INSERT INTO cart_event
(id,cart_id,domain,event_type,payload,tx_id,created_at)
VALUES
(1,1,'cart','CartProductAdded'
,'{"id":1,"accountId":1,"productId":1,"productActive":"active","productQuantity":1,"productInfo":{"id":1,"name":"Esprit Ruffle Shirt","categoryName":"women","amount":100,"originalPrice":80000,"salePercentage":5,"salePrice":4000,"resultPrice":76000,"img":"images/product-01.jpg"}}'
,'tx123123124'
,NOW());