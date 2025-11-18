# 📋 FUNCIONES MÁS IMPORTANTES - LOOP v7

Este documento muestra las funciones más importantes de la aplicación LOOP v7, organizadas por categorías.

---

## 🔐 1. AUTENTICACIÓN Y SESIÓN

### SessionManager.java
**Gestión de sesiones de usuario**

```java
// Crear sesión de login
public void createLoginSession(User user)
```
- Guarda los datos del usuario en SharedPreferences
- Establece el estado de login
- Almacena: ID, email, nombre, rol, teléfono

```java
// Verificar si el usuario está logueado
public boolean isLoggedIn()
```
- Retorna true si hay una sesión activa

```java
// Obtener usuario actual
public User getCurrentUser()
```
- Retorna el objeto User con los datos del usuario logueado

```java
// Verificar rol del usuario
public boolean isCliente()
public boolean isSocia()
```
- Verifica si el usuario es cliente o socia

```java
// Cerrar sesión
public void logoutUser()
```
- Limpia todos los datos de la sesión

---

### LoginActivity.java
**Pantalla de inicio de sesión**

```java
// Realizar login
private void performLogin()
```
- Valida email y contraseña
- Busca usuario en la base de datos
- Verifica credenciales
- Crea sesión si es exitoso
- Redirige a MainActivity

---

## 🏠 2. ACTIVIDAD PRINCIPAL Y NAVEGACIÓN

### MainActivity.java
**Actividad principal de la aplicación**

```java
// Configurar navegación
private void setupNavigation(Bundle savedInstanceState)
```
- Configura la navegación según el rol del usuario
- Establece los fragments disponibles para cliente o socia

```java
// Navegación para clientes
private void setupClientNavigation()
```
- Configura menú: Home, Services, Requests, Profile

```java
// Navegación para socias
private void setupSociaNavigation()
```
- Configura menú: Home, Pending, Requests, Reports, Profile

```java
// Seleccionar fragment específico
public void selectFragment(int fragmentIndex)
```
- Permite cambiar de fragment programáticamente
- Índices: 0=Home, 1=Requests, 2=Services, 3=Reports, 4=Profile

```java
// Redirigir a login
private void redirectToLogin()
```
- Redirige a LoginActivity si no hay sesión activa

---

## 💾 3. BASE DE DATOS

### DatabaseHelper.java
**Gestión completa de la base de datos SQLite**

#### USUARIOS

```java
// Insertar usuario
public long insertUser(User user)
```
- Crea un nuevo usuario en la base de datos
- Retorna el ID del usuario creado

```java
// Obtener usuario por email
public User getUserByEmail(String email)
```
- Busca un usuario por su email
- Retorna el objeto User o null

```java
// Obtener usuario por ID
public User getUserById(int id)
```
- Busca un usuario por su ID
- Retorna el objeto User completo

```java
// Actualizar usuario
public boolean updateUser(User user)
```
- Actualiza los datos de un usuario existente
- Retorna true si la actualización fue exitosa

```java
// Obtener todas las socias
public List<User> getSocias()
```
- Retorna lista de usuarios con rol "socia" y status "activo"

```java
// Actualizar estadísticas del usuario
public void updateUserStatsFromDatabase(int userId)
```
- Calcula servicios completados
- Calcula calificación promedio
- Actualiza fecha del último servicio
- Actualiza estadísticas en la base de datos

#### SERVICIOS

```java
// Obtener todos los servicios
public List<Service> getAllServices()
```
- Retorna lista de servicios activos

```java
// Obtener servicio por ID
public Service getServiceById(int id)
```
- Busca un servicio por su ID

```java
// Insertar servicio
public long insertService(Service service)
```
- Crea un nuevo servicio en la base de datos

```java
// Actualizar servicio
public boolean updateService(Service service)
```
- Actualiza los datos de un servicio existente

#### SOLICITUDES (REQUESTS)

```java
// Insertar solicitud
public long insertRequest(Request request)
```
- Crea una nueva solicitud de servicio
- Retorna el ID de la solicitud creada

```java
// Actualizar solicitud
public boolean updateRequest(Request request)
```
- Actualiza los datos de una solicitud existente
- Actualiza estado, socia asignada, fechas, etc.

```java
// Obtener solicitudes por cliente
public List<Request> getRequestsByClientId(int clientId)
```
- Retorna todas las solicitudes de un cliente específico

```java
// Obtener solicitudes por socia
public List<Request> getRequestsBySociaId(int sociaId)
```
- Retorna todas las solicitudes asignadas a una socia

```java
// Obtener solicitudes pendientes
public List<Request> getPendingRequests()
```
- Retorna todas las solicitudes con estado "pendiente"

```java
// Obtener solicitudes por estado
public List<Request> getRequestsByStatus(String status)
```
- Retorna solicitudes filtradas por estado específico

