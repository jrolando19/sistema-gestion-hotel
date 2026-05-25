# Sistema de Gestión de Hotel — MVP

Sistema básico para gestionar habitaciones, clientes, reservas, check-in/check-out y facturación de un hotel.

---

## Requisitos previos

| Herramienta  | Versión mínima |
| ------------ | -------------- |
| Java JDK     | 11             |
| Apache Maven | 3.8+           |

Verificar instalación:

```bash
java -version
mvn -version
```

---

## Cómo ejecutar el proyecto localmente

### 1. Clonar el repositorio

```bash
git clone https://github.com/jrolando19/sistema-gestion-hotel.git
cd sistema-gestion-hotel
```

### 2. Compilar el proyecto

```bash
mvn clean compile
```

### 3. Ejecutar las pruebas

```bash
mvn clean test
```

### 4. Ver reporte de cobertura (JaCoCo)

Después de ejecutar las pruebas, abrir en el navegador:

```
target/site/jacoco/index.html
```

---

## Estructura del proyecto

```
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
```

---

## Tecnologías utilizadas

- **Java 11**
- **JUnit 5.10** — pruebas unitarias
- **JaCoCo** — reporte de cobertura de código
- **Apache Maven 3.9** — gestión de dependencias y construcción

---

## Pruebas incluidas

Las pruebas están organizadas por módulo y aplican las técnicas de **Partición de Equivalencia (PE)** y **Análisis de Valores Límite (AVL)**:

| Módulo               | Técnica        | Casos |
| -------------------- | -------------- | ----- |
| Clientes             | PE             | 7     |
| Habitaciones         | PE + AVL       | 11    |
| Reservas             | PE + AVL       | 12    |
| Check-in / Check-out | PE             | 5     |
| Facturación          | PE + AVL       | 6     |
| Flujo completo       | PE integración | 2     |

**Total: 43 casos de prueba**
