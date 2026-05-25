# Sistema de Gestión de Hotel — MVP

<p align="center">
  <img src="https://img.shields.io/badge/Java-11-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 11" />
  <img src="https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/JUnit-5.10-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5.10" />
  <img src="https://img.shields.io/badge/Black--box%20tests-PE%20%2B%20AVL-0F62FE?style=for-the-badge" alt="Black-box tests" />
</p>

<p align="center">
  Proyecto Java (GUI) para gestionar clientes, habitaciones, reservas, check-in/check-out y facturación de un hotel.
</p>

<p align="center">
  <a href="#resumen-rápido">Resumen</a> ·
  <a href="#uso-rápido-quickstart">Uso rápido</a> ·
  <a href="#pruebas-incluidas">Pruebas</a> ·
  <a href="#clases-principales-y-api">API</a> ·
  <a href="#solución-de-problemas-comunes">Problemas</a>
</p>

> Proyecto académico con GUI Swing, lógica de negocio centralizada y pruebas de caja negra enfocadas en comportamiento observable.

---

## Resumen rápido

<table>
  <tr>
    <td valign="top" width="50%">

### Qué incluye

- Lógica de negocio en `hotel.GestionHotel`.
- Clases de dominio: `Cliente`, `Habitacion`, `Reserva`, `Factura`.
- Interfaz gráfica de usuario: `hotel.HotelApp` (Swing).
- Pruebas unitarias en `src/test/java/hotel/HotelTest.java` aplicando PE y AVL.

    </td>
    <td valign="top" width="50%">

### Vista rápida

| Aspecto | Detalle |
| --- | --- |
| Tipo de app | GUI de escritorio |
| Patrón principal | Fachada + entidades de dominio |
| Entrada visual | `hotel.HotelApp` |
| Pruebas | Caja negra |
| Cobertura | JaCoCo |

</td>
  </tr>
</table>

---

## Requisitos previos

<table>
  <tr>
    <th align="left">Herramienta</th>
    <th align="left">Versión mínima</th>
    <th align="left">Uso</th>
  </tr>
  <tr>
    <td>Java JDK</td>
    <td>11</td>
    <td>Compilación y ejecución</td>
  </tr>
  <tr>
    <td>Apache Maven</td>
    <td>3.8+</td>
    <td>Build, tests y reportes</td>
  </tr>
</table>

Verificar instalación:

```bash
java -version
mvn -version
```

---

## Uso rápido (Quickstart)

### 1. Clonar y compilar

```bash
git clone https://github.com/jrolando19/sistema-gestion-hotel.git
cd sistema-gestion-hotel
mvn clean package
```

### 2. Ejecutar la aplicación GUI

```bash
java -cp target/classes hotel.HotelApp
```

### 3. Ejecutar las pruebas

```bash
mvn test
```

### 4. Ver reporte de cobertura

Después de ejecutar las pruebas, abrir:

```text
target/site/jacoco/index.html
```

---

## Estructura del proyecto

<pre>
sistema-gestion-hotel/
├── pom.xml
├── README.md
├── REQUIREMENTS.md
└── src/
    ├── main/java/hotel/
    │   ├── TipoHabitacion.java      (enum: SIMPLE, DOBLE, SUITE)
    │   ├── EstadoHabitacion.java    (enum: DISPONIBLE, OCUPADA, MANTENIMIENTO)
    │   ├── EstadoReserva.java       (enum: CONFIRMADA, ACTIVA, FINALIZADA, CANCELADA)
    │   ├── Cliente.java             (entidad cliente)
    │   ├── Habitacion.java          (entidad habitación)
    │   ├── Reserva.java             (entidad reserva con cálculo de noches)
    │   ├── Factura.java             (entidad factura con IGV 18%)
    │   └── GestionHotel.java        (fachada principal del sistema)
    └── test/java/hotel/
        └── HotelTest.java           (pruebas de caja negra — PE y AVL)
</pre>

---

## Tecnologías utilizadas

<table>
  <tr>
    <th align="left">Tecnología</th>
    <th align="left">Rol</th>
  </tr>
  <tr>
    <td><strong>Java 11</strong></td>
    <td>Lenguaje y runtime</td>
  </tr>
  <tr>
    <td><strong>JUnit 5.10</strong></td>
    <td>Pruebas unitarias</td>
  </tr>
  <tr>
    <td><strong>JaCoCo</strong></td>
    <td>Cobertura de código</td>
  </tr>
  <tr>
    <td><strong>Apache Maven 3.9</strong></td>
    <td>Construcción y dependencias</td>
  </tr>
