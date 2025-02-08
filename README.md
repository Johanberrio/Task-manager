# Task Manager

## 📌 Instrucciones de Ejecución

### 🔧 Requisitos Previos
Asegurarse de tener instalado:
- **Java 17 o superior**
- **Maven 3.x**
- **Node.js 16+ y npm (para el frontend)**
- **MySQL** 
- **H2** (para pruebas en memoria)

### 🚀 Pasos para ejecutar el proyecto
1. Clonar el repositorio:
   ```sh
   git clone https://github.com/Johanberrio/Task-manager.git
   cd task-manager
   ```

2. Crear una base de datos en MYSQL:
   ```sh
   CREATE DATABASE task_manager;
   ```
   
3. Configurar la base de datos en src/main/resources/application.properties:
   ```sh
   spring.datasource.url=jdbc:mysql://localhost:3306/task_manager
   spring.datasource.username=johan
   spring.datasource.password=root
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  
   Clave secreta JWT
   app.jwt.secret=FfTMpA3jYf/ec1e0hsedJ16JkrpFVanwuXSK1/45qPk=
  
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   spring.jpa.hibernate.ddl-auto=update
  
   Configuración del servidor
   server.port=8080
   ```

4. Compilar y ejecutar la aplicación backend:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
   
5. Acceder al directorio del Frontend:
   ```sh
   cd frontend
   ```
   
6. Instalar las dependencias:
   ```sh
   npm install
   ```
7. Ejecutar la aplicación:
   ```sh
   npm run dev
   ```
   
8. Para el registro de usuarios Acceder en: [http://localhost:3000/register](http://localhost:3000/register)


9. Para el Login de usuarios Acceder en: [http://localhost:3000](http://localhost:3000)

10. Para la documentación en swagger acceder en: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


---

## 🛠️ Diseño y Decisiones Técnicas

### 📌 Arquitectura
El proyecto sigue una arquitectura basada en **Spring Boot** con los siguientes módulos:

- **Controller**: Maneja las solicitudes HTTP y la lógica de presentación.
- **Service**: Contiene la lógica de negocio.
- **Repository**: Gestiona el acceso a la base de datos.
- **Model**: Define las entidades de JPA.

### 📌 Tecnologías Utilizadas
- **Spring Boot** (Framework principal)
- **Spring Data JPA** (Persistencia de datos)
- **H2 / PostgreSQL / MySQL** (Base de datos en pruebas y producción)
- **Spring Security** (Autenticación y autorización)
- **JUnit & Mockito** (Pruebas unitarias y de integración)
- **Swagger** (Documentación de API REST)

### 📌 Decisiones Técnicas
- Se utiliza **H2 en memoria** para pruebas unitarias, evitando dependencias externas.
- Se sigue el principio de **Separación de Responsabilidades (SoC)**, manteniendo la lógica de negocio en `Service`.
- Uso de **DTOs** para evitar exponer directamente entidades en las respuestas de la API.

---

## ✅ Pruebas Unitarias y Cobertura

### 🔍 Ejecución de Pruebas
Para ejecutar los tests, usar:
```sh
mvn test
```

### 📊 Resultados de Cobertura
Se utiliza **JaCoCo** para medir la cobertura de código. Tras ejecutar `mvn test`, se puede generar el reporte de cobertura con:
```sh
mvn jacoco:report
```
El informe estará disponible en:
```
target/site/jacoco/index.html
```

### 📌 Ejemplo de Prueba Unitaria (`UserRepositoryTest.java`)
```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User("testuser", "password123");
        userRepository.save(user);
    }

    @Test
    void testFindByUsername_UserExists() {
        Optional<User> userOptional = userRepository.findByUsername("testuser");
        assertTrue(userOptional.isPresent());
        assertEquals("testuser", userOptional.get().getUsername());
    }
}
```

---


