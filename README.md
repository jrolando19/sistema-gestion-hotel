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

Las pruebas están organizadas por módulo y aplican las técnicas de **Partición de Equivalencia (PE)** y **Análisis de Valores Límite (AVL)**.

<table>
  <tr>
    <th align="left">Módulo</th>
    <th align="left">Técnica</th>
    <th align="left">Casos</th>
  </tr>
  <tr><td>Clientes</td><td>PE</td><td>7</td></tr>
  <tr><td>Habitaciones</td><td>PE + AVL</td><td>11</td></tr>
  <tr><td>Reservas</td><td>PE + AVL</td><td>12</td></tr>
  <tr><td>Check-in / Check-out</td><td>PE</td><td>5</td></tr>
  <tr><td>Facturación</td><td>PE + AVL</td><td>6</td></tr>
  <tr><td>Flujo completo</td><td>PE integración</td><td>2</td></tr>
</table>

**Total: 43 casos de prueba**

Las pruebas son de **caja negra**: validan el comportamiento observable del sistema sin depender de la implementación interna.

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

### `hotel.Cliente`
Entidad cliente con validación de datos y `getNombreCompleto()`.

### `hotel.Habitacion`
Entidad habitación con validación de número, tipo y precio, y estado disponible/ocupada/mantenimiento.

### `hotel.Reserva`
Entidad reserva con cálculo de noches, costo total y control de estados: confirmar, activar, finalizar y cancelar.

### `hotel.Factura`
Entidad factura que calcula subtotal, IGV y total a partir de una reserva finalizada.

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