</table>

---

## Pruebas incluidas

Las pruebas son de caja negra, las cuales validan el comportamiento observable del sistema sin depender de la implementación interna, utilizando **JUnit 5**. Estas están organizadas por módulos empleando las técnicas de **Partición de Equivalencia (PE)** y **Análisis de Valores Límite (AVL)**.

### Módulo 1: Gestión de Clientes (7 casos)
| ID | Técnica | Nombre de la Prueba | Descripción |
| :--- | :---: | :--- | :--- |
| **PE-C01** | PE | Registrar cliente válido | Valida el registro exitoso cuando se ingresan datos completos y correctos (DNI, nombre, apellido, teléfono, email). |
| **PE-C02** | PE | DNI nulo, vacío o con espacios | Verifica que el sistema rechace y lance un `IllegalArgumentException` si el DNI no contiene datos válidos. |
| **PE-C03** | PE | Email sin arroba (`@`) o vacío | Comprueba que se valide el formato del correo electrónico, lanzando una excepción si es inválido. |
| **PE-C04** | PE | Registrar DNI duplicado | Asegura que no se puedan registrar dos clientes con el mismo documento de identidad en el sistema. |
| **PE-C05** | PE | Buscar cliente existente | Verifica que la búsqueda por DNI devuelva el objeto `Cliente` correcto envuelto en un `Optional`. |
| **PE-C06** | PE | Buscar cliente inexistente | Valida que al buscar un DNI no registrado, el sistema responda con un contenedor vacío de forma segura. |
| **PE-C07** | PE | Obtener nombre completo | Comprueba que el método de dominio concatene correctamente el nombre y el apellido del cliente. |

### Módulo 2: Registro de Habitaciones (11 casos)
| ID | Técnica | Nombre de la Prueba | Descripción |
| :--- | :---: | :--- | :--- |
| **PE-H01** | PE | Crear habitación válida | Valida la correcta asignación de número, tipo, precio base y que su estado inicial sea `DISPONIBLE`. |
| **AVL-H02** | AVL | Número de habitación = 0 | Límite inferior inválido: Verifica que se rechace el número cero con una excepción. |
| **AVL-H03** | AVL | Número de habitación = 1 | Límite inferior válido: Asegura que el sistema acepte el primer número entero positivo. |
| **AVL-H04** | AVL | Número de habitación negativo | Límite inválido: Comprueba el rechazo absoluto de números menores a cero. |
| **AVL-H05** | AVL | Precio cero o negativo | Frontera inválida: Evalúa mediante pruebas parametrizadas que precios `0.0`, `-1.0` y `-100.0` lancen error. |
| **AVL-H06** | AVL | Precio mínimo válido (0.01) | Frontera válida: Valida que el sistema admita tarifas de habitación desde un centavo en adelante. |
| **PE-H07** | PE | Tipo de habitación nulo | Verifica que no se permita registrar una habitación sin especificar su enumeración (`SIMPLE`, `DOBLE`, `SUITE`). |
| **PE-H08** | PE | Registrar habitación duplicada | Previene la colisión de inventario impidiendo que se registren dos habitaciones con el mismo número. |
| **PE-H09** | PE | Listar habitaciones disponibles | Valida que los filtros de consulta reflejen únicamente los cuartos aptos para la renta. |
| **PE-H10** | PE | Cambiar estado de habitación | Evalúa la correcta transición interna a estados operativos como `MANTENIMIENTO` u `OCUPADA`. |
| **PE-H11** | PE | Cambiar estado a nulo | Previene corrupción de datos asegurando que no se pueda setear un estado vacío. |

