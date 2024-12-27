<style>
.first-level{
    font-weight: bold; 
    font-size: 14px; 
    list-style: none;
}

.first-level::before{
    content: "►";
    color: blue;
    display: inline-block;
    width: 1em;
    margin-right: 0.5em;
    margin-top: 0.5em;
}

.second-level {
    list-style: none;
    margin-top: 0.5rem;
}

.second-level::before {
    content: "-";
    margin-right: 0.5em;
}
</style>

<h1>LibraTech</h1>

<p style="font-size: 16px;">LibraTech es un proyecto personal que tiene como objetivo practicar backend y de ser posible Frontend.</p>

<h2>Caso de Uso: Sistema de Gestión de Biblioteca</h2>

<h3>Requisitos Funcionales:</h3>

<ul>
    <li class="first-level">Gestión de Usuarios:</li>
    <ul>
        <li class="second-level">Crear, actualizar, eliminar y listar usuarios de la biblioteca.</li>
        <li class="second-level">Un usuario puede tener múltiples roles (estudiante, profesor, administrador).</li>
    </ul>
    <li class="first-level">Gestión de Libros:</li>
    <ul>
        <li class="second-level">Registrar, actualizar, eliminar y listar libros en la biblioteca.</li>
        <li class="second-level">Un libro puede tener múltiples autores y categorías.</li>
    </ul>
    <li class="first-level">Préstamo de Libros:</li>
    <ul>
        <li class="second-level">Permitir a los usuarios solicitar y devolver libros.</li>
        <li class="second-level">Registrar la fecha de préstamo y de devolución.</li>
        <li class="second-level">Limitar la cantidad de libros que un usuario puede tener prestados simultáneamente.</li>
    </ul>
    <li class="first-level">Notificaciones:</li>
    <ul>
        <li class="second-level">Enviar notificaciones a los usuarios cuando la fecha de devolución de un libro esté próxima.</li>
        <li class="second-level">Registrar notificaciones enviadas.</li>
    </ul>
</ul>

<h3>Requisitos No Funcionales:</h3>
<ul>
    <li class="first-level">Seguridad:</li>
    <ul>
        <li class="second-level">Implementar autenticación y autorización utilizando JWT.</li>
        <li class="second-level">Asegurar que solo los usuarios autenticados puedan acceder a la API.</li>
    </ul>
    <li class="first-level">Testing:</li>
    <ul>
        <li class="second-level">Implementar pruebas unitarias para los servicios principales.</li>
        <li class="second-level">Implementar pruebas de integración para los endpoints.</li>
    </ul>
    <li class="first-level">Documentación:</li>
    <ul>
        <li class="second-level">Documentar la API utilizando Swagger/OpenAPI.</li>
        <li class="second-level">Proveer instrucciones claras para la configuración y despliegue del proyecto.</li>
    </ul>
</ul>

<h3>Requisitos Adicionales:</h3>
<ul>
    <li class="first-level">Búsqueda Avanzada:</li>
    <ul>
        <li class="second-level">Implementar funcionalidades de búsqueda avanzada por título, autor y categoría.</li>
    </ul>
    <li class="first-level">Pagos y Multas:</li>
    <ul>
        <li class="second-level">Implementar un sistema de multas para libros devueltos fuera de plazo.</li>
        <li class="second-level">Permitir a los usuarios pagar multas en línea.</li>
    </ul>
    <li class="first-level">Estadísticas y Reportes:</li>
    <ul>
        <li class="second-level">Generar reportes sobre los libros más prestados, usuarios más activos, etc.</li>
    </ul>
    <li class="first-level">Despliegue:</li>
    <ul>
        <li class="second-level">Configurar el despliegue continuo utilizando herramientas como Docker y Jenkins.</li>
    </ul>
</ul>
<h2>Base de Datos</h2>
<img src="assets/Diagrama BD.png" alt="Imagen de la Base de Datos">