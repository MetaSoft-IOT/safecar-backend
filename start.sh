#!/bin/bash
# Railway Database Connection Script
# Convierte variables de Railway al formato Spring Boot

echo "🚀 Iniciando SafeCar Backend en Railway..."

# Railway proporciona MYSQL_URL en formato: mysql://user:password@host:port/database
# Necesitamos convertirlo a JDBC format: jdbc:mysql://host:port/database

if [ ! -z "$MYSQL_URL" ]; then
    echo "✅ MYSQL_URL detectado de Railway"
    
    # Extraer componentes de la URL
    export DATABASE_USERNAME=$(echo $MYSQL_URL | sed -n 's/.*:\/\/\([^:]*\):.*/\1/p')
    export DATABASE_PASSWORD=$(echo $MYSQL_URL | sed -n 's/.*:\/\/[^:]*:\([^@]*\)@.*/\1/p')
    export DATABASE_HOST=$(echo $MYSQL_URL | sed -n 's/.*@\([^:]*\):.*/\1/p')
    export DATABASE_PORT=$(echo $MYSQL_URL | sed -n 's/.*:\([0-9]*\)\/.*/\1/p')
    export DATABASE_NAME=$(echo $MYSQL_URL | sed -n 's/.*\/\(.*\)/\1/p')
    
    # Construir JDBC URL
    export DATABASE_URL="jdbc:mysql://${DATABASE_HOST}:${DATABASE_PORT}/${DATABASE_NAME}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC"
    
    echo "📊 Configuración de Base de Datos:"
    echo "   Host: ${DATABASE_HOST}"
    echo "   Port: ${DATABASE_PORT}"
    echo "   Database: ${DATABASE_NAME}"
    echo "   User: ${DATABASE_USERNAME}"
else
    echo "⚠️  MYSQL_URL no encontrado, usando variables directas"
    
    # Usar variables directas de Railway si existen
    if [ ! -z "$MYSQLHOST" ]; then
        export DATABASE_URL="jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC"
        export DATABASE_USERNAME=$MYSQLUSER
        export DATABASE_PASSWORD=$MYSQLPASSWORD
        
        echo "📊 Configuración de Base de Datos (variables separadas):"
        echo "   Host: ${MYSQLHOST}"
        echo "   Port: ${MYSQLPORT}"
        echo "   Database: ${MYSQLDATABASE}"
    fi
fi

# Verificar que todas las variables necesarias estén configuradas
if [ -z "$DATABASE_URL" ] || [ -z "$DATABASE_USERNAME" ] || [ -z "$DATABASE_PASSWORD" ]; then
    echo "❌ ERROR: Variables de base de datos no configuradas correctamente"
    echo "   DATABASE_URL: ${DATABASE_URL:-NOT SET}"
    echo "   DATABASE_USERNAME: ${DATABASE_USERNAME:-NOT SET}"
    echo "   DATABASE_PASSWORD: ${DATABASE_PASSWORD:+SET (hidden)}"
    exit 1
fi

echo "✅ Variables de entorno configuradas correctamente"
echo "🔄 Iniciando aplicación Spring Boot..."
echo ""

# Ejecutar la aplicación Java
exec java -Dserver.port=$PORT \
    -Dspring.profiles.active=prod \
    -Xmx512m \
    -Xms256m \
    $JAVA_OPTS \
    -jar target/*.jar
