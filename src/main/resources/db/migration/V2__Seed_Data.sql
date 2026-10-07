-- ============================================================
-- Categories
-- ============================================================
INSERT INTO categories (name, description) VALUES
    ('Electronics',  'Gadgets, devices and electronic accessories'),
    ('Mobiles',      'Smartphones and mobile accessories'),
    ('Books',        'Fiction, non-fiction, academic and more'),
    ('Toys',         'Toys and games for all ages'),
    ('Beauty',       'Skincare, haircare and cosmetics'),
    ('Fashion',      'Clothing, footwear and accessories');

-- ============================================================
-- Brands
-- ============================================================
INSERT INTO brands (name) VALUES
    ('Apple'),
    ('Samsung'),
    ('Sony'),
    ('LG'),
    ('OnePlus'),
    ('Penguin Books'),
    ('HarperCollins'),
    ('LEGO'),
    ('Mattel'),
    ('L''Oreal'),
    ('Nivea'),
    ('Nike'),
    ('Adidas'),
    ('Zara');

-- ============================================================
-- Products
-- Category IDs:  1=Electronics, 2=Mobiles, 3=Books,
--                4=Toys, 5=Beauty, 6=Fashion
-- Brand IDs:     1=Apple, 2=Samsung, 3=Sony, 4=LG, 5=OnePlus,
--                6=Penguin Books, 7=HarperCollins, 8=LEGO,
--                9=Mattel, 10=L'Oreal, 11=Nivea,
--                12=Nike, 13=Adidas, 14=Zara
-- ============================================================