```java
// Obtener solicitud por ID
public Request getRequestById(int id)
```
- Busca una solicitud por su ID

```java
// Eliminar solicitud
public boolean deleteRequest(int requestId)
```
- Elimina una solicitud y sus datos relacionados
- Elimina pagos, calificaciones y notificaciones asociadas

#### PAGOS

```java
// Insertar pago
public long insertPayment(Payment payment)
```
- Registra un nuevo pago en la base de datos

#### CALIFICACIONES

```java
// Insertar calificación
public long insertRating(Rating rating)
```
- Registra una nueva calificación de servicio

```java
// Obtener calificaciones por usuario
public List<Rating> getRatingsByRatedId(int ratedId)
```
- Retorna todas las calificaciones recibidas por un usuario

#### NOTIFICACIONES

```java
// Obtener notificaciones de usuario
public List<Notification> getUserNotifications(int userId)
```
- Retorna todas las notificaciones de un usuario

```java
// Marcar notificación como leída
public boolean markNotificationAsRead(int notificationId)
```
- Marca una notificación como leída

```java
// Crear notificación
public long createNotification(int userId, String title, String message, 
                               String type, String category, 
                               String referenceType, Integer referenceId)
```
- Crea una nueva notificación en la base de datos

---

## 📱 4. FRAGMENTS PRINCIPALES

### HomeFragment.java
**Pantalla principal del usuario**

```java
// Cargar datos del usuario
private void loadUserData()
```
- Muestra nombre y rol del usuario
- Configura título según el rol (Cliente/Socia)

```java
// Cargar estadísticas
private void loadStatistics()
```
- Calcula y muestra:
  - Total de solicitudes
  - Solicitudes pendientes/en proceso
  - Solicitudes completadas
  - Ganancias totales (socias) o gastos (clientes)

```java
// Configurar listeners
private void setupListeners()
```
- Configura botones según el rol
- Clientes: botón para crear solicitud
- Socias: botón para ver solicitudes

---

### RequestsFragment.java
**Lista de solicitudes**

```java
// Cargar solicitudes
private void loadRequests()
```
- Carga solicitudes según el rol:
  - Clientes: sus propias solicitudes
  - Socias: solicitudes aceptadas y completadas
- Filtra solicitudes archivadas

```java
// Filtrar solicitudes
private void filterRequests(String query)
```
- Busca en: ID, dirección, notas, estado, fecha, hora
- Filtra en tiempo real mientras se escribe

```java
// Actualizar adapter
private void updateRequestAdapter()
```
- Configura el RecyclerView con las solicitudes
- Muestra estado vacío si no hay solicitudes

---

### ServicesFragment.java
**Catálogo de servicios**

```java
// Cargar servicios
private void loadServices()
```
- Carga todos los servicios activos de la base de datos
- Muestra servicios en RecyclerView

```java
// Filtrar servicios
private void filterServices(String query)
```
- Busca servicios por nombre, descripción o categoría

---

## ✏️ 5. CREAR Y GESTIONAR SOLICITUDES

### CreateRequestActivity.java
**Crear nueva solicitud de servicio**

```java
// Crear solicitud
private void createRequest()
```
- Valida todos los campos del formulario
- Valida servicio, fecha, hora, dirección
- Crea objeto Request
- Guarda en la base de datos
- Muestra mensaje de éxito

```java
// Actualizar solicitud
private void updateRequest()
```
- Actualiza una solicitud existente
- Valida todos los campos
- Actualiza en la base de datos

```java
// Mostrar selector de fecha
private void showDatePicker()
```
- Abre diálogo para seleccionar fecha
- No permite fechas pasadas

```java
// Mostrar selector de hora
private void showTimePicker()
```
- Abre diálogo para seleccionar hora

```java
// Actualizar precio total
private void updateTotalPrice()
```
- Actualiza el precio mostrado según el servicio seleccionado

---

### RequestDetailsActivity.java
**Detalles de una solicitud**

```java
// Cargar detalles de solicitud
private void loadRequestDetails(int requestId)
```
- Carga datos completos de la solicitud
- Carga información del servicio
- Carga información del cliente
- Carga información de la socia (si está asignada)

```java
// Aceptar solicitud
private void acceptRequest()
```
- Cambia estado a "aceptada"
- Asigna la socia actual a la solicitud
- Envía notificación al cliente
- Actualiza botones en la UI

```java
// Rechazar solicitud
private void rejectRequest()
```
- Cambia estado a "rechazada"
- Envía notificación al cliente

```java
// Completar servicio
private void completeRequest()
```
- Cambia estado a "en_progreso" o "completada"
- Actualiza estadísticas de la socia
- Envía notificaciones al cliente

```java
// Actualizar estadísticas de socia
private void updateSociaStats()
```
- Incrementa contador de servicios completados
- Actualiza fecha del último servicio
- Guarda en la base de datos

