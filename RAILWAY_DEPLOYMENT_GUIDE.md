# 🚀 Guía de Despliegue en Railway - SafeCar Backend

## 📋 Prerequisitos

1. **Cuenta en Railway**: [https://railway.app](https://railway.app)
2. **Base de datos MySQL en Railway**: Ya creada según tu mensaje
3. **Cuenta GitHub**: Repositorio del proyecto (recomendado para despliegue automático)

---

## 🔧 Configuración de Variables de Entorno en Railway

### Variables Requeridas

Una vez que crees tu proyecto en Railway, debes configurar las siguientes variables de entorno:

#### 1. Configuración de Base de Datos

```bash
# Railway proporciona estas automáticamente si conectas el servicio MySQL
DATABASE_URL=jdbc:mysql://containers-us-west-xxx.railway.app:6756/railway?allowPublicKeyRetrieval=true&useSSL=false
DATABASE_USERNAME=root
DATABASE_PASSWORD=tu_password_de_railway
```

**Nota**: Railway genera automáticamente estas variables cuando conectas una base de datos MySQL. Revisa en la pestaña "Variables" de tu servicio.

#### 2. Seguridad JWT

```bash
# Genera un secret fuerte (mínimo 256 bits)
JWT_SECRET=tu_jwt_secret_aqui_minimo_32_caracteres_aleatorios
```

**Generar JWT Secret**:
```bash
# Opción 1: PowerShell
-join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | % {[char]$_})

# Opción 2: Online
# https://generate-random.org/api-token-generator
```

#### 3. OpenAI Integration (Insights Context)

```bash
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxx
```

**Obtener API Key**: [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)

#### 4. Stripe Payments

```bash
# Stripe Secret Key (desde https://dashboard.stripe.com/test/apikeys)
STRIPE_SECRET_KEY=sk_test_51xxxxxxxxxxxxxxxxxxxxx

# Webhook Secret (desde https://dashboard.stripe.com/test/webhooks)
STRIPE_WEBHOOK_SECRET=whsec_xxxxxxxxxxxxxxxxxxxxxxxx

# Price IDs de tus planes (desde https://dashboard.stripe.com/test/products)
STRIPE_BASIC_PRICE_ID=price_1SQbsT3l890Fc29CerlSwh4r
STRIPE_PROFESSIONAL_PRICE_ID=price_1SQbt23l890Fc29CqoqLYCnu
STRIPE_PREMIUM_PRICE_ID=price_1SQbtK3l890Fc29COSEZ6iK4

# URL del frontend (para redirecciones post-pago)
FRONTEND_URL=https://tu-frontend.vercel.app
```

#### 5. Spring Boot Configuration

```bash
# Railway asigna automáticamente el PORT
PORT=8080

# Perfil activo
SPRING_PROFILES_ACTIVE=prod
```

---

## 📦 Método 1: Despliegue desde GitHub (Recomendado)

### Paso 1: Sube tu código a GitHub

```bash
cd c:\Users\janov\Desktop\develop\safecar-backend

# Inicializar Git si no lo has hecho
git init

# Agregar archivos
git add .

# Commit
git commit -m "Configuración para despliegue en Railway"

# Agregar remote (reemplaza con tu repositorio)
git remote add origin https://github.com/tu-usuario/safecar-backend.git

# Push
git push -u origin main
```

### Paso 2: Crear Proyecto en Railway

1. Ve a [Railway Dashboard](https://railway.app/dashboard)
2. Click en **"New Project"**
3. Selecciona **"Deploy from GitHub repo"**
4. Autoriza Railway para acceder a tu GitHub
5. Selecciona el repositorio `safecar-backend`

### Paso 3: Conectar Base de Datos

1. En tu proyecto Railway, click en **"+ New"**
2. Selecciona **"Database" → "MySQL"**
3. Railway creará automáticamente las variables:
   - `MYSQL_URL`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`
   - `MYSQL_DATABASE`

4. Ve a la pestaña **"Variables"** del servicio backend
5. Agrega manualmente las variables de conexión:

```bash
DATABASE_URL=jdbc:mysql://${{MySQL.MYSQL_HOST}}:${{MySQL.MYSQL_PORT}}/${{MySQL.MYSQL_DATABASE}}?allowPublicKeyRetrieval=true&useSSL=false
DATABASE_USERNAME=${{MySQL.MYSQL_USER}}
DATABASE_PASSWORD=${{MySQL.MYSQL_PASSWORD}}
```

**Nota**: Railway permite referencias cruzadas entre servicios usando `${{NombreServicio.VARIABLE}}`

### Paso 4: Configurar Variables de Entorno

En la pestaña **"Variables"** del servicio backend, agrega:

```
JWT_SECRET=tu_jwt_secret_64_caracteres_aleatorios
OPENAI_API_KEY=sk-proj-xxxxxxxxxxxxxxxxxxxxx
STRIPE_SECRET_KEY=sk_test_51xxxxxxxxxxxxxxxxxxxxx
STRIPE_WEBHOOK_SECRET=whsec_xxxxxxxxxxxxxxxxxxxxxxxx
STRIPE_BASIC_PRICE_ID=price_1SQbsT3l890Fc29CerlSwh4r
STRIPE_PROFESSIONAL_PRICE_ID=price_1SQbt23l890Fc29CqoqLYCnu
STRIPE_PREMIUM_PRICE_ID=price_1SQbtK3l890Fc29COSEZ6iK4
FRONTEND_URL=https://tu-frontend.vercel.app
```

### Paso 5: Deploy Automático

Railway detectará automáticamente:
- **`railway.json`**: Configuración de build y deploy
- **`Procfile`**: Comando de inicio
- **`pom.xml`**: Proyecto Maven

El build se ejecutará automáticamente:
1. `./mvnw clean package -DskipTests`
2. Crea el JAR ejecutable
3. Inicia la aplicación con `java -jar target/*.jar`

### Paso 6: Obtener URL Pública

1. En Railway, ve a **"Settings" → "Networking"**
2. Click en **"Generate Domain"**
3. Railway generará una URL como: `https://safecar-backend-production.up.railway.app`
4. Guarda esta URL para configurar CORS y Stripe webhooks

---

## 📦 Método 2: Despliegue desde CLI (Railway CLI)

### Paso 1: Instalar Railway CLI

```bash
# Windows (PowerShell como administrador)
iwr https://railway.app/install.ps1 | iex
```

### Paso 2: Login

```bash
railway login
```

### Paso 3: Inicializar Proyecto

```bash
cd c:\Users\janov\Desktop\develop\safecar-backend

# Conectar con proyecto existente o crear nuevo
railway init
```

### Paso 4: Vincular Base de Datos

```bash
# Listar servicios disponibles
railway service

# Si ya tienes MySQL en Railway, vincularlo:
railway link
```

### Paso 5: Configurar Variables

```bash
# Opción 1: Desde CLI
railway variables set JWT_SECRET=tu_jwt_secret_aqui
railway variables set OPENAI_API_KEY=sk-proj-xxx
railway variables set STRIPE_SECRET_KEY=sk_test_xxx
# ... continuar con todas las variables

# Opción 2: Importar desde archivo .env
# Crear archivo .env.railway
railway variables set --from-file .env.railway
```

### Paso 6: Deploy

```bash
railway up
```

---

## 🔍 Verificación del Despliegue

### 1. Ver Logs en Tiempo Real

```bash
# Desde Railway CLI
railway logs

# O desde Railway Dashboard → "Deployments" → Click en el deploy → "View Logs"
```

### 2. Health Check

```bash
# Reemplaza con tu URL de Railway
curl https://safecar-backend-production.up.railway.app/actuator/health

# Respuesta esperada:
# {"status":"UP"}
```

### 3. Swagger UI

Accede a la documentación interactiva:
```
https://safecar-backend-production.up.railway.app/swagger-ui.html
```

### 4. Probar Endpoint de Test

```bash
curl https://safecar-backend-production.up.railway.app/api/v1/authentication/sign-up \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "test@safecar.com",
    "password": "Test123!",
    "confirmPassword": "Test123!",
    "roles": ["ROLE_DRIVER"]
  }'
```

---

## 🔧 Configuración Post-Despliegue

### 1. Configurar CORS (si es necesario)

Si tu frontend está en otro dominio, actualiza en `application-prod.properties`:

```properties
# Permitir peticiones desde tu frontend
spring.web.cors.allowed-origins=${FRONTEND_URL}
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,PATCH,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

Luego agrega variable en Railway:
```bash
FRONTEND_URL=https://tu-frontend.vercel.app
```

### 2. Configurar Webhook de Stripe

1. Ve a [Stripe Dashboard → Webhooks](https://dashboard.stripe.com/test/webhooks)
2. Click en **"Add endpoint"**
3. Endpoint URL: `https://safecar-backend-production.up.railway.app/webhooks/stripe`
4. Selecciona eventos:
   - `customer.subscription.created`
   - `customer.subscription.updated`
   - `customer.subscription.deleted`
5. Copia el **Signing secret** (empieza con `whsec_`)
6. Agrégalo como variable en Railway: `STRIPE_WEBHOOK_SECRET=whsec_xxx`
7. Redeploy la aplicación

### 3. Verificar Integración con Base de Datos

```bash
# Conectar a MySQL en Railway (desde Railway Dashboard)
# Settings → Database → Connect → MySQL CLI

# Verificar tablas creadas por Hibernate
SHOW TABLES;

# Deberías ver:
# users, roles, person_profiles, business_profiles, 
# vehicles, drivers, workshops, mechanics, appointments, 
# telemetries, subscriptions, etc.
```

---

## 📊 Monitoreo y Troubleshooting

### Comandos Útiles

```bash
# Ver logs
railway logs --tail

# Ver variables configuradas
railway variables

# Reiniciar servicio
railway restart

# Ver información del proyecto
railway status
```

### Logs Comunes de Errores

#### Error 1: "Failed to configure a DataSource"
**Causa**: Variables de base de datos no configuradas
**Solución**: Verifica que `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD` estén configuradas

#### Error 2: "Port already in use"
**Causa**: Variable `PORT` no está siendo usada correctamente
**Solución**: Verifica que el Procfile use `$PORT`

#### Error 3: "401 Unauthorized" en endpoints protegidos
**Causa**: `JWT_SECRET` no configurado o diferente entre deploys
**Solución**: Configura `JWT_SECRET` y mantén el mismo valor

#### Error 4: Stripe webhook signature verification fails
**Causa**: `STRIPE_WEBHOOK_SECRET` incorrecto
**Solución**: Usa el signing secret del endpoint en Stripe Dashboard

---

## 🎯 Checklist Final

Antes de considerar el despliegue exitoso, verifica:

- [ ] ✅ Aplicación responde en la URL de Railway
- [ ] ✅ Health check retorna `{"status":"UP"}`
- [ ] ✅ Swagger UI es accesible
- [ ] ✅ Base de datos conectada (verifica logs de Hibernate)
- [ ] ✅ Endpoint de registro (`/sign-up`) funciona
- [ ] ✅ Endpoint de login (`/sign-in`) retorna JWT válido
- [ ] ✅ Webhook de Stripe configurado y funcionando
- [ ] ✅ Variables de entorno todas configuradas
- [ ] ✅ Logs no muestran errores críticos
- [ ] ✅ CORS configurado para tu frontend

---

## 📚 Recursos Adicionales

- **Railway Documentation**: https://docs.railway.app
- **Railway CLI Reference**: https://docs.railway.app/develop/cli
- **Stripe Webhooks**: https://stripe.com/docs/webhooks
- **Spring Boot on Railway**: https://docs.railway.app/guides/java-spring-boot

---

## 🆘 Soporte

Si encuentras problemas:

1. **Revisa logs**: `railway logs` o Railway Dashboard
2. **Verifica variables**: `railway variables`
3. **Verifica conectividad DB**: Railway Dashboard → MySQL → Metrics
4. **Community**: https://help.railway.app

---

## 🔄 Actualización Continua

Una vez configurado, cada `git push` a tu rama principal desplegará automáticamente:

```bash
# Hacer cambios en tu código local
git add .
git commit -m "Feature: nuevo endpoint"
git push origin main

# Railway automáticamente:
# 1. Detecta el push
# 2. Ejecuta build (mvnw package)
# 3. Despliega nueva versión
# 4. Mantiene las variables de entorno
```

---

**¡Listo!** Tu aplicación SafeCar Backend está desplegada en Railway con todas las configuraciones necesarias para producción. 🚀
