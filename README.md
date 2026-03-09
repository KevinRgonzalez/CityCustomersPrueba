# CustomerDataController

## Endpoints

### GET /customers/terminos

**Descripción:** Maneja las solicitudes GET para recuperar los términos y condiciones.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.

**Responses:**
- **200 OK:** Términos y condiciones.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### POST /customers

**Descripción:** Registra un nuevo cliente.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.
- `sfcc-cc-action` (opcional): Acción del cliente (e.g., `became-new-member` o `is-cc-member`).

**Body:**
- `CustomerRegistration`: Detalles del cliente a registrar.

**Responses:**
- **200 OK:** Cliente registrado.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### PATCH /customers/{customer_id}

**Descripción:** Actualiza la información de un cliente por su ID.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.
- `sfcc-cc-action` (opcional): Acción del cliente (e.g., `resend-account-activation-email` o `membership-link`).

**Path Variables:**
- `customer_id`: ID del cliente.

**Body:**
- `CustomerRegistration` (opcional): Detalles del cliente a actualizar.

**Responses:**
- **200 OK:** Cliente actualizado.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### GET /customers/{customer_id}

**Descripción:** Recupera la información de un cliente por su ID.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.

**Path Variables:**
- `customer_id`: ID del cliente.

**Query Parameters:**
- Parámetros de consulta adicionales.

**Responses:**
- **200 OK:** Información del cliente.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### POST /customers/password_reset

**Descripción:** Restablece la contraseña de un cliente.

**Body:**
- Parámetros del cuerpo de la solicitud.

**Query Parameters:**
- Parámetros de consulta adicionales.

**Responses:**
- **200 OK:** Contraseña restablecida.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### GET /customers/{customer_id}/basketsv2

**Descripción:** Recupera las cestas de un cliente (versión 2).

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.
- `sf-action-basket` (opcional): Acción de la cesta.
- `sfcc-cc-action` (opcional): Acción de SFCC.
- `fullData` (opcional): Recuperar todos los datos.

**Path Variables:**
- `customer_id`: ID del cliente.

**Responses:**
- **200 OK:** Cestas del cliente.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### GET /customers/{customer_id}/baskets

**Descripción:** Recupera las cestas de un cliente.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.
- `sf-action-basket` (opcional): Acción de la cesta.
- `sfcc-cc-action` (opcional): Acción de SFCC.
- `sfcc-cupones` (opcional): Recuperar detalles de los cupones.
- `fullData` (opcional): Recuperar todos los datos.

**Path Variables:**
- `customer_id`: ID del cliente.

**Responses:**
- **200 OK:** Cestas del cliente.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### POST /customers/{customer_id}/products-orders

**Descripción:** Recupera los productos por pedidos de un cliente.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.
- `homeReq` (opcional): Requiere entrega a domicilio.

**Path Variables:**
- `customer_id`: ID del cliente.

**Body:**
- `ProductsOrderReq`: Detalles del pedido de productos.

**Responses:**
- **200 OK:** Productos por pedidos del cliente.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.

---

### GET /customers/healthcheck

**Descripción:** Verifica el estado de salud del servicio.

**Responses:**
- **200 OK:** Servicio en línea.

---

### GET /customers/{customer_id}/orders

**Descripción:** Recupera los pedidos de un cliente.

**Headers:**
- `Authorization`: El encabezado de autorización que contiene las credenciales del usuario.
- `sfcc-cupones` (opcional): Recuperar detalles de los cupones.

**Path Variables:**
- `customer_id`: ID del cliente.

**Query Parameters:**
- Parámetros de consulta adicionales.

**Responses:**
- **200 OK:** Pedidos del cliente.
- **500 Internal Server Error:** Error interno, por favor vuelva a intentar.# CityCustomersPrueba
