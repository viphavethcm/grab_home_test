# --- STAGE 1: BUILD ---
# Sử dụng base image có sẵn Gradle và JDK 17 để build ứng dụng
FROM gradle:8.4-jdk17 AS build

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Chạy lệnh build của Gradle để tạo file JAR thực thi.
# '--no-daemon' là best practice khi chạy trong môi trường CI/CD hoặc Docker.
# '-x test' để bỏ qua các bài test, giúp build nhanh hơn cho người review.
RUN ./gradlew build --no-daemon -x test


# --- STAGE 2: RUN ---
# Sử dụng base image JRE (chỉ môi trường chạy) của Java 17, rất nhẹ.
FROM eclipse-temurin:17-jre-jammy

# Đặt thư mục làm việc
WORKDIR /app

# Copy file .jar đã được build ở Stage 1 vào image này
# Sử dụng wildcard (*) để tự động lấy đúng file jar mà không cần hardcode tên
COPY --from=build /app/build/libs/*.jar app.jar

# Báo cho Docker biết container sẽ lắng nghe ở cổng 8080 (cổng mặc định của Spring Boot)
EXPOSE 8080

# Lệnh để khởi động ứng dụng khi container bắt đầu
ENTRYPOINT ["java", "-jar", "app.jar"]