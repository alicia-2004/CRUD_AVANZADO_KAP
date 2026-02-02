INSERT INTO PROFILE_ (USERNAME, PASSWORD_, EMAIL, NAME_, TELEPHONE, SURNAME)
VALUES
('jlopez', 'pass123', 'jlopez@example.com', 'Juan', '987654321', 'Lopez'),
('mramirez', 'pass456', 'mramirez@example.com', 'Maria', '912345678', 'Ramirez'),
('cperez', 'pass789', 'cperez@example.com', 'Carlos', '934567890', 'Perez'),
('asanchez', 'qwerty', 'asanchez@example.com', 'Ana', '900112233', 'Sanchez'),
('rluna', 'zxcvbn', 'rluna@example.com', 'Rosa', '955667788', 'Luna');

INSERT INTO CARD (CVV, EXPIRATION_DATE, CARD_NUMBER) VALUES
(123, '2027-05-31', 'AA1234567890123456789012'),
(9,   '2026-12-31', 'BB0000000000000000000001'),
(600, '2030-01-15', 'CC9876543210987654321012');

INSERT INTO USER_ (USERNAME, GENDER, CARD_NUMBER)
VALUES
('jlopez', 'Masculino', 'AA1234567890123456789012'),
('mramirez', 'Femenino', 'BB0000000000000000000001'),
('cperez', 'Masculino', 'CC9876543210987654321012');

INSERT INTO ADMIN_ (USERNAME, CURRENT_ACCOUNT)
VALUES
('asanchez', 'CTA-001'),
('rluna', 'CTA-002');

INSERT INTO SHOE 
(PRICE, MODEL, SIZE, EXCLUSIVE, MANUFACTER_DATE, COLOR, ORIGIN, BRAND, RESERVED, STOCK, IMGPATH)
VALUES
(79.99, 'Air Max', 42, 'FALSE', '2024-01-10', 'Negro', 'España', 'Nike', 'FALSE', 50, 'nike_airmax90_negras.jpg'),
(120.50, 'Samba', 40, 'TRUE', '2023-11-05', 'Negro', 'Italia', 'Adidas', 'FALSE', 30, 'adidas_samba_negras.jpg'),
(95.00, 'Caven', 41, 'FALSE', '2024-02-20', 'Azul', 'Portugal', 'Puma', 'TRUE', 10, 'puma_caven_azul.jpg');

INSERT INTO REVIEW
(ID_SHOE, USERNAME, DESCRIPTION, RATING, DATE_)
VALUES
(1, 'jlopez', 'Muy cómodas', 5, '2024-03-01'),
(2, 'mramirez', 'Buen diseño', 4, '2024-03-05'),
(3, 'cperez', 'Algo ajustadas', 3, '2024-03-10');

INSERT INTO ORDER_
(USERNAME, SHOE_ID, DATE_, QUANTITY)
VALUES
('jlopez', 1, '2024-03-15', 2),
('mramirez', 2, '2024-03-16', 1),
('cperez', 3, '2024-03-17', 1);

DELIMITER //
CREATE PROCEDURE RegistrarUsuario( IN p_username VARCHAR(30), IN p_pswd VARCHAR(30))
BEGIN
    DECLARE  nuevo_profile_code INT;
    
    INSERT INTO PROFILE_ (EMAIL, USER_NAME, PSWD, TELEPHONE, NAME_, SURNAME)
    VALUES (null, p_username, p_pswd, null, null, null);

    SET nuevo_profile_code = LAST_INSERT_ID();

    INSERT INTO USER_ (PROFILE_CODE, GENDER, CARD_NO)
    VALUES (nuevo_profile_code, null, null);
    
    SELECT p.PROFILE_CODE, EMAIL, USER_NAME, PSWD, TELEPHONE, NAME_, SURNAME, GENDER, CARD_NO FROM PROFILE_ p JOIN USER_ u ON u.PROFILE_CODE=p.PROFILE_CODE WHERE p.PROFILE_CODE= nuevo_profile_code;


 END //

DELIMITER ; 