### Módulo 3: Gestión de Reservas (12 casos)
| ID | Técnica | Nombre de la Prueba | Descripción |
| :--- | :---: | :--- | :--- |
| **PE-R01** | PE | Crear reserva válida | Verifica que una reserva correcta genere un ID único, estado `CONFIRMADA` y calcule bien las noches. |
| **AVL-R02** | AVL | Fecha de salida igual a entrada | Límite inválido: Asegura que una estancia de cero horas (mismo día) sea rechazada en el negocio. |
| **AVL-R03** | AVL | Fecha de salida anterior a entrada | Límite inválido: Valida el bloqueo de inconsistencias temporales (viajes en el tiempo). |
| **AVL-R04** | AVL | Reserva de exactamente 1 noche | Límite mínimo válido: Comprueba que el cálculo de noches funcione perfectamente en el paso de frontera más corto. |
| **PE-R05** | PE | Reservar habitación ocupada/mantenimiento | Asegura que el sistema bloquee intentos de reserva si la habitación no está en estado `DISPONIBLE`. |
| **PE-R06** | PE | Cliente nulo en reserva | Verifica que se rechace la operación si falta la entidad del huésped responsable. |
| **PE-R07** | PE | Habitación nula en reserva | Verifica que se lance una excepción si no se asocia un cuarto físico a la reserva. |
| **PE-R08** | PE | Fechas nulas | Comprueba que omitir la fecha de ingreso o de salida aborte el flujo lanzando un error de argumento. |
| **PE-R09** | PE | Cancelar reserva confirmada | Valida que la cancelación cambie el estado a `CANCELADA` y libere automáticamente la habitación vinculada. |
| **PE-R10** | PE | Cancelar reserva ya cancelada | Control de flujo: Bloquea operaciones redundantes lanzando una excepción de estado ilegal. |
| **PE-R11** | PE | Activar reserva no confirmada | Previene violaciones de negocio impidiendo el Check-in directo de una reserva previamente cancelada. |
| **PE-R12** | PE | Finalizar reserva no activa | Asegura que no se pueda dar Check-out a estadías que nunca pasaron por el proceso de Check-in. |

### Módulo 4: Check-in y Check-out (5 casos)
| ID | Técnica | Nombre de la Prueba | Descripción |
| :--- | :---: | :--- | :--- |
| **PE-CI01** | PE | Check-in activa la reserva | Al llegar el huésped, cambia el estado de la reserva a `ACTIVA` para permitir la ocupación oficial. |
| **PE-CI02** | PE | Check-in con ID inexistente | Asegura el control de errores si se ingresa un código de reserva que no existe en el sistema. |
| **PE-CO01** | PE | Check-out finaliza y factura | Al retirarse el cliente, cierra la reserva como `FINALIZADA`, libera el cuarto y dispara la creación de su factura. |
| **PE-CO02** | PE | Check-out sin check-in previo | Valida las reglas de secuencia, impidiendo facturar a alguien que reglamentariamente no ha ingresado. |
| **PE-CO03** | PE | Check-out con ID inexistente | Verifica el rechazo seguro ante códigos de salida erróneos o manipulados manualmente. |

### Módulo 5: Facturación (6 casos)
| ID | Técnica | Nombre de la Prueba | Descripción |
| :--- | :---: | :--- | :--- |
| **PE-F01** | PE | Generación de factura válida | Evalúa que los montos clave se desglosen correctamente: Subtotal, cálculo exacto del **18% de IGV** y la suma Total. |
| **PE-F02** | PE | Facturar reserva no finalizada | Asegura que no se emita un comprobante de pago si el cliente no ha pasado por el flujo formal de Check-out. |
| **PE-F03** | PE | Factura con reserva nula | Comprueba que el constructor financiero aborte si no se le proveen los datos base de un hospedaje. |
| **AVL-F04** | AVL | Factura de 1 noche (Mínimo) | Verifica la exactitud matemática del subtotal e impuestos cobrados sobre la estancia mínima permitida. |
| **AVL-F05** | AVL | Cálculo parametrizado por noches | Procesa en lote una matriz de prueba (`CsvSource`) para certificar las matemáticas del negocio a las 1, 3, 5 y 10 noches. |
| **PE-F06** | PE | Listar facturas generadas | Asegura el correcto almacenamiento histórico de todos los comprobantes emitidos en el hotel. |

### Módulo 6: Integración del Flujo Completo (2 casos)
| ID | Técnica | Nombre de la Prueba | Descripción |
| :--- | :---: | :--- | :--- |
| **PE-INT01** | PE | Flujo feliz de punta a punta | **Prueba de integración integral**: Simula todo el comportamiento lógico del software. Registra un cliente/habitación → Crea reserva → Hace Check-in → Hace Check-out → Valida la consistencia del dinero de la Factura → Libera el cuarto. |
| **PE-INT02** | PE | Reutilización de habitación | Evalúa la persistencia reactiva asegurando que, al cancelar una reserva, el cuarto quede disponible de inmediato para otro cliente. |

