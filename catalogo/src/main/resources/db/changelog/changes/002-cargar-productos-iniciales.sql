--liquibase formatted sql

--changeset gaselvolcan:002-cargar-productos-iniciales
INSERT INTO productos (id, codigo, nombre, categoria, precio, precio_anterior, descuento, stock, oferta, imagen, descripcion) VALUES
(1, 'CL001', 'Cilindro GLP 5 kg', 'Cilindros de Gas', 6500, NULL, NULL, 80, FALSE, 'assets/cilindro5kg.jpg', 'Cilindro de gas licuado de petróleo 5 kg. Para uso residencial (cocina, calefacción pequeña).'),
(2, 'CL002', 'Cilindro GLP 11 kg', 'Cilindros de Gas', 11000, 12000, 8, 200, TRUE, 'assets/cilindro11kg.png', 'Cilindro estándar doméstico. El más utilizado en hogares chilenos. Compatible con reguladores estándar.'),
(3, 'CL003', 'Cilindro GLP 15 kg', 'Cilindros de Gas', 16000, NULL, NULL, 90, FALSE, 'assets/cilindro15kg.png', 'Cilindro de mayor capacidad para hogares de alto consumo o locales pequeños.'),
(4, 'CL004', 'Cilindro GLP 45 kg', 'Cilindros de Gas', 40000, 45000, 11, 30, TRUE, 'assets/cilindro45kg.png', 'Cilindro industrial. Uso comercial: restaurantes, talleres, calefacción de locales.'),
(5, 'RG001', 'Regulador doméstico estándar', 'Accesorios y Reguladores', 8990, NULL, NULL, 45, FALSE, 'assets/reguladordomestico.png', 'Regulador de 1 etapa para cilindros 5, 11 y 15 kg. Presión de salida 28 mbar.'),
(6, 'RG002', 'Regulador de alta presión', 'Accesorios y Reguladores', 18990, NULL, NULL, 12, FALSE, 'assets/reguladoraltapresion.png', 'Regulador para cocinas industriales o equipos de mayor consumo. Presión regulable.'),
(7, 'RG003', 'Regulador dual (2 salidas)', 'Accesorios y Reguladores', 13500, 14990, 10, 18, TRUE, 'assets/reguladordual.png', 'Permite conectar dos artefactos simultáneamente al mismo cilindro.'),
(8, 'MG001', 'Manguera gas 1.5 m', 'Accesorios y Reguladores', 3990, NULL, NULL, 80, FALSE, 'assets/mangueragas1.png', 'Manguera flexible homologada. Diámetro interior 9mm. Compatible con reguladores estándar.'),
(9, 'MG002', 'Manguera gas 3 m', 'Accesorios y Reguladores', 6990, NULL, NULL, 50, FALSE, 'assets/mangueragas3.png', 'Manguera larga para instalaciones donde el artefacto está alejado del cilindro.'),
(10, 'MG003', 'Abrazadera metálica', 'Accesorios y Reguladores', 990, NULL, NULL, 200, FALSE, 'assets/abrazaderametalica.png', 'Abrazadera de acero para asegurar la conexión manguera-regulador y manguera-artefacto.'),
(11, 'MG004', 'Kit conexión completo (regulador + manguera 1.5m + abrazaderas)', 'Accesorios y Reguladores', 11500, 12990, 11, 25, TRUE, 'assets/kitcompleto.png', 'Todo lo necesario para instalar un cilindro nuevo.'),
(12, 'AC001', 'Carro porta cilindro 11/15 kg', 'Accesorios y Reguladores', 12990, NULL, NULL, 20, FALSE, 'assets/carrocilindro.png', 'Carro metálico con ruedas para transportar cilindros dentro del hogar con seguridad.'),
(13, 'AC002', 'Tapa protectora para válvula', 'Accesorios y Reguladores', 1490, NULL, NULL, 60, FALSE, 'assets/tapaprotectora.png', 'Tapa de plástico ABS para proteger la válvula del cilindro durante el transporte.'),
(14, 'AC003', 'Detector de gas a batería', 'Accesorios y Reguladores', 17000, 19990, 15, 8, TRUE, 'assets/detectorgas.png', 'Sensor electroquímico. Alarma sonora y visual ante fuga de gas GLP o metano.');