```java
// Editar solicitud
private void editRequest()
```
- Navega a CreateRequestActivity en modo edición
- Solo disponible para clientes
- Solo para solicitudes pendientes o rechazadas

```java
// Eliminar solicitud
private void deleteRequest()
```
- Muestra diálogo de confirmación
- Elimina la solicitud de la base de datos
- Solo disponible para clientes
- Solo para solicitudes pendientes o rechazadas

```java
// Mostrar información de la socia
private void showSociaInfo()
```
- Muestra nombre, teléfono y calificación de la socia
- Solo si la solicitud fue aceptada

```java
// Actualizar visualización del estado
private void updateStatusDisplay()
```
- Muestra ícono y texto del estado
- Configura colores según el estado
- Muestra barra de progreso si está en progreso

---

## 🎬 6. PANTALLA DE INICIO (SPLASH)

### SplashActivity.java
**Pantalla de bienvenida**

```java
// Iniciar animaciones
private void startAnimations()
```
- Anima el logo (escala y opacidad)
- Anima el nombre de la app
- Anima el tagline

```java
// Navegar a siguiente actividad
private void navigateToNextActivity()
```
- Si está logueado: va a MainActivity
- Si es primera vez: va a OnboardingActivity
- Si no es primera vez: va a LoginActivity

---

## 🔄 7. FLUJO DE ESTADOS DE SOLICITUDES

El flujo completo de una solicitud:

1. **PENDIENTE** → Cliente crea la solicitud
2. **ACEPTADA** → Socia acepta la solicitud
3. **PAGADO** → Cliente realiza el pago
4. **EN_PROGRESO** → Socia inicia el servicio
5. **COMPLETADA** → Socia completa el servicio
6. **CALIFICADA** → Cliente califica el servicio
7. **ARCHIVADA** → Solicitud se archiva automáticamente

---

## 📊 8. ESTADÍSTICAS Y REPORTES

### Funciones de estadísticas en DatabaseHelper

```java
// Actualizar estadísticas de usuario
public void updateUserStatsFromDatabase(int userId)
```
- Calcula servicios completados
- Calcula calificación promedio basada en ratings
- Actualiza fecha del último servicio
- Guarda todas las estadísticas

---

## 🔔 9. NOTIFICACIONES

### NotificationHelper.java
**Gestión de notificaciones**

```java
// Notificar cambio de estado
public void notifyRequestStatusChange(Request request, 
                                      String oldStatus, 
                                      String newStatus)
```
- Crea notificación cuando cambia el estado de una solicitud
- Notifica al usuario correspondiente

---

## 🎯 10. FUNCIONES DE VALIDACIÓN

### ValidationHelper.java
**Validación de datos**

```java
// Validar fecha
public ValidationResult validateDate(String date, boolean allowPast)
```

```java
// Validar hora
public ValidationResult validateTime(String time)
```

```java
// Validar dirección
public ValidationResult validateAddress(String address)
```

```java
// Validar notas
public ValidationResult validateNotes(String notes)
```

---

## 📝 RESUMEN DE FUNCIONES POR CATEGORÍA

### Autenticación
- ✅ Login de usuarios
- ✅ Registro de usuarios
- ✅ Gestión de sesiones
- ✅ Verificación de roles

### Base de Datos
- ✅ CRUD completo de usuarios
- ✅ CRUD completo de servicios
- ✅ CRUD completo de solicitudes
- ✅ Gestión de pagos
- ✅ Gestión de calificaciones
- ✅ Gestión de notificaciones
- ✅ Estadísticas y reportes

### Gestión de Solicitudes
- ✅ Crear solicitud
- ✅ Editar solicitud
- ✅ Eliminar solicitud
- ✅ Aceptar solicitud (socia)
- ✅ Rechazar solicitud (socia)
- ✅ Completar servicio (socia)
- ✅ Pagar servicio (cliente)
- ✅ Calificar servicio (cliente)

### Interfaz de Usuario
- ✅ Navegación por roles
- ✅ Lista de solicitudes
- ✅ Catálogo de servicios
- ✅ Estadísticas del usuario
- ✅ Detalles de solicitud
- ✅ Búsqueda y filtrado

### Utilidades
- ✅ Validación de datos
- ✅ Manejo de errores
- ✅ Notificaciones
- ✅ Gestión de ubicación
- ✅ Animaciones de transición

---

## 🚀 FUNCIONES CRÍTICAS DEL SISTEMA

1. **Gestión de Sesiones** - SessionManager
2. **Operaciones de Base de Datos** - DatabaseHelper
3. **Flujo de Solicitudes** - RequestDetailsActivity
4. **Autenticación** - LoginActivity
5. **Navegación Principal** - MainActivity
6. **Creación de Solicitudes** - CreateRequestActivity
7. **Visualización de Datos** - Fragments (Home, Requests, Services)

---

*Documento generado para LOOP v7 - Sistema de Gestión de Servicios Domésticos*