-- Electronics (product_ids 1-5)
INSERT INTO products (category_id, brand_id, name, description, unit_price, currency_code, stock_quantity, thumbnail_url) VALUES
    (1, 1,  'Apple MacBook Air M2',          '13.6-inch Liquid Retina display, Apple M2 chip, 8GB RAM, 256GB SSD',                         1099.99, 'USD', 50,  'https://images.unsplash.com/photo-1611186871525-3c1f2b4e4fa5?w=400'),
    (1, 2,  'Samsung 55" QLED 4K TV',        '55-inch QLED 4K Smart TV with Quantum HDR and built-in Alexa',                               799.99, 'USD', 30,  'https://images.unsplash.com/photo-1593784991095-a205069533cd?w=400'),
    (1, 3,  'Sony WH-1000XM5 Headphones',    'Industry-leading noise cancelling wireless headphones with 30-hour battery life',             349.99, 'USD', 100, 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400'),
    (1, 4,  'LG 27" UltraGear Gaming Monitor','27-inch QHD 165Hz 1ms gaming monitor with NVIDIA G-Sync Compatible',                        399.99, 'USD', 40,  'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=400'),
    (1, 1,  'Apple AirPods Pro (2nd Gen)',    'Active noise cancellation, Adaptive Transparency, Personalized Spatial Audio',               249.99, 'USD', 150, 'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=400');

-- Mobiles (product_ids 6-10)
INSERT INTO products (category_id, brand_id, name, description, unit_price, currency_code, stock_quantity, thumbnail_url) VALUES
    (2, 1,  'Apple iPhone 15 Pro',           '6.1-inch Super Retina XDR, A17 Pro chip, 48MP camera system, Titanium design',              999.99, 'USD', 75,  'https://images.unsplash.com/photo-1697060346309-3e17b7c9b516?w=400'),
    (2, 2,  'Samsung Galaxy S24 Ultra',      '6.8-inch Dynamic AMOLED, 200MP camera, S Pen included, 5000mAh battery',                   1199.99, 'USD', 60,  'https://images.unsplash.com/photo-1706153697310-9f1f0ff14e66?w=400'),
    (2, 5,  'OnePlus 12',                    '6.82-inch LTPO AMOLED 120Hz, Snapdragon 8 Gen 3, 50MP Hasselblad camera, 100W charging',     799.99, 'USD', 80,  'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=400'),
    (2, 2,  'Samsung Galaxy A55',            '6.6-inch Super AMOLED, 50MP camera, 5000mAh battery, IP67 water resistance',                449.99, 'USD', 120, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400'),
    (2, 1,  'Apple iPhone 15',               '6.1-inch Super Retina XDR, A16 Bionic chip, 48MP main camera, Dynamic Island',              799.99, 'USD', 90,  'https://images.unsplash.com/photo-1695048133142-1a20484429be?w=400');

-- Books (product_ids 11-15)
INSERT INTO products (category_id, brand_id, name, description, unit_price, currency_code, stock_quantity, thumbnail_url) VALUES
    (3, 6,  'Atomic Habits',                 'James Clear - An easy and proven way to build good habits and break bad ones',                16.99, 'USD', 200, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400'),
    (3, 7,  'The Alchemist',                 'Paulo Coelho - A magical story about following your dreams',                                  14.99, 'USD', 180, 'https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=400'),
    (3, 6,  'Sapiens',                       'Yuval Noah Harari - A brief history of humankind',                                           18.99, 'USD', 150, 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400'),
    (3, 7,  'The Psychology of Money',       'Morgan Housel - Timeless lessons on wealth, greed, and happiness',                           15.99, 'USD', 160, 'https://images.unsplash.com/photo-1553729459-efe14ef6055d?w=400'),
    (3, 6,  'Clean Code',                    'Robert C. Martin - A handbook of agile software craftsmanship',                              45.99, 'USD', 100, 'https://images.unsplash.com/photo-1461360370896-922624d12aa1?w=400');

-- Toys (product_ids 16-20)
INSERT INTO products (category_id, brand_id, name, description, unit_price, currency_code, stock_quantity, thumbnail_url) VALUES
    (4, 8,  'LEGO Technic Bugatti Bolide',   '905-piece Technic set, authentic Bugatti Bolide hypercar model for ages 10+',                59.99, 'USD', 70,  'https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=400'),
    (4, 8,  'LEGO City Space Station',       '500-piece modular space station with 6 astronaut minifigures for ages 7+',                   49.99, 'USD', 85,  'https://images.unsplash.com/photo-1583364646800-d9a06ded9a67?w=400'),
    (4, 9,  'Barbie Dreamhouse',             '3-story dollhouse with 70+ accessories, elevator, pool and slide',                          199.99, 'USD', 45,  'https://images.unsplash.com/photo-1613682988402-54328781b8e6?w=400'),
    (4, 9,  'Hot Wheels Ultimate Garage',    'Multi-level playset with 2 elevators, helicopter pad and racing loop',                       89.99, 'USD', 55,  'https://images.unsplash.com/photo-1594736797933-d0401ba2fe65?w=400'),
    (4, 8,  'LEGO Harry Potter Hogwarts',    '6000+ piece iconic Hogwarts Castle set with 4 towers and 27 minifigures',                   459.99, 'USD', 25,  'https://images.unsplash.com/photo-1560942485-b2a11cc13456?w=400');

-- Beauty (product_ids 21-25)
INSERT INTO products (category_id, brand_id, name, description, unit_price, currency_code, stock_quantity, thumbnail_url) VALUES
    (5, 10, 'L''Oreal Revitalift Serum',     'Pure 1.5% hyaluronic acid face serum for intense hydration, 30ml',                           24.99, 'USD', 200, 'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=400'),
    (5, 11, 'Nivea Soft Moisturising Cream', 'Refreshing moisturising cream with jojoba oil and Vitamin E, 200ml',                          8.99, 'USD', 300, 'https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=400'),
    (5, 10, 'L''Oreal Elvive Shampoo',       'Extraordinary Oil nourishing shampoo for dry and damaged hair, 400ml',                       11.99, 'USD', 250, 'https://images.unsplash.com/photo-1585751119414-ef2636f8aede?w=400'),
    (5, 11, 'Nivea Men Face Wash',           'Deep cleaning face wash with activated charcoal for men, 150ml',                              9.99, 'USD', 180, 'https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=400'),
    (5, 10, 'L''Oreal True Match Foundation','Buildable lightweight coverage foundation with SPF 17, 30 shades',                           19.99, 'USD', 150, 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=400');

-- Fashion (product_ids 26-30)
INSERT INTO products (category_id, brand_id, name, description, unit_price, currency_code, stock_quantity, thumbnail_url) VALUES
    (6, 12, 'Nike Air Max 270',              'Lifestyle shoe featuring Max Air unit in the heel for all-day comfort',                      149.99, 'USD', 100, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400'),
    (6, 13, 'Adidas Ultraboost 23',          'Premium running shoe with responsive Boost midsole and Primeknit+ upper',                   189.99, 'USD', 80,  'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400'),
    (6, 14, 'Zara Slim Fit Chinos',          'Stretch slim fit chino trousers, available in multiple colours',                             49.99, 'USD', 200, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=400'),
    (6, 12, 'Nike Dri-FIT T-Shirt',          'Lightweight moisture-wicking training t-shirt with standard fit',                            34.99, 'USD', 300, 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=400'),
    (6, 13, 'Adidas Tiro Track Jacket',      'Slim-fit track jacket with moisture-absorbing AEROREADY technology',                        69.99, 'USD', 150, 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=400');

-- ============================================================
-- Product Images (2 images per product)
-- ============================================================

-- Electronics
INSERT INTO product_images (product_id, image_url) VALUES
    (1,  'https://images.unsplash.com/photo-1611186871525-3c1f2b4e4fa5?w=800'),
    (1,  'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800'),
    (2,  'https://images.unsplash.com/photo-1593784991095-a205069533cd?w=800'),
    (2,  'https://images.unsplash.com/photo-1571415060716-baff5a75c798?w=800'),
    (3,  'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=800'),
    (3,  'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800'),
    (4,  'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800'),
    (4,  'https://images.unsplash.com/photo-1547119957-637f8679db1e?w=800'),
    (5,  'https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=800'),
    (5,  'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800');

-- Mobiles
INSERT INTO product_images (product_id, image_url) VALUES
    (6,  'https://images.unsplash.com/photo-1697060346309-3e17b7c9b516?w=800'),
    (6,  'https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5?w=800'),
    (7,  'https://images.unsplash.com/photo-1706153697310-9f1f0ff14e66?w=800'),
    (7,  'https://images.unsplash.com/photo-1580910051074-3eb694886505?w=800'),
    (8,  'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=800'),
    (8,  'https://images.unsplash.com/photo-1585060544812-6b45742d762f?w=800'),
    (9,  'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=800'),
    (9,  'https://images.unsplash.com/photo-1604671801908-4b7b43702aba?w=800'),
    (10, 'https://images.unsplash.com/photo-1695048133142-1a20484429be?w=800'),
    (10, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=800');

-- Books
INSERT INTO product_images (product_id, image_url) VALUES
    (11, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800'),
    (11, 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=800'),
    (12, 'https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=800'),
    (12, 'https://images.unsplash.com/photo-1476275466078-4cdc8e06d328?w=800'),
    (13, 'https://images.unsplash.com/photo-1512820790803-83ca734da794?w=800'),
    (13, 'https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=800'),
    (14, 'https://images.unsplash.com/photo-1553729459-efe14ef6055d?w=800'),
    (14, 'https://images.unsplash.com/photo-1567427017947-545c5f8d16ad?w=800'),
    (15, 'https://images.unsplash.com/photo-1461360370896-922624d12aa1?w=800'),
    (15, 'https://images.unsplash.com/photo-1532012197267-da84d127e765?w=800');

-- Toys
INSERT INTO product_images (product_id, image_url) VALUES
    (16, 'https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=800'),
    (16, 'https://images.unsplash.com/photo-1519340241574-2cec6aef0c01?w=800'),
    (17, 'https://images.unsplash.com/photo-1583364646800-d9a06ded9a67?w=800'),
    (17, 'https://images.unsplash.com/photo-1560942485-b2a11cc13456?w=800'),
    (18, 'https://images.unsplash.com/photo-1613682988402-54328781b8e6?w=800'),
    (18, 'https://images.unsplash.com/photo-1602030638412-bb8dcc0bc8b0?w=800'),
    (19, 'https://images.unsplash.com/photo-1594736797933-d0401ba2fe65?w=800'),
    (19, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800'),
    (20, 'https://images.unsplash.com/photo-1560942485-b2a11cc13456?w=800'),
    (20, 'https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=800');

-- Beauty
INSERT INTO product_images (product_id, image_url) VALUES
    (21, 'https://images.unsplash.com/photo-1556228578-8c89e6adf883?w=800'),
    (21, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800'),
    (22, 'https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=800'),
    (22, 'https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=800'),
    (23, 'https://images.unsplash.com/photo-1585751119414-ef2636f8aede?w=800'),
    (23, 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=800'),
    (24, 'https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=800'),
    (24, 'https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=800'),
    (25, 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800'),
    (25, 'https://images.unsplash.com/photo-1583241475880-083f84372725?w=800');

-- Fashion
INSERT INTO product_images (product_id, image_url) VALUES
    (26, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800'),
    (26, 'https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=800'),
    (27, 'https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=800'),
    (27, 'https://images.unsplash.com/photo-1539185441755-769473a23570?w=800'),
    (28, 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=800'),
    (28, 'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=800'),
    (29, 'https://images.unsplash.com/photo-1576566588028-4147f3842f27?w=800'),
    (29, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=800'),
    (30, 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800'),
    (30, 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=800');

-- Made with Bob
