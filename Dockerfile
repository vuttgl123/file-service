# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom trước để tận dụng cache
COPY pom.xml .
COPY src ./src

# Build (bỏ test cho nhanh khi deploy)
RUN mvn -DskipTests clean package


# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# Render sẽ set PORT, bạn nên đọc PORT trong app
ENV PORT=8080
EXPOSE 8080

# Khuyến nghị set memory theo container
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
