# Gas El Volcán Backend

Backend compuesto por dos servicios Spring Boot:

- `auth`: registro e inicio de sesión, JWT y perfil básico del cliente.
- `catalogo`: consulta y administración de productos, creación y seguimiento de pedidos.

## Configuración local

Ambos servicios usan MariaDB/MySQL y requieren la variable `JWT_SECRET` con el mismo valor Base64. Configura también las credenciales de cada base mediante `AUTH_DB_URL`, `AUTH_DB_USER`, `AUTH_DB_PASSWORD`, `CATALOGO_DB_URL`, `CATALOGO_DB_USER` y `CATALOGO_DB_PASSWORD`.

El primer administrador se crea solo si se definen `INITIAL_ADMIN_EMAIL` e `INITIAL_ADMIN_PASSWORD` al iniciar Auth. Opcionalmente se puede definir `INITIAL_ADMIN_NAME`. No hay una contraseña de administrador predeterminada en el código.

Las migraciones Liquibase agregan automáticamente las columnas de perfil y las tablas de pedidos al iniciar cada servicio. Haz una copia de seguridad antes de aplicar migraciones en una base con datos importantes.

## API de Auth (puerto 8081)

- `POST /auth/registro`: crea una cuenta de rol `USER` y devuelve un JWT. Body:

  ```json
  {
    "nombre": "Ana Pérez",
    "correo": "ana@example.com",
    "telefono": "912345678",
    "direccion": "Calle 1",
    "password": "una-clave-de-al-menos-8-caracteres"
  }
  ```

- `POST /auth/login`: recibe `correo` y `password`; responde con `token`, `correo`, `nombre` y `rol`.

El backend ignora cualquier intento del cliente de asignarse un rol: el registro público siempre crea `USER`.

## API de Catálogo (puerto 8082)

- `GET /api/productos` y `GET /api/productos/{id}` son públicos.
- `POST /api/productos` y `PUT`/`DELETE /api/productos/{id}` requieren `Authorization: Bearer <token>` de un usuario `ADMIN`.
- Una oferta debe tener precio anterior mayor que el precio actual y un porcentaje de descuento que coincida con esos precios.

## Pedidos

Todas las rutas de pedidos requieren JWT. Un cliente solo puede consultar sus propios pedidos. Las operaciones administrativas requieren rol `ADMIN`.

- `POST /api/pedidos`: crea un pedido usando productos y cantidades. El backend recalcula precios y total con el catálogo actual y reserva el stock en una transacción.

  ```json
  {
    "nombreDestinatario": "Ana Pérez",
    "telefono": "912345678",
    "direccion": "Calle 1",
    "productos": [
      { "productoId": 1, "cantidad": 2 }
    ]
  }
  ```

- `GET /api/pedidos/mis-pedidos`: lista los pedidos del correo autenticado.
- `GET /api/pedidos/{id}`: permite al cliente ver su pedido o a un administrador ver cualquier pedido.
- `GET /api/pedidos`: lista todos los pedidos; solo `ADMIN`.
- `PATCH /api/pedidos/{id}/estado`: actualiza el estado; solo `ADMIN`. Estados válidos: `RECIBIDO`, `PREPARANDO`, `DESPACHADO`, `ENTREGADO`, `CANCELADO`. Al cancelar antes del despacho, se restaura el stock.

El pedido conserva una copia de nombre, código y precio de cada producto para mantener el historial si el catálogo cambia. Esta API no procesa pagos.

## Ejecutar

Desde cada carpeta, configura las variables de entorno requeridas y ejecuta:

```powershell
.\mvnw.cmd spring-boot:run
```

Auth escucha en `http://localhost:8081`; Catálogo en `http://localhost:8082`.