**Total: 43 casos de prueba cubiertos con éxito.**

---

## Clases principales y API

### `hotel.GestionHotel`
Fachada principal del sistema.

- `registrarCliente(String dni, String nombre, String apellido, String telefono, String email)`
- `buscarClientePorDni(String dni)`
- `registrarHabitacion(int numero, TipoHabitacion tipo, double precio)`
- `buscarHabitacionPorNumero(int numero)`
- `crearReserva(String dniCliente, int numeroHabitacion, LocalDate entrada, LocalDate salida)`
- `cancelarReserva(String idReserva)`
- `realizarCheckIn(String idReserva)`
- `realizarCheckOut(String idReserva)`
- `listarHabitacionesDisponibles()`
- `listarReservas()`
- `listarFacturas()`

### `hotel.Cliente`
Entidad cliente con validación de datos y `getNombreCompleto()`.

- `getDni()`
- `getNombre()`
- `getApellido()`
- `getTelefono()`
- `getEmail()`
- `getNombreCompleto()`

### `hotel.Habitacion`
Entidad habitación con validación de número, tipo y precio, y estado disponible/ocupada/mantenimiento.

- `getNumero()`
- `getTipo()`
- `getPrecioPorNoche()`
- `getEstado()`
- `isDisponible()`
- `setEstado(EstadoHabitacion estado)`

### `hotel.Reserva`
Entidad reserva con cálculo de noches, costo total y control de estados: confirmar, activar, finalizar y cancelar.

- `getId()`
- `getCliente()`
- `getHabitacion()`
- `getFechaEntrada()`
- `getFechaSalida()`
- `getEstado()`
- `getNochesReservadas()`
- `calcularCostoTotal()`
- `confirmar()`
- `cancelar()`
- `activar()`
- `finalizar()`
- `resetContador()`

### `hotel.Factura`
Entidad factura que calcula subtotal, IGV y total a partir de una reserva finalizada.

- `getNumeroFactura()`
- `getReserva()`
- `getSubtotal()`
- `getIgv()`
- `getTotal()`
- `getFechaEmision()`
- `resetContador()`

---

## Ejemplos de uso (programático)

### Registrar cliente, habitación y crear reserva

```java
GestionHotel h = new GestionHotel();
h.registrarCliente("12345678", "Ana", "García", "999888777", "ana@mail.com");
h.registrarHabitacion(101, TipoHabitacion.DOBLE, 200.0);
Reserva r = h.crearReserva("12345678", 101, LocalDate.now(), LocalDate.now().plusDays(3));
```

### Check-in, check-out y factura

```java
h.realizarCheckIn(r.getId());
Factura f = h.realizarCheckOut(r.getId());
System.out.println("Total: " + f.getTotal());
```

---

## Ejecución en IDE

- Importar como proyecto Maven usando `pom.xml`.
- Ejecutar la clase `hotel.HotelApp` para iniciar la GUI.

---

## Pruebas, cobertura y CI

### Ejecutar suite de pruebas

```bash
mvn test
```

### Omitir JaCoCo si da problemas con el JDK

```bash
mvn -Djacoco.skip=true test
```

### Recomendación para CI

- `mvn -B clean verify`
- Publicar el reporte de JaCoCo si la versión del runner es compatible.

---

## Solución de problemas comunes

### Error JaCoCo "Unsupported class file major version 69"
La versión del JDK en uso es más reciente que la soportada por la versión de JaCoCo configurada.

Opciones:
1. Ejecutar tests con un JDK compatible, por ejemplo JDK 11.
2. Ejecutar `mvn -Djacoco.skip=true test`.
3. Actualizar JaCoCo en `pom.xml`.

### Error al ejecutar la GUI
Verificar que `hotel.HotelApp` esté compilada en `target/classes`.

---

## Buenas prácticas y recomendaciones

- Mantener pruebas enfocadas en comportamiento observable.
- Añadir casos nuevos si cambia la API pública de `GestionHotel`.
- Documentar cambios funcionales relevantes en este README.
