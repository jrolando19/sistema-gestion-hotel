# REQUIREMENTS — Sistema de Gestión de Hotel

## Descripción general

MVP de sistema hotelero que permite registrar clientes y habitaciones, gestionar el ciclo completo de una reserva (confirmación → check-in → check-out) y generar facturas automáticas con IGV.

---

## Requisitos Funcionales

### RF-01 — Gestión de Clientes
- El sistema debe permitir registrar un cliente con: DNI, nombre, apellido, teléfono y email.
- El DNI no puede ser vacío, nulo ni solo espacios.
- El email debe contener el carácter `@`.
- No se permiten dos clientes con el mismo DNI.
- El sistema debe permitir buscar un cliente por su DNI.

### RF-02 — Gestión de Habitaciones
- El sistema debe permitir registrar habitaciones con: número, tipo (SIMPLE / DOBLE / SUITE) y precio por noche.
- El número de habitación debe ser positivo (≥ 1).
- El precio por noche debe ser positivo (> 0).
- No se permiten dos habitaciones con el mismo número.
- El sistema debe listar las habitaciones disponibles.
- Una habitación puede estar en estado: DISPONIBLE, OCUPADA o MANTENIMIENTO.

### RF-03 — Gestión de Reservas
- El sistema debe permitir crear una reserva asociando un cliente y una habitación, con fechas de entrada y salida.
- La fecha de salida debe ser estrictamente posterior a la fecha de entrada.
- Solo se puede reservar una habitación en estado DISPONIBLE.
- Al crear una reserva, la habitación pasa a estado OCUPADA.
- Una reserva puede cancelarse; al cancelarla, la habitación vuelve a DISPONIBLE.
- El sistema calcula automáticamente el número de noches y el costo total.

### RF-04 — Check-in
- El sistema debe permitir realizar el check-in de una reserva en estado CONFIRMADA.
- Al hacer check-in, la reserva pasa a estado ACTIVA.
- No se puede hacer check-in de una reserva cancelada o ya activa.

### RF-05 — Check-out y Facturación
- El sistema debe permitir realizar el check-out de una reserva en estado ACTIVA.
- Al hacer check-out, la reserva pasa a estado FINALIZADA y la habitación vuelve a DISPONIBLE.
- El sistema genera automáticamente una factura al realizar el check-out.
- La factura incluye: número correlativo, subtotal, IGV (18%) y total.
- No se puede generar factura de una reserva que no esté FINALIZADA.

---

## Requisitos No Funcionales

### RNF-01 — Validaciones
- Todas las entradas inválidas deben lanzar `IllegalArgumentException` con mensaje descriptivo.
- Los cambios de estado inválidos deben lanzar `IllegalStateException` con mensaje descriptivo.

### RNF-02 — Inmutabilidad
- Los atributos de `Cliente` son inmutables (todos `final`).
- Los atributos críticos de `Habitacion` y `Reserva` son inmutables excepto el estado.

### RNF-03 — Trazabilidad
- Cada factura registra automáticamente la fecha y hora de emisión mediante `LocalDateTime`.
- El ID de cada reserva es generado automáticamente con formato `RES-N`.
- El número de cada factura es generado automáticamente con formato `FAC-NNNN`.

### RNF-04 — Tecnología
- Lenguaje: Java 11
- Sin base de datos: persistencia en memoria durante la sesión.
- Sin interfaz gráfica: lógica de negocio pura.

---

## Reglas de Negocio

| ID   | Regla |
|------|-------|
| RN-01 | DNI del cliente: no vacío, no nulo |
| RN-02 | Email del cliente: debe contener `@` |
| RN-03 | Número de habitación: entero ≥ 1 |
| RN-04 | Precio por noche: valor > 0 |
| RN-05 | Fecha de salida > fecha de entrada |
| RN-06 | Solo habitaciones DISPONIBLES pueden reservarse |
| RN-07 | Check-in solo sobre reservas CONFIRMADAS |
| RN-08 | Check-out solo sobre reservas ACTIVAS |
| RN-09 | Factura solo sobre reservas FINALIZADAS |
| RN-10 | IGV = 18% del subtotal |
| RN-11 | Subtotal = precio por noche × número de noches |

---

## Casos de Prueba — Técnicas Caja Negra

### Partición de Equivalencia (PE)

Divide las entradas en clases válidas e inválidas:

| Campo | Clase Válida | Clase Inválida |
|-------|-------------|----------------|
| DNI | Cadena no vacía | Nulo, vacío, solo espacios |
| Email | Contiene `@` | Sin `@`, vacío, nulo |
| Número habitación | Entero ≥ 1 | 0, negativo |
| Precio por noche | Decimal > 0 | 0, negativo |
| Fecha salida | Posterior a entrada | Igual o anterior a entrada |
| Estado reserva para check-in | CONFIRMADA | ACTIVA, CANCELADA, FINALIZADA |
| Estado reserva para check-out | ACTIVA | CONFIRMADA, CANCELADA, FINALIZADA |

### Análisis de Valores Límite (AVL)

Evalúa los valores en las fronteras de cada rango:

| Campo | Límite inferior inválido | Límite inferior válido | Valor interior válido |
|-------|--------------------------|------------------------|----------------------|
| Número habitación | 0 | 1 | 101 |
| Precio por noche | 0.0 | 0.01 | 150.0 |
| Noches reservadas | 0 (salida = entrada) | 1 | 5 |
| Noches para factura | — | 1 noche | 5, 10 noches |
